package boot_test;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import acp.sdk.AcpService;
import acp.sdk.LogUtil;
import acp.sdk.SDKConfig;


/**
 * Hello world!
 *
 */
@RestController
@RequestMapping("/ACPSample_B2C")
public class UnionPayController
{
	
	//TODO preauth
	@RequestMapping( value = "/form_6_7_1_AuthDeal_Front", method = { RequestMethod.GET, RequestMethod.POST } )
	public String authDeal_Front(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse)
	{
		//前台页面传过来的
		String merId = httpRequest.getParameter("merId");
		String txnAmt = httpRequest.getParameter("txnAmt");
		
		Map<String, String> requestData = new HashMap<String, String>();
		
		/***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
		requestData.put("version", UnionBase.version);   			  //版本号，全渠道默认值
		requestData.put("encoding", UnionBase.encoding_UTF8); 			  //字符集编码，可以使用UTF-8,GBK两种方式
		requestData.put("signMethod", "01");            			  //签名方法，只支持 01：RSA方式证书加密
		requestData.put("txnType", "02");               			  //交易类型 ，02：预授权
		requestData.put("txnSubType", "01");            			  //交易子类型， 01：预授权
		requestData.put("bizType", "000201");           			  //业务类型，B2C网关支付，手机wap支付
		requestData.put("channelType", "07");           			  //渠道类型，这个字段区分B2C网关支付和手机wap支付；07：PC,平板  08：手机
		
		/***商户接入参数***/
		requestData.put("merId", merId);    	          			  //商户号码，请改成自己申请的正式商户号或者open上注册得来的777测试商户号
		requestData.put("accessType", "0");             			  //接入类型，0：直连商户 
		requestData.put("orderId",UnionBase.getOrderId());             //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则		
		requestData.put("txnTime", UnionBase.getCurrentTime());        //订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
		requestData.put("currencyCode", "156");         			  //交易币种（境内商户一般是156 人民币）		
		requestData.put("txnAmt", txnAmt);             			      //交易金额，单位分，不要带小数点
		//requestData.put("reqReserved", "透传字段");        		      //请求方保留域，如需使用请启用即可；透传字段（可以实现商户自定义参数的追踪）本交易的后台通知,对本交易的交易状态查询交易、对账文件中均会原样返回，商户可以按需上传，长度为1-1024个字节		
		
		//前台通知地址 （需设置为外网能访问 http https均可），支付成功后的页面 点击“返回商户”按钮的时候将异步通知报文post到该地址
		//如果想要实现过几秒中自动跳转回商户页面权限，需联系银联业务（发邮件到operation@unionpay.com）申请开通自动返回商户权限
		//异步通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
		requestData.put("frontUrl", UnionBase.frontUrl);
		
		//后台通知地址（需设置为【外网】能访问 http https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，失败的交易银联不会发送后台通知
		//后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
		//注意:1.需设置为外网能访问，否则收不到通知    2.http https均可  3.收单后台通知后需要10秒内返回http200或302状态码 
		//    4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200，那么银联会间隔一段时间再次发送。总共发送5次，每次的间隔时间为0,1,2,4分钟。
		//    5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败
		requestData.put("backUrl", UnionBase.backUrl);
		return parseRequestForUnionPayHttp(requestData);
		
	}
	
