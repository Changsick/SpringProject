package com.squirrel.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.squirrel.dto.ReCommentDTO;
import com.squirrel.service.ReCommentService;

@Controller
public class Re_CommentController {
	
	@Autowired
	ReCommentService recService;
	
	@RequestMapping(value = "/insertComment", produces = "text/plain;charset=utf-8") // ��� ����
	@ResponseBody
	public String insertComment(ReCommentDTO dto) {
		int n = recService.insertComment(dto);
		String mesg = "��ϵǾ����ϴ�";
		if(n==0) {
			mesg = "��� ����";
		}
		return mesg;
	}
	
	@RequestMapping(value = "/updateComment", produces = "text/plain;charset=utf-8") // ��� ����
	@ResponseBody
	public String updateComment(@RequestParam HashMap<String, String> map) {
		int n = recService.updateComment(map);
		String mesg = "�����Ǿ����ϴ�";
		if(n==0) {
			mesg = "���� ����";
		}
		return mesg;
	}
	
	@RequestMapping(value = "/deleteComment", produces = "text/plain;charset=utf-8") // ��� ����
	@ResponseBody
	public String deleteComment(@RequestParam("re_no") String re_no) {
		int n = recService.deleteComment(re_no);
		String mesg = "�����Ǿ����ϴ�";
		if(n==0) {
			mesg = "���� ����";
		}
		return mesg;
	}
	
}
