<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.List" %>
<%@ page import="com.changsan.settings.domain.DicValue" %>
<%@ page import="com.changsan.workbench.domain.Tran" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" +
request.getServerName() + ":" + request.getServerPort() +
request.getContextPath() + "/";

//准备数据字典中的stage的数据字典列表
List<DicValue> dvList = (List<DicValue>) application.getAttribute("stage");

//准备stage和possibility的关系表
	Map<String , String> pMap = (Map<String, String>) application.getAttribute("Pmap");

	//根据Pmap准备Pmap的Set集合
	Set<String> set = pMap.keySet();

	//准备： 前面正常阶段和后面丢失阶段的分界下标
	 int point = 0;
	 for (int i = 0 ; i < dvList.size() ; i++){
	 	//根据下标获取每个字典值
		 DicValue value = dvList.get(i);
		 //从中取得stage的value值
		 String stage = value.getValue();
		 //根据stage值获取所对应的可能性
		 String possibility = pMap.get(stage);
		 //比较possibility是否是0 如果是0代表已丢失的阶段下标
		 if ("0".equals(possibility)){
		 	//找到已丢失的阶段下标
			 point = i;
			 break;
		 }
	 }
%>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />

<style type="text/css">
.mystage{
	font-size: 20px;
	vertical-align: middle;
	cursor: pointer;
}
.closingDate{
	font-size : 15px;
	cursor: pointer;
	vertical-align: middle;
}
</style>
	
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

<script type="text/javascript">

	//默认情况下取消和保存按钮是隐藏的
	var cancelAndSaveBtnDefault = true;
	
	$(function(){
		$("#remark").focus(function(){
			if(cancelAndSaveBtnDefault){
				//设置remarkDiv的高度为130px
				$("#remarkDiv").css("height","130px");
				//显示
				$("#cancelAndSaveBtn").show("2000");
				cancelAndSaveBtnDefault = false;
			}
		});
		
		$("#cancelBtn").click(function(){
			//显示
			$("#cancelAndSaveBtn").hide();
			//设置remarkDiv的高度为130px
			$("#remarkDiv").css("height","90px");
			cancelAndSaveBtnDefault = true;
		});
		
		$(".remarkDiv").mouseover(function(){
			$(this).children("div").children("div").show();
		});
		
		$(".remarkDiv").mouseout(function(){
			$(this).children("div").children("div").hide();
		});
		
		$(".myHref").mouseover(function(){
			$(this).children("span").css("color","red");
		});
		
		$(".myHref").mouseout(function(){
			$(this).children("span").css("color","#E6E6E6");
		});
		
		
		//阶段提示框
		$(".mystage").popover({
            trigger:'manual',
            placement : 'bottom',
            html: 'true',
            animation: false
        }).on("mouseenter", function () {
                    var _this = this;
                    $(this).popover("show");
                    $(this).siblings(".popover").on("mouseleave", function () {
                        $(_this).popover('hide');
                    });
                }).on("mouseleave", function () {
                    var _this = this;
                    setTimeout(function () {
                        if (!$(".popover:hover").length) {
                            $(_this).popover("hide")
                        }
                    }, 100);
                });

		selectTranHistoryList();

	});

	function selectTranHistoryList() {
		$.ajax({
			url : "transaction/getTranHistoryByTranId.do",
			data : {
               "id" : "${tran.id}"
			},
			type : "get",
			dataType : "json",
			success : function (data) {
                    var html = "";
                    $.each(data , function (i , n) {
					    html += "<tr>";
						html += "<td>" + n.stage +"</td>";
						html += "<td>" + n.money + "</td>";
						html += "<td>" + n.possibility + "</td>";
						html += "<td>" + n.expectedDate + "</td>";
						html += "<td>" + n.createTime + "</td>";
						html += "<td>" + n.createBy + "</td>";
						html += "</tr>";
					})
				$("#tranHistoryBody").html(html);
			}
		})
	}
	/*
	* stage 需要变更的阶段
	* i  需要变更的下标
	* */
	function changeStage(stage , i) {

		$.ajax({
			url : "transaction/changeStage.do",
			data : {
             "stage" : stage,
				"id" : "${tran.id}",
				"money" : "${tran.money}",
				"expectedDate"  : "${tran.expectedDate}"
			},
			type : "get",
			dataType : "json",
			success : function (data) {
                  if (data.success){
                  $("#stage").html(data.t.stage);
                  $("#possibility").html(data.t.possibility);
                  $("#editBy").html(data.t.editBy);
                  $("#editTime").html(data.t.editTime);
                  selectTranHistoryList();
                  changeIcon(stage , i);
				  }else {
                  	alert("修改阶段失败");
				  }
			}

		})
	}
	function changeIcon(stage , index) {

		//当前阶段
		var currentStage = stage;
		//当前阶段的可能性
		var currentPossibility = $("#possibility").html();
		//当前阶段的下标
		var index = index;
		//分界的下标
		var point = "<%=point%>";

		if (currentPossibility == "0"){
			//当前阶段的可能性为0时，前面七个为正常阶段，全为黑圈 后面两个为丢失阶段 一个红叉 ， 一个黑叉
			//遍历正常阶段前七个
			for (var i = 0 ; i < point ; i++){
				//黑圈---------------
				$("#" + i).removeClass();
				$("#" + i).addClass("glyphicon glyphicon-record mystage");
				$("#" + i).css("color" , "#000000");
			}
			//遍历后两个
			for (var i = point ; i < <%=dvList.size()%> ; i++){
                 if (index == i){
                 	//红叉-------------
					 $("#" + i).removeClass();
					 $("#" + i).addClass("glyphicon glyphicon-remove mystage");
					 $("#" + i).css("color" , "#ff0000");
				 }else {
                 	//黑叉-------------
					 $("#" + i).removeClass();
					 $("#" + i).addClass("glyphicon glyphicon-remove mystage");
					 $("#" + i).css("color" , "#000000");
				 }
			}
		}else {
			//当前阶段可能性不为0，前面七个可能为当前位置 绿圈 黑圈 后面两个一定为黑叉
			for (var i = 0 ; i < point ; i++){
				if (i == index){
					//当前坐标----------
					$("#" + i).removeClass();
					$("#" + i).addClass("glyphicon glyphicon-map-marker mystage");
					$("#" + i).css("color" , "#90F790");
				}else if (i < index){
					//绿圈----------
					$("#" + i).removeClass();
					$("#" + i).addClass("glyphicon glyphicon-ok-circle mystage");
					$("#" + i).css("color" , "#90F790");
				}else {
					//黑圈-----------
					$("#" + i).removeClass();
					$("#" + i).addClass("glyphicon glyphicon-record mystage");
					$("#" + i).css("color" , "#000000");
				}
			}
			for (var i = point ; i < <%=dvList.size()%> ; i++) {
				//黑叉---------------
				$("#" + i).removeClass();
				$("#" + i).addClass("glyphicon glyphicon-remove mystage");
				$("#" + i).css("color" , "#000000");
			}
		}

	}
	
