package ca.bc.gov.open.staffnet.models;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class WorkerImageSetRequest implements Serializable {
    private String indiId;
    private String userId;
    private String callingModule;
    private byte[] photo;
    private String photoTakenDate;

    public String getIndiId() {
        return indiId;
    }

    public void setIndiId(String indiId) {
        this.indiId = indiId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCallingModule() {
        return callingModule;
    }

    public void setCallingModule(String callingModule) {
        this.callingModule = callingModule;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoTakenDate() {
        return photoTakenDate;
    }

    public void setPhotoTakenDate(String photoTakenDate) {
        this.photoTakenDate = photoTakenDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkerImageSetRequest that = (WorkerImageSetRequest) o;
        return Objects.equals(indiId, that.indiId)
                && Objects.equals(userId, that.userId)
                && Objects.equals(callingModule, that.callingModule)
                && Arrays.equals(photo, that.photo)
                && Objects.equals(photoTakenDate, that.photoTakenDate);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(indiId, userId, callingModule, photoTakenDate);
        result = 31 * result + Arrays.hashCode(photo);
        return result;
    }

    @Override
    public String toString() {
        return "WorkerImageSetRequest("
                + "indiId=" + indiId
                + ", "
                + "userId=" + userId
                + ", "
                + "callingModule=" + callingModule
                + ", "
                + "photo=" + Arrays.toString(photo)
                + ", "
                + "photoTakenDate=" + photoTakenDate
                + ")";
    }
}
