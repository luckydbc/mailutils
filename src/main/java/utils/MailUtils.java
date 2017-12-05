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
	// smtp服务器
		private static String host = "";
		// 发件人地址
		private static String from = "";
		// 收件人地址
		private static String to = "";
		// 附件地址
		private static String affix = "";
		// 附件名称
		private static String affixName = "";
		// 用户名
		private static String user = "";
		// 密码
		private static String pwd = "";
		// 邮件标题
		private static String subject = "";
		// 邮件内容
		private static String content = "";
		
		
		public static void setBase(String host, String user, String pwd) {
			MailUtils.host = host;
			MailUtils.user = user;
			MailUtils.pwd = pwd;
		}
		/**
		 * 
		 * @param from 发件人地址
		 * @param to  收件人地址
		 * @param subject 主题
		 */
		public static void setAddress(String from, String to, String subject) {
			MailUtils.from = from;
			MailUtils.to = to;
			MailUtils.subject = subject;
		}
		/**
		 * 
		 * @param affix 文件路径
		 * @param affixName 文件名称
		 */
		public static void setAffix(String affix, String affixName) {
			MailUtils.affix = affix;
			MailUtils.affixName = affixName;
		}
		/**
		 * 
		 * @param content 文件内容
		 */
		public static void setContent(String content) {
			MailUtils.content = content;
		}
		/**
		 * 
		 * @param host smtp服务器
		 * @param user 用户名
		 * @param pwd 密码
		 * @param flag 内容是否为html
		 */
		public static void send(boolean flag) {
			Properties properties = new Properties();
			properties.put("mail.smtp.host", host);
			properties.put("mail.smtp.auth", "true");
			// 创建session
			Session session = Session.getDefaultInstance(properties);
			session.setDebug(false);
			// 用session为参数定义消息对象
			MimeMessage message = new MimeMessage(session);
			try {
				// 加载发件人地址
				message.setFrom(new InternetAddress(from));
				// 加载收件人地址
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
				// 加载标题
				message.setSubject(subject);
				// 向multipart对象中添加各个部分内容，包含文本和附件
				Multipart multipart = new MimeMultipart();
				// 设置邮件的文本内容
				if (!isEmpty(content)) {
					BodyPart contentPart = new MimeBodyPart();
					if(flag){
						contentPart.setContent(content, "text/html; charset=utf-8");
					}else{
						contentPart.setText(content);
					}
					multipart.addBodyPart(contentPart);
				}
				// 添加附件
				if (!isEmpty(affix) && !isEmpty(affixName)) {
					BodyPart messageBodyPart = new MimeBodyPart();
					DataSource source = new FileDataSource(affix);
					messageBodyPart.setDataHandler(new DataHandler(source));
					// 添加附件内容
					sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
					messageBodyPart.setFileName("=?GBK?B?" + enc.encode(affixName.getBytes()) + "?=");
					multipart.addBodyPart(messageBodyPart);
				}
				// 将multipart放在message中
				message.setContent(multipart);
				// 保存邮件
				message.saveChanges();
				// 发送邮件
				Transport transport = session.getTransport("smtp");
				transport.connect(host, user, pwd);
				// 把邮件发出去
				transport.sendMessage(message, message.getAllRecipients());
				transport.close();
			} catch(Exception e) {
				System.err.println("发送邮件出错");
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
