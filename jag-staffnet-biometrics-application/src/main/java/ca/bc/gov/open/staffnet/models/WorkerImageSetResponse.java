package ca.bc.gov.open.staffnet.models;

import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
public class WorkerImageSetResponse implements Serializable {
    private String successYN;
    private String errorMessage;
}
