package in.bugr.management.component;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @author BugRui
 * @date 2020/3/23 下午8:10
 **/
public class ListLongConverter implements AttributeConverter<List<Long>, String> {
    private static final Gson GSON = new Gson();

    @Override
    public String convertToDatabaseColumn(List<Long> attribute) {
        return GSON.toJson(attribute);
    }

    @Override
    public List<Long> convertToEntityAttribute(String dbData) {
        return GSON.fromJson(dbData, new TypeToken<CopyOnWriteArrayList<Long>>() {
        }.getType());
    }
}
