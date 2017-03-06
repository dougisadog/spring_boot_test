<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html lang="en">

<script>
function sub() {
	document.getElementById("form").submit();
}
//打开联系人列表
function openContactsList() {
	alert("openContactsList");
	window.functionslistner.openContactsList();
}
//开启默认铃声
function ringtone() {
	alert("ringtone");
	window.functionslistner.ringtone();
}
//开启震动 repeat 为"repeat"时 重复 wait 为间隔等待时间 last为 持续时间  1000= 1s 
function vibrator(repeat,wait,last) {
	alert("vibrator" + repeat + wait + last);
	window.functionslistner.vibrator(repeat,wait,last);
}

//监听 联系人列表 选择  name 为联系人姓名 tel为电话 
function onContactSelected(name ,tel) {
	alert("onContactSelected:" + name + tel);
}


function checkPic() {
    var picPath = document.getElementById("picPath").value;
    var type = picPath.substring(picPath.lastIndexOf(".") + 1, picPath.length).toLowerCase();
    if (type != "jpg" && type != "bmp" && type != "gif" && type != "png") {
        alert("请上传正确的图片格式");
        return false;
    }
    return true;
}
//只要 有元素 type="file" 即可调起 图片上传 下发js 只是显示逻辑 
//建议在客户端使用web.loadUrl("http://www.chuantu.biz"); 进行测试 
//图片预览  当客户端获取 图片后 可接受到回调 
function PreviewImage(divImage, upload, width, height) {
    if (checkPic()) {
        try {
            var imgPath;      //图片路径

            var Browser_Agent = navigator.userAgent;
            //判断浏览器的类型
            if (Browser_Agent.indexOf("Firefox") != -1) {
                //火狐浏览器

//getAsDataURL在Firefox7.0 无法预览本地图片，这里需要修改
                imgPath = upload.files[0].getAsDataURL();
                document.getElementById(divImage).innerHTML = "<img id='imgPreview' src='" + imgPath + "' width='" + width + "' height='" + height + "'/>";
            } else {
                //IE浏览器 ie9 必须在兼容模式下才能显示预览图片
                var Preview = document.getElementById(divImage);
                Preview.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = upload.value;
                Preview.style.width = width;
                Preview.style.height = height;
            }
        } catch (e) {
            alert("请上传正确的图片格式");
        }
    }
}
</script>
<body>
	<c:url value="/resources/text.txt" var="url"/>
	<spring:url value="/resources/text.txt" htmlEscape="true" var="springUrl" />
	Spring URL: ${springUrl} at ${time}
	<br>
	JSTL URL: ${url}
	<br>
	Message: ${message}
	
	<form id ="form" action="form" method="post">
	<input type="" id="a" name = "a">aaa</input>
	<input type="" id="b" name = "b">bbb</input>
	<button type="button" onclick="sub();">submit</button>
	</form>
	<button type="button" style="padding: 20px;" onclick="openContactsList();">openContactsList</button>
	<button type="button" style="padding: 20px;" onclick="ringtone();">Ringtone</button>
	<button type="button" style="padding: 20px;" onclick="vibrator('repeat','2000','1000');">Vibrator</button>
	    <input type="file" id="picPath" name="doc" onchange="PreviewImage('Preview',this,120,120);" />
    <div id="Preview" style="filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale);">
    </div>
</body>

</html>
