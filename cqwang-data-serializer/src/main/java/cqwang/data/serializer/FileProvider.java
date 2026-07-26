package cqwang.data.serializer;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;

public class FileProvider {
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> T readFile(String path, TypeReference<T> typeReference) {
        try {
            InputStream inputStream = FileProvider.class.getResourceAsStream(path);
            if (inputStream == null) {
                throw new RuntimeException("File not found: " + path);
            }
            return mapper.readValue(inputStream, typeReference);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read file: " + path, e);
        }
    }
}

