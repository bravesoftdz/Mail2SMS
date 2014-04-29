package de.mucbug.code.java.m2sserver;
import de.mucbug.code.java.m2sserver.handler.MailHandler;
import org.subethamail.smtp.server.SMTPServer;

/**
 * Created by marcel on 23.03.14.
 */
public class M2SServer {
    public static void main(String[] args) {

        // starten des Servers und MessageHandlers
        MailHandler m2sHandler = new MailHandler() ;
        SMTPServer smtpServer = new SMTPServer(m2sHandler);
        smtpServer.setPort(25000);
        smtpServer.start();

    }
}
