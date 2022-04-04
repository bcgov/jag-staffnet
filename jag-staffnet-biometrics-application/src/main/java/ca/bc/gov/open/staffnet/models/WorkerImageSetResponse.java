package ca.bc.gov.open.staffnet.models;

import java.io.Serializable;
import lombok.Data;

@Data
public class WorkerImageSetResponse implements Serializable {
    private String successYN;
    private String errorMessage;
}
