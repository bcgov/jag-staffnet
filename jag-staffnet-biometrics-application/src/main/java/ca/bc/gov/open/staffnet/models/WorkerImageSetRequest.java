package ca.bc.gov.open.staffnet.models;

import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
public class WorkerImageSetRequest implements Serializable {
    private String indiId;
    private String userId;
    private String callingModule;
    private byte[] photo;
    private Instant photoTakenDate;
}
