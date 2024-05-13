package be.ucll.utilits;

import java.time.LocalDate;

/// Used for Publications to get current DateTime values and for Tests to set custom DateTime values

public class TimeTracker {
    private static Integer year;
    private static LocalDate today;

    public static void setCustomYear(Integer customYear) {
        year = customYear;
    }

    public static void setCustomToday(LocalDate customToday) {
        today = customToday;
    }

    public static void resetYear() {
        year = null;
    }

    public static void resetToday() {
        today = null;
    }

    public static Integer getCurrentYear() {
        if (year != null) {
            return year;
        }

        return java.time.Year.now().getValue();
    }

    public static LocalDate getToday() {
        if (today != null) {
            return today;
        }

        return LocalDate.now();
    }
}
