package com.yzjiang.common.plugin.mq.serialization;

import com.alibaba.fastjson.JSONObject;

public class JsonSerializer implements ISerializer<JSONObject> {
	
	public JsonSerializer() {}
	
	@Override
	public JSONObject deserialize(byte[] data) {
		if (data == null) {
			return null;
		} else {
			return JSONObject.parseObject(data, JSONObject.class);
		}
	}

	@Override
	public byte[] serialize(JSONObject data) {
		 if (data == null) {
			 return null;
		 } else {
			return JSONObject.toJSONBytes(data);
		 }
	}

}
