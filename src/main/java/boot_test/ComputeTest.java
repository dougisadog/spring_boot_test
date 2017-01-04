package boot_test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import service.ComputeClient;

/**
 * Rest 返回對象的json
 *
 */
@RestController
@RequestMapping("/rest")
public class ComputeTest {
//	 @Autowired
//	 ComputeClient computeClient;


//	@Autowired
//	private DiscoveryClient client;

//	 @RequestMapping(value = "/addcompute", method = RequestMethod.GET)
//	 public Integer add() {
//	 return computeClient.add(10, 20);
//	 }

//	@RequestMapping(value = "/add", method = RequestMethod.GET)
//	public String add(@RequestParam Integer a, @RequestParam Integer b) {
//		ServiceInstance instance = client.getLocalServiceInstance();
//		
//		Integer r = a + b;
//		return r + "sercice id = " + instance.getServiceId();
//	}

}
