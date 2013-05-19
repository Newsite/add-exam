package add.exam.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.google.common.base.Throwables;

/**
 * Global exception handler for all controllers.
 */
@ControllerAdvice
public class ExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandler.class);

    /**
     * Handler for exceptions caused by wrong (missing, invalid, etc) arguments.
     *
     * @param exception
     *            Occurred exception.
     * @return HTTP response with {@link org.springframework.http.HttpStatus#BAD_REQUEST} status.
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleServletRequestBindingException(IllegalArgumentException exception) {
        LOG.error("Illegal request handled. {} response code will be returned. {}", HttpStatus.BAD_REQUEST,
                Throwables.getStackTraceAsString(exception));
        return new ResponseEntity<String>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handler for exceptions caused by missing resources.
     *
     * @param exception
     *            Occurred exception.
     * @return HTTP response with {@link org.springframework.http.HttpStatus#NOT_FOUND} status.
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleServletRequestBindingException(ResourceNotFoundException exception) {
        LOG.error("Resource not found handled. {} response code will be returned. {}", HttpStatus.NOT_FOUND,
                Throwables.getStackTraceAsString(exception));
        return new ResponseEntity<String>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handler for all unhandled exceptions
     *
     * @param exception
     *            Occurred exception.
     * @return HTTP response with {@link org.springframework.http.HttpStatus#INTERNAL_SERVER_ERROR} status.
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleServletRequestBindingException(Exception exception) {
        LOG.error("Exception handled. {} response code will be returned. {}", HttpStatus.INTERNAL_SERVER_ERROR,
                Throwables.getStackTraceAsString(exception));
        return new ResponseEntity<String>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

