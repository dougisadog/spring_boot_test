package boot_test;

public class ConstantConfig {
	
	/********************************************************************************************************************************************************/
	/**
	/** 微信
	/**
	/********************************************************************************************************************************************************/
			
	public final static String WX_APP_ID = "wxab78122b71fe5483"; //微信支付的APP_ID   //wxab78122b71fe5483 wxab78122b71fe5483
	public static final String WX_API_KEY="eb28dd2c943e8b5953c29c55363a3ece";   //微信的API密匙,//  API密钥，在商户平台设置
	public static final String WX_PAY_KEY="gawneljt932jlmvsm293052jfovm2i35";   //微信的API密匙,//  API密钥，在商户平台设置
	public final static String WX_PAY_DATA = "/rest/order/wx-order-data"; //从 [公司服务器] 上获取 [微信支付数据]
	public final static String WX_MCH_ID = "1251319701"; //从 [公司服务器] 上获取 [微信支付数据]

	//支付宝支付的详细信息
	//商户PID
	public static final String PARTNER = "2088611519872488";
	//商户收款账号
	public static final String SELLER = "shuanggekeji@126.com";
	public final static String ALI_NOTIFY_URL = "/order/alipay-notify-url"; // [支付宝服务器] 回调  [公司服务器]
	//支付宝商户私钥
	public final static String RSA_PRIVATE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALa2VqJqy7/wtHbehfKwU9cMAt0S+dDe3VNcJJ1tJOcs1PA4Q5rGna2nnTc69nIUNm6gKWtb9tmLA9YxpRgyt8rcuFhYywAosX1sxnWHdRKx9wRgOdvNk5AnNMIaMy7DWAduH0IzJvKhyv1EGfmWfbDhNyRlAZdiozNYgVFBmSVDAgMBAAECgYAHl23kE7HhiLvG0JoaKk9heQNJcjdlAU2K4CI5VEabQFacoInWjXgRtgwnNlD1DnfwgsEVz91izo7bQHbOmZTfS9Nx24Br2CB1abf8J/fRryxVZsmqZQis1fmlbxCm5coWLUnL/qD7faSME71tziEn0Nj+dKgSIc2eIUD3o2UG8QJBAPJ5CwjuSLIcMRgETaARL12mws4crFruF8jDSCWsDPGvUx+Y3fnDUt9mrA0Im777vWMKXX/RwR/t8hZjBujQJmsCQQDA584FqLHFVqJWUboZmJGJtjUONMQEpLr2s1d6rX5Jhz0P0fO+U7l13GY/SyJHSz17R0KmhilheV/TLDbyJ0KJAkEA1lUO3w8a7W4kK3GqWGK4dtUw/9ayuBIcrheIz9wc+QqctKKBHQV+XQG59i901MZcK47/BTyZtSq1Qvq4IdXVDwJBAKqC+3fDEkfVeS8FlJMVaeepOCJzf6R/G4f/JF8axdsmgFHgiiv9A5zrkTF3LziHiDPU3FQnmKJBT/NwTK0lCMkCQQDi4uPc3942xYb7Nx7ZXf2AxyV3Oi6kUt+rtKx5IaMbaLb2k3rGd+5YAadxZc+RXPu5IkzfkeB/5kQyI9duqUN/"; 
	//支付宝公钥
	public final static String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC2tlaiasu/8LR23oXysFPXDALdEvnQ3t1TXCSdbSTnLNTwOEOaxp2tp503OvZyFDZuoClrW/bZiwPWMaUYMrfK3LhYWMsAKLF9bMZ1h3USsfcEYDnbzZOQJzTCGjMuw1gHbh9CMybyocr9RBn5ln2w4TckZQGXYqMzWIFRQZklQwIDAQAB"; 
	

}
