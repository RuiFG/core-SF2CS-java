package in.bugr.component;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.persistence.AttributeConverter;

/**
 * @author BugRui
 * @date 2020/2/4 下午2:31
 **/
public class JsonObjectConverter implements AttributeConverter<JsonObject, String> {
    private static final Gson GSON = new Gson();

    @Override
    public String convertToDatabaseColumn(JsonObject attribute) {
        return GSON.toJson(attribute);
    }

    @Override
    public JsonObject convertToEntityAttribute(String dbData) {
        return JsonParser.parseString(dbData).getAsJsonObject();
    }
}
