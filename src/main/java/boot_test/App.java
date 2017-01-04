package boot_test;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Hello world!
 *
 */
@Controller
public class App
{
	
	@RequestMapping("/")
	String home() {
		return "redirect:/rest/a";
	}
	
	@RequestMapping("/2")
	String home2(RedirectAttributes attr) {
		 attr.addAttribute("param", "p");
		return "redirect:/form";
	}
	
	
	@Value("${application.message:Hello World}")
	private String message = "Hello World";
	
	@Value("${spring.mvc.view.prefix:Hello World}")
	private String prefix = "Hello World";
	
	@GetMapping("/jsp")
	public String welcome(Map<String, Object> model) {
		model.put("time", new Date());
		model.put("message", this.message);
		model.put("prefix", this.prefix);
		return "welcome";
	}
	
	@GetMapping("/ACPSample_B2C")
	public String acpSample_B2C(Map<String, Object> model) {
		return "index";
	}
	
	@GetMapping("/ACPSample_B2C/index_preauth")
	public String acpSample_B2C_preauth(Map<String, Object> model) {
		return "index_preauth";
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
		for (Entry<String, String[]> entry : m.entrySet()) {
			System.out.println("key = " + entry.getKey() + "&value = " + entry.getValue()[0]);
			
		}
		return "index";
		
	}
    
    
}
