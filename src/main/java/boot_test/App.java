package boot_test;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello world!
 *
 */
@Controller
public class App
{
	
	@RequestMapping("/")
	String home() {
		return "Hello World!";
	}
	
	@Value("${application.message:Hello World}")
	private String message = "Hello World";
	
	@GetMapping("/jsp")
	public String welcome(Map<String, Object> model) {
		model.put("time", new Date());
		model.put("message", this.message);
		return "welcome";
	}
	
	@RequestMapping("/start")
	String start(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		String s = "";
			try {
				s = ALIPayManager.getInstance().doPost();
				httpResponse.setContentType("text/html;charset=utf-8");
				httpResponse.getWriter().write(s);// 直接将完整的表单html输出到页面
				httpResponse.getWriter().flush();
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return "";
	}
	
	@RequestMapping( value = "/form", method = { RequestMethod.GET, RequestMethod.POST } )
	public String getLogin(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse)
	{
		Map<String, String[]> m = httpRequest.getParameterMap();
		String t= m.get("a")[0];
		return "done";
		
	}
    
    
}
