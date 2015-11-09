package com.example.administrator.emailcontact.model;

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
