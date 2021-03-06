<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:if test="${pickMesg != null|| not mesg eq ''}">
<script type="text/javascript">
alert('${pickMesg}');
</script>
</c:if>

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script>
	$(document).ready(function(){
		// 전체선택
		$("#chkall").on("click",function(){
			$(".check").each(function(idx,ele){
				$(ele).prop("checked",$("#chkall").prop("checked"));
			});
		});
		
		// 하나라도 체크 풀면 전체선택 체크풀림
		$(".check").on("click",function(){
			if($(".check:checked").length == ${pickList.size()}){
				$("#chkall").prop("checked",true);
			}else{
				$("#chkall").prop("checked",false);
			}
		});
		
		$("#delete").on("click",function(event){
			if($(".check:checked").length == 0){
				event.preventDefault();
				alert('하나 이상 체크해라');
			}else{
				$("form[name='myForm']").attr({"action":"deletePick","method":"post"});
			}
		});
		
		
		$(".order").on("click",function(event){
			
			var pick_no = $(this).attr("data-pick_no");
			$("#pick_no"+pick_no).attr("checked","checked");
			console.log(pick_no);
			console.log($("#pick_no"+pick_no).val());
			$("form[name='myForm']").attr({"action":"orderConfirm","method":"get"});
		});
		
		//$(this).attr("data-pick_no")
		
		
	});
</script>


<form name="myForm" class="form_main" style="margin-left: 200px;margin-top: 15px">
<h3>장바구니</h3>
	<table class="line_table">
		<tr>
			<td class="line_td"><input type="checkbox" id="chkall"><font size="2">전체선택</font></td>
			<th colspan="2" class="line_th" align="center">골프장</th>
			<th class="line_th">티업시간</th>
			<th class="line_th">그린피</th>
			<th class="line_th">구매</th>
		</tr>
		<c:choose>
			<c:when test="${empty pickList}">
				<tr>
					<td colspan="6" align="center" class="line_td"><h3 style="color: #665b5f">장바구니에 등록한 상품이 없습니다</h3></td>
				</tr>
			</c:when>
			<c:otherwise>
				<c:forEach var="pList" items="${pickList}">
					
					<tr>
						<td class="line_td"><input type="checkbox" name="check" class="check"
							value="${pList.pick_no}" id="check${pList.pick_no}"></td>
						<td width="120" class="line_td"><img
							src="img/GOLFCC/${pList.loc_id}/${pList.cc_img}"
							onerror="this.src='<c:url value="img/GOLFCC/noimg.jpg"/>'" border="0"
							align="middle" width="120" height="80" /></td>
						<td class="line_td">
							<table>
								<tr>
									<td><a href="#"><b>${pList.cc_name}</b></a></td>
									<td>${pList.p_maxpeople}명${pList.p_hole}홀&nbsp;&nbsp;<span
										style="color: blue">예약인원 : ${pList.pick_amount} 명</span></td>
								</tr>
								<tr>
									<td><font size="2" color="#4374D9">${pList.p_uploaddate}</font></td>
									<td><font size="2" color="#665b5f">캐디유무 :
											${pList.p_caddyyn} 식사유무 : ${pList.p_babyn} 카트유무 :
											${pList.p_cartyn}</font></td>
								</tr>
							</table>
						</td>
						<td class="line_td"><b>${pList.p_pdate}</b><br>${pList.nickname}
							☎${pList.phone_id}</td>
						<td align="center" class="line_td">${pList.p_price}만원<br> <c:if
								test="${pList.emergency eq '긴급'}">
								<b style="color: red">[${pList.emergency}]</b>
							</c:if></td>
						<td class="orderVal" style="border-bottom: 1px solid #444444">
							<button class="order" data-pick_no="${pList.pick_no}">구매하기</button>
							<input type="checkbox" hidden="true" id="pick_no${pList.pick_no}" name="pick_no" value="${pList.pick_no}">
						</td>
					</tr>
				</c:forEach>

				<tr>
					<td colspan="6" align="center">
						<div class="list_number">
						<c:set var="curPage" value="${curPage+1}" /> <%-- 1 --%> 
						<c:set var="maxBlock" value="${maxBlock}" />
						<c:set var="minBlock" value="${minBlock+1}" /> 
						<p><div class="list_n_menu">					
						<c:if test="${curPage != 1}">
							<a href="pickListView?curPage=1">〈〈</a>
								<c:choose>
									<c:when test="${curPage>showBlock}">
										<a href="pickListView?curPage=${minBlock-1}">〈</a>
									</c:when>
									<c:otherwise>
										<span class="disabled">〈</span>
									</c:otherwise>
								</c:choose>
								
						</c:if>
						<c:if test="${curPage == 1}">
							<span class="disabled">〈〈</span>
							<span class="disabled">〈</span>
						</c:if>
						<c:forEach var="i" begin="${minBlock}" end="${maxBlock}" step="1">
							<c:choose>
								<c:when test="${curPage eq i}">
									<span class="current">${i}</span>
								</c:when>
								<c:when test="${curPage != i}">
									<a href="pickListView?curPage=${i}">${i}</a>
								</c:when>
							</c:choose>
						</c:forEach>
						
						<c:if test="${curPage != totalPage}">
							<c:choose>
								<c:when test="${curPage<=showBlock*perBlock}">
									<a href="pickListView?curPage=${maxBlock+1}">〉</a>
								</c:when>
								<c:otherwise>
									<span class="disabled">〉</span>
								</c:otherwise>
							</c:choose>
							<a href="pickListView?curPage=${totalPage}">〉〉</a>
						</c:if>
						<c:if test="${curPage == totalPage}">
							<span class="disabled">〉</span>
							<span class="disabled">〉〉</span>
						</c:if>
						</div></p>
						</div>
					</td>
				</tr>
			</c:otherwise>
		</c:choose>

	</table>
	<button id="delete">삭제</button>
</form>
