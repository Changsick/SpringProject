package com.squirrel.util.email;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.annotation.Loginchk;
import com.squirrel.dto.MemberDTO;
import com.squirrel.service.MemberService;
import com.squirrel.util.aes.AESManager;

@Controller
public class MailForUtil {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	MemberService memService;

	// mailSending �ڵ�
	@RequestMapping(value = "/sendPWMail") // ȸ�� ��й�ȣ ã�� �� �ӽú�й�ȣ �߼�
	public ModelAndView mailSending(@RequestParam HashMap<String, String> map) {

		String sendMail = map.get("email");
		String phoneid = map.get("phoneid");

		Random random = new Random();
		String[] pwval = new String[7];
		String pw = "";
		for (int i = 0; i < pwval.length; i++) {
			pwval[i] = String.valueOf(random.nextInt(9));
			pw += pwval[i];
		}

		HashMap<String, String> insertmap = new HashMap<String, String>();
		insertmap.put("userpw", pw);
		insertmap.put("phone_id", phoneid);
		memService.updatePW(insertmap);

		String setfrom = "tlakffja@naver.com";
		String tomail = sendMail;
		String title = "GolfHi �ӽú�й�ȣ �߼�";
		String content = "<h2>�ȳ��ϼ��� MS :p GolfHi �Դϴ�!</h2><br><br>" + "�ӽú�й�ȣ: " + pw;

		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

			messageHelper.setFrom(setfrom); // �����»�� �����ϰų� �ϸ� �����۵��� ����
			messageHelper.setTo(tomail); // �޴»�� �̸���
			messageHelper.setSubject(title); // ���������� ������ �����ϴ�
			messageHelper.setText(content); // ���� ����

			mailSender.send(message);
		} catch (Exception e) {
			System.out.println(e);
		}
		ModelAndView mav = new ModelAndView();
		mav.addObject("email", sendMail);
		mav.setViewName("email/sendEmail");
		return mav;
	}

	// �Ķ���ͷ� �Ѿ�� ���ڿ��� �ؽ��ڵ尪���� ����(MD5 : 128bit)
	public String testMD5(String str) {
		String MD5 = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte byteData[] = md.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
			MD5 = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			MD5 = null;
		}
		return MD5;
	}

	@RequestMapping(value = "sendMail")
	@Loginchk
	public String sendMail(HttpSession session) {
		MemberDTO user = (MemberDTO) session.getAttribute("login");
		String username = user.getUsername();

		// �ð� ���� ���� ���߿� db���� �����ð�.
		Date dateTmp = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// �ӽù���. ���߿� date ������ sql���� ������ �ð���(���ڿ�) ������ ��.
		// format.parse(source) -> �ð��񱳽� Ȯ��
		String date = format.format(dateTmp);
		// �ð� ���� ��
		String code = date + "/" + user.getUser_no();

		// ���� ? �ð� = aes��ȣȭ�� & �����ڵ� = �ð�+�������̵� �ؽ���
		
		AESManager aes = new AESManager();
		String aesname = "1q2w3e4r5t6y7u8i";
		String enco = aes.enCodeText(aesname, date);
		String dnco = aes.deCodeText(aesname,enco);
		String iscode = testMD5(code);
		
//		HashMap<String, Integer> emailchk = (HashMap<String, Integer>) getServletContext().getAttribute("emailchk");
		
//		if (emailchk == null) {
//			emailchk = new HashMap<String, Integer>();
//			getServletContext().setAttribute("emailchk", emailchk);
//		}
//
//		emailchk.put(iscode, user.getUser_no());
		
		
		String setfrom = "tlakffja@naver.com";
		String tomail = "��������";
		String title = "GolfHi �ӽú�й�ȣ �߼�";
		String content = "��������";

		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

			messageHelper.setFrom(setfrom); // �����»�� �����ϰų� �ϸ� �����۵��� ����
			messageHelper.setTo(tomail); // �޴»�� �̸���
			messageHelper.setSubject(title); // ���������� ������ �����ϴ�
			messageHelper.setText(content); // ���� ����

			mailSender.send(message);
		} catch (Exception e) {
			System.out.println(e);
		}

		return "";
	}

}