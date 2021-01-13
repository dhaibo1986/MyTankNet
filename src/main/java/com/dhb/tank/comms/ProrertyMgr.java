package com.dhb.tank.comms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
*@author haibo.duan
*@description  静态属性获取，将构造方法设为private，避免其他类将此类实例化
*@date  2020/11/12 19:12
*/
public class ProrertyMgr {

	private ProrertyMgr() {

	}

	static Properties props = new Properties();

	static {
		try {
			props.load(ProrertyMgr.class.getClassLoader().getResourceAsStream("config"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Object get(String key) {
		if(props == null) {
			return null;
		}
		return props.get(key);
	}

	public static List<String> getList(String key) {
		List<String> result = new ArrayList<>();
		if(props != null && null != props.get(key)) {
			String str = (String) props.get(key);
			String[] array = str.split(",");
			for(String item:array){
				result.add(item);
			}
		}
		return result;
	}

	public static Integer getInt(String key) {
		if(props == null) {
			return null;
		}
		return Integer.parseInt((String)props.get(key));
	}

	public static String getString(String key) {
		if(props == null) {
			return null;
		}
		return (String)props.get(key);
	}

	public static void main(String[] args) {
		System.out.println(ProrertyMgr.get("initTankCount"));
	}
}
