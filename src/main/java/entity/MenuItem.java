package entity;

import java.util.ArrayList;
import java.util.List;

public class MenuItem {
	
	public final static int VIEW = 1;
	public final static int CLICK = 2;
	public final static int PARENT = 0;
	
	private List<MenuItem> menuItems = new ArrayList<MenuItem>();
	
	public MenuItem (){
		
	}
	
	public MenuItem (String name, String key, int type){
		this.name = name;
		this.key = key;
		this.type = type;
	}
	
	private int type;
	
	private String name;
	
	private String key; //CLICK 的点击事件  view的跳转链接

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<MenuItem> getMenuItems() {
		return menuItems;
	}

	public void setMenuItems(List<MenuItem> menuItems) {
		this.menuItems = menuItems;
	}

}
