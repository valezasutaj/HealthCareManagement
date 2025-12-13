package model;

public enum AppointmentStatus {
    SCHEDULED("I planifikuar"),
    COMPLETED("I perfunduar"),
    CANCELLED("I anuluar");

    private final String label;

    AppointmentStatus(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}