	@RequestMapping( value = "/form_6_7_2_AuthUndo", method = { RequestMethod.GET, RequestMethod.POST } )
	public String authUndo(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse)
	{
		String origQryId = httpRequest.getParameter("origQryId");
		String txnAmt = httpRequest.getParameter("txnAmt");
		
		Map<String, String> data = new HashMap<String, String>();
		
		/***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
		data.put("version", UnionBase.version);            //版本号
		data.put("encoding", UnionBase.encoding_UTF8);          //字符集编码 可以使用UTF-8,GBK两种方式
		data.put("signMethod", "01");                     //签名方法 目前只支持01-RSA方式证书加密
		data.put("txnType", "32");                        //交易类型 31-预授权撤销
		data.put("txnSubType", "00");                     //交易子类型  默认00
		data.put("bizType", "000201");                    //业务类型 B2C网关支付，手机wap支付
		data.put("channelType", "07");                    //渠道类型，07-PC，08-手机
		
		/***商户接入参数***/
		data.put("merId", "777290058110048");             //商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
		data.put("accessType", "0");                      //接入类型，商户接入固定填0，不需修改	
		data.put("orderId", UnionBase.getOrderId());       //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则，重新产生，不同于原消费		
		data.put("txnTime", UnionBase.getCurrentTime());   //订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
		data.put("txnAmt", txnAmt);                       //【撤销金额】，撤销时必须和原消费金额相同	
		data.put("currencyCode", "156");                  //交易币种(境内商户一般是156 人民币)
		//data.put("reqReserved", "透传信息");                 //请求方保留域，如需使用请启用即可；透传字段（可以实现商户自定义参数的追踪）本交易的后台通知,对本交易的交易状态查询交易、对账文件中均会原样返回，商户可以按需上传，长度为1-1024个字节		
		data.put("backUrl", UnionBase.backUrl);            //后台通知地址，后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费撤销交易 商户通知,其他说明同消费交易的商户通知
		
		/***要调通交易以下字段必须修改***/
		data.put("origQryId", origQryId);   			  //【原始交易流水号】，原消费交易返回的的queryId，可以从消费交易后台通知接口中或者交易状态查询接口中获取
		
		return parseRequestForUnionPayHttp(data);
	}
	
	@RequestMapping( value = "/form_6_7_3_AuthFinish", method = { RequestMethod.GET, RequestMethod.POST } )
	public String authFinish(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse)
	{
		String origQryId = httpRequest.getParameter("origQryId");
		String txnAmt = httpRequest.getParameter("txnAmt");
		
		Map<String, String> data = new HashMap<String, String>();
		
		/***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
		data.put("version", UnionBase.version);            //版本号
		data.put("encoding", UnionBase.encoding_UTF8);          //字符集编码 可以使用UTF-8,GBK两种方式
		data.put("signMethod", "01");                     //签名方法 目前只支持01-RSA方式证书加密
		data.put("txnType", "03");                        //交易类型 03-预授权完成
		data.put("txnSubType", "00");                     //交易子类型  默认00
		data.put("bizType", "000201");                    //业务类型 B2C网关支付，手机wap支付
		data.put("channelType", "07");                    //渠道类型，07-PC，08-手机
		
		/***商户接入参数***/
		data.put("merId", "777290058110048");             //商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
		data.put("accessType", "0");                      //接入类型，商户接入固定填0，不需修改	
		data.put("orderId", UnionBase.getOrderId());       //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则，重新产生，不同于原消费		
		data.put("txnTime", UnionBase.getCurrentTime());   //订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
		data.put("txnAmt", txnAmt);                       //【完成金额】，金额范围为预授权金额的0-115%	
		data.put("currencyCode", "156");                  //交易币种(境内商户一般是156 人民币)
		//data.put("reqReserved", "透传信息");                 //请求方保留域，如需使用请启用即可；透传字段（可以实现商户自定义参数的追踪）本交易的后台通知,对本交易的交易状态查询交易、对账文件中均会原样返回，商户可以按需上传，长度为1-1024个字节		
		data.put("backUrl", UnionBase.backUrl);            //后台通知地址，后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费撤销交易 商户通知,其他说明同消费交易的商户通知
		
		
		/***要调通交易以下字段必须修改***/
		data.put("origQryId", origQryId);   			  //【原始交易流水号】，原消费交易返回的的queryId，可以从消费交易后台通知接口中或者交易状态查询接口中获取
		
		return parseRequestForUnionPayHttp(data);
	}
	
