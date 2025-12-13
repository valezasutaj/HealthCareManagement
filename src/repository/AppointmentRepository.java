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
    private static final String DELIMITER = ";";

    private final List<Appointment> appointments = new ArrayList<>();
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentRepository(PatientRepository patientRepository, DoctorRepository doctorRepository) {
        this.patientRepository = (patientRepository == null) ? new PatientRepository() : patientRepository;
        this.doctorRepository = (doctorRepository == null) ? new DoctorRepository() : doctorRepository;

        try {
            loadAppointmentsFromFile();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public Appointment createAppointment(int patientId, int doctorId, Date date, String report) throws CustomException {
        Patient patient = patientRepository.getPatientById(patientId);
        Doctor doctor = doctorRepository.getDoctorById(doctorId);

        if (report.contains(";") || report.contains("\n")) {
            throw new CustomException("Raporti nuk duhet te permbaje ';' ose rresht te ri");
        }

        int id = getNextId();
        Appointment appointment = new Appointment(id, patient, doctor, date, report);
        appointments.add(appointment);
        saveAppointmentsToFile();

        return appointment;
    }

    public Appointment getAppointmentById(int id) throws CustomException {
        for (Appointment a : appointments) {
            if (a.getId() == id) {
                return a;
            }
        }
        throw new CustomException("Nuk u gjet termini me ID: " + id);
    }

    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments);
    }

    public void updateAppointment(Appointment updatedAppointment) throws CustomException {
        if (updatedAppointment == null) {
            throw new CustomException("Termini i perditesuar nuk mund te jete null!");
        }

        Appointment existing = getAppointmentById(updatedAppointment.getId());
        appointments.remove(existing);
        appointments.add(updatedAppointment);
        saveAppointmentsToFile();
    }

    public boolean removeAppointment(int id) {
        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i).getId() == id) {
                appointments.remove(i);
                saveAppointmentsToFile();
                return true;
            }
        }
        return false;
    }

    private int getNextId() {
        int maxId = 0;
        for (Appointment a : appointments) {
            if (a.getId() > maxId) {
                maxId = a.getId();
            }
        }
        return maxId + 1;
    }

    private void saveAppointmentsToFile() {
        try {
            FileWriter fw = new FileWriter(FILE_NAME);
            BufferedWriter bw = new BufferedWriter(fw);

            for (Appointment a : appointments) {
                bw.write(
                        a.getId() + DELIMITER +
                                a.getPatient().getId() + DELIMITER +
                                a.getStaff().getId() + DELIMITER +
                                a.getDate().getTime() + DELIMITER +
                                a.getReport() + DELIMITER
                );

                if (a.getStatus() != null) {
                    bw.write(a.getStatus().name());
                }

                bw.newLine();
            }

            bw.close();
            fw.close();

        } catch (IOException e) {
            System.out.println("Gabim gjatë ruajtjes së termineve");
        }
    }

    private void loadAppointmentsFromFile() throws IOException {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            file.createNewFile();
            return;
        }

        appointments.clear();

        try {
            FileReader fr = new FileReader(FILE_NAME);
            BufferedReader br = new BufferedReader(fr);
            String line = "";

            while (true) {
                line = br.readLine();
                if (line == null) {
                    break;
                }

                if (line.trim().equals("")) {
                    continue;
                }

                String[] parts = line.split(DELIMITER);
                if (parts.length < 5) {
                    continue;
                }

                int id = Integer.parseInt(parts[0]);
                int patientId = Integer.parseInt(parts[1]);
                int doctorId = Integer.parseInt(parts[2]);
                long dateMillis = Long.parseLong(parts[3]);
                String report = parts[4];

                Patient patient = patientRepository.getPatientById(patientId);
                Doctor doctor = doctorRepository.getDoctorById(doctorId);

                Appointment appointment = new Appointment(id, patient, doctor, new Date(dateMillis), report);

                if (parts.length > 5 && !parts[5].equals("")) {
                    appointment.setStatus(AppointmentStatus.valueOf(parts[5]));
                }

                appointments.add(appointment);
            }
            br.close();
            fr.close();
        } catch (Exception e) {
            System.out.println("Gabim gjatë leximit të termineve");
        }
    }
}
