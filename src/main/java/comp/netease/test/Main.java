package comp.netease.test;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.notnoop.apns.APNS;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		Map m = new HashMap();
//		
//		JSONObject json = new JSONObject(m);
//		m.put("body", "this is body");
//		m.put("title", "this is title");
//		String payload = APNS.newPayload().alertBody(json.toJSONString()).sound("this is sound").customField("mutable-content", 1).build();
//
//		System.out.println(payload);
		
		J1 j1 = new J1();
		j1.setA("aaaaaaa");
//		j1.setB("bbbbbbbb");
//		j1.setC("cccccccc");
		
		System.out.println(JSONObject.toJSON(j1));
		String j1str = JSONObject.toJSON(j1).toString();
		J j = (J) JSONObject.parseObject(j1str, J.class);
		System.out.println(j.getA());
	}

}
