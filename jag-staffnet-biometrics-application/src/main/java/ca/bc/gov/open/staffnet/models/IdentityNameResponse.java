package ca.bc.gov.open.staffnet.models;
import lombok.Data;
import java.io.Serializable;

@Data
public class IdentityNameResponse implements Serializable {
    protected String type;
    protected String givenName;
    protected String middleName;
    protected String lastName;
}
