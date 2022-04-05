package ca.bc.gov.open.staffnet.models;

import ca.bc.gov.open.staffnet.biometrics.three.IdentityName;
import java.io.Serializable;
import java.util.List;

import ca.bc.gov.open.staffnet.biometrics.three.ResponseCode;
import lombok.Data;

@Data
public class WorkerInfoResponse implements Serializable {
    private String did;
    private byte[] photoBase64;
    private String dateOfBirth;
    private List<IdentityName> identityNames;
    private ResponseCode responseCode;
    private String responseMessage;
}
