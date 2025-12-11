package model;

public class Doctor extends Person {
    private String speciality;

    public Doctor(int id, String name, String phone, String email, String speciality) throws CustomException {
        super(id, name, phone, email);
        if(speciality == null){
            throw new CustomException("Specializimi per doktor duhet te specifikohet!");
        }
        this.speciality = speciality;
    }

    public String getDetails(){ return "";}

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }
}
