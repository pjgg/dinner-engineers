package org.engineers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public record TimeSlot(LocalDateTime start, LocalDateTime end) {
    private final static int DATE_PART = 0;
    private final static int HOUR_PART = 1;
    private final static int START_HOUR = 0;
    private final static int END_HOUR = 1;
    private final static String HOUR_SEPARATOR = " \\| ";

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static List<TimeSlot> fromStrings(List<String> timeSlots) {
        return timeSlots.stream().map(TimeSlot::new).toList();
    }

    public TimeSlot(String slot) {
        this(parseDateTime(slot, START_HOUR), parseDateTime(slot, END_HOUR));
    }

    /**
     * Computes the overlap between this time slot and another time slot.
     *
     * @param other
     *            The other time slot to compare with.
     *
     * @return An Optional containing the overlapping TimeSlot, or empty if no overlap exists.
     */
    public Optional<TimeSlot> getOverlap(TimeSlot other) {
        LocalDateTime overlapStart = this.start.isAfter(other.start) ? this.start : other.start;
        LocalDateTime overlapEnd = this.end.isBefore(other.end) ? this.end : other.end;

        if (overlapStart.isBefore(overlapEnd)) {
            return Optional.of(new TimeSlot(overlapStart, overlapEnd));
        } else {
            return Optional.empty();
        }
    }

    private static LocalDateTime parseDateTime(String slot, int hourIndex) {
        String sanitizedSlot = slot.replaceAll("\\s*-\\s*", " - ").replaceAll("\\s*\\|\\s*", " | ");
        String[] parts = sanitizedSlot.split(" - ");
        String datePart = parts[DATE_PART];
        String[] hours = parts[HOUR_PART].split(HOUR_SEPARATOR);
        return LocalDateTime.parse(datePart + " " + hours[hourIndex], DATE_TIME_FORMATTER);
    }
}