	@RequestMapping( value = "/form_6_7_4_AutnFinishUndo", method = { RequestMethod.GET, RequestMethod.POST } )
	public String autnFinishUndo(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse)
	{
		String origQryId = httpRequest.getParameter("origQryId");
		String txnAmt = httpRequest.getParameter("txnAmt");
		
		Map<String, String> data = new HashMap<String, String>();
		
		/***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
		data.put("version", UnionBase.version);            //版本号
		data.put("encoding", UnionBase.encoding_UTF8);          //字符集编码 可以使用UTF-8,GBK两种方式
		data.put("signMethod", "01");                     //签名方法 目前只支持01-RSA方式证书加密
		data.put("txnType", "33");                        //交易类型 03-预授权完成
		data.put("txnSubType", "00");                     //交易子类型  默认00
		data.put("bizType", "000201");                    //业务类型 B2C网关支付，手机wap支付
		data.put("channelType", "07");                    //渠道类型，07-PC，08-手机
		
		/***商户接入参数***/
		data.put("merId", "777290058110048");             //商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
		data.put("accessType", "0");                      //接入类型，商户接入固定填0，不需修改	
		data.put("orderId", UnionBase.getOrderId());       //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则，重新产生，不同于原消费		
		data.put("txnTime", UnionBase.getCurrentTime());   //订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
		data.put("txnAmt", txnAmt);                       //【完成金额】，金额范围为预授权金额的0-115%	
		data.put("currencyCode", "156");                  //交易币种(境内商户一般是156 人民币)
		//data.put("reqReserved", "透传信息");                 //如需使用请启用即可；请求方保留域，透传字段（可以实现商户自定义参数的追踪）本交易的后台通知,对本交易的交易状态查询交易、对账文件中均会原样返回，商户可以按需上传，长度为1-1024个字节		
		data.put("backUrl", UnionBase.backUrl);            //后台通知地址，后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费撤销交易 商户通知,其他说明同消费交易的商户通知
		
		
		/***要调通交易以下字段必须修改***/
		data.put("origQryId", origQryId);   			  //【原始交易流水号】，原消费交易返回的的queryId，可以从消费交易后台通知接口中或者交易状态查询接口中获取
		
		return parseRequestForUnionPayHttp(data);
	}
	
	
	//TODO consume
	@RequestMapping(value = "/form_6_2_FrontConsume", method = { RequestMethod.GET, RequestMethod.POST } )
	public String frontConsume(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse)
	{
		//前台页面传过来的
				String merId = httpRequest.getParameter("merId");
				String txnAmt = httpRequest.getParameter("txnAmt");
				
				Map<String, String> requestData = new HashMap<String, String>();
				
				/***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
				requestData.put("version", UnionBase.version);   			  //版本号，全渠道默认值
				requestData.put("encoding", UnionBase.encoding_UTF8); 			  //字符集编码，可以使用UTF-8,GBK两种方式
				requestData.put("signMethod", "01");            			  //签名方法，只支持 01：RSA方式证书加密
				requestData.put("txnType", "01");               			  //交易类型 ，01：消费
				requestData.put("txnSubType", "01");            			  //交易子类型， 01：自助消费
				requestData.put("bizType", "000201");           			  //业务类型，B2C网关支付，手机wap支付
				requestData.put("channelType", "07");           			  //渠道类型，这个字段区分B2C网关支付和手机wap支付；07：PC,平板  08：手机
				
				/***商户接入参数***/
				requestData.put("merId", merId);    	          			  //商户号码，请改成自己申请的正式商户号或者open上注册得来的777测试商户号
				requestData.put("accessType", "0");             			  //接入类型，0：直连商户 
				requestData.put("orderId",UnionBase.getOrderId());             //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则		
				requestData.put("txnTime", UnionBase.getCurrentTime());        //订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
				requestData.put("currencyCode", "156");         			  //交易币种（境内商户一般是156 人民币）		
				requestData.put("txnAmt", txnAmt);             			      //交易金额，单位分，不要带小数点
				//requestData.put("reqReserved", "透传字段");        		      //请求方保留域，如需使用请启用即可；透传字段（可以实现商户自定义参数的追踪）本交易的后台通知,对本交易的交易状态查询交易、对账文件中均会原样返回，商户可以按需上传，长度为1-1024个字节		
				
				//前台通知地址 （需设置为外网能访问 http https均可），支付成功后的页面 点击“返回商户”按钮的时候将异步通知报文post到该地址
				//如果想要实现过几秒中自动跳转回商户页面权限，需联系银联业务申请开通自动返回商户权限
				//异步通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
				requestData.put("frontUrl", UnionBase.frontUrl);
				
				//后台通知地址（需设置为【外网】能访问 http https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，失败的交易银联不会发送后台通知
				//后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
				//注意:1.需设置为外网能访问，否则收不到通知    2.http https均可  3.收单后台通知后需要10秒内返回http200或302状态码 
				//    4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200，那么银联会间隔一段时间再次发送。总共发送5次，每次的间隔时间为0,1,2,4分钟。
				//    5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败
				requestData.put("backUrl", UnionBase.backUrl);
				
				//////////////////////////////////////////////////
				//
				//       报文中特殊用法请查看 PCwap网关跳转支付特殊用法.txt
				//
				//////////////////////////////////////////////////
				
				/**请求参数设置完毕，以下对请求参数进行签名并生成html表单，将表单写入浏览器跳转打开银联页面**/
				Map<String, String> submitFromData = AcpService.sign(requestData,UnionBase.encoding_UTF8);  //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
				
				String requestFrontUrl = SDKConfig.getConfig().getFrontRequestUrl();  //获取请求银联的前台地址：对应属性文件acp_sdk.properties文件中的acpsdk.frontTransUrl
				String html = AcpService.createAutoFormHtml(requestFrontUrl, submitFromData,UnionBase.encoding_UTF8);   //生成自动跳转的Html表单
				
				LogUtil.writeLog("打印请求HTML，此为请求报文，为联调排查问题的依据："+html);
				//将生成的html写到浏览器中完成自动跳转打开银联支付页面；这里调用signData之后，将html写到浏览器跳转到银联页面之前均不能对html中的表单项的名称和值进行修改，如果修改会导致验签不通过
//				resp.getWriter().write(html);
				return html;
	}
	
