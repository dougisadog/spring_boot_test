package boot_test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.wicket.ajax.json.JSONException;
import org.apache.wicket.ajax.json.JSONObject;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import entity.Announce;
import me.hao0.wechat.core.Wechat;
import me.hao0.wechat.core.WechatBuilder;
import me.hao0.wechat.exception.EventException;
import me.hao0.wechat.model.message.receive.RecvMessage;
import me.hao0.wechat.model.message.receive.event.RecvEvent;
import me.hao0.wechat.model.message.receive.event.RecvLocationEvent;
import me.hao0.wechat.model.message.receive.event.RecvMenuEvent;
import me.hao0.wechat.model.message.receive.event.RecvScanEvent;
import me.hao0.wechat.model.message.receive.event.RecvSubscribeEvent;
import me.hao0.wechat.model.message.receive.msg.RecvImageMessage;
import me.hao0.wechat.model.message.receive.msg.RecvLinkMessage;
import me.hao0.wechat.model.message.receive.msg.RecvLocationMessage;
import me.hao0.wechat.model.message.receive.msg.RecvMsg;
import me.hao0.wechat.model.message.receive.msg.RecvShortVideoMessage;
import me.hao0.wechat.model.message.receive.msg.RecvTextMessage;
import me.hao0.wechat.model.message.receive.msg.RecvVideoMessage;
import me.hao0.wechat.model.message.receive.msg.RecvVoiceMessage;
import me.hao0.wechat.model.message.resp.Article;
import push.Demo;
import push.ali.AndroidPushManager;
import util.HttpReqest;

/**
 * Rest 返回對象的json
 *
 */
@RestController
@RequestMapping("/rest")
public class RestTest {
	
