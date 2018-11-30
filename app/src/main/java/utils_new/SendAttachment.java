package utils_new;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class SendAttachment extends AsyncTask {

private ProgressDialog statusDialog;
private Activity sendMailActivity;
    Custom_Variables_And_Method customVariablesAndMethod;

    public SendAttachment(Activity activity) {
            sendMailActivity = activity;
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();

    }

    protected void onPreExecute() {
            statusDialog = new ProgressDialog(sendMailActivity);
            statusDialog.setMessage("Getting ready...");
            statusDialog.setIndeterminate(false);
            statusDialog.setCancelable(false);
            statusDialog.show();
    }

    @Override
    protected Object doInBackground(Object... args) {
        String to="mobilereporting@cboinfotech.com";//change accordingly
        String to1=customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(sendMailActivity,"ERROR_EMAIL","");    //"javed.hussain19911@gmail.com";
        final String user="mobilereporting@cboinfotech.com";//change accordingly
        final String password="mreporting";//change accordingly

        //1) get the session object
        Properties properties = System.getProperties();
        //properties.setProperty("mail.smtp.host", "mail.javatpoint.com");//change accordingly
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.debug", "true");

        Session session = Session.getDefaultInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user,password);
                    }
                });

            //2) compose message
            try{
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(user));
                message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
                if (!to1.trim().isEmpty()) {
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(to1));
                }
                message.setSubject(args[0].toString());
                publishProgress("Reporting Please wait....");
                //3) create MimeBodyPart object and set your message content
                BodyPart messageBodyPart1 = new MimeBodyPart();
                messageBodyPart1.setText(args[1].toString());

                //4) create new MimeBodyPart object and set DataHandler object to this object
                MimeBodyPart messageBodyPart2 = new MimeBodyPart();

                /*String filename = "SendAttachment.java";//change accordingly
                DataSource source = new FileDataSource(filename);*/
                File source = new File(args[2].toString());//file name
                messageBodyPart2.attachFile(source);
               // messageBodyPart2.setDataHandler(new DataHandler(source));
               // messageBodyPart2.setFileName(filename);


                //5) create Multipart object and add MimeBodyPart objects to this object
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart1);
                multipart.addBodyPart(messageBodyPart2);

                //6) set the multiplart object to the message object
                message.setContent(multipart );

                //7) send message
                Transport.send(message);

                System.out.println("message sent....");
                publishProgress("message sent....");
            }catch (MessagingException ex) {
                ex.printStackTrace();
                publishProgress("Error Occoured");
            } catch (IOException e) {
                publishProgress("Error Occoured");
                e.printStackTrace();
            }
        return true;
    }

    @Override
    public void onProgressUpdate(Object... values) {
             statusDialog.setMessage(values[0].toString());

            }

    @Override
    public void onPostExecute(Object result) {
            statusDialog.dismiss();
        customVariablesAndMethod.getAlert(sendMailActivity,"Thank You !!!","Complained Successfull");
    }

   /* private void main1(String [] args){


    }*/
}