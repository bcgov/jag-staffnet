package ca.bc.gov.open.staffnet.models;

import java.io.Serializable;
import java.util.Objects;

public class WorkerImageSetResponse implements Serializable {
    private String successYN;
    private String errorMessage;

    public String getSuccessYN() {
        return successYN;
    }

    public void setSuccessYN(String successYN) {
        this.successYN = successYN;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkerImageSetResponse that = (WorkerImageSetResponse) o;
        return Objects.equals(successYN, that.successYN)
                && Objects.equals(errorMessage, that.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(successYN, errorMessage);
    }

    @Override
    public String toString() {
        return "WorkerImageSetResponse("
                + "successYN=" + successYN
                + ", "
                + "errorMessage=" + errorMessage
                + ")";
    }
}
