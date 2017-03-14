package boot_test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import me.hao0.wechat.core.Callback;
import me.hao0.wechat.core.MenuBuilder;
import me.hao0.wechat.model.menu.Menu;
import me.hao0.wechat.model.message.send.TemplateField;
import util.wechatHelper.WechatHaoHelper;

/**
 * Rest 返回對象的json
 *
 */
@RestController
@RequestMapping("/weiixinback")
public class RestWeixinBack {
	
	
	@RequestMapping(value = "", method = {RequestMethod.GET,
			RequestMethod.POST})
	public String getAccess(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {
		Map<String, String[]> m = httpRequest.getParameterMap();
		for (Entry<String, String[]> entry : m.entrySet()) {
			System.out.println("key = " + entry.getKey() + "&value = " + entry.getValue()[0]);
			
		}
		return "success";

	}
	
	@RequestMapping(value = "/sendtemplate", method = {RequestMethod.GET,
			RequestMethod.POST})
	public String sendtemplate(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {
		List<TemplateField> fields = Arrays.asList(
                new TemplateField("params1", "下单成功"),
                new TemplateField("params2", "感谢您的使用。"),
                new TemplateField("keyword1", "TD113123123"),
                new TemplateField("keyword2", "2015-11-11 11:11:11"),
                new TemplateField("keyword3", "123456")
            );
		WechatHaoHelper.getInstance().replyXmlTemplate("oMvjavwH3x2wbkq5xdfw3lQsksCU", "8F4qB2m7Xra5tinmmf3L_gGGubaZKM5-tEx3fzdPR7k", fields, "https://www.baidu.com/");
		
		Map<String, String[]> m = httpRequest.getParameterMap();
		for (Entry<String, String[]> entry : m.entrySet()) {
			System.out.println("key = " + entry.getKey() + "&value = " + entry.getValue()[0]);
			
		}
		return "success";

	}
    
	@RequestMapping(value = "/menu", method = {RequestMethod.GET,
			RequestMethod.POST})
	public String createWeixinMenu(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {
		Map<String, String[]> m = httpRequest.getParameterMap();
		for (Entry<String, String[]> entry : m.entrySet()) {
			System.out.println("key = " + entry.getKey() + "&value = " + entry.getValue()[0]);
			
		}
//		wechat.menu().create(getBuildMenu(), new Callback<Boolean>() {
//			
//			@Override
//			public void onSuccess(Boolean t) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onFailure(Exception e) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		WechatHaoHelper.getInstance().createMenu(null, new Callback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean t) {
				System.out.println("create success");
				
			}
			
			@Override
			public void onFailure(Exception e) {
				System.out.println("create fail");
				
			}
		});
		return "success";

	}
	
	@RequestMapping(value = "/menu/delete", method = {RequestMethod.GET,
			RequestMethod.POST})
	public String deleteWeixinMenu(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {
		Map<String, String[]> m = httpRequest.getParameterMap();
		for (Entry<String, String[]> entry : m.entrySet()) {
			System.out.println("key = " + entry.getKey() + "&value = " + entry.getValue()[0]);
			
		}
		WechatHaoHelper.getInstance().deleteMenu(null);
//		wechat.menu().delete(new Callback<Boolean>() {
//			
//			@Override
//			public void onSuccess(Boolean t) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onFailure(Exception e) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		return "success";

	}
	
	private String getBuildMenu() {
	        MenuBuilder menuBuilder = MenuBuilder.newBuilder();

	        menuBuilder.view("我要下单", "http://www.baidu.com");

	        menuBuilder.click("优惠活动", "ACTIVITIES");

	        Menu more = menuBuilder.newParentMenu("更多");
	        menuBuilder.click(more, "关于我们", "http://www.baidu.com");
	        menuBuilder.view(more, "我的订单", "http://www.baidu.com");

	        menuBuilder.menu(more);

	        String jsonMenu = menuBuilder.build();
	        return jsonMenu;
//	        assertEquals("{\"button\":[{\"name\":\"我要下单\",\"type\":\"view\",\"url\":\"http://www.hao0.me\"},{\"name\":\"优惠活动\",\"type\":\"click\",\"key\":\"ACTIVITIES\"},{\"name\":\"更多\",\"sub_button\":[{\"name\":\"关于我们\",\"type\":\"click\",\"key\":\"http://www.hao0.me\"},{\"name\":\"我的订单\",\"type\":\"view\",\"url\":\"http://www.hao0.me\"}]}]}", jsonMenu);
	}
	
}
