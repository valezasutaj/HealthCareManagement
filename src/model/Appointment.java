package model;

import java.util.Date;
import java.text.SimpleDateFormat;

public class Appointment {
    private int id;
    private Patient patient ;
    private Doctor staff ;
    private Date date ;
    private String report;
    private AppointmentStatus status;

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
        this.status = AppointmentStatus.SCHEDULED;
        this.report = null;
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

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
        if (status != AppointmentStatus.COMPLETED) {
            this.report = null;
        }
    }

    public void setReport(String report) throws CustomException {
        if (status != AppointmentStatus.COMPLETED) {
            throw new CustomException("Raporti lejohet vetem kur termini eshte i perfunduar!");
        }
        this.report = report;
    }

    public String toString() {
        String patientInfo = (patient == null) ? "-" : patient.getId() + " - " + patient.getName();
        String doctorInfo = (staff == null) ? "-" : staff.getId() + " - " + staff.getName();
        String dateStr = (date == null) ? "-" : new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
        String statusStr = (status == null) ? "-" : status.toString();
        String reportStr;
        if (status == AppointmentStatus.COMPLETED) {
            reportStr = (report == null || report.isBlank()) ? "-" : report;
        } else {
            reportStr = "(raporti shfaqet pas perfundimit te terminit!)";
        }

        return String.format(
                "Termin\n" +
                        "  ID       : %d\n" +
                        "  Pacienti : %s\n" +
                        "  Doktori  : %s\n" +
                        "  Data     : %s\n" +
                        "  Status   : %s\n" +
                        "  Raporti  : %s",
                id,
                patientInfo,
                doctorInfo,
                dateStr,
                statusStr,
                reportStr
        );
    }
}
