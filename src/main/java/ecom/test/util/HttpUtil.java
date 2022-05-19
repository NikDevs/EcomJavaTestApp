package ecom.test.util;

import javax.servlet.http.HttpServletRequest;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpUtil {

    private static final String[] IP_HEADERS = {
            "Origin",
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR",
    };

    public static String extractIpAddress(HttpServletRequest request) {
        for (final String header : IP_HEADERS) {
            final String value = getHeaderValue(request.getHeader(header));
            if (value != null) {
                return value;
            }
        }
        return request.getRemoteAddr();
    }

    private static String getHeaderValue(String header) {
        if (header == null || header.isBlank()) {
            return null;
        }

        final String[] parts = header.split("\\s*,\\s*");
        return parts[0];
    }
}
