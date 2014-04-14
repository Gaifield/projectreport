 		//去左空格;
        function ltrim(s) {
            return s.replace(/^\s*/, "");
        }
        //去右空格;
        function rtrim(s) {
            return s.replace(/\s*$/, "");
        }
        //左右空格;
        function trim(s) {
            return rtrim(ltrim(s));
        }


        //点击“保存模板”，检验是否达到最大模板数
        function checkTemplate() {
        	var tag = true;
        	//数字，检验项目ID
        	var intZ = /^[1-9]\d*$/;
        	var projectId = $("#projectId").val();
        	if (projectId != "" && !intZ.test(projectId)) {
                tag = false;
                //alert("项目ID格式错误！请重新输入！");
                $("#tishiMessage").text("项目ID格式错误！请重新输入！");
                popLayer("#tishi");
        	}
        	if(tag == false){
            	return;
            }
            $.ajax({
                url: path+'checkTemplateSize.do',
                type: "post",
                success: function (msg) {
                    var data = $.parseJSON(msg);
                    if (data.status == 100) {
                        alert(data.data);
                    } else {
                        $("#Name").attr("value", "");
                        popLayer('#layer_save');
                    }
                }
            });
        }

        //保存模板
        function saveTemplate(obj) {
            closeLayer(obj);
            var tag = true;
            var projectType = $("#projectType").val();
            var projectStatus = $("#projectStatus").val();
            var projectId = $("#projectId").val();
            var performCity = $("#performCity").val();
            var projectName = $("#projectName").val();
            var startTime = $("#startTime").val();
            var endTime = $("#endTime").val();
            var performField = $("#performField").val();
            var name = $("#Name").val();
            if(name==null||name==""){
            	tag = false;
            	//alert("模板不能为空！请重新输入！");
            	 $("#tishiMessage").text("模板不能为空！请重新输入！");
                 popLayer("#tishi");
            	
            }
            if(tag == false){
            	return false;
            }
            $.ajax({
                url: path+'saveTemplate.do',
                type: "post",
                dataType: "json",
                data: {'templateContentVo.projectType': projectType,
                    'templateContentVo.projectStatus': projectStatus,
                    'templateContentVo.projectId': projectId,
                    'templateContentVo.performCity': performCity,
                    'templateContentVo.projectName': projectName,
                    'templateContentVo.startTime': startTime,
                    'templateContentVo.endTime': endTime,
                    'templateContentVo.performField': performField,
                    templateName: name},
                success: function (msg) {
                    if (msg.status == 100) {
                        //模板内容相同，是否合并？
                        $("#templateMerge").html("新建模板与“" + msg.data.templateName + "”模板内容相同，是否合并？");
                        $("#templateID").attr("value", msg.data.templateId);
                        popLayer('#layer_re');

                    } else if (msg.status == 300) {
                        $("#Name").attr("value", "");
                       // alert("模板名称已存在！请重新命名！");
                        $("#tishiMessage").text("模板名称已存在！请重新命名！");
                        popLayer("#tishi");
                    } else {
                        closeLayer(obj);
                        window.location.reload();
                    }
                }
            });
        }
        
      //保存模板
        function saveTemplateOther(obj) {
            closeLayer(obj);
            var tag = true;
            var projectType = $("#projectType").val();
            var projectStatus = $("#projectStatus").val();
            var projectId = $("#projectId").val();
            var performCity = $("#performCity").val();
            var projectName = $("#projectName").val();
            var startTime = $("#startTime").val();
            var endTime = $("#endTime").val();
            var performField = $("#performField").val();
            var name = $("#Name").val();
            if(name==null||name==""){
            	tag = false;
            	//alert("模板不能为空！请重新输入！");
            	 $("#tishiMessage").text("模板不能为空！请重新输入！");
                 popLayer("#tishi");
            }
            if(tag == false){
            	return false;
            }
            $.ajax({
                url: path+'saveTemplate.do',
                type: "post",
                dataType: "json",
                data: {'templateContentVo.projectType': projectType,
                    'templateContentVo.projectStatus': projectStatus,
                    'templateContentVo.projectId': projectId,
                    'templateContentVo.performCity': performCity,
                    'templateContentVo.projectName': projectName,
                    'templateContentVo.startTime': startTime,
                    'templateContentVo.endTime': endTime,
                    'templateContentVo.performField': performField,
                    templateName: name},
                success: function (msg) {
                    if (msg.status == 100) {
                        //模板内容相同，是否合并？
                        $("#templateMerge").html("新建模板与“" + msg.data.templateName + "”模板内容相同，是否合并？");
                        $("#templateID").attr("value", msg.data.templateId);
                        popLayer('#layer_re');

                    } else if (msg.status == 300) {
                        $("#Name").attr("value", "");
                        //alert("模板名称已存在！请重新命名！");
                        $("#tishiMessage").text("模板名称已存在！请重新命名！");
                        popLayer("#tishi");
                    } else {
                        closeLayer(obj);
                        queryProjectOther();
                    }
                }
            });
        }

        //关闭弹出层
        function closeLayer(obj) {
            $(obj).parents(".author_layer").hide();
            $(".mask").remove();
        }

        //编辑模板
        function updateTemplate(templateId) {
        	$("#sub").show();
        	$("#returnSet").show();
        	$("#saveTemplate").hide();
        	$("#sure").hide();
        	$("#reset").hide();
        	$("#modifyTemplateID").attr("value",templateId);
        	$.ajax({
                url: path+'updateTemplate.do',
                type: "post",
                dataType: "json",
                data: {
                    templateId: templateId},
                success: function (msg) {
                    $("#projectType").attr("value", msg.projectType);
                    $("#projectStatus").attr("value", msg.projectStatus);
                    if (msg.projectId == 0) {
                        $("#projectId").attr("value", "");
                    } else {
                        $("#projectId").attr("value", msg.projectId);
                    }
                    $("#performCity").attr("value", msg.performCity);
                    $("#projectName").attr("value", msg.projectName);
                    $("#startTime").attr("value", msg.startTime);
                    $("#endTime").attr("value", msg.endTime);
                    $("#performField").attr("value", msg.performField);
                }
            });
        }
		//编辑模板后保存模板
		function modifyTemplate(){
            //数字，检验项目ID
            var intZ = /^[1-9]\d*$/;
            var templateID = $("#modifyTemplateID").val();
            var projectType = $("#projectType").val();
            var projectStatus = $("#projectStatus").val();
            var projectId = $("#projectId").val();
            var performCity = $("#performCity").val();
            var projectName = $("#projectName").val();
            var startTime = $("#startTime").val();
            var endTime = $("#endTime").val();
            var performField = $("#performField").val();
            var name = $("#Name").val();
            if (projectId != "" && !intZ.test(projectId)) {
                tag = false;
            }
            $.ajax({
                url: path+'saveTemplate.do',
                type: "post",
                dataType: "json",
                data: {templateId:templateID,
                	'templateContentVo.projectType': projectType,
                    'templateContentVo.projectStatus': projectStatus,
                    'templateContentVo.projectId': projectId,
                    'templateContentVo.performCity': performCity,
                    'templateContentVo.projectName': projectName,
                    'templateContentVo.startTime': startTime,
                    'templateContentVo.endTime': endTime,
                    'templateContentVo.performField': performField,
                    templateName: name},
                success: function (msg) {
                    if (msg.status == 100) {
                       // alert("模板内容与“"+msg.data.templateName+"”模板内容相同！请重新编辑！");
                        $("#tishiMessage").text("模板内容与“"+msg.data.templateName+"”模板内容相同！请重新编辑！");
                        popLayer("#tishi");
					}else{
                        window.location.reload();
                    }
                }
            });
		}
        
        
        //合并模板
        function MergeTemplate() {
            var templateID = $("#templateID").val();
            var templateName = $("#Name").val();
            $.ajax({
                url: path+'mergeTemplate.do',
                type: "post",
                dataType: "json",
                data: {
                    templateName: templateName,
                    templateId: templateID
                },
                success: function (msg) {
                    if (msg.status == 200) {
                        window.location.reload();
                    } else {
                       // alert("合并失败！请重试！");
                        $("#tishiMessage").text("合并失败！请重试！");
                        popLayer("#tishi");
                        window.location.reload();
                    }
                }
            });
        }
        
      //合并模板
        function MergeTemplateOther() {
            var templateID = $("#templateID").val();
            var templateName = $("#Name").val();
            $.ajax({
                url: path+'mergeTemplate.do',
                type: "post",
                dataType: "json",
                data: {
                    templateName: templateName,
                    templateId: templateID
                },
                success: function (msg) {
                    if (msg.status == 200) {
                    	queryProjectOther();
                    } else {
                       // alert("合并失败！请重试！");
                        $("#tishiMessage").text("合并失败！请重试！");
                        popLayer("#tishi");
                        queryProjectOther();
                    }
                }
            });
        }

        //弹出删除提示框
        function openDeleteTemplate(templateId) {
            $("#delTemplate").attr("value", templateId);
            var templateName = $("#delTemplate" + templateId).val();
            $("#delTem").html("确定要删除模板“" + templateName + "”?");
            popLayer('#layer_del');
        }

        //删除模板
        function deleteTemplate(obj) {
            var templateId = $("#delTemplate").val();
            $.ajax({
                url: path + 'deleteTemplate.do',
                type: "post",
                dataType: "json",
                data: {
                    templateId: templateId
                },
                success: function (msg) {
                    if (msg.status == 200) {
                        closeLayer(obj);
                        window.location.reload();
                    } else {
                       // alert("合并失败！请重试！");
                        $("#tishiMessage").text("合并失败！请重试！");
                        popLayer("#tishi");
                        window.location.reload();
                    }
                }
            });
        }
        
      //删除模板
        function deleteTemplateOther(obj) {
            var templateId = $("#delTemplate").val();
            $.ajax({
                url: path + 'deleteTemplate.do',
                type: "post",
                dataType: "json",
                data: {
                    templateId: templateId
                },
                success: function (msg) {
                    if (msg.status == 200) {
                        closeLayer(obj);
                        queryProjectOther();
                    } else {
                       // alert("合并失败！请重试！");
                        $("#tishiMessage").text("合并失败！请重试！");
                        popLayer("#tishi");
                        queryProjectOther();
                    }
                }
            });
        }

        //清空模板
        function empty() {
            /*$("#projectType").attr("value", 0);
            $("#projectStatus").attr("value", 0);
            $("#projectId").attr("value", "");
            $("#performCity").attr("value", "");
            $("#projectName").attr("value", "");
            $("#startTime").attr("value", "");
            $("#endTime").attr("value", "");
            $("#performField").attr("value", "");*/
        	loadData();
        }
        
        function loadData(msg){
        	 $("#projectType").attr("value", msg ? msg.projectType:"");
             $("#projectStatus").attr("value", msg ?msg.projectStatus:"");
    
                 $("#projectId").attr("value", msg ? (msg.projectId ==0 ? "" : msg.projectId ) :"");
          
             $("#performCity").attr("value", msg ?msg.performCity:"");
             $("#projectName").attr("value", msg ?msg.projectName:"");
             $("#startTime").attr("value", msg ?msg.startTime:"");
             $("#endTime").attr("value", msg ?msg.endTime:"");
             $("#performField").attr("value", msg ?msg.performField:"");
        }
        //"取消"刷新页面
        function returnSet(){
        	$("#sub").hide();
        	$("#returnSet").hide();
        	$("#saveTemplate").show();
        	$("#sure").show();
        	$("#reset").show();
        	empty();
        }
        
      
      
      //点击“模板”名称回显数据
      function templateNameSearcher(templateId){
    	$("#sub").hide();
      	$("#returnSet").hide();
      	$("#saveTemplate").show();
      	$("#sure").show();
      	$("#reset").show();
    	$.ajax({
              url: path+'updateTemplate.do',
              type: "post",
              dataType: "json",
              data: {
                  templateId: templateId},
              success: function (msg) {
            	  loadData(msg);
            	  /*
                  $("#projectType").attr("value", msg.projectType);
                  $("#projectStatus").attr("value", msg.projectStatus);
                  if (msg.projectId == 0) {
                      $("#projectId").attr("value", "");
                  } else {
                      $("#projectId").attr("value", msg.projectId);
                  }
                  $("#performCity").attr("value", msg.performCity);
                  $("#projectName").attr("value", msg.projectName);
                  $("#startTime").attr("value", msg.startTime);
                  $("#endTime").attr("value", msg.endTime);
                  $("#performField").attr("value", msg.performField);
                  */
                  $("#page").attr("value",0);
                  $("#frmTemplete").submit();
              }
          });
      }
      
      //点击“查询”提交表单查询项目
      function queryProject(){
    	  var tag = true;
    		var intZ = /^[1-9]\d*$/;
    		var projectId = $("#projectId").val();
    		if (projectId != "" && !intZ.test(projectId)) {
    	        tag = false;
    	    }
    		if(tag==false){
    			//alert("项目ID格式错误！请重新输入！");
    			 $("#tishiMessage").text("项目ID格式错误！请重新输入！");
                 popLayer("#tishi");
    			return;
    		}
    	  $("#frmTemplete").submit();
      }
      
    //点击“模板”名称回显数据
      function templateNameSearcrojectherOther(templateId){
    	  $("#sub").hide();
        	$("#returnSet").hide();
        	$("#saveTemplate").show();
        	$("#sure").show();
        	$("#reset").show();
      	  	//回显数据
    	  $.ajax({
              url: path+'updateTemplate.do',
              type: "post",
              dataType: "json",
              data: {
                  templateId: templateId},
              success: function (msg) {
                  $("#projectType").attr("value", msg.projectType);
                  $("#projectStatus").attr("value", msg.projectStatus);
                  if (msg.projectId == 0) {
                      $("#projectId").attr("value", "");
                  } else {
                      $("#projectId").attr("value", msg.projectId);
                  }
                  $("#performCity").attr("value", msg.performCity);
                  $("#projectName").attr("value", msg.projectName);
                  $("#startTime").attr("value", msg.startTime);
                  $("#endTime").attr("value", msg.endTime);
                  $("#performField").attr("value", msg.performField);
              }
          });
    	 }
   