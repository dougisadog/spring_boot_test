package boot_test;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import me.hao0.wechat.core.Callback;
import me.hao0.wechat.core.MenuBuilder;
import me.hao0.wechat.core.Wechat;
import me.hao0.wechat.core.WechatBuilder;
import me.hao0.wechat.model.menu.Menu;

/**
 * Rest 返回對象的json
 *
 */
@RestController
@RequestMapping("/weiixinback")
public class RestWeixinBack {
	
    private Wechat wechat;

    public void init() {
    	if (null == wechat)
        wechat = WechatBuilder.newBuilder("wx39bb940a2cc30c6b", "03ff7c0fedfa63c6d54294e08b6333b6")
                .build();
    }
	
	@RequestMapping(value = "/menu", method = {RequestMethod.GET,
			RequestMethod.POST})
	public String createWeixinMenu(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {
		init();
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
		WechatHaoHelper.getInstance().createMenu(null, null);
		return "success";

	}
	
	@RequestMapping(value = "/menu/delete", method = {RequestMethod.GET,
			RequestMethod.POST})
	public String deleteWeixinMenu(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {
		init();
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
