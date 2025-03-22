package org.engineers.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class HtmlReport {

    private final static String REPORT_FILE_NAME = "index.html";
    private final static String[] LOCATION = { "docs" };

    public static void writeHtmlResult(List<LocalDateTime> bestMeetingDates, LocalDateTime selectedDate) {
        String datesHtml = bestMeetingDates.stream()
                .map(date -> "<li>" + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "</li>")
                .collect(Collectors.joining("\n"));

        String selectedDateHtml = selectedDate != null
                ? "<p><strong>Selected Meeting Date: </strong>"
                        + selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "</p>"
                : "";

        String htmlContent = "<html>\n" + "<head>\n" + "<title>Best Meeting Dates</title>\n" + "<style>\n"
                + "body { font-family: Arial, sans-serif; margin: 40px; }\n" + "h1 { color: #4CAF50; }\n"
                + "ul { list-style-type: none; padding: 0; }\n"
                + "li { background-color: #f2f2f2; margin: 10px 0; padding: 10px; border-radius: 5px; }\n"
                + "</style>\n" + "</head>\n" + "<body>\n" + "<h1>Available Meeting Dates</h1>\n" + selectedDateHtml
                + "<ul>\n" + datesHtml + "\n" + "</ul>\n" + "</body>\n" + "</html>";

        Path resourcePath = Paths.get(String.join(File.separator, LOCATION));
        File directory = resourcePath.toFile();
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(directory, REPORT_FILE_NAME);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(htmlContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
