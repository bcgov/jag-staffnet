package ca.bc.gov.open.staffnet.models;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.xml.bind.annotation.XmlEnumValue;

public enum MatchIdentityNameTypeResponse {
    LEGAL("Legal"),
    KNOWNAS("KnownAs");

    private String type;

    MatchIdentityNameTypeResponse(String type) {
        this.type = type;
    }

    @JsonValue
    public String getType() {
        return type;
    }
}
