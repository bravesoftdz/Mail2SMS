package de.mucbug.code.java.m2sserver.handler;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

/**
 * Created by marcel on 23.03.14.
 */

public class SMSHandler {

    private String smsVon;
    private String smsAn;
    private String smsInhalt;

    public void setSmsVon(String smsVon) {
        this.smsVon = smsVon;
    }

    public void setSmsAn(String smsAn) {
        this.smsAn = smsAn.substring(0,smsAn.indexOf("@"));
    }

    public void setSmsInhalt(String smsInhalt) {
        this.smsInhalt = smsInhalt.substring((smsInhalt.indexOf(MailHandler.smsStartExpr)+9),smsInhalt.indexOf(MailHandler.smsEndExpr));
        System.out.println("SMS-Inhalt: ");
        System.out.println(this.smsInhalt);
    }

    private String[] portNames = SerialPortList.getPortNames();

    public void getPorts() {
        for (int i = 0; i < portNames.length; i++) {
            System.out.println(portNames[i]);
        }
    }
    public void sendSMS() {
        SerialPort serialPort = new SerialPort("/dev/ttyUSB0");
        System.out.println("Sende SMS von " + smsVon + " an: " + smsAn + " mit dem Inhalt: " + smsInhalt );
        try {
            serialPort.openPort();      //oeffnet den seriellen port
            serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            serialPort.writeString("AT+CMGF=1");
            serialPort.writeString("\r");
            serialPort.writeString("AT+CMGS=\""+smsAn+"\"");
            serialPort.writeString("\r");
            serialPort.writeString(smsInhalt + (char) 26);
            //serialPort.writeString((char)26);
            serialPort.closePort();

        } catch (SerialPortException ex) {
            System.out.println(ex);
        }
    }


}
