package ca.bc.gov.open.staffnet.models;

import ca.bc.gov.open.staffnet.biometrics.two.IdentityName;
import ca.bc.gov.open.staffnet.biometrics.two.ResponseCode;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class WorkerInfoResponse implements Serializable {
    private String did;
    private byte[] photoBase64;
    private String dateOfBirth;
    private List<IdentityNameResponse> identityNames;
    private ResponseCode responseCode;
    private String responseMessage;

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public byte[] getPhotoBase64() {
        return photoBase64;
    }

    public void setPhotoBase64(byte[] photoBase64) {
        this.photoBase64 = photoBase64;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<IdentityNameResponse> getIdentityNames() {
        return identityNames;
    }

    public void setIdentityNames(List<IdentityNameResponse> identityNames) {
        this.identityNames = identityNames;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkerInfoResponse that = (WorkerInfoResponse) o;
        return Objects.equals(did, that.did)
                && Arrays.equals(photoBase64, that.photoBase64)
                && Objects.equals(dateOfBirth, that.dateOfBirth)
                && Objects.equals(identityNames, that.identityNames)
                && Objects.equals(responseCode, that.responseCode)
                && Objects.equals(responseMessage, that.responseMessage);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(did, dateOfBirth, identityNames, responseCode, responseMessage);
        result = 31 * result + Arrays.hashCode(photoBase64);
        return result;
    }

    @Override
    public String toString() {
        return "WorkerInfoResponse("
                + "did=" + did
                + ", "
                + "photoBase64=" + Arrays.toString(photoBase64)
                + ", "
                + "dateOfBirth=" + dateOfBirth
                + ", "
                + "identityNames=" + identityNames
                + ", "
                + "responseCode=" + responseCode
                + ", "
                + "responseMessage=" + responseMessage
                + ")";
    }
}
