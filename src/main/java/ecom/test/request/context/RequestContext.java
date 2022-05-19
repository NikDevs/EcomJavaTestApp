package ecom.test.request.context;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import ecom.test.util.HttpUtil;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class RequestContext {

    private final ThreadLocal<HttpServletRequest> requests = new ThreadLocal<>();

    public void register(HttpServletRequest request) {
        requests.set(request);
    }

    public String getCurrentIp() {
        return HttpUtil.extractIpAddress(requests.get());
    }
}
