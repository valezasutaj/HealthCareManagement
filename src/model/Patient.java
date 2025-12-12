package model;

public class Patient extends Person {
    private int age;

    public Patient(int id, String name, String phone, String email, int age) throws CustomException {
        super(id, name, phone, email);
        this.age = age;
    }

    public String getDetails(){return ""; }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    @Override
    public String toString() {
        return getClass().getSimpleName() + " [ID: " + getId() + ", Emri: " + getName() + "]";
    }
}
