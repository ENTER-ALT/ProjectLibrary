package be.ucll.utilits;

/// Used for Publications to get current Year and for Tests to set custom Year for testing purposes.

public class TimeTracker {
    private static Integer Year;

    public static void SetCustomYear(Integer customYear) {
        Year = customYear;
    }

    public static void ResetYear() {
        Year = null;
    }

    public static Integer GetCurrentYear() {
        if (Year != null) {
            return Year;
        }

        return java.time.Year.now().getValue();
    }
}
