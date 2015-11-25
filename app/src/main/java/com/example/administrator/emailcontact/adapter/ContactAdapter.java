package com.example.administrator.emailcontact.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.emailcontact.R;
import com.example.administrator.emailcontact.model.Contact;
import com.example.administrator.emailcontact.model.ContactItem;

import java.util.LinkedList;
import java.util.List;
/**
 * 联系人适配器
 * */
public class ContactAdapter extends BaseAdapter {
    private final LinkedList<ContactItem> mItems;
    private final LayoutInflater mInflater;
    private List<String> mEmails;

    public ContactAdapter(LayoutInflater inflater, List<String> mEmails) {
        mInflater = inflater;
        mItems = new LinkedList<>();
        this.mEmails = mEmails;
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

    public ContactItem getItem(int position) {
        return mItems.get(position);
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
        View view;
        ContactItem item = getItem(position);
        switch (item.type) {
            case PARENT:
                view = mInflater.inflate(R.layout.expand_parent_item, parent, false);
                ((TextView) view.findViewById(android.R.id.text1)).setText(item.name);
                break;
            case GROUP:
                view = mInflater.inflate(R.layout.expand_group_item, parent, false);
                ((TextView) view.findViewById(android.R.id.text1)).setText(item.name);
                break;
            case CONTACT:
                view = mInflater.inflate(R.layout.contact_list_item, parent, false);
                final Contact contact = (Contact)item.object;
                final boolean isChecked = mEmails.contains(contact.getEmail());
                ((TextView) view.findViewById(R.id.text1)).setText(item.name);
                ((TextView) view.findViewById(R.id.text2)).setText(contact.getEmail());
                ((ImageView) view.findViewById(R.id.icon)).setImageResource(iconForType(item.type));
                ((CheckBox) view.findViewById(R.id.checkbox)).setChecked(isChecked);
                ((CheckBox) view.findViewById(R.id.checkbox)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        String email = contact.getEmail();
                        if (b) {
                            if (!isChecked)
                                mEmails.add(email);
                        } else {
                            if (isChecked)
                                mEmails.remove(email);
                        }
                    }
                });
                break;
            default:
                view = mInflater.inflate(R.layout.contact_list_item, parent, false);
                break;
        }
        return view;
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
}
