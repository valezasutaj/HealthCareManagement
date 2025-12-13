package model;

public class Doctor extends Person {

    private String speciality;

    public Doctor(int id, String name, String phone, String email, String speciality) throws CustomException {
        super(id, safeName(name), phone, email);
        setSpeciality(speciality);
    }

    private static String safeName(String name) throws CustomException {
        if (name == null || name.trim().isEmpty()) {
            throw new CustomException("Personi duhet te kete nje emer!");
        }
        return name.trim();
    }

    @Override
    public String getDetails() {
        return "Doctor [ID=" + getId()
                + ", Name=" + getName()
                + ", Specialty=" + speciality
                + ", Phone=" + getPhone()
                + ", Email=" + getEmail()
                + "]";
    }

    @Override
    public String toString() {
        return getDetails();
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) throws CustomException {
        if (speciality == null || speciality.trim().isEmpty()) {
            throw new CustomException("Specializimi per doktor duhet te specifikohet!");
        }
        this.speciality = speciality.trim();
    }
}
