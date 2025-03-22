package org.engineers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class DinnerEngineersBruteForceImpl implements DinnerEngineersService {

    @Override
    public List<LocalDateTime> findBestMeetingDates(Map<String, List<TimeSlot>> availabilityMap) {
        if (availabilityMap.isEmpty()) {
            return List.of();
        }

        List<List<TimeSlot>> allTimeSlots = new ArrayList<>(availabilityMap.values());
        List<TimeSlot> commonTimeSlots = findCommonTimeSlots(allTimeSlots);
        return commonTimeSlots.stream().map(TimeSlot::start).collect(Collectors.toList());
    }

    /**
     * Finds the intersection of time slots across all participants.
     */
    private List<TimeSlot> findCommonTimeSlots(List<List<TimeSlot>> allTimeSlots) {
        List<TimeSlot> commonTimeSlots = new ArrayList<>(allTimeSlots.get(0));
        for (int i = 1; i < allTimeSlots.size(); i++) {
            List<TimeSlot> currentParticipantTimeSlots = allTimeSlots.get(i);
            commonTimeSlots = findOverlappingTimeSlots(commonTimeSlots, currentParticipantTimeSlots);

            if (commonTimeSlots.isEmpty()) {
                break;
            }
        }

        return commonTimeSlots;
    }

    /**
     * Finds overlapping time slots between two lists of time slots.
     */
    private List<TimeSlot> findOverlappingTimeSlots(List<TimeSlot> timeSlots1, List<TimeSlot> timeSlots2) {
        List<TimeSlot> overlappingTimeSlots = new ArrayList<>();

        for (TimeSlot slot1 : timeSlots1) {
            for (TimeSlot slot2 : timeSlots2) {
                Optional<TimeSlot> overlap = slot1.getOverlap(slot2);
                overlap.ifPresent(overlappingTimeSlots::add);
            }
        }

        return overlappingTimeSlots;
    }
}
