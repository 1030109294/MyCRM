<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String basePath = request.getScheme() + "://" +
request.getServerName() + ":" + request.getServerPort() +
request.getContextPath() + "/";
%>
<%
     Map<String , String> Pmap = (Map<String, String>) application.getAttribute("Pmap");
	Set<String> set = Pmap.keySet();
%>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>
    <script type="text/javascript" src="jquery/bs_typeahead/bootstrap3-typeahead.min.js"></script>
	<script type="text/javascript">
		var json = {
			<%
			 for (String key : set){
			 	String value = Pmap.get(key);
			 %>
			 "<%=key%>" : <%=value%>,
			<%
			 }
			%>
		}
     $(function () {
		 $("#create-customerName").typeahead({
			 source: function (query, process) {
				 $.post(
						 "transaction/getCustomerName.do",
						 { "name" : query },
						 function (data) {
							 //alert(data);
							 process(data);
						 },
						 "json"
				 );
			 },
			 delay: 1500
		 })
		 $(".time").datetimepicker({
			 minView: "month",
			 language:  'zh-CN',
			 format: 'yyyy-mm-dd',
			 autoclose: true,
			 todayBtn: true,
			 pickerPosition: "bottom-left"
		 });
		 $(".time1").datetimepicker({
			 minView: "month",
			 language:  'zh-CN',
			 format: 'yyyy-mm-dd',
			 autoclose: true,
			 todayBtn: true,
			 pickerPosition: "top-left"
		 });
		 $("#saveBtn").click(function () {
              $("#saveForm").submit();
		 })
		 //根据活动名查找市场活动
		 $("#aname").keydown(function (event) {
       if (event.keyCode == 13){
		   $.ajax({
			   url : "transaction/getActivityByName.do",
			   data : {
                  "name" : $("#aname").val()
			   },
			   type : "get",
			   dataType : "json",
			   success : function (data) {
				   var html = "";
                   $.each(data , function (i , n) {
					   html += '<tr>';
					   html += '<td><input type="radio" name="activity" value="' + n.id + '"/></td>';
					   html += '<td id="' + n.id + '">' + n.name + '</td>';
					   html += '<td>' + n.startDate + '</td>';
					   html += '<td>' + n.endDate + '</td>';
					   html += '<td>' + n.owner + '</td>';
					   html += '</tr>';
				   })
				   $("#activitySrcBody").html(html);
			   }
		   })
		   return false;
	   }
		 })
		 //提交查询的市场活动
		 $("#submitBtn").click(function () {
             $xz = $("input[name=activity]:checked");
             var id = $xz.val();
			 $.ajax({
				 url : "transaction/getActivitySrc.do",
				 data : {
				 	"id" : id
				 },
				 type : "get",
				 dataType : "json",
				 success : function (data) {
                   $("#create-activitySrc").val(data.name);
                   $("#activityId").val(data.id);
                   $("#findMarketActivity").modal("hide");
				 }

			 })
		 })
		 //根据联系人名称查询联系人
		 $("#cname").keydown(function (event) {
          if (event.keyCode == 13) {
			  $.ajax({
				  url: "transaction/getContactByName.do",
				  data: {
					  "fullname": $("#cname").val()
				  },
				  type: "get",
				  dataType: "json",
				  success: function (data) {
					  var html = "";
					  $.each(data, function (i, n) {
						  html += '<tr>';
						  html += '<td><input type="radio" name="contacts" value="' + n.id + '"/></td>';
						  html += '<td>' + n.fullname + '</td>';
						  html += '<td>' + n.email + '</td>';
						  html += '<td>' + n.mphone + '</td>';
						  html += '</tr>';
					  })
					  $("#contactsSrcBody").html(html);
				  }
			  })
			  return false;
		  }
		 })
		 //提交查询的联系人
		 $("#submitContactsBtn").click(function () {
            $xz = $("input[name=contacts]:checked");
            var id = $xz.val();
			 $.ajax({
				 url : "transaction/getContactById.do",
				 data : {
                 "id" : id
				 },
				 type : "get",
				 dataType : "json",
				 success : function (data) {
                      $("#create-contactsName").val(data.fullname);
                      $("#contactsId").val(data.id);
                      $("#findContacts").modal("hide");
				 }
			 })
		 })
		 $("#create-stage").change(function () {
			 var stage = $("#create-stage").val();
			 var possibility = json[stage];
			 $("#create-possibility").val(possibility);
		 })

     })
	</script>
