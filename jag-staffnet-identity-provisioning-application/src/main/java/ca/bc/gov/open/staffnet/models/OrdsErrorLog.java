package ca.bc.gov.open.staffnet.models;

public class OrdsErrorLog {

    private String message;
    private String method;
    private String exception;
    private Object request;

    public OrdsErrorLog(String message, String method, String exception, Object request) {
        this.message = message;
        this.method = method;
        this.exception = exception;
        this.request = request;
    }

    public String getMessage() {
        return message;
    }

    public String getMethod() {
        return method;
    }

    public String getException() {
        return exception;
    }

    public Object getRequest() {
        return request;
    }
}
