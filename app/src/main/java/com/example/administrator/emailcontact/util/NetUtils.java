package com.example.administrator.emailcontact.util;

import android.util.Log;

import com.example.administrator.emailcontact.model.Contact;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Administrator on 2015/11/19.
 */
public class NetUtils {

    public static String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        StringBuffer stringBuffer = null;
        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            int response = conn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK){
                is = conn.getInputStream();
                stringBuffer = new StringBuffer();
                String temp;
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                while ((temp = reader.readLine()) != null){
                    stringBuffer.append(temp);
                }
                reader.close();
            }
            return stringBuffer == null ? "" : stringBuffer.toString();
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public static boolean uploadUrl(String myurl, String param) throws IOException {
        OutputStream os = null;
        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.connect();
            os = conn.getOutputStream();
            os.write(URLEncoder.encode(param, "utf-8").getBytes());

            int response = conn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK){
                return true;
            }
            return false;
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }

    public static void post(String myurl) throws Exception {
        URL url = new URL("http://yoururl.com");
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        List<Contact> params = new ArrayList<>();

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, "UTF-8"));
        writer.write(getQuery(params));
        writer.flush();
        writer.close();
        os.close();
        conn.connect();
    }

    private static String getQuery(List<Contact> params) throws Exception
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Contact pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getEmail(), "UTF-8"));
        }

        return result.toString();
    }
}
