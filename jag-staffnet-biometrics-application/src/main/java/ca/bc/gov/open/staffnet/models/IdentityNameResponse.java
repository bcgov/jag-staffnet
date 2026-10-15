package ca.bc.gov.open.staffnet.models;
import java.io.Serializable;
import java.util.Objects;

public class IdentityNameResponse implements Serializable {
    protected String type;
    protected String givenName;
    protected String middleName;
    protected String lastName;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdentityNameResponse that = (IdentityNameResponse) o;
        return Objects.equals(type, that.type)
                && Objects.equals(givenName, that.givenName)
                && Objects.equals(middleName, that.middleName)
                && Objects.equals(lastName, that.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, givenName, middleName, lastName);
    }

    @Override
    public String toString() {
        return "IdentityNameResponse("
                + "type=" + type
                + ", "
                + "givenName=" + givenName
                + ", "
                + "middleName=" + middleName
                + ", "
                + "lastName=" + lastName
                + ")";
    }
}
