package comp.netease.test;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.notnoop.apns.APNS;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map m = new HashMap();
		
		JSONObject json = new JSONObject(m);
		m.put("body", "this is body");
		m.put("title", "this is title");
		String payload = APNS.newPayload().alertBody(json.toJSONString()).sound("this is sound").customField("mutable-content", 1).build();

		System.out.println(payload);
	}

}
