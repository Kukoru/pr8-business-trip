package ru.edu.pr05;

import java.time.LocalDate;

public class BusinessTrip {
    private final String id; 
    private final LocalDate start; 
    private final LocalDate end; 
    private TripStatus status = TripStatus.DRAFT; 
    private String rejectionReason;

    // Вариант 7: Минимальная длина причины = 40
    public static final int MIN_REASON_LENGTH = 40;

    public BusinessTrip(String id, LocalDate start, LocalDate end) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("ID не может быть пустым");
        if (start == null || end == null) throw new IllegalArgumentException("Даты не могут быть null");
        if (end.isBefore(start)) throw new IllegalArgumentException("Дата окончания не может быть раньше даты начала");
        
        this.id = id;
        this.start = start;
        this.end = end;
    }

    public void submit() {
        if (status != TripStatus.DRAFT) {
            throw new IllegalStateException("Отправить можно только из статуса DRAFT");
        }
        this.status = TripStatus.SUBMITTED;
    }

    public void approve() {
        if (status != TripStatus.SUBMITTED) {
            throw new IllegalStateException("Одобрить можно только из статуса SUBMITTED");
        }
        this.status = TripStatus.APPROVED;
    }

    public void reject(String reason) {
        if (status != TripStatus.SUBMITTED) {
            throw new IllegalStateException("Отклонить можно только из статуса SUBMITTED");
        }
        // Вариант 7: проверка длины причины
        if (reason == null || reason.trim().length() < MIN_REASON_LENGTH) {
            throw new IllegalArgumentException("Причина отклонения должна содержать минимум " + MIN_REASON_LENGTH + " символов");
        }
        this.rejectionReason = reason;
        this.status = TripStatus.REJECTED;
    }

    public void cancel() {
        if (status == TripStatus.COMPLETED || status == TripStatus.CANCELLED || status == TripStatus.REJECTED) {
            throw new IllegalStateException("Отмена невозможна из статуса " + status);
        }
        this.status = TripStatus.CANCELLED;
    }

    public void complete(LocalDate today) {
        if (status != TripStatus.APPROVED) {
            throw new IllegalStateException("Завершить можно только из статуса APPROVED");
        }
        if (today.isBefore(end)) {
            throw new IllegalStateException("Командировка еще не окончена (дата окончания: " + end + ")");
        }
        this.status = TripStatus.COMPLETED;
    }

    public TripStatus getStatus() { return status; } 
    public String getRejectionReason() { return rejectionReason; }
}