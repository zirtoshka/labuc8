package server.json;
import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.*;
import common.exceptions.InvalidDateFormatException;
import common.utils.DateConverter;

/**
 * type adapter for json deserialization
 */
public class DateDeserializer implements JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        try{
            return DateConverter.parseDate(json.getAsJsonPrimitive().getAsString());
        }
        catch (InvalidDateFormatException e){
            throw new JsonParseException("invalid date format");
        }
    }
}
