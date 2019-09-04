package com.squirrel.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.regexp.recompile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.annotation.Loginchk;
import com.annotation.Loginchk.Role;
import com.squirrel.dto.MemberDTO;
import com.squirrel.dto.NoticeListDTO;
import com.squirrel.dto.PageDTO;
import com.squirrel.dto.view.PickListviewDTO;
import com.squirrel.service.NoticeService;
import com.squirrel.service.PickService;

@Controller
public class NoticeController {

	@Autowired
	NoticeService sevice;
	
	@RequestMapping("/NoteView")
	public ModelAndView NoteView() {
		ModelAndView mav = new ModelAndView();
		List<NoticeListDTO> NoticeList = sevice.notelist();
		mav.addObject("NoteView", NoticeList );
		mav.setViewName("note/Notice/NoticeList");
		
		return mav;
	}
	@RequestMapping("/NoteContent")
	public ModelAndView NoteContent() {
		ModelAndView mav = new ModelAndView();
		List<NoticeListDTO> NoteContent = sevice.noteconent();
		mav.addObject("NoteContent", NoteContent);
		mav.setViewName("note/Notice/NoteContent");
		return mav;
	}
	
	@Loginchk(role = Role.ADMIN)
	@RequestMapping("/NoteInput") //공지사항 입력창으로 이동
	public String NoteInput() {

		return "note/Notice/note";
	}
	@Loginchk(role = Role.ADMIN)
	@RequestMapping("/NoteAdd") //공지사항 추가하는거
	public String NoteAdd(NoticeListDTO dto,HttpSession session) {
		System.out.println(dto);
		MemberDTO memberDTO = (MemberDTO)session.getAttribute("login");
		dto.setUser_no(memberDTO.getUser_no());
		sevice.NoticeInsert(dto);
		return "redirect:/NoteView";
	}
}

