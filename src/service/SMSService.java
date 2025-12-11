package service;

public class SMSService implements NotificationService {
    private String number;

    public SMSService(String number) {
        this.number = number;
    }

    public void sendNotification(String message){
        System.out.println("Notification nga: " + number + "\nMesazhi: " + message);
    };
}
