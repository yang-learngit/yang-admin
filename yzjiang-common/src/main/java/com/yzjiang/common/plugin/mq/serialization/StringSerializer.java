package com.yzjiang.common.plugin.mq.serialization;

import java.io.UnsupportedEncodingException;

/**
 * @author Administrator
 */
public class StringSerializer implements ISerializer<String> {
	private String encoding = "UTF-8";
	
	public StringSerializer() {}
	
	public StringSerializer(String encoding) {
		this.encoding = encoding;
	}
	
	@Override
	public String deserialize(byte[] data) {
		if (data == null) {
			return null;
		} else {
			try {
				return new String(data, encoding);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public byte[] serialize(String data) {
		 if (data == null) {
			 return null;
		 } else {
			 try {
				return data.getBytes(encoding);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		 }
	}

}
