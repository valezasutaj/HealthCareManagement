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
        Console.printHeader();

        while (true) {
            Console.printMainMenu();
            String choice = readNonEmpty("Zgjedhja: ").toUpperCase(Locale.ROOT);

            switch (choice) {
                case "P" -> patientMenu();
                case "D" -> doctorMenu();
                case "T" -> appointmentMenu();
                case "N" -> notificationMenu();
                case "Q" -> {
                    System.out.println("Mireupafshim!");
                    return;
                }
                default -> System.out.println("Opsion i pavlefshem!\n");
            }
        }
    }

    private void patientMenu() {
        while (true) {
            Console.printPatientMenu();
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
                Console.error(e.getMessage());
            }
        }
    }

    private void doctorMenu() {
        while (true) {
            Console.printDoctorMenu();
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
                Console.error(e.getMessage());
            }
        }
    }

    private void appointmentMenu() {
        while (true) {
            Console.printAppointmentMenu();
            String c = readNonEmpty("Zgjedhja: ").toUpperCase(Locale.ROOT);

            try {
                switch (c) {
                    case "1" -> scheduleAppointment();
                    case "2" -> listAppointments();
                    case "3" -> listAppointmentsByPatient();
                    case "4" -> listAppointmentsByStatus();
                    case "5" -> completeAppointment();
                    case "6" -> cancelAppointment();
                    case "7" -> removeAppointment();
                    case "B" -> {return;}
                    default -> System.out.println("Opsion i pavlefshem!");
                }
            } catch (CustomException e) {
                Console.error(e.getMessage());
            }
        }
    }

    private void notificationMenu() {
        while (true) {
            Console.printNotiftMenu();
            String c = readNonEmpty("Zgjedhja: ").toUpperCase(Locale.ROOT);
            try {
                switch (c) {
                    case "1" -> sendEmailNotification();
                    case "2" -> sendSmsNotification();
                    case "B" -> {return;}
                    default -> System.out.println("Opsion i pavlefshem!");
                }
            } catch (CustomException e) {
                Console.error(e.getMessage());
            }
        }
    }

    private void addPatient() throws CustomException {
        Console.section("Regjistrim pacienti");

        int suggestedId = nextPatientId();
        System.out.println("Sugjerim ID (automatik): " + suggestedId);
        int id = readIntWithDefault("ID (shtyp Enter per " + suggestedId + "): ", suggestedId);

        String name = readNonEmpty("Emri: ");
        String phone = readLine("Tel: ");
        String email = readLine("Email: ");
        int age = readInt("Mosha: ");

        Patient p = new Patient(id, name, phone, email, age);
        patientRepo.addPatient(p);
        Console.success("U regjistrua: " + p.getDetails());
    }

    private void listPatients() {
        List<Patient> list = patientRepo.getAllPatients();
        if (list.isEmpty()) {
            Console.error("\nNuk ka paciente te regjistruar.");
            return;
        }
        Console.section("Lista e pacienteve (" + list.size() + ")");
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
        Console.section("Modifikim pacienti");
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
        Console.success("U perditesua: " + updated.getDetails());
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
        Console.section("Regjistrim doktori");
        String name = readNonEmpty("Emri: ");
        String phone = readLine("Tel: ");
        String email = readLine("Email: ");
        String speciality = readNonEmpty("Specializimi: ");

        Doctor d = doctorRepo.createDoctor(name, phone, email, speciality);
        Console.success("U regjistrua: " + d.getDetails());
    }

    private void listDoctors() {
        List<Doctor> list = doctorRepo.getAllDoctors();
        if (list.isEmpty()) {
            Console.error("\nNuk ka doktore te regjistruar.");
            return;
        }
        Console.section("Lista e Doktoreve (" + list.size() + ")");
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
        Console.section("Modifikim i Doktorit)");
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
        Console.success("U perditesua: " + updated.getDetails());
    }

    private void removeDoctor() {
        int id = readInt("ID e doktorit per fshirje: ");
        boolean ok = doctorRepo.removeDoctor(id);
        System.out.println(ok ? "Doktori u fshi." : "Doktori nuk u gjet.");
    }

    // == terminet ==

    private String statusSq(AppointmentStatus st) {
        return switch (st) {
            case SCHEDULED -> "I planifikuar";
            case COMPLETED -> "I perfunduar";
            case CANCELLED -> "I anuluar";
        };
    }

    private void scheduleAppointment() throws CustomException {
        Console.section("Bej Terminin");
        if (patientRepo.getAllPatients().isEmpty()) {
            Console.error("\nNuk ka paciente!");
            return;
        }
        if (doctorRepo.getAllDoctors().isEmpty()) {
            Console.error("\nNuk ka doktore!");
            return;
        }

        listPatientsForAppointment();
        int patientId = readInt("Zgjedh ID e pacientit: ");

        listDoctorsForAppointment();
        int doctorId = readInt("Zgjedh ID e doktorit: ");

        Date date = readDate("Koha: (yyyy-MM-dd HH:mm): ");
        Appointment a = appointmentRepo.createAppointment(patientId, doctorId, date, "");
        Console.success("U planifikua termini: ID = " + a.getId() + ", Status =" + statusSq(a.getStatus()));
    }

    private void listAppointments() {
        List<Appointment> list = appointmentRepo.getAllAppointments();
        if (list.isEmpty()) {
            Console.error("\nNuk ka termine te regjistruara.");
            return;
        }

        Console.section("Lista e termineve (" + list.size() + ")");
        for (Appointment a : list) {
            System.out.println(a);
        }
    }

    private void listAppointmentsByPatient() {
        int patientId = readInt("ID e pacientit: ");
        List<Appointment> filtered =
                appointmentRepo.getAllAppointments().stream()
                .filter(a -> a.getPatient() != null && a.getPatient().getId() == patientId)
                .toList();

        if (filtered.isEmpty()) {
            Console.error("Nuk u gjet asnje termin per kete pacient.");
            return;
        }

        Console.section("Terminet per pacientin ID = " + patientId);
        filtered.forEach(System.out::println);
    }

    private AppointmentStatus readStatus() {
        while (true) {
            System.out.println("Statuset: \n1 = SCHEDULED\n2 = COMPLETED\n3 = CANCELLED");
            String s = readNonEmpty("Zgjedh statusin (1/2/3): ");
            switch (s) {
                case "1" -> {return AppointmentStatus.SCHEDULED;}
                case "2" -> {return AppointmentStatus.COMPLETED;}
                case "3" -> {return AppointmentStatus.CANCELLED;}
                default -> System.out.println("Opsion i pavlefshem!");
            }
        }
    }

    private void listAppointmentsByStatus() {
        AppointmentStatus st = readStatus();
        List<Appointment> filtered =
                appointmentRepo.getAllAppointments().stream()
                        .filter(a -> a.getStatus() == st)
                        .toList();

        if (filtered.isEmpty()) {
            Console.error("Nuk u gjet asnje termin me kete status.");
            return;
        }

        Console.section("Terminet me status: " + statusSq(st));
        filtered.forEach(System.out::println);
    }

    private void completeAppointment() throws CustomException {
        int id = readInt("ID e terminit: ");
        Appointment a = appointmentRepo.getAppointmentById(id);
        String report = readNonEmpty("Raporti: ");
        a.setStatus(AppointmentStatus.COMPLETED);
        a.setReport(report);

        appointmentRepo.updateAppointment(a);

        Console.success("Termini u perfundua. Status = " + statusSq(a.getStatus()));
    }

    private void cancelAppointment() throws CustomException {
        int id = readInt("ID e terminit: ");
        Appointment a = appointmentRepo.getAppointmentById(id);
        a.setStatus(AppointmentStatus.CANCELLED);

        appointmentRepo.updateAppointment(a);

        Console.error("Termini u anulua. Status =" + statusSq(a.getStatus()));
    }

    private void removeAppointment() throws CustomException {
        int id = readInt("ID e terminit: ");
        appointmentRepo.removeAppointment(id);

        Console.success("Termini u fshi.");
    }

    private void listPatientsForAppointment() {
        System.out.println("\nPacientet:");
        for (Patient p : patientRepo.getAllPatients()) {
            System.out.println(p.getDetails());
        }
    }

    private void listDoctorsForAppointment() {
        System.out.println("\nDoktoret:");
        for (Doctor d : doctorRepo.getAllDoctors()) {
            System.out.println(d.getDetails());
        }
    }

    private void sendEmailNotification() throws CustomException {
        int patientId = readInt("ID e pacientit: ");
        Patient p = patientRepo.getPatientById(patientId);
        if (p.getEmail() == null || p.getEmail().isBlank()) {
            Console.error("Pacienti nuk ka email.");
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
            Console.error("Pacienti nuk ka numer telefoni.");
            return;
        }
        String msg = readNonEmpty("Mesazhi ");
        NotificationService ns = new SMSService(p.getPhone());
        ns.sendNotification("per: " + p.getName() + " | " + msg);
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return sc.nextLine();
    }

    private String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine();
            if (s != null && !s.trim().isEmpty()) {
                return s.trim();
            }
            Console.error("Nuk lejohet bosh!");
        }
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine();
            try {
                return Integer.parseInt(s.trim());
            } catch (Exception e) {
                Console.error("Shkruaj nje numer te sakte!");
            }
        }
    }

    private int readIntWithDefault(String prompt, int def) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine();
            if (s == null || s.trim().isEmpty()) {
                return def;
            }
            try {
                return Integer.parseInt(s.trim());
            } catch (Exception e) {
                Console.error("Shkruaj nje numer te sakte!");
            }
        }
    }

    private String readLineWithDefault(String prompt, String def) {
        System.out.print(prompt);
        String s = sc.nextLine();
        if (s == null || s.trim().isEmpty()) {
            return def;
        }
        return s.trim();
    }

    private Date readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine();
            try {
                Date inputDate = dtFmt.parse(s.trim());
                Date now = new Date();
                if (inputDate.before(now)) {
                    Console.error("Data nuk mund te jete ne te kaluaren!");
                    continue;
                }
                return inputDate;
            } catch (ParseException e) {
                Console.error("Format i gabuar. Shembull: 2025-12-13 15:40");
            }
        }
    }
}
