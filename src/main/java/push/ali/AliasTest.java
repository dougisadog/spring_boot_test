package push.ali;

import org.junit.Test;

import com.aliyuncs.push.model.v20160801.BindAliasRequest;
import com.aliyuncs.push.model.v20160801.BindAliasResponse;
import com.aliyuncs.push.model.v20160801.QueryAliasesRequest;
import com.aliyuncs.push.model.v20160801.QueryAliasesResponse;
import com.aliyuncs.push.model.v20160801.UnbindAliasRequest;
import com.aliyuncs.push.model.v20160801.UnbindAliasResponse;

/**
 * Created by lingbo on 16/8/15.
 */
public class AliasTest extends BaseTest {

    /**
     * 查询别名
     * 参考文档 ：https://help.aliyun.com/document_detail/48078.html
     */
    @Test
    public void testQueryAliases() throws Exception {

        QueryAliasesRequest request = new QueryAliasesRequest();
        request.setAppKey(appKey);
        request.setDeviceId(deviceId);

        QueryAliasesResponse response = client.getAcsResponse(request);
        System.out.printf("RequestId: %s\n",
                response.getRequestId());
        for(QueryAliasesResponse.AliasInfo aliasInfo : response.getAliasInfos()){
            System.out.println("aliasName: " + aliasInfo.getAliasName());
        }

    }

    /**
     * 绑定别名
     * 参考文档 ：https://help.aliyun.com/document_detail/48079.html
     */

    @Test
    public void testBindAlias() throws Exception {

        BindAliasRequest request = new BindAliasRequest();
        request.setAppKey(appKey);
        request.setDeviceId(deviceId);
        request.setAliasName(alias);

        BindAliasResponse response = client.getAcsResponse(request);
        System.out.printf("RequestId: %s\n",
                response.getRequestId());

    }

    /**
     * 解绑别名
     * https://help.aliyun.com/document_detail/48080.html
     */

    @Test
    public void testUnbindAlias() throws Exception {

        UnbindAliasRequest request = new UnbindAliasRequest();
        request.setAppKey(appKey);
        request.setDeviceId(deviceId);
        request.setAliasName(alias);

        UnbindAliasResponse response = client.getAcsResponse(request);
        System.out.printf("RequestId: %s\n",
                response.getRequestId());

    }

}
