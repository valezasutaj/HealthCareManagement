package model;

import java.util.Date;

public class Appointment {
    private int id;
    private Patient patient ;
    private Doctor staff ;
    private Date date ;
    private String report;
    AppointmentStatus as;

    public Appointment(int id, Patient patient, Doctor staff, Date date, String report) throws CustomException {
        if(id < 0){
            throw new CustomException("ID duhet te jete pozitive!");
        }
        if(patient == null){
            throw new CustomException("Pacienti duhet specifikuar!");
        }
        if(staff == null){
            throw new CustomException("Doktori duhet specifikuar!");
        }
        if(date == null){
            throw new CustomException("Data duhet specifikuar!");
        }
        this.id = id;
        this.patient = patient;
        this.staff = staff;
        this.date = date;
        this.report = report;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getStaff() {
        return staff;
    }

    public void setStaff(Doctor staff) {
        this.staff = staff;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public AppointmentStatus getAs() {
        return as;
    }

    public void setAs(AppointmentStatus as) {
        this.as = as;
    }
}
