package ca.bc.gov.open.staffnet.models;

import java.io.Serializable;
import lombok.Data;

@Data
public class WorkerImageSetRequest implements Serializable {
    private String indiId;
    private String userId;
    private String callingModule;
    private byte[] photo;
    private String photoTakenDate;
}
