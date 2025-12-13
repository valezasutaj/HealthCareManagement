package repository;

import model.Patient;
import model.CustomException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PatientRepository {
    private static final String FILE_NAME = "Patients.txt";
    private static final String DELIMITER = ";";
    private List<Patient> patients;


    public PatientRepository() {
        patients = new ArrayList<>();
        try {
            loadPatientsFromFile(); // Ngarko pacientët nga skedari në fillim
        }
        catch (Exception e) {
            System.out.println("Gabim gjatë ngarkimit të pacientëve: " + e.getMessage());
        }
    }

    // 1. SHTO PACIENTIN
    public void addPatient(Patient patient) throws CustomException {
        if (isIdUsed(patient.getId())) {
            throw new CustomException("ID " + patient.getId() + " është përdorur tashmë!");
        }
        patients.add(patient);
        savePatientsToFile(); // Ruaj në skedar
    }

    // 2. KËRKO PACIENTIN ME ID
    public Patient getPatientById(int id) throws CustomException {
        return patients.stream()
                .filter(p -> p.getId() == id)
                .findAny()
                .orElseThrow(() -> new CustomException("Pacienti nuk ekziston!"));
    }

    // 3. KONTROLLO NËSE ID ËSHTË PËRDORUR
    public boolean isIdUsed(int id) {
        return patients.stream().anyMatch(p -> p.getId() == id);
    }

    // 4. MERRE TË GJITHË PACIENTËT
    public List<Patient> getAllPatients() {
        return new ArrayList<>(patients); // Kthej kopje për mbrojtjen e të dhënave
    }

    // 5. FSHIJ PACIENTIN
    public boolean removePatient(int id) {
        boolean removed = patients.removeIf(p -> p.getId() == id);
        if (removed) {
            savePatientsToFile(); // Përditëso skedarin
        }
        return removed;
    }

    // 6. PËRDITËSO PACIENTIN
    public void updatePatient(Patient updatedPatient) throws CustomException {
        Patient existingPatient = getPatientById(updatedPatient.getId());
        patients.remove(existingPatient);
        patients.add(updatedPatient);
        savePatientsToFile();
    }

    // 7. METODA PËR TË RUAJTUR NË SKEDAR
    private void savePatientsToFile() {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {

            for (Patient patient : patients) {
                String line = String.join(DELIMITER,
                        String.valueOf(patient.getId()),
                        patient.getName(),
                        patient.getPhone(),
                        patient.getEmail(),
                        String.valueOf(patient.getAge())
                );
                writer.write(line);
                writer.newLine();


            }
            writer.flush(); // Force the data to be written to disk

        } catch (IOException e) {
            System.out.println("Gabim gjatë ruajtjes në skedar: " + e.getMessage());
            e.printStackTrace(); // Add this to see full error
        }
    }

    // 8. METODA PËR TË NGARKUAR NGA SKEDARI
    private void loadPatientsFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            try {
                file.createNewFile(); // Create the file if it doesn't exist
            } catch (IOException e) {
                System.out.println("Gabim gjatë krijimit të skedarit: " + e.getMessage());
            }
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(DELIMITER);
                if (parts.length == 5) {
                    try {
                        int id = Integer.parseInt(parts[0].trim());
                        String name = parts[1].trim();
                        String phone = parts[2].trim();
                        String email = parts[3].trim();
                        int age = Integer.parseInt(parts[4].trim());

                        Patient patient = new Patient(id, name, phone, email, age);
                        patients.add(patient);
                    } catch (Exception e) {
                        System.out.println("Gabim në parsimin e rreshtit: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Gabim gjatë leximit të skedarit: " + e.getMessage());
        }
    }


    // 9. METODA PËR TË GJUAR SKEDARIN
    public void clearAllPatients() {
        patients.clear();
        savePatientsToFile(); // Skedari do të bëhet bosh
    }
}