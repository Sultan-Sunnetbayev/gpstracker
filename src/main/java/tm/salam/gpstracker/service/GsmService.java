package tm.salam.gpstracker.service;

import java.util.ArrayList;

public interface GsmService {
    String[] getSmsStorage();

    String executeAT(String at, int waitingTime);

    String executeUSSD(String ussd);

    ArrayList<String[]> readSms();

    String sendSms(String num, String sms);

    String deleteSms(int smsId, String storage);

    String deleteAllSms(String storage);

    boolean initialize(String port);

    String[] getSystemPorts();

    boolean closePort();
}
