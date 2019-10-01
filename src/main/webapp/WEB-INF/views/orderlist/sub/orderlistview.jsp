<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		$(".cancle").on("click",function(event){
			
			var o_no = $(this).attr("data-o_no");
			$("#o_no"+o_no).attr("checked","checked");
			console.log(o_no);
			console.log($("#o_no"+o_no).val());
			var cancleConfirm = confirm("상품 예약을 취소하시겠습니까?");
			if (cancleConfirm == true) {
				$("form[name='myForm']").attr({"action":"orderCancle","method":"post"});
			} else if (cancleConfirm == false) {
				event.preventDefault();
				$("#o_no"+o_no).prop("checked",false);
			}
			
		});
	});
</script>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">

<form action="orderList" method="post" class="form_main" name="myForm">
<h3>예약 정보</h3>
<table class="line_table">
	<tr>
		<th class="line_th">상품명</th>
		<th class="line_th">판매가</th>
		<th class="line_th">수량</th>
		<th class="line_th">총 구매 가격</th>
		<th class="line_th">구매일</th>
		<th class="line_th">담당 매니저</th>
		<th class="line_th"></th>
	</tr>
	<c:choose>
		<c:when test="${empty orderList}">
			<td colspan="6" align="center" class="line_td"><h3 style="color: #665b5f">예약내역이 없습니다</h3></td>
		</c:when>
		<c:otherwise>
			<c:forEach var="oList" items="${orderList}">
	<tr>
		<td class="line_td" align="center">${oList.cc_name}</td>
		<td class="line_td" align="center">${oList.p_price} 만원</td>
		<td class="line_td" align="center">${oList.o_amount} 명</td>
		<td class="line_td" align="center">${oList.o_price} 만원</td>
		<td class="line_td" align="center">${oList.o_date}</td>
		<td class="line_td" align="center">${oList.nickname} <font style="color:blue" size="3">☎ ${oList.phone_id}</font></td>
		<td class="orderVal" style="border-bottom: 1px solid #444444">
			<button class="cancle" data-o_no="${oList.o_no}">예약취소</button>
			<input type="hidden" name="o_amount${oList.o_no}" value="${oList.o_amount}">
			<input type="hidden" name="p_id${oList.o_no}" value="${oList.p_id}">
			<input type="checkbox" hidden="true" id="o_no${oList.o_no}" name="o_no" value="${oList.o_no}">
		</td>
	</tr>
	</c:forEach>
	<tr>
		<td colspan="6" align="center">
		<ul class="pagination justify-content-center" style="margin-top:10px">
			<c:set var="curPage" value="${curPage+1}"/> <%-- 1 --%>
			<c:set var="maxBlock" value="${maxBlock}"/> 
			<c:set var="minBlock" value="${minBlock+1}"/> 
			
			<c:if test="${curPage != 1}">
				<li class="page-item"><a class="page-link" href="orderList?curPage=1"><<</a></li>
				<c:if test="${curPage>showBlock}"><li class="page-item"><a class="page-link" href="orderList?curPage=${minBlock-1}"><</a></li></c:if>
			</c:if>
			
			
			<c:forEach var="i" begin="${minBlock}" end="${maxBlock}" step="1">
				<c:choose>
					<c:when test="${curPage eq i}">
						<li class="page-item disabled"><a class="page-link" href="orderList?curPage=${i}">${i}</a></li>
					</c:when>
					<c:when test="${curPage != i}">
						<li class="page-item"><a class="page-link" href="orderList?curPage=${i}">${i}</a></li>
					</c:when>
				</c:choose>	
			</c:forEach>
			
			<c:if test="${curPage != totalPage}">
				<c:if test="${curPage<=showBlock*perBlock}"><li class="page-item"><a class="page-link" href="orderList?curPage=${maxBlock+1}">></a></li></c:if>
				<li class="page-item"><a class="page-link" href="orderList?curPage=${totalPage}">>></a></li>
			</c:if>
			</ul>
		</td>
	</tr>
		</c:otherwise>
	</c:choose>
	
</table>

</form>