package service;

public class SMSService implements NotificationService {
    private String number;

    public SMSService(String number) {
        this.number = number;
    }

    public void sendNotification(String message){
        System.out.println("Njoftim dergohet tek: " + number + " (SMS)\nMesazhi: " + message);
    };
}
