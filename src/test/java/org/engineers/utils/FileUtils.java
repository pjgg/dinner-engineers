package org.engineers.utils;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class FileUtils {

    private Gson gson;
    private Type contentType;

    public FileUtils(Gson gson) {
        this.gson = gson;
        this.contentType = new TypeToken<List<String>>() {
        }.getType();
    }

    public String loadFile(String file) {
        try {
            return IOUtils.toString(FileUtils.class.getResourceAsStream(file), StandardCharsets.UTF_8);
        } catch (IOException e) {
            fail("Could not load file " + file + " . Caused by " + e.getMessage());
        }

        return "";
    }

    public JsonArray loadFileAsJson(String file) {
        try {
            String fileAsString = loadFile(file);
            List<String> stringList = this.gson.fromJson(fileAsString, this.contentType);

            JsonArray jsonArray = new JsonArray();
            for (String item : stringList) {
                jsonArray.add(item);
            }
            return jsonArray;
        } catch (JsonSyntaxException e) {
            fail("Could not load file " + file + " . Caused by " + e.getMessage());
        }

        return new JsonArray();
    }
}
