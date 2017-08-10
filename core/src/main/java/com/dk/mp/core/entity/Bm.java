package com.dk.mp.core.entity;

import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @since 
 * @version 2013-3-13
 * @author wwang
 */
public class Bm extends RealmObject{
	@PrimaryKey
	private String idDepart;//部门id
	private String nameDepart;//部门名称

	public String getIdDepart() {
		return idDepart;
	}

	public void setIdDepart(String idDepart) {
		this.idDepart = idDepart;
	}

	public String getNameDepart() {
		return nameDepart;
	}
	public void setNameDepart(String nameDepart) {
		this.nameDepart = nameDepart;
	}

}
