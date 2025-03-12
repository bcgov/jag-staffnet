package ca.bc.gov.open.staffnet.models;

import ca.bc.gov.open.staffnet.biometrics.two.IdentityName;
import ca.bc.gov.open.staffnet.biometrics.two.ResponseCode;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class WorkerInfoResponse implements Serializable {
    private String did;
    private byte[] photoBase64;
    private String dateOfBirth;
    private List<IdentityNameResponse> identityNames;
    private ResponseCode responseCode;
    private String responseMessage;
}
