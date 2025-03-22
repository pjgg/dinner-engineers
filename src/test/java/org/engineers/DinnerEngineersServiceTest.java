package org.engineers;

import static org.engineers.TimeSlot.fromStrings;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import org.engineers.utils.FileUtils;
import org.engineers.utils.HtmlReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

public class DinnerEngineersServiceTest {
    private static final String TEST_DATA_FOLDER_NAME = "availability_test_data";
    private String inputDataFolder;
    private FileUtils fileUtils;
    private DinnerEngineersService dinnerEngineersBruteForce;
    private DinnerEngineersService dinnerEngineersGreedy;
    private Random random;

    @BeforeEach
    void setUp() {
        this.fileUtils = new FileUtils(new Gson());
        this.dinnerEngineersBruteForce = new DinnerEngineersBruteForceImpl();
        this.dinnerEngineersGreedy = new DinnerEngineersGreedyImpl();
        this.random = new Random();
        inputDataFolder = System.getProperty("data.folder", TEST_DATA_FOLDER_NAME);
    }

    @Test
    void testFindBestMeetingDatesBruteForce() {
        Map<String, List<TimeSlot>> availabilityMap = loadAvailability();
        List<LocalDateTime> bestMeetingDates = dinnerEngineersBruteForce.findBestMeetingDates(availabilityMap);

        assertNotNull(bestMeetingDates);
        assertFalse(bestMeetingDates.isEmpty(), "No common meeting dates found");
        assertTrue(bestMeetingDates.contains(LocalDateTime.of(2025, 3, 31, 19, 00)), "Expected meeting date missing");

        if (requireAnHtmlReport(inputDataFolder)) {
            LocalDateTime selectedDate = bestMeetingDates.get(random.nextInt(bestMeetingDates.size())); // Randomly
                                                                                                        // select a date
            HtmlReport.writeHtmlResult(bestMeetingDates, selectedDate);
        }
    }

    @Test
    void testFindBestMeetingDatesGreedy() {
        Map<String, List<TimeSlot>> availabilityMap = loadAvailability();
        List<LocalDateTime> bestMeetingDates = dinnerEngineersGreedy.findBestMeetingDates(availabilityMap);

        assertNotNull(bestMeetingDates);
        assertFalse(bestMeetingDates.isEmpty(), "No common meeting dates found");
        assertTrue(bestMeetingDates.contains(LocalDateTime.of(2025, 3, 31, 19, 00)), "Expected meeting date missing");
    }

    private Map<String, List<TimeSlot>> loadAvailability() {
        Map<String, List<TimeSlot>> availabilityMap = new HashMap<>();
        ClassLoader classLoader = getClass().getClassLoader();
        try (var inputStream = classLoader.getResourceAsStream(inputDataFolder)) {
            if (inputStream == null) {
                throw new RuntimeException("Availability folder not found: " + inputDataFolder);
            }

            String folderContents = new Scanner(inputStream, StandardCharsets.UTF_8).useDelimiter("\\A").next();
            String[] fileNames = folderContents.split("\n");

            for (String fileName : fileNames) {
                if (fileName.endsWith(".json")) {
                    String resourcePath = "/" + inputDataFolder + "/" + fileName;
                    JsonArray jsonArray = fileUtils.loadFileAsJson(resourcePath);
                    List<String> availabilityStrings = new Gson().fromJson(jsonArray, List.class);
                    List<TimeSlot> timeSlots = fromStrings(availabilityStrings);
                    String key = fileName.replace(".json", "");
                    availabilityMap.put(key, timeSlots);
                }
            }
        } catch (Exception e) {
            fail("Failed to load resource: " + inputDataFolder, e);
        }

        return availabilityMap;
    }

    private boolean requireAnHtmlReport(String dataFolder) {
        return dataFolder.equals("availability");
    }
}
