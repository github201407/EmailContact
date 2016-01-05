package com.example.administrator.emailcontact.model;

import junit.framework.TestCase;

/**
 * Created by Administrator on 2016/1/4.
 */
public class PlaxoTest extends TestCase {
    
    public void testNotNull(){

        Plaxo plaxo = new Plaxo();
        assertNotNull(plaxo);
    }

    public void testPlaxoEqual1(){
        String id;
        String userid;
        String regtime;
        String actiontype;
        String updatetime;
        String states;
        String name;
        String phone;
        String photo;
        String email;
        String address;
        String groups;
        String custom;
    }

    public void testPlaxoEqual(){
        Plaxo plaxo = new Plaxo();
        plaxo.id = "Hello";
        assertEquals("Hello", plaxo.id);
    }

    public void testPlaxoNotEqual(){
        Plaxo plaxo = new Plaxo();
        plaxo.id = "Hello";
        assertEquals("hello", plaxo.id);
    }


}