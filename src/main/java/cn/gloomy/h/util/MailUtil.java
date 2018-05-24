package cn.gloomy.h.util;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailUtil {

  private static String host         = "smtp.qq.com";

  private static String port         = "465";

  private static String fromMail     = "452882261@qq.com";

  private static String toMail       = "iani452882261@qq.com";

  private static String attachFile[] = { "E:\\文档/帐号和密码.txt" };

  private static String content      = "<a href='www.baidu.com'>百度</a>";

  private static String title        = "测试邮件";

  private static String user         = "452882261@qq.com";

  private static String password     = "mdqelajinqjjcaij";

  private static String timeOut      = "5000";

  private static String connTimeout  = "5000";

  /**
   * just a sample of sending mail.
   * 
   * @throws NoSuchProviderException
   */
  public static void sendMail() {
    Properties pros = new Properties();
    pros.put("mail.smtp.host", host);
    pros.put("mail.smtp.port", port);

    // session need auth(true) when exit auth in logining
    pros.put("mail.smtp.auth", "true");

    // the config of port is 587,please hide the next three line of code
    pros.put("mail.smtp.ssl.enable", "true");
    pros.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    pros.put("mail.smtp.socketFactory.port", port);

    pros.put("mail.smtp.timeout", timeOut);
    pros.put("mail.smtp.connectiontimeout", connTimeout);

    Session session = Session.getInstance(pros, new Authenticator() {

      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        // TODO Auto-generated method stub
        return new PasswordAuthentication(user, password);
      }
    });

    //
    session.setDebug(true);
    try {
      Message msg = new MimeMessage(session);
      msg.setFrom(new InternetAddress(fromMail));
      msg.setSubject(title);
      msg.setSentDate(new Date());// 设置发信时间
      msg.setRecipient(RecipientType.TO, new InternetAddress(toMail));

      Multipart mulPart = new MimeMultipart();
      MimeBodyPart part1 = new MimeBodyPart();
      part1.setContent(content, "text/html;charset=utf-8");
      mulPart.addBodyPart(part1);
      if (attachFile != null && attachFile.length > 0) {
        part1 = new MimeBodyPart();
        part1.attachFile(attachFile[0]);
        // we can use this method to change the content of file which will be
        // sent,
        // but the local file is not changed.
        // part1.setContent("","");
        mulPart.addBodyPart(part1);
      }

      msg.setContent(mulPart);
      Transport.send(msg);
    } catch (AddressException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (MessagingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    MailUtil.sendMail();
  }
}
