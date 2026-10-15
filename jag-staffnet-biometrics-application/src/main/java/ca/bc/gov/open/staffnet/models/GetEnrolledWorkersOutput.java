package ca.bc.gov.open.staffnet.models;

import ca.bc.gov.open.staffnet.biometrics.two.ReconciliationItem;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class GetEnrolledWorkersOutput implements Serializable {
    private List<ReconciliationItem> workers;
    private String responseCd;

    public List<ReconciliationItem> getWorkers() {
        return workers;
    }

    public void setWorkers(List<ReconciliationItem> workers) {
        this.workers = workers;
    }

    public String getResponseCd() {
        return responseCd;
    }

    public void setResponseCd(String responseCd) {
        this.responseCd = responseCd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetEnrolledWorkersOutput that = (GetEnrolledWorkersOutput) o;
        return Objects.equals(workers, that.workers)
                && Objects.equals(responseCd, that.responseCd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workers, responseCd);
    }

    @Override
    public String toString() {
        return "GetEnrolledWorkersOutput("
                + "workers=" + workers
                + ", "
                + "responseCd=" + responseCd
                + ")";
    }
}