	@RequestMapping( value = "/form_6_3_ConsumeUndo", method = { RequestMethod.GET, RequestMethod.POST } )
	public String consumeUndo(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse)
	{
		String origQryId = httpRequest.getParameter("origQryId");
		String txnAmt = httpRequest.getParameter("txnAmt");
		
		Map<String, String> data = new HashMap<String, String>();
		
		/***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
		data.put("version", UnionBase.version);            //版本号
		data.put("encoding", UnionBase.encoding_UTF8);          //字符集编码 可以使用UTF-8,GBK两种方式
		data.put("signMethod", "01");                     //签名方法 目前只支持01-RSA方式证书加密
		data.put("txnType", "31");                        //交易类型 31-消费撤销
		data.put("txnSubType", "00");                     //交易子类型  默认00
		data.put("bizType", "000201");                    //业务类型 B2C网关支付，手机wap支付
		data.put("channelType", "07");                    //渠道类型，07-PC，08-手机
		
		/***商户接入参数***/
		data.put("merId", "777290058110048");             //商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
		data.put("accessType", "0");                      //接入类型，商户接入固定填0，不需修改	
		data.put("orderId", UnionBase.getOrderId());       //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则，重新产生，不同于原消费		
		data.put("txnTime", UnionBase.getCurrentTime());   //订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
		data.put("txnAmt", txnAmt);                       //【撤销金额】，消费撤销时必须和原消费金额相同	
		data.put("currencyCode", "156");                  //交易币种(境内商户一般是156 人民币)
		//data.put("reqReserved", "透传信息");                 //请求方保留域，，如需使用请启用即可；透传字段（可以实现商户自定义参数的追踪）本交易的后台通知,对本交易的交易状态查询交易、对账文件中均会原样返回，商户可以按需上传，长度为1-1024个字节		
		data.put("backUrl", UnionBase.backUrl);            //后台通知地址，后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费撤销交易 商户通知,其他说明同消费交易的商户通知
		
		/***要调通交易以下字段必须修改***/
		data.put("origQryId", origQryId);   			  //【原始交易流水号】，原消费交易返回的的queryId，可以从消费交易后台通知接口中或者交易状态查询接口中获取
		return parseRequestForUnionPayHttp(data);
	}
	
