package util.wechatHelper;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

public class WechatBase {

	public WechatBase(HttpServletRequest httpRequest) {
		Map<String, String[]> m = httpRequest.getParameterMap();
		if (m.containsKey("msg_signature")) {
			msg_signature = m.get("msg_signature")[0];
		}
		if (m.containsKey("timestamp")) {
			timestamp = m.get("timestamp")[0];
		}
		if (m.containsKey("nonce")) {
			nonce = m.get("nonce")[0];
		}
		if (m.containsKey("echostr")) {
			echostr = m.get("echostr")[0];
		}
		if (m.containsKey("encrypt_type")) {
			encrypt_type = m.get("encrypt_type")[0];
		}
		if (m.containsKey("openid")) {
			openid = m.get("openid")[0];
		}
		if (m.containsKey("signature")) {
			signature = m.get("signature")[0];
		}
		content = getXml(httpRequest);
	}
	
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

	private String echostr; // url验证信息回复
	
	private String msg_signature; // ase加密key

	private String timestamp; // 时间戳

	private String encrypt_type;// 加密类型

	private String nonce; // 随机数

	private String signature; // 加密验证签名

	private String openid; // 请求方openid
	
	private String content; // 请求流内容

	public String getEchostr() {
		return echostr;
	}

	public void setEchostr(String echostr) {
		this.echostr = echostr;
	}

	public String getMsg_signature() {
		return msg_signature;
	}

	public void setMsg_signature(String msg_signature) {
		this.msg_signature = msg_signature;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getEncrypt_type() {
		return encrypt_type;
	}

	public void setEncrypt_type(String encrypt_type) {
		this.encrypt_type = encrypt_type;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
