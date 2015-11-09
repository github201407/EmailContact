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
        ContactItem item = mItems.get(position);
        if (convertView == null) {
            v = getItemView(item.type , item, parent);
        } else {
            v = getItemView(item.type , item, parent);
        }
        return v;
    }

    @Override
    public int getViewTypeCount() {
        return ContactItem.Type.values().length;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return ContactItem.Type.PARENT.ordinal();
            case 1:
                return ContactItem.Type.GROUP.ordinal();
            case 2:
                return ContactItem.Type.CONTACT.ordinal();
        }
        return super.getItemViewType(position);
    }

    private View getItemView(ContactItem.Type type, ContactItem item, ViewGroup parent) {
        View view;
        int position = getItemViewType(type.ordinal());
        switch (position) {
            case 0:
                view = mInflater.inflate(R.layout.expand_parent_item, parent, false);
                ((TextView) view.findViewById(android.R.id.text1)).setText("返回上一级");
                break;
            case 1:
                view = mInflater.inflate(R.layout.expand_group_item, parent, false);
                ((TextView) view.findViewById(android.R.id.text1)).setText(item.name);
                break;
            case 2:
                view = mInflater.inflate(R.layout.contact_list_item, parent, false);
                ((TextView) view.findViewById(R.id.text1)).setText(item.name);
                ((ImageView) view.findViewById(R.id.icon)).setImageResource(iconForType(item.type));
                ((ImageView) view.findViewById(R.id.icon)).setColorFilter(Color.argb(255, 0, 0, 0));
                break;
            default:
                view = mInflater.inflate(R.layout.contact_list_item, parent, false);
                break;
        }
        return view;
    }
}