	//TODO function
	@RequestMapping( value = "/form_6_4_Refund", method = { RequestMethod.GET, RequestMethod.POST } )
	public String refund(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse)
	{
		String origQryId = httpRequest.getParameter("origQryId");
		String txnAmt = httpRequest.getParameter("txnAmt");
		Map<String, String> data = new HashMap<String, String>();
		
		/***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
		data.put("version", UnionBase.version);               //版本号
		data.put("encoding", UnionBase.encoding_UTF8);             //字符集编码 可以使用UTF-8,GBK两种方式
		data.put("signMethod", "01");                        //签名方法 目前只支持01-RSA方式证书加密
		data.put("txnType", "04");                           //交易类型 04-退货		
		data.put("txnSubType", "00");                        //交易子类型  默认00		
		data.put("bizType", "000201");                       //业务类型 B2C网关支付，手机wap支付	
		data.put("channelType", "07");                       //渠道类型，07-PC，08-手机		
		
		/***商户接入参数***/
		data.put("merId", "777290058110048");                //商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
		data.put("accessType", "0");                         //接入类型，商户接入固定填0，不需修改		
		data.put("orderId", UnionBase.getOrderId());          //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则，重新产生，不同于原消费		
		data.put("txnTime", UnionBase.getCurrentTime());      //订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效		
		data.put("currencyCode", "156");                     //交易币种（境内商户一般是156 人民币）		
		data.put("txnAmt", txnAmt);                          //****退货金额，单位分，不要带小数点。退货金额小于等于原消费金额，当小于的时候可以多次退货至退货累计金额等于原消费金额		
		//data.put("reqReserved", "透传信息");                  //请求方保留域，如需使用请启用即可；透传字段（可以实现商户自定义参数的追踪）本交易的后台通知,对本交易的交易状态查询交易、对账文件中均会原样返回，商户可以按需上传，长度为1-1024个字节		
		data.put("backUrl", UnionBase.backUrl);               //后台通知地址，后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 退货交易 商户通知,其他说明同消费交易的后台通知
		
		/***要调通交易以下字段必须修改***/
		data.put("origQryId", origQryId);      //****原消费交易返回的的queryId，可以从消费交易后台通知接口中或者交易状态查询接口中获取
		return parseRequestForUnionPayHttp(data);
	}
	
	//TODO function
		@RequestMapping( value = "/form_6_5_Query", method = { RequestMethod.GET, RequestMethod.POST } )
		public String query(HttpServletRequest httpRequest,
				HttpServletResponse httpResponse)
		{
			String orderId = httpRequest.getParameter("orderId");
			String txnTime = httpRequest.getParameter("txnTime");
			
			Map<String, String> data = new HashMap<String, String>();
			
			/***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
			data.put("version", UnionBase.version);                 //版本号
			data.put("encoding", UnionBase.encoding_UTF8);               //字符集编码 可以使用UTF-8,GBK两种方式
			data.put("signMethod", "01");                          //签名方法 目前只支持01-RSA方式证书加密
			data.put("txnType", "00");                             //交易类型 00-默认
			data.put("txnSubType", "00");                          //交易子类型  默认00
			data.put("bizType", "000201");                         //业务类型 B2C网关支付，手机wap支付
			
			/***商户接入参数***/
			data.put("merId", "777290058110048");                  //商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
			data.put("accessType", "0");                           //接入类型，商户接入固定填0，不需修改
			
			/***要调通交易以下字段必须修改***/
			data.put("orderId", orderId);                 //****商户订单号，每次发交易测试需修改为被查询的交易的订单号
			data.put("txnTime", txnTime);                 //****订单发送时间，每次发交易测试需修改为被查询的交易的订单发送时间

			return parseRequestForUnionPayHttp(data);
		}
		
