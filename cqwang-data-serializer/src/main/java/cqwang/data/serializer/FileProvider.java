package cqwang.data.serializer;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileProvider {

    public static <T> T readFile(String path, TypeReference<T> typeReference) {
        try {
            InputStream inputStream = FileProvider.class.getResourceAsStream(path);
            if (inputStream == null) {
                throw new RuntimeException("File not found: " + path);
            }
            return JSON.getMapper().readValue(inputStream, typeReference);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read file: " + path, e);
        }
    }

    public static <T> T readFile(String file, Class<T> clazz) {
        try {
            InputStream inputStream = FileProvider.class.getResourceAsStream(file);
            if (inputStream == null) {
                throw new RuntimeException("File not found: " + file);
            }
            return JSON.getMapper().readValue(inputStream, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read file: " + file, e);
        }
    }
}

