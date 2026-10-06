package ca.bc.gov.open.staffnet.models;

import java.util.Objects;

public class RequestSuccessLog {
    private String type;
    private String endpoint;

    public RequestSuccessLog(String type, String endpoint) {
        this.type = type;
        this.endpoint = endpoint;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestSuccessLog that = (RequestSuccessLog) o;
        return Objects.equals(type, that.type)
                && Objects.equals(endpoint, that.endpoint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, endpoint);
    }

    @Override
    public String toString() {
        return "RequestSuccessLog("
                + "type=" + type
                + ", "
                + "endpoint=" + endpoint
                + ")";
    }
}
