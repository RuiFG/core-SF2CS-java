package in.bugr.web.commponent;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.persistence.AttributeConverter;

/**
 * @author BugRui
 * @date 2020/2/4 下午2:31
 **/
public class JsonObjectConverter implements AttributeConverter<JsonObject, String> {
    @Override
    public String convertToDatabaseColumn(JsonObject attribute) {
        return new Gson().toJson(attribute);
    }

    @Override
    public JsonObject convertToEntityAttribute(String dbData) {
        return JsonParser.parseString(dbData).getAsJsonObject();
    }
}
