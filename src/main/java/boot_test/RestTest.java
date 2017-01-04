package boot_test;

import static java.lang.System.out;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.wicket.ajax.json.JSONException;
import org.apache.wicket.ajax.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.gson.JsonObject;

import entity.Announce;
import push.Demo;
import service.ComputeClient;
import util.HttpReqest;

/**
 * Rest 返回對象的json
 *
 */
@RestController
@RequestMapping("/rest")
public class RestTest {
	
	@RequestMapping("/a/{code}")
	Announce home(@PathVariable("code") String code) {
		Announce a = new Announce();
		Map<String, String> postParams = new HashMap<>();
		postParams.put("appkey", "1a80080bb01d6");
		postParams.put("phone", "18341134983");
		postParams.put("zone", "86");
		postParams.put("code", code);
		String r = HttpReqest.doPost("https://webapi.sms.mob.com/sms/verify", postParams, "UTF-8", false);
		try {
			JSONObject result = new JSONObject(r);
			if (null != result) {
				int status = result.getInt("status");
				a.setContentTaT("status");
				a.setTitleTaT(status + "");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return a;
	}
	
	@RequestMapping("/beta")
	Announce beta() {
		Announce a = new Announce();
		a.setContentTaT("c");
		a.setTitleTaT("title");
		try {
			new Demo().sendAndroidUnicast(false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;
	}
	
    private boolean saveFile(MultipartFile file, HttpServletRequest request) {  
        // 判断文件是否为空  
        if (!file.isEmpty()) {  
            try {  
                // 文件保存路径  
                String filePath = request.getSession().getServletContext().getRealPath("/") + "upload/"  
                        + file.getOriginalFilename();  
                // 转存文件  
                file.transferTo(new File(filePath));  
                return true;  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
        return false;  
    }
	
	@RequestMapping(value = "/fileupload",method = RequestMethod.POST)
	public String iconUpload(HttpServletRequest request){
        
        MultipartHttpServletRequest multipartRequest  =  (MultipartHttpServletRequest) request;  
         
        MultipartFile file1 = multipartRequest.getFile("iconImg");
         
        Map<String,MultipartFile> fileMap  =multipartRequest.getFileMap();
        for (Entry<String, MultipartFile> entry : fileMap.entrySet()) {
        	saveFile(entry.getValue(), multipartRequest);
        	System.out.println(entry.getKey() + ":" + entry.getValue());
			
		}
        for (Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
        	System.out.println(entry.getKey() + ":" + entry.getValue()[0]);
			
		}
		return "success";}

	@RequestMapping("/b")
	String start(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {
		String s = "";
		try {
			s = ALIPayManager.getInstance().doPost();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	@RequestMapping(value = "/form", method = {RequestMethod.GET,
			RequestMethod.POST})
	public String getLogin(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {
		Map<String, String[]> m = httpRequest.getParameterMap();
		String t = m.get("a")[0];
		return "done";

	}

}
