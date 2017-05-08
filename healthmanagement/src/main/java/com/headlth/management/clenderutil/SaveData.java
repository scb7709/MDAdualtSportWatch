package com.headlth.management.clenderutil;

import java.io.Serializable;

/**
 * 该类是存储数据的实体
 *
 */
public class SaveData implements Serializable{
	private long time; // 存储的时间
	private Object obj;// 存储的数据

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

}
