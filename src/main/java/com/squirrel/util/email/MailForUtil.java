package com.squirrel.util.email;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
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
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	
	@Autowired
	AESManager aesManager;
	
	

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
			messageHelper.setText(content,true); // ���� ����

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

	@RequestMapping(value = "/sendMail")
	@Loginchk
	public String sendMail(HttpSession session) {
		MemberDTO user = (MemberDTO) session.getAttribute("login");
		String username = user.getUsername();
		int user_no = user.getUser_no();
		Date dateTmp = new Date(); 
		long date = dateTmp.getTime(); // �߼۽ð�
		long endDate = date + (24*60*60*1000); // ��ȿ�ð� : �߼۽ð� + 24�ð� 

		// �ð� ���� ��
		String code = date+"/"+endDate+"/"+user_no; // isTime : ��ȣȭ�� ��(�߼۽ð�+��ȿ�ð�+����)

		String enco = aesManager.enCodeText("email", code); // ��ȣȭ��
//		System.out.println("��ȣȭ>>>>"+enco);
		String deco = aesManager.deCodeText("email", enco);
//		System.out.println("��ȣȭ>>>>"+deco);
		String iscode = testMD5(enco+"golfHi"); // isCode : �ؽ���(��ȣ�� + ��������)
				
		String setfrom = "tlakffja@naver.com";
		String tomail = user.getEmail();
		String title = "GolfHi �̸��� ����";
		String content = "<h2>�ȳ��ϼ��� MS :p GolfHi �Դϴ�!</h2><br><br>" + "<h3>" + username + "��</h3>"
				+"<p>�����ϱ� ��ư�� �����ø� ��й�ȣ �н� �� �̸����� ���� Ȯ���� �� �ֽ��ϴ�</p>"
				+"<a href='localhost:8090/golfhi/emailCheck?isTime="+enco+"&isCode="+iscode+"'>�����ϱ�</a>"
				+ "(Ȥ�� �߸� ���޵� �����̶�� �� �̸����� �����ϼŵ� �˴ϴ�)";

		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

			messageHelper.setFrom(setfrom); // �����»�� �����ϰų� �ϸ� �����۵��� ����
			messageHelper.setTo(tomail); // �޴»�� �̸���
			messageHelper.setSubject(title); // ���������� ������ �����ϴ�
			messageHelper.setText(content,true); // ���� ����

			mailSender.send(message);
		} catch (Exception e) {
			System.out.println(e);
		}

		return "email/sendEmail";
	}

	@RequestMapping(value = "/emailCheck")
	public String emailCheck(@RequestParam("isTime") String isTime,
			@RequestParam("isCode") String isCode, HttpSession session,
			RedirectAttributes redData) {
		System.out.println(">>>������ ��ȣȭ��>>>"+isTime);
		System.out.println(">>>������ �ؽ���>>>"+isCode);
		String compareHash = isTime;
		isTime = isTime.replace(" ", "+");
		isTime = aesManager.deCodeText("email", isTime); // ��ȣȭ
		
		Date curDate = new Date(); 
		long serverTime = curDate.getTime(); // ������ ������ �ð�(�����ð�)
		String[] data = isTime.split("/");
		
		long sendTime = Long.parseLong(data[0]); // �߼۽ð�
		System.out.println(">>>�߼۽ð� : "+sendTime);
		long checkTime = Long.parseLong(data[1]); // ��ȿ�ð�
		System.out.println(">>>��ȿ�ð�~~~"+checkTime);
		System.out.println(">>>�����ð�~~~"+serverTime);
		if(checkTime - serverTime > 0) { // ��ȿ�ð�(�߼۽ð����κ���24�ð�) - �����������ϱ⴩�� �ð�
			// isCode �ؽ� �� ���ؾߵ�
			if(testMD5(compareHash+"golfHi")==isCode) {
				int user_no = Integer.parseInt(data[2]);
				memService.updateEmail(user_no);
				if(session.getAttribute("login") == null) { // ���� ������ ���ų� �޶����� �� �α����� null�� �� �ִ�
					MemberDTO dto = memService.getUser(user_no);
					session.setAttribute("login", dto);
				}
			}else {
				redData.addFlashAttribute("mesg","���� \n �����ڵ尡 �ٸ��ϴ�");
			}
			
		}else {
			redData.addFlashAttribute("mesg", "���� \n ���� �ð��� �������ϴ�\n �����ð� : 24�ð�\n �������������� �̸��������� �����ϼ���");
		}
		return "redirect:/";
	}
	
}