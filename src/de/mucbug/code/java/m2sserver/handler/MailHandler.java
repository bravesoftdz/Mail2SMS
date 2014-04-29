package de.mucbug.code.java.m2sserver.handler;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
import org.subethamail.smtp.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
/**
 * Created by marcel on 23.03.14.
 */
public class MailHandler implements MessageHandlerFactory {

    private String mailVon;
    private String mailAn;
    private String mailInhalt;
    private SMSHandler neueSMS = new SMSHandler();
    private static String smsDomain = "@m2s.sms";
    protected static String smsStartExpr = "SMSSTART";
    protected static String smsEndExpr = "SMSEND";
    protected static String serverStopExpr = "ADIOSSERVER";

        public MessageHandler create(MessageContext ctx) {
            return new Handler(ctx);
        }

        class Handler implements MessageHandler {
            MessageContext ctx;

            public Handler(MessageContext ctx) {
                this.ctx = ctx;
            }

            public void from(String from) throws RejectException {
                System.out.println("FROM:"+from);
                mailVon = from;
            }

            public void recipient(String recipient) throws RejectException {
                System.out.println("RECIPIENT:"+recipient);
                mailAn = recipient;
            }

            public void data(InputStream data) throws IOException {
                /*System.out.println("MAIL DATA");
                System.out.println("= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =");
                System.out.println(this.convertStreamToString(data));
                System.out.println("= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =");*/
                mailInhalt = this.convertStreamToString(data);
            }


            public void done() {
                System.out.println("Finished");
                System.out.println(mailInhalt);
                if (mailInhalt.contains(serverStopExpr)){
                    System.out.println("Server Stoppt");
                } else if (!mailVon.contains("@") | !mailVon.contains(".")) {
                    System.out.println("Ungültiger Absender");
                } else if (!mailAn.contains(smsDomain) || !mailAn.substring(0,mailAn.indexOf(smsDomain)).matches("[+-]?[0-9]+")) {
                    System.out.println("Ungültiger Empfänger");
                }
                else if (!mailInhalt.contains(smsStartExpr) | !mailInhalt.contains(smsEndExpr)) {
                    System.out.println("SMS Teil fehlt oder ist nicht abgeschlossen");
                }
                else                {
                    generateSMS();
                    neueSMS.sendSMS();
                }

            }

            public String convertStreamToString(InputStream is) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();

                String line = null;
                try {
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return sb.toString();
            }

            public void generateSMS(){
                neueSMS.setSmsAn(mailAn);
                neueSMS.setSmsVon(mailVon);
                neueSMS.setSmsInhalt(mailInhalt);
            }


        }
    }