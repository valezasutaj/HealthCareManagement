package repository;

import model.CustomException;
import model.Doctor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DoctorRepository {

    private static final String FILE_NAME = "doctors.txt";
    private static final String DELIMITER = ";";
    private final List<Doctor> doctors = new ArrayList<>();

    public DoctorRepository() {
        loadDoctorsFromFile();
    }

    public void addDoctor(Doctor doctor) throws CustomException {
        if (doctor == null) {
            throw new CustomException("Doktori nuk mund te jete null!");
        }
        if (isIdUsed(doctor.getId())) {
            throw new CustomException("ID " + doctor.getId() + " eshte perdorur tashme!");
        }
        doctors.add(doctor);
        saveDoctorsToFile();
    }

    public Doctor createDoctor(String name, String phone, String email, String speciality) throws CustomException {
        validateName(name);
        validateSpeciality(speciality);

        int id = getNextId();
        Doctor d = new Doctor(id, name.trim(), safeTrim(phone), safeTrim(email), speciality.trim());
        addDoctor(d);
        return d;
    }

    public Doctor getDoctorById(int id) throws CustomException {
        for (Doctor d : doctors) {
            if (d.getId() == id) return d;
        }
        throw new CustomException("Doktori nuk ekziston!");
    }

    public boolean isIdUsed(int id) {
        for (Doctor d : doctors) {
            if (d.getId() == id) return true;
        }
        return false;
    }

    public List<Doctor> getAllDoctors() {
        return new ArrayList<>(doctors);
    }

    public boolean removeDoctor(int id) {
        boolean removed = doctors.removeIf(d -> d.getId() == id);
        if (removed) saveDoctorsToFile();
        return removed;
    }

    public void updateDoctor(Doctor updatedDoctor) throws CustomException {
        if (updatedDoctor == null) {
            throw new CustomException("Doktori nuk mund te jete null!");
        }

        Doctor existing = getDoctorById(updatedDoctor.getId());
        doctors.remove(existing);
        doctors.add(updatedDoctor);
        saveDoctorsToFile();
    }

    public void clearAllDoctors() {
        doctors.clear();
        saveDoctorsToFile();
    }

    public int getNextId() {
        int max = 0;
        for (Doctor d : doctors) {
            if (d.getId() > max) max = d.getId();
        }
        return max + 1;
    }


    private void saveDoctorsToFile() {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(FILE_NAME), StandardCharsets.UTF_8)
        )) {
            for (Doctor doctor : doctors) {
                // CSV row: id,name,phone,email,speciality
                String line =
                        doctor.getId() + DELIMITER +
                                csvField(doctor.getName()) + DELIMITER +
                                csvField(doctor.getPhone()) + DELIMITER +
                                csvField(doctor.getEmail()) + DELIMITER +
                                csvField(doctor.getSpeciality());

                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Gabim gjate ruajtjes ne skedar: " + e.getMessage());
        }
    }

    private void loadDoctorsFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        doctors.clear();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(FILE_NAME), StandardCharsets.UTF_8)
        )) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                List<String> parts = parseCsvLine(line);
                if (parts.size() != 5) {
                    System.err.println("Rresht i pavlefshem (format i gabuar): " + line);
                    continue;
                }

                try {
                    int id = Integer.parseInt(parts.get(0).trim());
                    String name = parts.get(1);
                    String phone = parts.get(2);
                    String email = parts.get(3);
                    String speciality = parts.get(4);


                    validateName(name);
                    validateSpeciality(speciality);

                    Doctor d = new Doctor(id, name.trim(), safeTrim(phone), safeTrim(email), speciality.trim());

                    if (!isIdUsed(id)) {
                        doctors.add(d);
                    } else {
                        System.err.println("ID e dyfishte ne skedar: " + id);
                    }
                } catch (Exception ex) {
                    System.err.println("Gabim ne parsimin e rreshtit: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Gabim gjate leximit te skedarit: " + e.getMessage());
        }
    }

    private String csvField(String s) {
        if (s == null) s = "";
        s = s.replace("\r", " ").replace("\n", " ");

        boolean mustQuote = s.contains(",") || s.contains("\"");
        if (s.contains("\"")) {
            s = s.replace("\"", "\"\"");
        }
        return mustQuote ? "\"" + s + "\"" : s;
    }

    private List<String> parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (inQuotes) {
                if (c == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        cur.append('"');
                        i++;
                    } else {
                        inQuotes = false;
                    }
                } else {
                    cur.append(c);
                }
            } else {
                if (c == '"') {
                    inQuotes = true;
                } else if (c == ';') {
                    fields.add(cur.toString().trim());
                    cur.setLength(0);
                } else {
                    cur.append(c);
                }
            }
        }
        fields.add(cur.toString().trim());
        return fields;
    }



    private void validateName(String name) throws CustomException {
        if (name == null || name.trim().isEmpty()) {
            throw new CustomException("Personi duhet te kete nje emer!");
        }
    }

    private void validateSpeciality(String speciality) throws CustomException {
        if (speciality == null || speciality.trim().isEmpty()) {
            throw new CustomException("Specializimi per doktor duhet te specifikohet!");
        }
    }

    private String safeTrim(String s) {
        return (s == null) ? "" : s.trim();
    }
}
