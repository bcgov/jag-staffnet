package ca.bc.gov.open.staffnet.models;

import ca.bc.gov.open.staffnet.biometrics.three.IdentityName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class WorkerInfoResponse implements Serializable {
    private String did;
    private byte[] photoBase64;
    private String dateOfBirth;
    private List<IdentityName> identityNames;
    private String responseCode;
    private String responseMessage;
}
