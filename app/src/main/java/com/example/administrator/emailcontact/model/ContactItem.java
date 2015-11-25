package com.example.administrator.emailcontact.model;
/**
 *
 * 适配器item对象，根据Type类型做区分，把object转成对应的对象以便显示。
 *
 * */
public class ContactItem {
	public enum Type {
		PARENT, GROUP, CONTACT
	}

	final public Type type;
	final public String name;
	final public Object object;

	public ContactItem(Type t, String n, Object object) {
		this.type = t;
		this.name = n;
		this.object = object;
	}
}
