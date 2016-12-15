package boot_test;

public class WXPayManager {

	private static WXPayManager wXPayManager;

	private WXPayManager() {
	};

	public static WXPayManager getInstance() {
		if (null == wXPayManager)
			wXPayManager = new WXPayManager();
		return wXPayManager;
	}

}
