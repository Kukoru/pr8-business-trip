package ru.edu.pr05;

import java.time.LocalDate;

public class SelfCheck {
    public static void main(String[] args) {
        boolean ok = true;
        try {
            LocalDate start = LocalDate.of(2026, 10, 10);
            LocalDate end = LocalDate.of(2026, 10, 12);
            BusinessTrip trip = new BusinessTrip("T-01", start, end);

            // 1. Проверка submit
            trip.submit();
            ok &= trip.getStatus() == TripStatus.SUBMITTED;

            // 2. Проверка длины причины (39 символов - ошибка)
            try {
                trip.reject("Короткая причина (всего 39 символов)"); 
                ok = false;
            } catch (IllegalArgumentException e) {}

            // 3. Проверка длины причины (40+ символов - успех)
            String longReason = "Это причина отклонения, которая содержит более сорока символов.";
            trip.reject(longReason);
            ok &= trip.getStatus() == TripStatus.REJECTED;

            // 4. Проверка cancel (нельзя из REJECTED)
            try {
                trip.cancel();
                ok = false;
            } catch (IllegalStateException e) {}

            // 5. Проверка полного цикла (approve -> complete)
            BusinessTrip trip2 = new BusinessTrip("T-02", start, end);
            trip2.submit();
            trip2.approve();
            trip2.complete(end.plusDays(1)); // Завершаем после даты окончания
            ok &= trip2.getStatus() == TripStatus.COMPLETED;

            // 6. Проверка даты (нельзя завершить до окончания)
            BusinessTrip trip3 = new BusinessTrip("T-03", start, end);
            trip3.submit();
            trip3.approve();
            try {
                trip3.complete(end.minusDays(1)); // Раньше окончания
                ok = false;
            } catch (IllegalStateException e) {}

        } catch (Exception e) {
            System.out.println("FAIL exception " + e.getMessage());
            ok = false;
        }
        System.out.println(ok ? "PASS" : "FAIL  complete TODO methods");
    }
}