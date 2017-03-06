package push.ali;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.Test;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.push.model.v20160801.PushMessageToAndroidRequest;
import com.aliyuncs.push.model.v20160801.PushMessageToAndroidResponse;
import com.aliyuncs.push.model.v20160801.PushNoticeToAndroidRequest;
import com.aliyuncs.push.model.v20160801.PushNoticeToAndroidResponse;

public class AndroidPushManager {
	
    protected static String region;
    protected static long appKey;
    protected static String deviceIds;
    protected static String deviceId;
    protected static String accounts;
    protected static String account;
    protected static String aliases;
    protected static String alias;
    protected static String tag;
    protected static String tagExpression;

    protected static DefaultAcsClient client;
	
	public AndroidPushManager() {
		  InputStream inputStream = BaseTest.class.getClassLoader().getResourceAsStream("push.properties");
	        Properties properties = new Properties();
	        try {
				properties.load(inputStream);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	        String accessKeyId = properties.getProperty("accessKeyId");

	        String accessKeySecret = properties.getProperty("accessKeySecret");

	        String key = properties.getProperty("appKey");

	        region = properties.getProperty("regionId");
	        appKey = Long.valueOf(key);
//	        deviceIds = properties.getProperty("deviceIds");
//	        deviceId = properties.getProperty("deviceId");
	        deviceId = "2dcb2ff1932a412dac30a36cac149dd3";
//	        accounts = properties.getProperty("accounts");
//	        account = properties.getProperty("account");
//	        aliases = properties.getProperty("aliases");
//	        alias = properties.getProperty("alias");
//	        tag = properties.getProperty("tag");
//	        tagExpression = properties.getProperty("tagExpression");

	        IClientProfile profile = DefaultProfile.getProfile(region, accessKeyId, accessKeySecret);
	        client = new DefaultAcsClient(profile);
	}

	   /**
     * 推送消息给android
     * <p>
     * 参见文档 https://help.aliyun.com/document_detail/48085.html
     */
    public void PushMessageToAndroid() throws Exception {

        PushMessageToAndroidRequest androidRequest = new PushMessageToAndroidRequest();
        //安全性比较高的内容建议使用HTTPS
        androidRequest.setProtocol(ProtocolType.HTTPS);
        //内容较大的请求，使用POST请求
        androidRequest.setMethod(MethodType.POST);
        androidRequest.setAppKey(appKey);
        androidRequest.setTarget("DEVICE");
        androidRequest.setTargetValue("2dcb2ff1932a412dac30a36cac149dd3");
        androidRequest.setTitle("66666666666");
        androidRequest.setBody("6");
        PushMessageToAndroidResponse pushMessageToAndroidResponse = client.getAcsResponse(androidRequest);
        System.out.printf("RequestId: %s, MessageId: %s\n",
                pushMessageToAndroidResponse.getRequestId(), pushMessageToAndroidResponse.getMessageId());

    }
    
    /**
     * 推送通知给android
     * <p>
     * 参见文档 https://help.aliyun.com/document_detail/48087.html
     */
    public void testPushNoticeToAndroid() throws Exception {

        PushNoticeToAndroidRequest androidRequest = new PushNoticeToAndroidRequest();
        //安全性比较高的内容建议使用HTTPS
        androidRequest.setProtocol(ProtocolType.HTTPS);
        //内容较大的请求，使用POST请求
        androidRequest.setMethod(MethodType.POST);
        androidRequest.setAppKey(appKey);
        androidRequest.setTarget("DEVICE");
        androidRequest.setTargetValue("2dcb2ff1932a412dac30a36cac149dd3");
        androidRequest.setTitle("title");
        androidRequest.setBody("Body");
        androidRequest.setExtParameters("{\"k1\":\"v1\"}");

        PushNoticeToAndroidResponse pushNoticeToAndroidResponse = client.getAcsResponse(androidRequest);
        System.out.printf("RequestId: %s, MessageId: %s\n",
                pushNoticeToAndroidResponse.getRequestId(), pushNoticeToAndroidResponse.getMessageId());

    }
}
