package ca.bc.gov.open.staffnet.models.serializers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Locale;
import java.util.TimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

public class InstantDeserializer extends ValueDeserializer<Instant> {

    private static final Logger log = LoggerFactory.getLogger(InstantDeserializer.class);

    @Override
    public Instant deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws JacksonException {

        String text = jsonParser.getString();
        try {
            if (text.split("-")[2].split(" ")[0].length() == 4) {
                var sfd = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
                sfd.setTimeZone(TimeZone.getTimeZone("UTC"));
                return sfd.parse(text).toInstant();
            }

            if (text.split("-")[0].length() < 4) {
                var sfd = new SimpleDateFormat("dd-MMM-yy hh.mm.ss.SSSSSS a", Locale.US);
                sfd.setTimeZone(TimeZone.getTimeZone("UTC"));
                return sfd.parse(text).toInstant();
            } else {
                var sfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                sfd.setTimeZone(TimeZone.getTimeZone("UTC"));
                return sfd.parse(text).toInstant();
            }
        } catch (ParseException e) {
            try {
                var sfd = new SimpleDateFormat("dd-MMM-yy", Locale.US);
                sfd.setTimeZone(TimeZone.getTimeZone("UTC"));
                return sfd.parse(text).toInstant();
            } catch (ParseException e2) {
                log.error(e2.getLocalizedMessage());
            }
        }
        return null;
    }
}
