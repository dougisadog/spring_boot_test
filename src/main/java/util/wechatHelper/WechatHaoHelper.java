package util.wechatHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import entity.MenuItem;
import me.hao0.wechat.core.Callback;
import me.hao0.wechat.core.MenuBuilder;
import me.hao0.wechat.core.Wechat;
import me.hao0.wechat.core.WechatBuilder;
import me.hao0.wechat.model.menu.Menu;
import me.hao0.wechat.model.message.receive.RecvMessage;
import me.hao0.wechat.model.message.receive.event.RecvMenuEvent;
import me.hao0.wechat.model.message.receive.msg.RecvTextMessage;
import me.hao0.wechat.model.message.resp.Article;
import me.hao0.wechat.model.message.send.TemplateField;
import me.hao0.wechat.model.user.User;
import me.hao0.wechat.model.user.UserList;
import util.wechatHelper.aes.AesException;
import util.wechatHelper.aes.WXBizMsgCrypt;

public class WechatHaoHelper {

	private static WechatHaoHelper wechatHaoHelper;
	
	/**
	 * wechahao的组件
	 */
    private Wechat wechat;
    
    /**
     * 加密用组件
     */
    private WXBizMsgCrypt pc;
    
//    private final static String APP_ID = "wx39bb940a2cc30c6b";
//    
//    private final static String APP_SECRET = "03ff7c0fedfa63c6d54294e08b6333b6";
    
    private final static String APP_ID = "wx0ca51c740253dd75";
    
    private final static String APP_SECRET = "9dc0de34b1949e390d043eb266211fc8";
    
    private final static String APP_MSG_KEY = "xvv2igfFxRrdJklnOTCiXootjuy3ZlCYW7YIWMwaJkv";
    
    private final static String APP_TOKEN = "dougisadog";
    
    /**
     * 当前是否开启加密
     */
    private final static boolean NEED_TOKEN = true;
    
