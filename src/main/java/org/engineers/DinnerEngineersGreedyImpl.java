package org.engineers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class DinnerEngineersGreedyImpl implements DinnerEngineersService {
    @Override
    public List<LocalDateTime> findBestMeetingDates(Map<String, List<TimeSlot>> availabilityMap) {
        if (availabilityMap.isEmpty()) {
            return List.of();
        }

        List<TimeSlot> allTimeSlots = availabilityMap.values().stream().flatMap(List::stream)
                .sorted(Comparator.comparing(TimeSlot::start)).collect(Collectors.toList());

        List<TimeSlot> commonTimeSlots = findCommonTimeSlotsGreedy(allTimeSlots, availabilityMap.size());
        return commonTimeSlots.stream().map(TimeSlot::start).collect(Collectors.toList());
    }

    /**
     * Finds overlapping time slots using a greedy approach.
     *
     * @param allTimeSlots
     *            All time slots sorted by start time.
     * @param numParticipants
     *            The number of participants.
     *
     * @return A list of overlapping time slots.
     */
    private List<TimeSlot> findCommonTimeSlotsGreedy(List<TimeSlot> allTimeSlots, int numParticipants) {
        allTimeSlots.sort(Comparator.comparing(TimeSlot::start));

        List<TimeSlot> commonTimeSlots = new ArrayList<>();
        Map<TimeSlot, Integer> overlapCount = new HashMap<>();

        for (int i = 0; i < allTimeSlots.size(); i++) {
            TimeSlot slot = allTimeSlots.get(i);
            int count = 1;
            for (int j = i + 1; j < allTimeSlots.size(); j++) {
                TimeSlot otherSlot = allTimeSlots.get(j);
                Optional<TimeSlot> overlap = slot.getOverlap(otherSlot);
                if (overlap.isPresent()) {
                    count++;
                    if (count == numParticipants) {
                        if (!commonTimeSlots.contains(overlap.get())) {
                            commonTimeSlots.add(overlap.get());
                        }
                    }
                }
            }
        }

        return commonTimeSlots;
    }
}
