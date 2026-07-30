package cqwang.data.serializer;


import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileProvider {

    /**
     * 使用绝对路径读取文件
     * @param path
     * @return
     */
    public static String readFileContent(String path){
        try {
            return Files.readString(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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

    // 无法向resourc目录写入文件，可以指定其他目录
//
//    public static <T> void writeFile(String path, T data, TypeReference<T> typeReference) {
//        try {
//            String jsonString = JSON.getMapper().writerFor(typeReference).writeValueAsString(data);
//            Files.write(Paths.get(path), jsonString.getBytes());
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to write file: " + path, e);
//        }
//    }
//
//    public static <T> void writeFile(String path, T data, Class<T> clazz) {
//        try {
//            String jsonString = JSON.getMapper().writerFor(clazz).writeValueAsString(data);
//            Files.write(Paths.get(path), jsonString.getBytes());
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to write file: " + path, e);
//        }
//    }
}

