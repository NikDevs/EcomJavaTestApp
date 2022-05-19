package ecom.test.request.limiter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ecom.test.exception.MethodTimeLimitException;
import ecom.test.request.context.RequestContext;
import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class RequestLimiter {

    private final Map<String, CircularTimeChecker> CHECKERS = new ConcurrentHashMap<>();

    private final RequestContext requestContext;

    @Value("${limiter.request.maxCount}")
    private int requestMaxCount;

    @Value("${limiter.time.limit}")
    private long timeLimit;

    @Value("${limiter.release.after}")
    private long releaseAfter;

    @Before("@annotation(ecom.test.request.limiter.RequestLimit)")
    public void checkLimit(JoinPoint joinPoint) {
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final String checkerName = requestContext.getCurrentIp() + signature.getMethod().getName();

        final CircularTimeChecker checker = CHECKERS.computeIfAbsent(checkerName,
                name -> new CircularTimeChecker(requestMaxCount, timeLimit));

        if (!checker.check()) {
            throw new MethodTimeLimitException("Request limit exceeded");
        }
    }

    @Scheduled(cron = "${limiter.release.schedule}")
    private void releaseUnused() {
        final long minTimestamp = System.currentTimeMillis() - releaseAfter;
        for (final Map.Entry<String, CircularTimeChecker> checker : CHECKERS.entrySet()) {
            if (checker.getValue().getOldestTimestamp() < minTimestamp) {
                CHECKERS.remove(checker.getKey());
            }
        }
    }
}