    private String getXml(HttpServletRequest httpRequest) {
        ServletInputStream in;
        StringBuilder xmlMsg = new StringBuilder();  
		try {
			in = httpRequest.getInputStream();
		        byte[] b = new byte[4096];  
		        for (int n; (n = in.read(b)) != -1;) {  
		            xmlMsg.append(new String(b, 0, n, "UTF-8"));  
		        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
       
		return xmlMsg.toString();  
    }
	
	@RequestMapping(value = "", method = {RequestMethod.GET,
			RequestMethod.POST})
	public String getWeixin(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {
		Map<String, String[]> m = httpRequest.getParameterMap();
		for (Entry<String, String[]> entry : m.entrySet()) {
			System.out.println("key = " + entry.getKey() + "&value = " + entry.getValue()[0]);
			
		}
		String result = "";
		//更换 地址的验证
		if (m.containsKey("echostr")) {
			result = m.get("echostr")[0];
		}
		//处理微信的推送消息
		else {
			try {
				result = testMessageReceive(getXml(httpRequest));
			} catch (Exception e) {
				if (e instanceof EventException) {
					//模板推送的过滤处理
					System.out.println("未知类型");
				}
				else {
					e.printStackTrace();
				}
			}
		}
		return result;

	}
	
	private String resultXml(RecvMessage message, boolean check) {
		String result = null;
		System.out.println(message.getFromUserName());
		System.out.println(message.getMsgType());
		System.out.println(message.getToUserName());
		System.out.println(message.getCreateTime());
		//对话的时间推送 此处仅为文字 更多扩展见RecvMsg
		if (message instanceof RecvTextMessage) {
			String content = ((RecvTextMessage)message).getContent();
			System.out.println(content);
//			result = wechat.msg().respText(message, content);
			result = WechatHaoHelper.getInstance().replyXmlText(message, content);
		}
		//点击菜单的事件推送
		else if (message instanceof RecvMenuEvent) {
			RecvMenuEvent event = ((RecvMenuEvent)message);
			String type = event.getEventType();
			if ("TEMPLATE".equals(event.getEventKey())){
				WechatHaoHelper.getInstance().replyXmlTemplate(
						message.getFromUserName(),
						"8F4qB2m7Xra5tinmmf3L_gGGubaZKM5-tEx3fzdPR7k", null, "https://www.baidu.com/");
				return "";
			}
			List<Article> articles = Arrays.asList(
					new Article("图文标题1", "图文描述",
							"http://mpic.tiankong.com/c5f/9d6/c5f9d67fbfd7e5deb25e297aa2da00a5/east-A21-559966.jpg@360h",
							"链接"),
					new Article("图文标题2", "图文描述", "图片链接",
							"https://www.baidu.com/"),
					new Article("图文标题3", "图文描述", "图片链接", "链接"),
					new Article("图文标题4", "图文描述", "图片链接", "链接"));
			// result = wechat.msg().respNews(message,articles);
			result = WechatHaoHelper.getInstance().replyXmlArticles(message, articles);
		}
		return result;
	}
	
	    public String testMessageReceive(String xml){
//	        xml = "<xml>\n" +
//	                " <ToUserName><![CDATA[toUser]]></ToUserName>\n" +
//	                " <FromUserName><![CDATA[fromUser]]></FromUserName> \n" +
//	                " <CreateTime>1348831860</CreateTime>\n" +
//	                " <MsgType><![CDATA[text]]></MsgType>\n" +
//	                " <Content><![CDATA[this is a test]]></Content>\n" +
//	                " <MsgId>1234567890123456</MsgId>\n" +
//	                " </xml>";
//	        RecvMessage message = wechat.msg().receive(xml);
	    	RecvMessage message = WechatHaoHelper.getInstance().parseMessageXml(xml);
	        return resultXml(message, message instanceof RecvMsg && message instanceof RecvTextMessage);
	        //	        xml = "<xml>\n" +
//	                " <ToUserName><![CDATA[toUser]]></ToUserName>\n" +
//	                " <FromUserName><![CDATA[fromUser]]></FromUserName>\n" +
//	                " <CreateTime>1348831860</CreateTime>\n" +
//	                " <MsgType><![CDATA[image]]></MsgType>\n" +
//	                " <PicUrl><![CDATA[this is a url]]></PicUrl>\n" +
//	                " <MediaId><![CDATA[media_id]]></MediaId>\n" +
//	                " <MsgId>1234567890123456</MsgId>\n" +
//	                " </xml>";
//	        message = wechat.msg().receive(xml);
//	        assertTrue(message instanceof RecvMsg && message instanceof RecvImageMessage);
//
//	        xml = "<xml>\n" +
//	                "<ToUserName><![CDATA[toUser]]></ToUserName>\n" +
//	                "<FromUserName><![CDATA[fromUser]]></FromUserName>\n" +
//	                "<CreateTime>1357290913</CreateTime>\n" +
//	                "<MsgType><![CDATA[voice]]></MsgType>\n" +
//	                "<MediaId><![CDATA[media_id]]></MediaId>\n" +
//	                "<Format><![CDATA[Format]]></Format>\n" +
//	                "<MsgId>1234567890123456</MsgId>\n" +
//	                "</xml>";
//	        message = wechat.msg().receive(xml);
//	        assertTrue(message instanceof RecvMsg && message instanceof RecvVoiceMessage);
//
//	        xml = "<xml>\n" +
//	                "<ToUserName><![CDATA[toUser]]></ToUserName>\n" +
//	                "<FromUserName><![CDATA[fromUser]]></FromUserName>\n" +
//	                "<CreateTime>1357290913</CreateTime>\n" +
//	                "<MsgType><![CDATA[video]]></MsgType>\n" +
//	                "<MediaId><![CDATA[media_id]]></MediaId>\n" +
//	                "<ThumbMediaId><![CDATA[thumb_media_id]]></ThumbMediaId>\n" +
//	                "<MsgId>1234567890123456</MsgId>\n" +
//	                "</xml>";
//	        message = wechat.msg().receive(xml);
//	        assertTrue(message instanceof RecvMsg && message instanceof RecvVideoMessage);
//
//	        xml = "<xml>\n" +
//	                "<ToUserName><![CDATA[toUser]]></ToUserName>\n" +
//	                "<FromUserName><![CDATA[fromUser]]></FromUserName>\n" +
//	                "<CreateTime>1357290913</CreateTime>\n" +
//	                "<MsgType><![CDATA[shortvideo]]></MsgType>\n" +
//	                "<MediaId><![CDATA[media_id]]></MediaId>\n" +
//	                "<ThumbMediaId><![CDATA[thumb_media_id]]></ThumbMediaId>\n" +
//	                "<MsgId>1234567890123456</MsgId>\n" +
//	                "</xml>";
//	        message = wechat.msg().receive(xml);
//	        assertTrue(message instanceof RecvMsg && message instanceof RecvShortVideoMessage);
//
//	        xml = "<xml>\n" +
//	                "<ToUserName><![CDATA[toUser]]></ToUserName>\n" +
//	                "<FromUserName><![CDATA[fromUser]]></FromUserName>\n" +
//	                "<CreateTime>1351776360</CreateTime>\n" +
//	                "<MsgType><![CDATA[location]]></MsgType>\n" +
//	                "<Location_X>23.134521</Location_X>\n" +
//	                "<Location_Y>113.358803</Location_Y>\n" +
//	                "<Scale>20</Scale>\n" +
//	                "<Label><![CDATA[位置信息]]></Label>\n" +
//	                "<MsgId>1234567890123456</MsgId>\n" +
//	                "</xml> ";
//	        message = wechat.msg().receive(xml);
//	        assertTrue(message instanceof RecvMsg && message instanceof RecvLocationMessage);
//
//	        xml = "<xml>\n" +
//	                "<ToUserName><![CDATA[toUser]]></ToUserName>\n" +
//	                "<FromUserName><![CDATA[fromUser]]></FromUserName>\n" +
//	                "<CreateTime>1351776360</CreateTime>\n" +
//	                "<MsgType><![CDATA[link]]></MsgType>\n" +
//	                "<Title><![CDATA[公众平台官网链接]]></Title>\n" +
//	                "<Description><![CDATA[公众平台官网链接]]></Description>\n" +
//	                "<Url><![CDATA[url]]></Url>\n" +
//	                "<MsgId>1234567890123456</MsgId>\n" +
//	                "</xml>";
//	        message = wechat.msg().receive(xml);
//	        assertTrue(message instanceof RecvMsg && message instanceof RecvLinkMessage);
//
//	        xml = "<xml>\n" +
//	                "<ToUserName><![CDATA[toUser]]></ToUserName>\n" +
//	                "<FromUserName><![CDATA[FromUser]]></FromUserName>\n" +
//	                "<CreateTime>123456789</CreateTime>\n" +
//	                "<MsgType><![CDATA[event]]></MsgType>\n" +
//	                "<Event><![CDATA[subscribe]]></Event>\n" +
//	                "</xml>";
//	        message = wechat.msg().receive(xml);
//	        assertTrue(message instanceof RecvEvent && message instanceof RecvSubscribeEvent);
//
//	        xml = "<xml><ToUserName><![CDATA[toUser]]></ToUserName>\n" +
//	                "<FromUserName><![CDATA[FromUser]]></FromUserName>\n" +
//	                "<CreateTime>123456789</CreateTime>\n" +
//	                "<MsgType><![CDATA[event]]></MsgType>\n" +
//	                "<Event><![CDATA[subscribe]]></Event>\n" +
//	                "<EventKey><![CDATA[qrscene_123123]]></EventKey>\n" +
//	                "<Ticket><![CDATA[TICKET]]></Ticket>\n" +
//	                "</xml>";
//	        message = wechat.msg().receive(xml);
//	        assertTrue(message instanceof RecvEvent && message instanceof RecvSubscribeEvent);
//
//	        xml = "<xml>\n" +
//	                "<ToUserName><![CDATA[toUser]]></ToUserName>\n" +
//	                "<FromUserName><![CDATA[FromUser]]></FromUserName>\n" +
//	                "<CreateTime>123456789</CreateTime>\n" +
//	                "<MsgType><![CDATA[event]]></MsgType>\n" +
//	                "<Event><![CDATA[SCAN]]></Event>\n" +
//	                "<EventKey><![CDATA[SCENE_VALUE]]></EventKey>\n" +
//	                "<Ticket><![CDATA[TICKET]]></Ticket>\n" +
//	                "</xml>";
//	        message = wechat.msg().receive(xml);
//	        assertTrue(message instanceof RecvEvent && message instanceof RecvScanEvent);
//
//	        xml = "<xml>\n" +
//	                "<ToUserName><![CDATA[toUser]]></ToUserName>\n" +
//	                "<FromUserName><![CDATA[fromUser]]></FromUserName>\n" +
//	                "<CreateTime>123456789</CreateTime>\n" +
//	                "<MsgType><![CDATA[event]]></MsgType>\n" +
//	                "<Event><![CDATA[LOCATION]]></Event>\n" +
//	                "<Latitude>23.137466</Latitude>\n" +
//	                "<Longitude>113.352425</Longitude>\n" +
//	                "<Precision>119.385040</Precision>\n" +
//	                "</xml>";
//	        message = wechat.msg().receive(xml);
//	        assertTrue(message instanceof RecvEvent && message instanceof RecvLocationEvent);
//
//	        xml = "<xml>\n" +
//	                "<ToUserName><![CDATA[toUser]]></ToUserName>\n" +
//	                "<FromUserName><![CDATA[FromUser]]></FromUserName>\n" +
//	                "<CreateTime>123456789</CreateTime>\n" +
//	                "<MsgType><![CDATA[event]]></MsgType>\n" +
//	                "<Event><![CDATA[CLICK]]></Event>\n" +
//	                "<EventKey><![CDATA[EVENTKEY]]></EventKey>\n" +
//	                "</xml>";
//	        message = wechat.msg().receive(xml);
//	        assertTrue(message instanceof RecvEvent && message instanceof RecvMenuEvent);
//
//	        xml = "<xml>\n" +
//	                "<ToUserName><![CDATA[toUser]]></ToUserName>\n" +
//	                "<FromUserName><![CDATA[FromUser]]></FromUserName>\n" +
//	                "<CreateTime>123456789</CreateTime>\n" +
//	                "<MsgType><![CDATA[event]]></MsgType>\n" +
//	                "<Event><![CDATA[VIEW]]></Event>\n" +
//	                "<EventKey><![CDATA[www.qq.com]]></EventKey>\n" +
//	                "</xml>";
//	        message = wechat.msg().receive(xml);
//	        assertTrue(message instanceof RecvEvent && message instanceof RecvMenuEvent);
	    }
	
	@RequestMapping("/a/{code}")
	Announce home(@PathVariable("code") String code) {
		Announce a = new Announce();
		Map<String, String> postParams = new HashMap<String, String>();
		postParams.put("appkey", "1a80080bb01d6");
		postParams.put("phone", "18341134983");
		postParams.put("zone", "86");
		postParams.put("code", code);
		String r = HttpReqest.doPost("https://webapi.sms.mob.com/sms/verify", postParams, "UTF-8", false);
		try {
			JSONObject result = new JSONObject(r);
			if (null != result) {
				int status = result.getInt("status");
				a.setContentTaT("status");
				a.setTitleTaT(status + "");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return a;
	}
	
	@RequestMapping("/wexin/test")
	Announce aaa(@PathVariable("code") String code) {
		Announce a = new Announce();
		Map<String, String> postParams = new HashMap<String, String>();
		postParams.put("appkey", "wx39bb940a2cc30c6b");
		postParams.put("phone", "03ff7c0fedfa63c6d54294e08b6333b6");
		postParams.put("zone", "86");
		postParams.put("code", code);
		String r = HttpReqest.doPost("https://webapi.sms.mob.com/sms/verify", postParams, "UTF-8", false);
		try {
			JSONObject result = new JSONObject(r);
			if (null != result) {
				int status = result.getInt("status");
				a.setContentTaT("status");
				a.setTitleTaT(status + "");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return a;
	}
	
	@RequestMapping("/alipush")
	Announce alipush() {
		Announce a = new Announce();
		a.setContentTaT("c");
		a.setTitleTaT("title");
		try {
			new AndroidPushManager().PushMessageToAndroid();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;
	}
	
	@RequestMapping("/aliNotice")
	Announce alinotifition() {
		Announce a = new Announce();
		a.setContentTaT("c");
		a.setTitleTaT("title");
		try {
			new AndroidPushManager().testPushNoticeToAndroid();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;
	}
	
	@RequestMapping("/beta")
	Announce beta() {
		Announce a = new Announce();
		a.setContentTaT("c");
		a.setTitleTaT("title");
		try {
			new Demo().sendAndroidUnicast(false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;
	}
	
    private boolean saveFile(MultipartFile file, HttpServletRequest request) {  
        // 判断文件是否为空  
        if (!file.isEmpty()) {  
            try {  
                // 文件保存路径  
                String filePath = request.getSession().getServletContext().getRealPath("/") + "upload/"  
                        + file.getOriginalFilename();  
                // 转存文件  
                file.transferTo(new File(filePath));  
                return true;  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
        return false;  
    }
	
	@RequestMapping(value = "/fileupload",method = RequestMethod.POST)
	public String iconUpload(HttpServletRequest request){
        
        MultipartHttpServletRequest multipartRequest  =  (MultipartHttpServletRequest) request;  
         
        MultipartFile file1 = multipartRequest.getFile("iconImg");
         
        Map<String,MultipartFile> fileMap  =multipartRequest.getFileMap();
        for (Entry<String, MultipartFile> entry : fileMap.entrySet()) {
        	saveFile(entry.getValue(), multipartRequest);
        	System.out.println(entry.getKey() + ":" + entry.getValue());
			
		}
        for (Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
        	System.out.println(entry.getKey() + ":" + entry.getValue()[0]);
			
		}
		return "success";
		}


	@RequestMapping(value = "/form", method = {RequestMethod.GET,
			RequestMethod.POST})
	public String getLogin(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {
		Map<String, String[]> m = httpRequest.getParameterMap();
		String t = m.get("a")[0];
		return "done";

	}

}
