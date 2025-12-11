package service;

public class EmailService implements NotificationService {
    private String email;

    public EmailService(String email) {
        this.email = email;
    }

    public void sendNotification(String message){
        System.out.println("Notification nga: " + email + "\nMesazhi: " + message);
    };
}
