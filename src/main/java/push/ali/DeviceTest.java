package push.ali;

import com.aliyuncs.push.model.v20160801.CheckDeviceRequest;
import com.aliyuncs.push.model.v20160801.CheckDeviceResponse;
import org.junit.Test;

/**
 * Created by lingbo on 2016/12/15.
 * 设备相关接口demo
 */
public class DeviceTest extends BaseTest {

    /**
     * 查询设备是否为有效设备
     * 参考文档：https://help.aliyun.com/document_detail/48300.html
     * */
    @Test
    public void testCheckDevice() throws Exception {
        CheckDeviceRequest request = new CheckDeviceRequest();
        request.setAppKey(appKey);
        request.setDeviceId(deviceIds);
        CheckDeviceResponse response = client.getAcsResponse(request);
        System.out.print("Available: " + response.getAvailable());
    }

}
