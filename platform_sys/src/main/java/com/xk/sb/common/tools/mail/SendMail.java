package com.xk.sb.common.tools.mail;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @ClassName: Sendmail
 * @Description: Sendmail类继承Thread，因此Sendmail就是一个线程类，这个线程类用于给指定的用户发送Email
 *               发送邮件是一件非常耗时的事情，因此这里设计一个线程类来发送邮件
 * @author: hdb
 *
 */
public class SendMail extends Thread
{
	// 用于给用户发送邮件的邮箱
	private String from = "2350290241@qq.com";
	// 邮箱的用户名
	private String username = "2350290241@qq.com";
	// 邮箱授权码
	private String password = "yknutsslinuqdidd";
	// 发送邮件的服务器地址
	private String host = "smtp.qq.com";

	private Email email;

	public SendMail(Email email)
	{
		this.email = email;
	}

	/*
	 * 重写run方法的实现，在run方法中发送邮件给指定的用户
	 *
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run()
	{
		try
		{
			Properties prop = new Properties();
			prop.setProperty("mail.smtp.host", host);
			prop.setProperty("mail.transport.protocol", "smtp");
			prop.setProperty("mail.smtp.auth", "true");
			//添加ssl支持
			prop.setProperty("mail.smtp.ssl.enable", "true");
			prop.put("mail.smtp.port", 465);// 设置端口


			Session session = Session.getInstance(prop);
			session.setDebug(true);
			Transport ts = session.getTransport();
			ts.connect(host, username, password);
			Message message = createEmail(session, email);
			ts.sendMessage(message, message.getAllRecipients());
			System.out.println("发送邮件成功...");
			ts.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @Method: createEmail
	 * @Description: 创建要发送的邮件
	 * @Anthor:hdb
	 *
	 * @param session
	 * @param email
	 * @return
	 * @throws Exception
	 */
	public Message createEmail(Session session, Email email) throws Exception
	{

		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		// 设置收件人
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(email.getRecipient()));
		// 设置自定义发件人昵称
		String nickname = "";
		try
		{
			nickname = javax.mail.internet.MimeUtility.encodeText(email.getNickname());
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		message.setFrom(new InternetAddress(nickname + "<" + from + ">"));
		message.setSubject(email.getSubject());
		message.setContent(email.getInfo(), "text/html;charset=UTF-8");
		message.saveChanges();
		return message;
	}
}