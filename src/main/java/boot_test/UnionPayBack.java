package boot_test;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import acp.sdk.AcpService;
import acp.sdk.LogUtil;
import acp.sdk.SDKConstants;

/**
 * Rest 返回對象的json
 *
 */
@Controller
@RequestMapping("/ACPSample_B2C")
public class UnionPayBack
{
	
	@RequestMapping( value = "/BackRcvResponse", method = { RequestMethod.GET, RequestMethod.POST } )
	public String getLogin(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) throws ServletException, IOException
	{
	LogUtil.writeLog("BackRcvResponse接收后台通知开始");
		
		String encoding = httpRequest.getParameter(SDKConstants.param_encoding);
		// 获取银联通知服务器发送的后台通知参数
		Map<String, String> reqParam = getAllRequestParam(httpRequest);

		LogUtil.printRequestLog(reqParam);

		Map<String, String> valideData = null;
		if (null != reqParam && !reqParam.isEmpty()) {
			Iterator<Entry<String, String>> it = reqParam.entrySet().iterator();
			valideData = new HashMap<String, String>(reqParam.size());
			while (it.hasNext()) {
				Entry<String, String> e = it.next();
				String key = (String) e.getKey();
				String value = (String) e.getValue();
				value = new String(value.getBytes(encoding), encoding);
				valideData.put(key, value);
			}
		}

		//重要！验证签名前不要修改reqParam中的键值对的内容，否则会验签不过
		if (!AcpService.validate(valideData, encoding)) {
			LogUtil.writeLog("验证签名结果[失败].");
			//验签失败，需解决验签问题
			
		} else {
			LogUtil.writeLog("验证签名结果[成功].");
			//【注：为了安全验签成功才应该写商户的成功处理逻辑】交易成功，更新商户订单状态
			
			String orderId =valideData.get("orderId"); //获取后台通知的数据，其他字段也可用类似方式获取
			String respCode =valideData.get("respCode"); //获取应答码，收到后台通知了respCode的值一般是00，可以不需要根据这个应答码判断。
			
		}
		LogUtil.writeLog("BackRcvResponse接收后台通知结束");
		//返回给银联服务器http 200  状态码
//		resp.getWriter().print("ok");
		return "index";
		
	}
	
	@RequestMapping( value = "/FrontRcvResponse", method = { RequestMethod.GET, RequestMethod.POST } )
	public String frontRcvResponse(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, RedirectAttributes attr) throws ServletException, IOException
	{
		LogUtil.writeLog("FrontRcvResponse前台接收报文返回开始");

		String encoding = httpRequest.getParameter(SDKConstants.param_encoding);
		LogUtil.writeLog("返回报文中encoding=[" + encoding + "]");
		String pageResult = "";
		if (UnionBase.encoding_UTF8.equalsIgnoreCase(encoding)) {
//			pageResult = "/utf8_result.jsp";
			pageResult = "utf8_result";
		} else {
//			pageResult = "/gbk_result.jsp";
			pageResult = "gbk_result";
		}
		Map<String, String> respParam = getAllRequestParam(httpRequest);

		// 打印请求报文
		LogUtil.printRequestLog(respParam);

		Map<String, String> valideData = null;
		StringBuffer page = new StringBuffer();
		if (null != respParam && !respParam.isEmpty()) {
			Iterator<Entry<String, String>> it = respParam.entrySet()
					.iterator();
			valideData = new HashMap<String, String>(respParam.size());
			while (it.hasNext()) {
				Entry<String, String> e = it.next();
				String key = (String) e.getKey();
				String value = (String) e.getValue();
				value = new String(value.getBytes(encoding), encoding);
				page.append("<tr><td width=\"30%\" align=\"right\">" + key
						+ "(" + key + ")</td><td>" + value + "</td></tr>");
				valideData.put(key, value);
			}
		}
		if (!AcpService.validate(valideData, encoding)) {
			page.append("<tr><td width=\"30%\" align=\"right\">验证签名结果</td><td>失败</td></tr>");
			LogUtil.writeLog("验证签名结果[失败].");
		} else {
			page.append("<tr><td width=\"30%\" align=\"right\">验证签名结果</td><td>成功</td></tr>");
			LogUtil.writeLog("验证签名结果[成功].");
			System.out.println(valideData.get("orderId")); //其他字段也可用类似方式获取
		}
//		httpRequest.setAttribute("result", page.toString());
//		httpRequest.getRequestDispatcher(pageResult).forward(httpRequest, resp);

		LogUtil.writeLog("FrontRcvResponse前台接收报文返回结束");
		attr.addAttribute("result", page.toString());
		return "redirect:/" + pageResult;
	}
    
	/**
	 * 获取请求参数中所有的信息
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, String> getAllRequestParam(final HttpServletRequest request) {
		Map<String, String> res = new HashMap<String, String>();
		Enumeration<?> temp = request.getParameterNames();
		if (null != temp) {
			while (temp.hasMoreElements()) {
				String en = (String) temp.nextElement();
				String value = request.getParameter(en);
				res.put(en, value);
				//在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
				//System.out.println("ServletUtil类247行  temp数据的键=="+en+"     值==="+value);
				if (null == res.get(en) || "".equals(res.get(en))) {
					res.remove(en);
				}
			}
		}
		return res;
	}
}
