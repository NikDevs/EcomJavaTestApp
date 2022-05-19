package ecom.test.exception;

import java.io.Serial;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class MethodTimeLimitException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 80780246687623436L;

    public MethodTimeLimitException(String message) {
        super(message);
    }
}
