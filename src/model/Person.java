package model;

public abstract class Person {
    private int id;
    private String name;
    private String phone;
    private String email;

    public Person(int id, String name, String phone, String email) throws CustomException {
        if(id < 0){
            throw new CustomException("ID duhet te jete pozitive!");
        }
        if(name == null && name.trim().isEmpty()){
            throw new CustomException("Personi duhet te kete nje emer!");
        }
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public abstract String getDetails();

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
