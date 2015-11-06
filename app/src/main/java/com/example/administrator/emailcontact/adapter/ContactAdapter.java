package com.example.administrator.emailcontact.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.emailcontact.R;
import com.example.administrator.emailcontact.model.ContactItem;

import java.util.LinkedList;

public class ContactAdapter extends BaseAdapter {
    private final LinkedList<ContactItem> mItems;
    private final LayoutInflater mInflater;

    public ContactAdapter(LayoutInflater inflater) {
        mInflater = inflater;
        mItems = new LinkedList<ContactItem>();
    }

    public void clear() {
        mItems.clear();
    }

    public void add(ContactItem item) {
        mItems.add(item);
        notifyDataSetChanged();
    }

    public int getCount() {
        return mItems.size();
    }

    public Object getItem(int i) {
        return null;
    }

    public long getItemId(int arg0) {
        return 0;
    }

    private int iconForType(ContactItem.Type type) {
        switch (type) {
            case PARENT:
                return R.mipmap.head_man;
            case GROUP:
                return R.mipmap.head_man;
            case CONTACT:
                return R.mipmap.head_woman;
            default:
                return 0;
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        if (convertView == null) {
            v = mInflater.inflate(R.layout.contact_list_item, null);
        } else {
            v = convertView;
        }
        ContactItem item = mItems.get(position);
        ((TextView) v.findViewById(R.id.text1)).setText(item.name);
        ((ImageView) v.findViewById(R.id.icon)).setImageResource(iconForType(item.type));
        ((ImageView) v.findViewById(R.id.icon)).setColorFilter(Color.argb(255, 0, 0, 0));
        return v;
    }

}
