package field.platform.exception;

import org.springframework.http.HttpStatus;

public interface BaseException {
    String getErrorCode();
    String getMessage();
    HttpStatus getHttpStatus();
}
