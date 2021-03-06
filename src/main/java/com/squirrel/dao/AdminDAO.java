package com.squirrel.dao;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.squirrel.dto.GolfCcDTO;
import com.squirrel.dto.MemberDTO;
import com.squirrel.dto.ProductDTO;

@Repository
public class AdminDAO {

	@Autowired
	SqlSessionTemplate template;

	public int totalRecord(HashMap<String, Object> map) {
		
		return template.selectOne("AdminMapper.totalRecord", map);
	}
	
	public List<MemberDTO> adminMemberSelect(HashMap<String, Object> map) {
		
		return template.selectList("AdminMapper.adminMemberSelect", map);
	}

	public List<ProductDTO> adminProductSelect(HashMap<String, Object> map) {
		
		return template.selectList("AdminMapper.adminProductSelect", map);
	}

	public List<GolfCcDTO> adminGolfCcSelect(HashMap<String, Object> map) {
		
		return template.selectList("AdminMapper.adminGolfCcSelect", map);
	}
	
}
