package app;
import model.*;
import repository.*;
import service.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class HealthApp {

    private final PatientRepository patientRepo;
    private final DoctorRepository doctorRepo;
    private final AppointmentRepository appointmentRepo;
    private final Scanner sc;

    private final SimpleDateFormat dtFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ROOT);

    public HealthApp() {
        this.patientRepo = new PatientRepository();
        this.doctorRepo = new DoctorRepository();
        this.appointmentRepo = new AppointmentRepository(patientRepo, doctorRepo);
        this.sc = new Scanner(System.in);

        dtFmt.setLenient(false);
    }

    public static void main(String[] args) {
        new HealthApp().run();
    }

    public void run() {
        System.out.println("=== Sistemi i Menaxhimit te Shendetit (CLI) ===");

        while (true) {
            printMainMenu();
            String choice = readNonEmpty("Zgjedhja: ").toUpperCase(Locale.ROOT);

            switch (choice) {
                case "P" -> patientMenu();
                case "D" -> doctorMenu();
                case "T" -> appointmentMenu();
                case "N" -> notificationMenu();
                case "Q" -> {
                    System.out.println("Dalje...\nMirupafshim!");
                    return;
                }
                default -> System.out.println("Opsion i pavlefshem!\n");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n--- MENU KRYESORE ---");
        System.out.println("P - Pacientet");
        System.out.println("D - Doktoret");
        System.out.println("T - Terminet");
        System.out.println("N - Dërgo njoftim");
        System.out.println("Q - Dil");
    }

    private void patientMenu() {
        while (true) {
            System.out.println("\n--- PACIENTET ---");
            System.out.println("1 - Regjistro pacient");
            System.out.println("2 - Shfaq pacientet");
            System.out.println("3 - Kerko pacient me ID");
            System.out.println("4 - Modifiko pacient");
            System.out.println("5 - Fshij pacient");
            System.out.println("B - Kthehu");

            String c = readNonEmpty("Zgjedhja: ").toUpperCase(Locale.ROOT);
            try {
                switch (c) {
                    case "1" -> addPatient();
                    case "2" -> listPatients();
                    case "3" -> findPatientById();
                    case "4" -> updatePatient();
                    case "5" -> removePatient();
                    case "B" -> {return;}
                    default -> System.out.println("Opsion i pavlefshem!");
                }
            } catch (CustomException e) {
                System.out.println("Gabim: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Gabim i papritur: " + e.getMessage());
            }
        }
    }

    private void doctorMenu() {
        while (true) {
            System.out.println("\n--- DOKTORET ---");
            System.out.println("1 - Regjistro doktor");
            System.out.println("2 - Shfaq doktoret");
            System.out.println("3 - Kerko doktor me ID");
            System.out.println("4 - Modifiko doktor");
            System.out.println("5 - Fshij doktor");
            System.out.println("B - Kthehu");

            String c = readNonEmpty("Zgjedhja: ").toUpperCase(Locale.ROOT);
            try {
                switch (c) {
                    case "1" -> addDoctor();
                    case "2" -> listDoctors();
                    case "3" -> findDoctorById();
                    case "4" -> updateDoctor();
                    case "5" -> removeDoctor();
                    case "B" -> {return;}
                    default -> System.out.println("Opsion i pavlefshem!");
                }
            } catch (CustomException e) {
                System.out.println("Gabim: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Gabim i papritur: " + e.getMessage());
            }
        }
    }

    private void appointmentMenu() {
        while (true) {
            System.out.println("\n--- TERMINET ---");
            System.out.println("1 - Planifiko termin");
            System.out.println("2 - Shfaq te gjitha terminet");
            System.out.println("3 - Shfaq terminet per pacient");
            System.out.println("4 - Shfaq terminet per status");
            System.out.println("5 - Perfundo termin (vendos raport + COMPLETED)");
            System.out.println("6 - Anulo termin (CANCELLED)");
            System.out.println("B - Kthehu");

            String c = readNonEmpty("Zgjedhja: ").toUpperCase(Locale.ROOT);
            try {
                switch (c) {
                    case "1" -> scheduleAppointment();
                    case "2" -> listAppointments();
                    case "3" -> listAppointmentsByPatient();
                    case "4" -> listAppointmentsByStatus();
                    case "5" -> completeAppointment();
                    case "6" -> cancelAppointment();
                    case "B" -> {return;}
                    default -> System.out.println("Opsion i pavlefshem!");
                }
            } catch (CustomException e) {
                System.out.println("Gabim: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Gabim i papritur: " + e.getMessage());
            }
        }
    }

    private void notificationMenu() {
        while (true) {
            System.out.println("\n--- NJOFTIMET ---");
            System.out.println("1 - Dergo Email");
            System.out.println("2 - Dergo SMS");
            System.out.println("B - Kthehu");

            String c = readNonEmpty("Zgjedhja: ").toUpperCase(Locale.ROOT);
            try {
                switch (c) {
                    case "1" -> sendEmailNotification();
                    case "2" -> sendSmsNotification();
                    case "B" -> {return;}
                    default -> System.out.println("Opsion i pavlefshem!");
                }
            } catch (CustomException e) {
                System.out.println("Gabim: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Gabim i papritur: " + e.getMessage());
            }
        }
    }

    private void addPatient() throws CustomException {
        System.out.println("\n--- Regjistrim pacienti ---");

        int suggestedId = nextPatientId();
        System.out.println("Sugjerim ID (automatik): " + suggestedId);
        int id = readIntWithDefault("ID (shtyp Enter per " + suggestedId + "): ", suggestedId);

        String name = readNonEmpty("Emri: ");
        String phone = readLine("Tel: ");
        String email = readLine("Email: ");
        int age = readInt("Mosha: ");

        Patient p = new Patient(id, name, phone, email, age);
        patientRepo.addPatient(p);
        System.out.println("U regjistrua: " + p.getDetails());
    }

    private void listPatients() {
        List<Patient> list = patientRepo.getAllPatients();
        if (list.isEmpty()) {
            System.out.println("Nuk ka paciente te regjistruar.");
            return;
        }
        System.out.println("\n--- Lista e pacienteve (" + list.size() + ") ---");
        for (Patient p : list) {
            System.out.println(p.getDetails());
        }
    }

    private void findPatientById() throws CustomException {
        int id = readInt("ID e pacientit: ");
        Patient p = patientRepo.getPatientById(id);
        System.out.println(p.getDetails());
    }

    private void updatePatient() throws CustomException {
        System.out.println("\n--- Modifikim pacienti ---");
        int id = readInt("ID e pacientit: ");
        Patient old = patientRepo.getPatientById(id);

        System.out.println("Aktual: " + old.getDetails());
        System.out.println("Sheno vlerat e reja (Enter per ta lene siç eshte).\n");

        String name = readLineWithDefault("Emri: ", old.getName());
        String phone = readLineWithDefault("Tel: ", old.getPhone());
        String email = readLineWithDefault("Email: ", old.getEmail());
        int age = readIntWithDefault("Mosha: ", old.getAge());

        Patient updated = new Patient(id, name, phone, email, age);
        patientRepo.updatePatient(updated);
        System.out.println("U perditesua: " + updated.getDetails());
    }

    private void removePatient() {
        int id = readInt("ID e pacientit per fshirje: ");
        boolean ok = patientRepo.removePatient(id);
        System.out.println(ok ? "Pacienti u fshi." : "Pacienti nuk u gjet.");
    }

    private int nextPatientId() {
        int max = 0;
        for (Patient p : patientRepo.getAllPatients()) {
            if (p.getId() > max) max = p.getId();
        }
        return max + 1;
    }

    private void addDoctor() throws CustomException {
        System.out.println("\n--- Regjistrim doktori ---");
        String name = readNonEmpty("Emri: ");
        String phone = readLine("Tel: ");
        String email = readLine("Email: ");
        String speciality = readNonEmpty("Specializimi: ");

        Doctor d = doctorRepo.createDoctor(name, phone, email, speciality);
        System.out.println("U regjistrua: " + d.getDetails());
    }

    private void listDoctors() {
        List<Doctor> list = doctorRepo.getAllDoctors();
        if (list.isEmpty()) {
            System.out.println("Nuk ka doktore te regjistruar.");
            return;
        }
        System.out.println("\n--- Lista e doktoreve (" + list.size() + ") ---");
        for (Doctor d : list) {
            System.out.println(d.getDetails());
        }
    }

    private void findDoctorById() throws CustomException {
        int id = readInt("ID e doktorit: ");
        Doctor d = doctorRepo.getDoctorById(id);
        System.out.println(d.getDetails());
    }

    private void updateDoctor() throws CustomException {
        System.out.println("\n--- Modifikim doktori ---");
        int id = readInt("ID e doktorit: ");
        Doctor old = doctorRepo.getDoctorById(id);

        System.out.println("Aktual: " + old.getDetails());
        System.out.println("Sheno vlerat e reja (Enter per ta lene siç eshte).\n");

        String name = readLineWithDefault("Emri: ", old.getName());
        String phone = readLineWithDefault("Tel: ", old.getPhone());
        String email = readLineWithDefault("Email: ", old.getEmail());
        String speciality = readLineWithDefault("Specializimi: ", old.getSpeciality());

        Doctor updated = new Doctor(id, name, phone, email, speciality);
        doctorRepo.updateDoctor(updated);
        System.out.println("U perditesua: " + updated.getDetails());
    }

    private void removeDoctor() {
        int id = readInt("ID e doktorit per fshirje: ");
        boolean ok = doctorRepo.removeDoctor(id);
        System.out.println(ok ? "Doktori u fshi." : "Doktori nuk u gjet.");
    }

    private void scheduleAppointment() throws CustomException {
        System.out.println("\n--- Planifikim termini ---");

        if (patientRepo.getAllPatients().isEmpty()) {
            System.out.println("Nuk ka paciente! Regjistro pacient fillimisht.");
            return;
        }
        if (doctorRepo.getAllDoctors().isEmpty()) {
            System.out.println("Nuk ka doktore! Regjistro doktor fillimisht.");
            return;
        }

        listPatientsShort();
        int patientId = readInt("Zgjedh ID e pacientit: ");

        listDoctorsShort();
        int doctorId = readInt("Zgjedh ID e doktorit: ");

        Date date = readDate("Data/ora (yyyy-MM-dd HH:mm): ");

        Appointment a = appointmentRepo.createAppointment(patientId, doctorId, date, "");
        a.setStatus(AppointmentStatus.SCHEDULED);
        appointmentRepo.updateAppointment(a);

        System.out.println("U planifikua termini: ID=" + a.getId() + ", Status=" + statusSq(a.getStatus()));
    }

    private void listAppointments() {
        List<Appointment> list = appointmentRepo.getAllAppointments();
        if (list.isEmpty()) {
            System.out.println("Nuk ka termine te regjistruara.");
            return;
        }
        System.out.println("\n--- Lista e termineve (" + list.size() + ") ---");
        for (Appointment a : list) {
            System.out.println(formatAppointment(a));
        }
    }

    private void listAppointmentsByPatient() {
        int patientId = readInt("ID e pacientit: ");
        List<Appointment> list = appointmentRepo.getAllAppointments();

        boolean any = false;
        for (Appointment a : list) {
            if (a.getPatient() != null && a.getPatient().getId() == patientId) {
                if (!any) {
                    System.out.println("\n--- Terminet per pacientin ID=" + patientId + " ---");
                    any = true;
                }
                System.out.println(formatAppointment(a));
            }
        }
        if (!any) System.out.println("Nuk u gjet asnje termin per kete pacient.");
    }

    private void listAppointmentsByStatus() {
        AppointmentStatus st = readStatus();
        List<Appointment> list = appointmentRepo.getAllAppointments();

        boolean any = false;
        for (Appointment a : list) {
            if (a.getStatus() == st) {
                if (!any) {
                    System.out.println("\n--- Terminet me status: " + statusSq(st) + " ---");
                    any = true;
                }
                System.out.println(formatAppointment(a));
            }
        }
        if (!any) System.out.println("Nuk u gjet asnje termin me kete status.");
    }

    private void completeAppointment() throws CustomException {
        int id = readInt("ID e terminit: ");
        Appointment a = appointmentRepo.getAppointmentById(id);

        String report = readNonEmpty("Raporti (i detyrueshem): ");
        a.setReport(report);
        a.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepo.updateAppointment(a);

        System.out.println("Termini u perfundua. Status=" + statusSq(a.getStatus()));
    }

    private void cancelAppointment() throws CustomException {
        int id = readInt("ID e terminit: ");
        Appointment a = appointmentRepo.getAppointmentById(id);
        a.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepo.updateAppointment(a);
        System.out.println("Termini u anulua. Status=" + statusSq(a.getStatus()));
    }

    private void listPatientsShort() {
        System.out.println("\nPacientet:");
        for (Patient p : patientRepo.getAllPatients()) {
            System.out.println("  - ID=" + p.getId() + ", Emri=" + p.getName());
        }
    }

    private void listDoctorsShort() {
        System.out.println("\nDoktoret:");
        for (Doctor d : doctorRepo.getAllDoctors()) {
            System.out.println("  - ID=" + d.getId() + ", Emri=" + d.getName() + ", Specializimi=" + d.getSpeciality());
        }
    }

    private String formatAppointment(Appointment a) {
        String dateStr = (a.getDate() == null) ? "" : dtFmt.format(a.getDate());
        String p = (a.getPatient() == null) ? "" : (a.getPatient().getId() + " - " + a.getPatient().getName());
        String d = (a.getStaff() == null) ? "" : (a.getStaff().getId() + " - " + a.getStaff().getName());

        String report = (a.getReport() == null || a.getReport().isBlank()) ? "(pa raport)" : a.getReport();
        if (a.getStatus() != AppointmentStatus.COMPLETED) {
            report = "(raporti shfaqet vetem kur perfundon)";
        }

        return "Appointment ID=" + a.getId() +
                " | Pacienti=" + p +
                " | Doktori=" + d +
                " | Data=" + dateStr +
                " | Status=" + statusSq(a.getStatus()) +
                " | Raporti=" + report;
    }

    private String statusSq(AppointmentStatus st) {
        if (st == null) return "(pa status)";
        return switch (st) {
            case SCHEDULED -> "I planifikuar";
            case COMPLETED -> "I perfunduar";
            case CANCELLED -> "I anuluar";
        };
    }

    private AppointmentStatus readStatus() {
        while (true) {
            System.out.println("Statuset: 1=SCHEDULED (I planifikuar), 2=COMPLETED (I perfunduar), 3=CANCELLED (I anuluar)");
            String s = readNonEmpty("Zgjedh statusin (1/2/3): ");
            switch (s) {
                case "1" -> {return AppointmentStatus.SCHEDULED;}
                case "2" -> {return AppointmentStatus.COMPLETED;}
                case "3" -> {return AppointmentStatus.CANCELLED;}
                default -> System.out.println("Opsion i pavlefshem!");
            }
        }
    }

    private void sendEmailNotification() throws CustomException {
        int patientId = readInt("ID e pacientit: ");
        Patient p = patientRepo.getPatientById(patientId);
        if (p.getEmail() == null || p.getEmail().isBlank()) {
            System.out.println("Pacienti nuk ka email.");
            return;
        }
        String msg = readNonEmpty("Mesazhi: ");
        NotificationService svc = new EmailService(p.getEmail());
        svc.sendNotification("Per: " + p.getName() + " | " + msg);
    }

    private void sendSmsNotification() throws CustomException {
        int patientId = readInt("ID e pacientit: ");
        Patient p = patientRepo.getPatientById(patientId);
        if (p.getPhone() == null || p.getPhone().isBlank()) {
            System.out.println("Pacienti nuk ka numer telefoni.");
            return;
        }
        String msg = readNonEmpty("Mesazhi: ");
        NotificationService svc = new SMSService(p.getPhone());
        svc.sendNotification("Per: " + p.getName() + " | " + msg);
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return sc.nextLine();
    }

    private String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine();
            if (s != null && !s.trim().isEmpty()) return s.trim();
            System.out.println("Nuk lejohet bosh!");
        }
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine();
            try {
                return Integer.parseInt(s.trim());
            } catch (Exception e) {
                System.out.println("Shkruaj nje numer te sakte!");
            }
        }
    }

    private int readIntWithDefault(String prompt, int def) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine();
            if (s == null || s.trim().isEmpty()) return def;
            try {
                return Integer.parseInt(s.trim());
            } catch (Exception e) {
                System.out.println("Shkruaj nje numer te sakte!");
            }
        }
    }

    private String readLineWithDefault(String prompt, String def) {
        System.out.print(prompt);
        String s = sc.nextLine();
        if (s == null || s.trim().isEmpty()) return def;
        return s.trim();
    }

    private Date readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine();
            try {
                return dtFmt.parse(s.trim());
            } catch (ParseException e) {
                System.out.println("Format i gabuar. Shembull: 2025-12-12 14:30");
            }
        }
    }
}