    /**
     * appid appsecret可修改为 文件props读取
     */
	private WechatHaoHelper() {
		wechat = WechatBuilder.newBuilder(APP_ID, APP_SECRET).msgKey(APP_MSG_KEY)
                .build();
		if (NEED_TOKEN) {
			try {
				pc = new WXBizMsgCrypt(APP_TOKEN, APP_MSG_KEY, APP_ID);
			} catch (AesException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	};
	
	/**
	 * 构建加密信息
	 * @param orginXml 原始未加密xml
	 * @param timeStamp 时间戳，可以自己生成，也可以用URL参数的timestamp
	 * @param nonce 随机串，可以自己生成，也可以用URL参数的nonce
	 * @return 已加密结果
	 * @throws Exception
	 */
	public String encodeXml(String orginXml, String timestamp, String nonce) throws Exception {
		if (!NEED_TOKEN) return orginXml;
		String mingwen = pc.encryptMsg(orginXml, timestamp, nonce);
		System.out.println("加密后: " + mingwen);
		return mingwen;
	}
	
	/**
	 * 解密微信推送的加密内容
	 * @param orginXml  第三方收到公众号平台发送的消息
	 * @param msgSignature url传递的加密签名key
	 * @param timeStamp 时间戳，可以自己生成，也可以用URL参数的timestamp
	 * @param nonce 随机串，可以自己生成，也可以用URL参数的nonce
	 * @return 解密内容xml
	 * @throws Exception
	 */
	public String decodeXml(String orginXml, String msgSignature, String timestamp, String nonce) throws Exception {
		if (!NEED_TOKEN) return orginXml;

		// 第三方收到公众号平台发送的消息
		String mingwen = pc.decryptMsg(msgSignature, timestamp, nonce, orginXml);
		System.out.println("解密后明文: " + mingwen);
		return mingwen;
	}

	public static WechatHaoHelper getInstance() {
		if (null == wechatHaoHelper)
			wechatHaoHelper = new WechatHaoHelper();
		return wechatHaoHelper;
	}
	
	/**
	 * 处理文字消息
	 * @param xml
	 * @return
	 */
	public RecvMessage parseMessageXml(String xml) {
		RecvMessage message = wechat.msg().receive(xml);
		return message;
	}
	
	/**
	 * 返回模板消息
	 * @param openId 接收方openid
	 * @param templateId 模板id
	 * @param fields 模板参数
	 * @return
	 */
	public String replyXmlTemplate(String openId, String templateId, List<TemplateField> fields) {
		Long msgId = wechat.msg().sendTemplate(openId, templateId, fields);
		return msgId + "";
	}
	
	/**
	 * 返回模板消息
	 * @param openId 接收方openid
	 * @param templateId 模板id
	 * @param fields 模板参数
	 * @param link 模板跳转地址
	 * @return
	 */
	public String replyXmlTemplate(String openId, String templateId, List<TemplateField> fields, String link) {
		Long msgId = wechat.msg().sendTemplate(openId, templateId, fields, link);
		return msgId + "";
	}
	
	/**
	 * 返回用户详情(其中包含UnionId)
	 * @param openId
	 * @return
	 */
	public User getUserDetail(String openId) {
		 return wechat.user().getUser(openId);
	}
	
	/**
	 * 返回关注的用户列表（只包含openid）
	 * @return
	 */
	public UserList getUsers() {
		 return wechat.user().getUsers(null);
	}
	
	 
	
	/**
	 * 回复文字
	 * @param message 请求方信息
	 * @param content 回复内容
	 * @return
	 */
	public String replyXmlText(RecvMessage message, String content) {
		String result = null;
		//对话的时间推送 此处仅为文字 更多扩展见RecvMsg
		if (message instanceof RecvTextMessage) {
			content = ((RecvTextMessage)message).getContent();
//			System.out.println(content);
			result = wechat.msg().respText(message, content);
		}
		return result;
	}
	
	/**
	 * 回复图文
	 * @param message 请求方信息
	 * @param articles 图文内容
	 * @return
	 */
	public String replyXmlArticles(RecvMessage message, List<Article> articles) {
		String result = null;
		//对话的时间推送 此处仅为文字 更多扩展见RecvMsg
		if (message instanceof RecvMenuEvent) {
			//TODO DELETE
			if (null == articles || articles.size() == 0) {
				articles = Arrays.asList(
                        new Article("图文标题1", "图文描述", "http://mpic.tiankong.com/c5f/9d6/c5f9d67fbfd7e5deb25e297aa2da00a5/east-A21-559966.jpg@360h", "链接"),
                        new Article("图文标题2", "图文描述", "图片链接", "https://www.baidu.com/"),
                        new Article("图文标题3", "图文描述", "图片链接", "链接"),
                        new Article("图文标题4", "图文描述", "图片链接", "链接")
                );
			}
			result = wechat.msg().respNews(message,articles);
		}
		return result;
	}
	
	/**
	 * 创建菜单
	 * @param menuItems
	 * @param cb
	 */
	public void createMenu(List<MenuItem> menuItems, Callback<Boolean> cb) {
		String jsonMenu = getBuildMenu(menuItems);
		if (null == cb) {
			wechat.menu().create(jsonMenu);
		}
		else {
			wechat.menu().create(jsonMenu,cb);
		}
	}
	
	/**
	 * 删除菜单
	 * @param menuItems
	 * @param cb
	 */
	public void deleteMenu(Callback<Boolean> cb) {
		if (null == cb) {
			wechat.menu().delete();
		}
		else {
			wechat.menu().delete(cb);
		}
	}
	/**
	 * check根项按钮
	 * @param menuItems
	 * @return true 是  false 否
	 */
	private boolean checkMenuItems(List<MenuItem> menuItems) {
		return null == menuItems || menuItems.size() == 0;
	}
	
	/**
	 * 构建菜单json
	 * @param menuItems
	 * @return
	 */
	private String getBuildMenu(List<MenuItem> menuItems) {
		if (checkMenuItems(menuItems)) {
			//return null;
			
			//TODO DELETE DEMO
			menuItems = new ArrayList<MenuItem>();
			MenuItem m1 = new MenuItem("我要下单", "http://www.baidu.com", MenuItem.VIEW);
			MenuItem m2 = new MenuItem("优惠活动", "ACTIVITIES", MenuItem.CLICK);
			MenuItem m3 = new MenuItem("更多", "", MenuItem.PARENT);
			m3.setMenuItems(Arrays.asList(
					new MenuItem("关于我们", "ACT", MenuItem.CLICK),
					new MenuItem("模板推送", "TEMPLATE", MenuItem.CLICK),
					new MenuItem("我的订单", "http://www.baidu.com", MenuItem.VIEW)));
			menuItems.add(m1);
			menuItems.add(m2);
			menuItems.add(m3);
			
		}
		MenuBuilder menuBuilder = MenuBuilder.newBuilder();
		for (MenuItem menuItem : menuItems) {
			if (checkMenuItems(menuItem.getMenuItems()) && menuItem.getType() != MenuItem.PARENT) {
				if (menuItem.getType() == MenuItem.VIEW) {
					menuBuilder.view(menuItem.getName(), menuItem.getKey());
				}
				else if (menuItem.getType() == MenuItem.CLICK) {
					menuBuilder.click(menuItem.getName(), menuItem.getKey());
				}
			}
			else {
				Menu more = menuBuilder.newParentMenu(menuItem.getName());
				 for (MenuItem childMenuItem : menuItem.getMenuItems()) {
					 if (childMenuItem.getType() == MenuItem.CLICK) {
						 menuBuilder.click(more, childMenuItem.getName(), childMenuItem.getKey());
					 }
					 else if (childMenuItem.getType() == MenuItem.VIEW) {
						 menuBuilder.view(more, childMenuItem.getName(), childMenuItem.getKey());
					 }
				}
				 menuBuilder.menu(more);
			}
		}
        String jsonMenu = menuBuilder.build();
        return jsonMenu;
	}

}
