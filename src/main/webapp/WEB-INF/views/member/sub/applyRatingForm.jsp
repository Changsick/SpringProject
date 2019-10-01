<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		

	}
</script>
<div style="margin-left: 200px;margin-top: 15px">
<form action="applyRatingUp" method="post">
<h3>등업사항 안내</h3>
<b>안녕하세요 GolfHi 입니다</b><br>
<textarea style="width:100%;border:1;overflow:visible;text-overflow:ellipsis;" rows=500 cols="50" readonly="readonly">
매니저 등급신청해 관해 안내드립니다
일반 등급 회원분들은 등업신청하기 버튼을 통해 등업신청할 수 있습니다.
등업 확인은 관리자를 통해 이루어지며 신청날짜로 부터 빠른 시일내에 승인되면 매니저 등급으로 활동하시게 됩니다
매니저 회원이 되시면 각 골프장에대한 상품을 등록/삭제/수정 이 가능하며, 거래내역 또한 확인 가능합니다. 
</textarea><br>
<font style="color: red">불법 행동이나 사기 행위가 적발될 시 매니저 등급에서 박탈되며 강제 탈퇴 조치가 취해집니다</font>

<c:if test="${ratingUp == null }">
<button>등업신청</button>
</c:if>
<c:if test="${ratingUp != null }">
<br><br>이미 신청하였습니다
</c:if>
</form>
</div>