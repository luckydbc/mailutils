package utils;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailUtils {
	// smtp������
		private static String host = "";
		// �����˵�ַ
		private static String from = "";
		// �ռ��˵�ַ
		private static String to = "";
		// ������ַ
		private static String affix = "";
		// ��������
		private static String affixName = "";
		// �û���
		private static String user = "";
		// ����
		private static String pwd = "";
		// �ʼ�����
		private static String subject = "";
		// �ʼ�����
		private static String content = "";
		
		
		public static void setBase(String host, String user, String pwd) {
			MailUtils.host = host;
			MailUtils.user = user;
			MailUtils.pwd = pwd;
		}
		/**
		 * 
		 * @param from �����˵�ַ
		 * @param to  �ռ��˵�ַ
		 * @param subject ����
		 */
		public static void setAddress(String from, String to, String subject) {
			MailUtils.from = from;
			MailUtils.to = to;
			MailUtils.subject = subject;
		}
		/**
		 * 
		 * @param affix �ļ�·��
		 * @param affixName �ļ�����
		 */
		public static void setAffix(String affix, String affixName) {
			MailUtils.affix = affix;
			MailUtils.affixName = affixName;
		}
		/**
		 * 
		 * @param content �ļ�����
		 */
		public static void setContent(String content) {
			MailUtils.content = content;
		}
		/**
		 * 
		 * @param host smtp������
		 * @param user �û���
		 * @param pwd ����
		 * @param flag �����Ƿ�Ϊhtml
		 */
		public static void send(boolean flag) {
			Properties properties = new Properties();
			properties.put("mail.smtp.host", host);
			properties.put("mail.smtp.auth", "true");
			// ����session
			Session session = Session.getDefaultInstance(properties);
			session.setDebug(false);
			// ��sessionΪ����������Ϣ����
			MimeMessage message = new MimeMessage(session);
			try {
				// ���ط����˵�ַ
				message.setFrom(new InternetAddress(from));
				// �����ռ��˵�ַ
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
				// ���ر���
				message.setSubject(subject);
				// ��multipart��������Ӹ����������ݣ������ı��͸���
				Multipart multipart = new MimeMultipart();
				// �����ʼ����ı�����
				if (!isEmpty(content)) {
					BodyPart contentPart = new MimeBodyPart();
					if(flag){
						contentPart.setContent(content, "text/html; charset=utf-8");
					}else{
						contentPart.setText(content);
					}
					multipart.addBodyPart(contentPart);
				}
				// ��Ӹ���
				if (!isEmpty(affix) && !isEmpty(affixName)) {
					BodyPart messageBodyPart = new MimeBodyPart();
					DataSource source = new FileDataSource(affix);
					messageBodyPart.setDataHandler(new DataHandler(source));
					// ��Ӹ�������
					sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
					messageBodyPart.setFileName("=?GBK?B?" + enc.encode(affixName.getBytes()) + "?=");
					multipart.addBodyPart(messageBodyPart);
				}
				// ��multipart����message��
				message.setContent(multipart);
				// �����ʼ�
				message.saveChanges();
				// �����ʼ�
				Transport transport = session.getTransport("smtp");
				transport.connect(host, user, pwd);
				// ���ʼ�����ȥ
				transport.sendMessage(message, message.getAllRecipients());
				transport.close();
			} catch(Exception e) {
				System.err.println("�����ʼ�����");
				e.printStackTrace();
			}
			
		}
		public static  boolean isEmpty(final CharSequence str) {
	        if ( str == null || str.length() == 0 ) {
	            return true;
	        }else{
	        	return false;
	        }
	    }
}
