package com.dk.mp.core.entity;

import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Ks extends RealmObject{
	/**
	 * name : 鏍￠暱鍔炲叕瀹�
	 * values : ["021-84765162/8162"]
	 * id : 6
	 */
	@PrimaryKey
	private String id;
	private String name;
//	private List<String> values;

	private String departmentid;//部门id
	private String departmentname;//部门名称
	private String tels;

	public String getDepartmentid() {
		return departmentid;
	}

	public void setDepartmentid(String departmentid) {
		this.departmentid = departmentid;
	}

	public String getDepartmentname() {
		return departmentname;
	}

	public void setDepartmentname(String departmentname) {
		this.departmentname = departmentname;
	}

	public String getTels() {
		return tels;
	}

	public void setTels(String tels) {
		this.tels = tels;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/*public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}*/
}