		//TODO function
		@RequestMapping( value = "/form_6_6_FileTransfer", method = { RequestMethod.GET, RequestMethod.POST } )
		public String fileTransfer(HttpServletRequest httpRequest,
				HttpServletResponse httpResponse)
		{
			String merId = httpRequest.getParameter("merId");
			String settleDate = httpRequest.getParameter("settleDate");
			
			Map<String, String> data = new HashMap<String, String>();
			
			/***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
			data.put("version", UnionBase.version);               //版本号 全渠道默认值
			data.put("encoding", UnionBase.encoding_UTF8);             //字符集编码 可以使用UTF-8,GBK两种方式
			data.put("signMethod", "01");                        //签名方法 目前只支持01-RSA方式证书加密
			data.put("txnType", "76");                           //交易类型 76-对账文件下载
			data.put("txnSubType", "01");                        //交易子类型 01-对账文件下载
			data.put("bizType", "000000");                       //业务类型，固定
			
			/***商户接入参数***/
			data.put("accessType", "0");                         //接入类型，商户接入填0，不需修改
			data.put("merId", merId);                	         //商户代码，请替换正式商户号测试，如使用的是自助化平台注册的777开头的商户号，该商户号没有权限测文件下载接口的，请使用测试参数里写的文件下载的商户号和日期测。如需777商户号的真实交易的对账文件，请使用自助化平台下载文件。
			data.put("settleDate", settleDate);                  //清算日期，如果使用正式商户号测试则要修改成自己想要获取对账文件的日期， 测试环境如果使用700000000000001商户号则固定填写0119
			data.put("txnTime",UnionBase.getCurrentTime());       //订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
			data.put("fileType", "00");    
			//文件类型，一般商户填写00即可
			return parseRequestForUnionPayHttp(data);
		}
		
	
	private String parseRequestForUnionPayHttp(Map<String, String> data) {
		
		/**请求参数设置完毕，以下对请求参数进行签名并发送http post请求，接收同步应答报文**/
		
		Map<String, String> reqData  = AcpService.sign(data,UnionBase.encoding_UTF8);//报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
		String url = SDKConfig.getConfig().getBackRequestUrl();//交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.backTransUrl
		
		Map<String,String> rspData = AcpService.post(reqData,url,UnionBase.encoding_UTF8);//发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过

		/**对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考------------->**/
		//应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》
		if(!rspData.isEmpty()){
			if(AcpService.validate(rspData, UnionBase.encoding_UTF8)){
				LogUtil.writeLog("验证签名成功");
				String respCode = rspData.get("respCode");
				if("00".equals(respCode)){
					//交易已受理(不代表交易已成功），等待接收后台通知确定交易成功，也可以主动发起 查询交易确定交易状态。
					//TODO
				}else if("03".equals(respCode) ||
						 "04".equals(respCode) ||
						 "05".equals(respCode)){
					//后续需发起交易状态查询交易确定交易状态。
					//TODO
				}else{
					//其他应答码为失败请排查原因
					//TODO
				}
			}else{
				LogUtil.writeErrorLog("验证签名失败");
				//TODO 检查验证签名失败的原因
			}
		}else{
			//未返回正确的http状态
			LogUtil.writeErrorLog("未获取到返回报文或返回http状态码非200");
		}
		String reqMessage = UnionBase.genHtmlResult(reqData);
		String rspMessage = UnionBase.genHtmlResult(rspData);
		return "</br>请求报文:<br/>"+reqMessage+"<br/>" + "应答报文:</br>"+rspMessage+"";
//		resp.getWriter().write("</br>请求报文:<br/>"+reqMessage+"<br/>" + "应答报文:</br>"+rspMessage+"");
		
	}
    
}
