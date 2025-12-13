package repository;

import model.Appointment;
import model.AppointmentStatus;
import model.CustomException;
import model.Doctor;
import model.Patient;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppointmentRepository {
    private static final String FILE_NAME = "appointments.txt";
    private static final String DELIMITER = ",";

    private final List<Appointment> appointments = new ArrayList<>();
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentRepository() {
        this(new PatientRepository(), new DoctorRepository());
    }

    public AppointmentRepository(PatientRepository patientRepository, DoctorRepository doctorRepository) {
        this.patientRepository = (patientRepository == null) ? new PatientRepository() : patientRepository;
        this.doctorRepository = (doctorRepository == null) ? new DoctorRepository() : doctorRepository;

        try {
            loadAppointmentsFromFile();
        } catch (Exception e) {
            System.out.println("Gabim gjatë ngarkimit të termineve: " + e.getMessage());
        }
    }

    public void addAppointment(Appointment appointment) throws CustomException {
        if (appointment == null) {
            throw new CustomException("Termini nuk mund të jetë null!");
        }
        if (isIdUsed(appointment.getId())) {
            throw new CustomException("ID " + appointment.getId() + " është përdorur tashmë!");
        }
        appointments.add(appointment);
        saveAppointmentsToFile();
    }

    public Appointment createAppointment(int patientId, int doctorId, Date date, String report) throws CustomException {
        Patient patient = patientRepository.getPatientById(patientId);
        Doctor doctor = doctorRepository.getDoctorById(doctorId);

        int id = getNextId();
        Appointment a = new Appointment(id, patient, doctor, date, report);
        addAppointment(a);
        return a;
    }

    public Appointment getAppointmentById(int id) throws CustomException {
        for (Appointment a : appointments) {
            if (a.getId() == id) {
                return a;
            }
        }
        throw new CustomException("Nuk u gjet termini me ID: " + id);
    }

    public boolean isIdUsed(int id) {
        for (Appointment a : appointments) {
            if (a.getId() == id) return true;
        }
        return false;
    }

    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments);
    }

    public boolean removeAppointment(int id) {
        boolean removed = appointments.removeIf(a -> a.getId() == id);
        if (removed) {
            saveAppointmentsToFile();
        }
        return removed;
    }

    public void updateAppointment(Appointment updatedAppointment) throws CustomException {
        if (updatedAppointment == null) {
            throw new CustomException("Termini i përditësuar nuk mund të jetë null!");
        }
        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i).getId() == updatedAppointment.getId()) {
                appointments.set(i, updatedAppointment);
                saveAppointmentsToFile();
                return;
            }
        }
        throw new CustomException("Nuk u gjet termini me ID: " + updatedAppointment.getId());
    }

    public void clearAllAppointments() {
        appointments.clear();
        saveAppointmentsToFile();
    }

    public int getNextId() {
        int maxId = 0;
        for (Appointment a : appointments) {
            if (a.getId() > maxId) maxId = a.getId();
        }
        return maxId + 1;
    }

    private void saveAppointmentsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Appointment a : appointments) {
                String line = String.join(DELIMITER,
                        String.valueOf(a.getId()),
                        String.valueOf(a.getPatient().getId()),
                        String.valueOf(a.getStaff().getId()),
                        String.valueOf(a.getDate().getTime()),
                        escape(a.getReport()),
                        (a.getStatus() == null) ? "" : a.getStatus().name()
                );
                writer.write(line);
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            System.out.println("Gabim gjatë ruajtjes së termineve në skedar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadAppointmentsFromFile() throws IOException {
        File file = new File(FILE_NAME);
        if (!file.exists()) {


            file.createNewFile();
            return;
        }

        appointments.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(DELIMITER, -1);
                if (parts.length < 5) {
                    continue;
                }

                try {
                    int id = Integer.parseInt(parts[0].trim());
                    int patientId = Integer.parseInt(parts[1].trim());
                    int doctorId = Integer.parseInt(parts[2].trim());
                    long dateMillis = Long.parseLong(parts[3].trim());
                    String report = unescape(parts[4]);

                    Patient patient = patientRepository.getPatientById(patientId);
                    Doctor doctor = doctorRepository.getDoctorById(doctorId);

                    Appointment a = new Appointment(id, patient, doctor, new Date(dateMillis), report);

                    if (parts.length >= 6) {
                        String status = parts[5].trim();
                        if (!status.isEmpty()) {
                            try {
                                a.setStatus(AppointmentStatus.valueOf(status));
                            } catch (IllegalArgumentException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    }

                    appointments.add(a);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\n", "\\n").replace(DELIMITER, " ");
    }

    private String unescape(String s) {
        if (s == null) return "";
        return s.replace("\\n", "\n").replace("\\\\", "\\");
    }
}