</head>
<body>

	<!-- 查找市场活动 -->	
	<div class="modal fade" id="findMarketActivity" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">查找市场活动</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" class="form-control" id="aname" style="width: 300px;" placeholder="请输入市场活动名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable3" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
							</tr>
						</thead>
						<tbody id="activitySrcBody">

						</tbody>

					</table>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
						<button type="button" class="btn btn-primary" id="submitBtn">提交</button>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- 查找联系人 -->	
	<div class="modal fade" id="findContacts" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">查找联系人</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" class="form-control" id="cname" style="width: 300px;" placeholder="请输入联系人名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>邮箱</td>
								<td>手机</td>
							</tr>
						</thead>
						<tbody id="contactsSrcBody">


						</tbody>
					</table>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
						<button type="button" class="btn btn-primary" id="submitContactsBtn">提交</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	
	<div style="position:  relative; left: 30px;">
		<h3>创建交易</h3>
	  	<div style="position: relative; top: -40px; left: 70%;">
			<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
			<button type="button" class="btn btn-default" onclick="window.history.back()">取消</button>
		</div>
		<hr style="position: relative; top: -40px;">
	</div>
	<form class="form-horizontal" role="form" style="position: relative; top: -30px;" id="saveForm" action="transaction/save.do" method="post">
		<div class="form-group">
			<label for="create-transactionOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;" >
				<select class="form-control" id="create-owner" name="owner">
               <c:forEach items="${userList}" var="u">
				   <option value="${u.id}" ${user.id eq u.id ? "selected" : ""}>${u.name}</option>
			   </c:forEach>
				</select>
			</div>
			<label for="create-amountOfMoney" class="col-sm-2 control-label">金额</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-money" name="money">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-transactionName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-name" name="name">
			</div>
			<label for="create-expectedClosingDate" class="col-sm-2 control-label">预计成交日期<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control time" id="create-expectedDate" readonly name="expectedDate">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-accountName" class="col-sm-2 control-label">客户名称<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-customerName" placeholder="支持自动补全，输入客户不存在则新建" name="customerName">
			</div>
			<label for="create-transactionStage" class="col-sm-2 control-label">阶段<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
			  <select class="form-control" id="create-stage" name="stage">
			  	<option></option>
			  	<c:forEach var="s" items="${stage}">
					<option value="${s.value}">${s.text}</option>
				</c:forEach>
			  </select>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-transactionType" class="col-sm-2 control-label">类型</label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-type" name="type">
					<option></option>
					<c:forEach var="t" items="${transactionType}">
						<option value="${t.value}">${t.text}</option>
					</c:forEach>
				</select>
			</div>
			<label for="create-possibility" class="col-sm-2 control-label">可能性</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-possibility"  readonly>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-clueSource" class="col-sm-2 control-label">来源</label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-clueSource" name="source">
					<option></option>
					<c:forEach var="s" items="${source}">
						<option value="${s.value}">${s.text}</option>
					</c:forEach>
				</select>
			</div>
			<label for="create-activitySrc" class="col-sm-2 control-label">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" data-toggle="modal" data-target="#findMarketActivity"><span class="glyphicon glyphicon-search"></span></a></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-activitySrc">
				<input type="hidden" id="activityId" name="activityId">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-contactsName" class="col-sm-2 control-label">联系人名称&nbsp;&nbsp;<a href="javascript:void(0);" data-toggle="modal" data-target="#findContacts"><span class="glyphicon glyphicon-search"></span></a></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-contactsName">
				<input type="hidden" id="contactsId" name="contactsId">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-describe" class="col-sm-2 control-label">描述</label>
			<div class="col-sm-10" style="width: 70%;">
				<textarea class="form-control" rows="3" id="create-description" name="description"></textarea>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
			<div class="col-sm-10" style="width: 70%;">
				<textarea class="form-control" rows="3" id="create-contactSummary" name="contactSummary"></textarea>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control time1" id="create-nextContactTime" name="nextContactTime" readonly>
			</div>
		</div>
		
	</form>
</body>
</html>
