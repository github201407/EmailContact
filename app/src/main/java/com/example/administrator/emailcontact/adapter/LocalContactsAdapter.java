package com.example.administrator.emailcontact.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.emailcontact.R;

/**
 * Created by Administrator on 2015/11/30 0030.
 */
public class LocalContactsAdapter extends BaseAdapter{

    ViewHolder holder = null ;
    private Context mContext = null ;

    public void LocalContactsAdapter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if( view == null ){
            view = LayoutInflater.from(mContext).inflate(R.layout.fragment_list_item, null );
            holder = new ViewHolder();
            holder.head = (ImageView) view.findViewById(R.id.head);
            holder.name = (TextView) view.findViewById(R.id.contact_name);
            holder.phone = (TextView) view.findViewById(R.id.contact_phone);
            holder.addrOrNum = (TextView) view.findViewById(R.id.contact_addr_or_num);
            holder.time = (TextView) view.findViewById(R.id.dialog_time);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        return view;

    }

    private static class ViewHolder{
        ImageView head;
        TextView name;
        TextView phone;
        TextView addrOrNum;
        TextView time;
    }
}
