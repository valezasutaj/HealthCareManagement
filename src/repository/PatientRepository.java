package repository;

import model.Patient;
import model.CustomException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PatientRepository {
    private static final String FILE_NAME = "Patients.txt";
    private static final String DELIMITER = ",";
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
                        System.err.println("Gabim në parsimin e rreshtit: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Gabim gjatë leximit të skedarit: " + e.getMessage());
        }
    }

    // 9. METODA PËR TË GJUAR SKEDARIN
    public void clearAllPatients() {
        patients.clear();
        savePatientsToFile(); // Skedari do të bëhet bosh
    }
    public static void main(String[] args) {
        try {


            System.out.println("=== TESTIM I PATIENTREPOSITORY ===");

            // Create repository instance
            PatientRepository repo = new PatientRepository();



            System.out.println("\n1. Testimi i krijimit të repository dhe skedarit...");
            System.out.println("Numri i pacientëve në fillim: " + repo.getAllPatients().size());

            System.out.println("\n2. Testimi i shtimit të pacientëve...");
          //   Test 1: Add patients
            Patient p1 = new Patient(1, "Filani", "123-456-7890", "john.doe@email.com", 30);
            Patient p2 = new Patient(2, "Jane Smith", "098-765-4321", "jane.smith@email.com", 25);
            Patient p3 = new Patient(3, "Bob Johnson", "555-123-4567", "bob.j@email.com", 40);

            repo.addPatient(p1);
            System.out.println("Pacienti 1 u shtua: " + p1.getName());

            repo.addPatient(p2);
            System.out.println("Pacienti 2 u shtua: " + p2.getName());

            repo.addPatient(p3);
            System.out.println("Pacienti 3 u shtua: " + p3.getName());

            System.out.println("\n3. Testimi i ID të përsëritur...");
            try {
                Patient p4 = new Patient(1, "Duplicate ID", "111-222-3333", "dup@email.com", 35);
                repo.addPatient(p4);
            } catch (CustomException e) {
                System.out.println("Gabim i pritur: " + e.getMessage());
            }

            System.out.println("\n4. Testimi i kërkimit me ID...");
            Patient found = repo.getPatientById(2);
            System.out.println("Pacienti i gjetur me ID 2: " + found.getName() + ", Mosha: " + found.getAge());

            System.out.println("\n5. Testimi i kërkimit të ID që nuk ekziston...");
            try {
                repo.getPatientById(999);
            } catch (CustomException e) {
                System.out.println("Gabim i pritur: " + e.getMessage());
            }

            System.out.println("\n6. Testimi i marrjes së të gjithë pacientëve...");
            List<Patient> allPatients = repo.getAllPatients();
            System.out.println("Total pacientë: " + allPatients.size());
            System.out.println("Lista e pacientëve:");
            for (Patient p : allPatients) {
                System.out.println("  - " + p.getId() + ": " + p.getName() + " (" + p.getEmail() + ")");
            }

            System.out.println("\n7. Testimi i kontrollit të ID...");
            System.out.println("ID 1 është i përdorur? " + repo.isIdUsed(1));
            System.out.println("ID 999 është i përdorur? " + repo.isIdUsed(999));

            System.out.println("\n8. Testimi i përditësimit të pacientit...");
            Patient updatedPatient = new Patient(2, "Jane Smith Updated", "999-888-7777", "jane.updated@email.com", 26);
            repo.updatePatient(updatedPatient);

            Patient afterUpdate = repo.getPatientById(2);
            System.out.println("Pas përditësimit: " + afterUpdate.getName() + ", Telefoni: " + afterUpdate.getPhone());

            System.out.println("\n9. Testimi i fshirjes së pacientit...");
            boolean removed = repo.removePatient(3);
            System.out.println("Pacienti 3 u fshi? " + removed);
            System.out.println("Numri i pacientëve pas fshirjes: " + repo.getAllPatients().size());

            System.out.println("\n10. Testimi i persistencës (rinisim repository)...");
            // Create a new repository instance to test file persistence
            PatientRepository repo2 = new PatientRepository();
            System.out.println("Numri i pacientëve në repository të ri: " + repo2.getAllPatients().size());

            // Get patient from new repository
            Patient persistedPatient = repo2.getPatientById(1);
            System.out.println("Pacienti i persistuar me ID 1: " + persistedPatient.getName());



            System.out.println("\n=== TESTIMI U PËRFUNDUA ME SUKSES ===");

        } catch (CustomException e) {
            System.err.println("Gabim gjatë testimit: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Gabim i papritur: " + e.getMessage());
            e.printStackTrace();
        }
    }
}