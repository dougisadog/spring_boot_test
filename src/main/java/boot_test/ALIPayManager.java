package boot_test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayOpenPublicTemplateMessageIndustryModifyRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayOpenPublicTemplateMessageIndustryModifyResponse;

public class ALIPayManager {

	private static ALIPayManager aLIPayManager;

	private ALIPayManager() {
	};

	public static ALIPayManager getInstance() {
		if (null == aLIPayManager)
			aLIPayManager = new ALIPayManager();
		return aLIPayManager;
	}

	public void init() {
		String APP_ID = ConstantConfig.PARTNER;
		String APP_PRIVATE_KEY = ConstantConfig.RSA_PRIVATE;
		String ALIPAY_PUBLIC_KEY = ConstantConfig.RSA_PUBLIC;
		// 实例化客户端
		AlipayClient client = new DefaultAlipayClient(
				"https://openapi.alipay.com/gateway.do", APP_ID,
				APP_PRIVATE_KEY, "json", "GBK", ALIPAY_PUBLIC_KEY);
		// 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.open.public.template.message.industry.modify
		AlipayOpenPublicTemplateMessageIndustryModifyRequest request = new AlipayOpenPublicTemplateMessageIndustryModifyRequest();
		// SDK已经封装掉了公共参数，这里只需要传入业务参数
		// 此次只是参数展示，未进行字符串转义，实际情况下请转义

		// 参数参见
		// https://doc.open.alipay.com/doc2/detail.htm?treeId=203&articleId=105463&docType=1
		request.setBizContent(
				" {'primary_industry_name':'IT科技/IT软件与服务', 'primary_industry_code':'10001/20102','secondary_industry_code':'10001/20102', 'secondary_industry_name':'IT科技/IT软件与服务'}");
		AlipayOpenPublicTemplateMessageIndustryModifyResponse response = null;
		try {
			response = client.execute(request);
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 调用成功，则处理业务逻辑
		if (response.isSuccess()) {
			String result = response.getBody();

		}

	}

	public String doPost()
			throws ServletException, IOException {
		String APP_ID = ConstantConfig.PARTNER;
		String APP_PRIVATE_KEY = ConstantConfig.RSA_PRIVATE;
		String ALIPAY_PUBLIC_KEY = ConstantConfig.RSA_PUBLIC;
		// 实例化客户端
		AlipayClient alipayClient = new DefaultAlipayClient(
				"https://openapi.alipay.com/gateway.do", APP_ID,
				APP_PRIVATE_KEY, "json", "GBK", ALIPAY_PUBLIC_KEY);

		AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();// 创建API对应的request
		alipayRequest.setReturnUrl("http://nangua.webok.net:9973/");
		alipayRequest.setNotifyUrl("http://nangua.webok.net:9973/form");// 在公共参数中设置回跳和通知地址
		alipayRequest.setBizContent(
				"{" + "    \"out_trade_no\":\"20150320010101002\","
						+ "    \"total_amount\":0.01,"
						+ "    \"subject\":\"Iphone6 16G\","
						+ "    \"seller_id\":\"2088123456789012\","
						+ "    \"product_code\":\"QUICK_WAP_PAY\"" + "  }");// 填充业务参数
		String form = "";
		try {
			form = alipayClient.pageExecute(alipayRequest).getBody();
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 调用SDK生成表单
		return form;
	}
}
