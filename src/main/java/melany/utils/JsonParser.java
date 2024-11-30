package melany.utils;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import melany.model.SalesDoc;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility class responsible for parsing JSON
 *
 * @author andjela.djekic
 */
public class JsonParser {

    private final Gson gson;

    public JsonParser() {
        this.gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter()).setFieldNamingStrategy(new UpperCaseFields()).create();
    }

    public String listToJSON(List<?> list, String key) {
        Map<String, List<?>> map = new HashMap<>();
        map.put(key, list);
        return gson.toJson(map);
    }

    public String mapToJSON(Map<?, ?> map) {
        return gson.toJson(map);
    }

    /**
     * Custom utility class for converting key names in JSON to uppercase.
     */
    private static class UpperCaseFields implements FieldNamingStrategy {
        @Override
        public String translateName(Field field) {
            return field.getName().toUpperCase();
        }
    }


}
