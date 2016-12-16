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

import entity.Announce;

/**
 * Rest 返回對象的json
 *
 */
@RestController
@RequestMapping("/rest")
public class RestTest
{
	
	@RequestMapping("/a")
	Announce home() {
		Announce a = new Announce();
		a.setContentTaT("c");
		a.setTitleTaT("title");
		return a;
	}
	
	@RequestMapping("/b")
	String start(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
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
	
	@RequestMapping( value = "/form", method = { RequestMethod.GET, RequestMethod.POST } )
	public String getLogin(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse)
	{
		Map<String, String[]> m = httpRequest.getParameterMap();
		String t= m.get("a")[0];
		return "done";
		
	}
    
    
}
