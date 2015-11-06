package com.example.administrator.emailcontact.model;

public class ContactItem {
	public enum Type {
		PARENT, GROUP, CONTACT
	}

	final public Type type;
	final public String name;

	public ContactItem(Type t, String n) {
		type = t;
		name = n;
	}
}
