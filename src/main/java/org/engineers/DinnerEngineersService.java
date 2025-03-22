package org.engineers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface DinnerEngineersService {
    List<LocalDateTime> findBestMeetingDates(Map<String, List<TimeSlot>> availabilityMap);
}
