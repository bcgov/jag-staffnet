package ca.bc.gov.open.staffnet.models;

import ca.bc.gov.open.staffnet.biometrics.three.ReconciliationItem;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class GetEnrolledWorkersOutput implements Serializable {
    private List<ReconciliationItem> workers;
    private String responseCd;
}