</script>

</head>
<body>
	
	<!-- 返回按钮 -->
	<div style="position: relative; top: 35px; left: 10px;">
		<a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left" style="font-size: 20px; color: #DDDDDD"></span></a>
	</div>
	
	<!-- 大标题 -->
	<div style="position: relative; left: 40px; top: -30px;">
		<div class="page-header">
			<h3>${tran.customerId}-${tran.name} <small>￥${tran.money}</small></h3>
		</div>
		<div style="position: relative; height: 50px; width: 250px;  top: -72px; left: 700px;">
			<button type="button" class="btn btn-default" onclick="window.location.href='edit.jsp';"><span class="glyphicon glyphicon-edit"></span> 编辑</button>
			<button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
		</div>
	</div>

	<!-- 阶段状态 -->
	<div style="position: relative; left: 40px; top: -50px;">
		阶段&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<%
		   //准备当前阶段
			Tran currentTran = (Tran) request.getAttribute("tran");
			String currentStage = currentTran.getStage();
			//根据当前阶段获取可能性
			String currentPossibility= pMap.get(currentStage);

			//如果可能性为0那么一定是后两个 前七个为黑圈，后两个一个为红叉，一个为黑叉
			if ("0".equals(currentPossibility)){
				//遍历数据字典找到根据value找到所对应的stage阶段 再根据阶段找到每个阶段的可能性
               for (int i = 0 ; i < dvList.size() ; i++){
               	DicValue value = dvList.get(i);
               	String stage = value.getValue();
               	String possibility = pMap.get(stage);

               	//比较所有的可能性，为0则为后两个丢失阶段 一个为红叉 一个为黑叉
               	if ("0".equals(possibility)){
               		//如果当前阶段为丢失阶段为红叉
               		if (stage.equals(currentStage)){
               			//红叉---------------
		%>
		<span id="<%=i%>" onclick="changeStage('<%=stage%>' , '<%=i%>')"
			  class="glyphicon glyphicon-remove mystage"
			  data-toggle="popover" data-placement="bottom"
			  data-content="<%=value.getText()%>" style="color: #FF0000;"></span>
		-----------
		<%
					}else {
               			//不然就为黑叉-----------------
					}
               		%>
		<span id="<%=i%>" onclick="changeStage('<%=stage%>' , '<%=i%>')"
			  class="glyphicon glyphicon-remove mystage"
			  data-toggle="popover" data-placement="bottom"
			  data-content="<%=value.getText()%>" style="color: #000000;"></span>
		-----------
		<%
				}else {
               		//为正常阶段  全部为黑圈------------------------
        %>
		<span id="<%=i%>" onclick="changeStage('<%=stage%>' , '<%=i%>')"
			  class="glyphicon glyphicon-record mystage"
			  data-toggle="popover" data-placement="bottom"
			  data-content="<%=value.getText()%>" style="color: #000000;"></span>
		-----------
		<%
				}
			   }
			}else {
				//当前阶段下标
				int index = 0;
				for (int i = 0 ; i < dvList.size() ; i++){
					DicValue dicValue = dvList.get(i);
					String stage = dicValue.getValue();
					if (stage.equals(currentStage)){
						index = i;
						break;
					}
				}
				//可能性不为0那么一定为前七个 前七个可能为当前位置 绿圈 黑圈 后两个一定是黑叉
				for (int i = 0 ; i < dvList.size() ; i++){
					DicValue value = dvList.get(i);
					String stage = value.getValue();
					String possibility = pMap.get(stage);
					//如果可能性为0 那么一定为丢失部分 为后两个黑叉
					if ("0".equals(possibility)){
						//黑叉------------------------
		%>
		<span id="<%=i%>" onclick="changeStage('<%=stage%>' , '<%=i%>')"
			  class="glyphicon glyphicon-remove mystage"
			  data-toggle="popover" data-placement="bottom"
			  data-content="<%=value.getText()%>" style="color: #000000;"></span>
		-----------
		<%
					}else {
						//为正常阶段 可能为当前位置 绿圈 黑圈
						if (i == index){
							//当前位置-------------------------
		%>
		<span id="<%=i%>" onclick="changeStage('<%=stage%>' , '<%=i%>')"
			  class="glyphicon glyphicon-map-marker mystage"
			  data-toggle="popover" data-placement="bottom"
			  data-content="<%=value.getText()%>" style="color: #90F790;"></span>
		-----------
		<%
						}else if (i < index){
							//绿圈-------------------
		%>
		<span id="<%=i%>" onclick="changeStage('<%=stage%>' , '<%=i%>')"
			  class="glyphicon glyphicon-ok-circle mystage"
			  data-toggle="popover" data-placement="bottom"
			  data-content="<%=value.getText()%>" style="color: #90F790;"></span>
		-----------
		<%
						}else {
							//黑圈------------------
							%>
		<span id="<%=i%>" onclick="changeStage('<%=stage%>' , '<%=i%>')"
			  class="glyphicon glyphicon-record mystage"
			  data-toggle="popover" data-placement="bottom"
			  data-content="<%=value.getText()%>" style="color: #000000;"></span>
		-----------
		<%
						}
					}
				}
			}
		%>
		<%--<span class="glyphicon glyphicon-ok-circle mystage" data-toggle="popover" data-placement="bottom" data-content="资质审查" style="color: #90F790;"></span>
		-----------
		<span class="glyphicon glyphicon-ok-circle mystage" data-toggle="popover" data-placement="bottom" data-content="需求分析" style="color: #90F790;"></span>
		-----------
		<span class="glyphicon glyphicon-ok-circle mystage" data-toggle="popover" data-placement="bottom" data-content="价值建议" style="color: #90F790;"></span>
		-----------
		<span class="glyphicon glyphicon-ok-circle mystage" data-toggle="popover" data-placement="bottom" data-content="确定决策者" style="color: #90F790;"></span>
		-----------
		<span class="glyphicon glyphicon-map-marker mystage" data-toggle="popover" data-placement="bottom" data-content="提案/报价" style="color: #90F790;"></span>
		-----------
		<span class="glyphicon glyphicon-record mystage" data-toggle="popover" data-placement="bottom" data-content="谈判/复审"></span>
		-----------
		<span class="glyphicon glyphicon-record mystage" data-toggle="popover" data-placement="bottom" data-content="成交"></span>
		-----------
		<span class="glyphicon glyphicon-record mystage" data-toggle="popover" data-placement="bottom" data-content="丢失的线索"></span>
		-----------
		<span class="glyphicon glyphicon-record mystage" data-toggle="popover" data-placement="bottom" data-content="因竞争丢失关闭"></span>
		-------------%>
		<span class="closingDate">${tran.expectedDate}</span>
	</div>
	
	<!-- 详细信息 -->
	<div style="position: relative; top: 0px;">
		<div style="position: relative; left: 40px; height: 30px;">
			<div style="width: 300px; color: gray;">所有者</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${tran.owner}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">金额</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${tran.money}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 10px;">
			<div style="width: 300px; color: gray;">名称</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${tran.name}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">预计成交日期</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${tran.expectedDate}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 20px;">
			<div style="width: 300px; color: gray;">客户名称</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${tran.customerId}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">阶段</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b id="stage">${tran.stage}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 30px;">
			<div style="width: 300px; color: gray;">类型</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${tran.type}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">可能性</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;" ><b id="possibility">${tran.possibility}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 40px;">
			<div style="width: 300px; color: gray;">来源</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${tran.source}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">市场活动源</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${tran.activityId}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 50px;">
			<div style="width: 300px; color: gray;">联系人名称</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${tran.contactsId}</b></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 60px;">
			<div style="width: 300px; color: gray;">创建者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${tran.createBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${tran.createTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 70px;">
			<div style="width: 300px; color: gray;">修改者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b id="editBy">${tran.editBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;" id="editTime">${tran.editTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 80px;">
			<div style="width: 300px; color: gray;">描述</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					${tran.description}
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 90px;">
			<div style="width: 300px; color: gray;">联系纪要</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					&nbsp;${tran.contactSummary}
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 100px;">
			<div style="width: 300px; color: gray;">下次联系时间</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${tran.nextContactTime}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
	</div>
	
	<!-- 备注 -->
	<div style="position: relative; top: 100px; left: 40px;">
		<div class="page-header">
			<h4>备注</h4>
		</div>
		
		<!-- 备注1 -->
		<div class="remarkDiv" style="height: 60px;">
			<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
			<div style="position: relative; top: -40px; left: 40px;" >
				<h5>哎呦！</h5>
				<font color="gray">交易</font> <font color="gray">-</font> <b>动力节点-交易01</b> <small style="color: gray;"> 2017-01-22 10:10:10 由zhangsan</small>
				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
				</div>
			</div>
		</div>
		
		<!-- 备注2 -->
		<div class="remarkDiv" style="height: 60px;">
			<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
			<div style="position: relative; top: -40px; left: 40px;" >
				<h5>呵呵！</h5>
				<font color="gray">交易</font> <font color="gray">-</font> <b>动力节点-交易01</b> <small style="color: gray;"> 2017-01-22 10:20:10 由zhangsan</small>
				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
				</div>
			</div>
		</div>
		
		<div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
			<form role="form" style="position: relative;top: 10px; left: 10px;">
				<textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"  placeholder="添加备注..."></textarea>
				<p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
					<button id="cancelBtn" type="button" class="btn btn-default">取消</button>
					<button type="button" class="btn btn-primary">保存</button>
				</p>
			</form>
		</div>
	</div>
	
	<!-- 阶段历史 -->
	<div>
		<div style="position: relative; top: 100px; left: 40px;">
			<div class="page-header">
				<h4>阶段历史</h4>
			</div>
			<div style="position: relative;top: 0px;">
				<table id="activityTable" class="table table-hover" style="width: 900px;">
					<thead>
						<tr style="color: #B3B3B3;">
							<td>阶段</td>
							<td>金额</td>
							<td>可能性</td>
							<td>预计成交日期</td>
							<td>创建时间</td>
							<td>创建人</td>
						</tr>
					</thead>
					<tbody id="tranHistoryBody">


					</tbody>
				</table>
			</div>
			
		</div>
	</div>
	
	<div style="height: 200px;"></div>
	
</body>
</html>
