package com.example.administrator.emailcontact.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.administrator.emailcontact.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/22.
 */
public class RecyclerAdapter extends CursorAdapter {

    private List<String> mList = new ArrayList<String>();
    private SparseBooleanArray mMap = new SparseBooleanArray();

    public RecyclerAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        while(c.moveToNext()){
            int id = c.getInt(0);
            String email = c.getString(2);
            mMap.put(id,false);
            mList.add(email);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.contact_list_item, null);
        return convertView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int id = cursor.getInt(0);
        String displayName = cursor.getString(1);
        String email = cursor.getString(2);
        ViewHolder mHolder = new ViewHolder(view);
        mHolder.setData(id, displayName, email);
    }

    class ViewHolder {
        TextView text1;
        TextView text2;
        CheckBox checkBox;

        public ViewHolder(View view) {
            text1 = (TextView) view.findViewById(R.id.text1);
            text2 = (TextView) view.findViewById(R.id.text2);
            checkBox = (CheckBox) view.findViewById(R.id.checkbox);
        }

        public void setData(final int id, String displayName, final String email) {
            text1.setText(displayName);
            text2.setText(email);
            checkBox.setTag(email);
            checkBox.setChecked(mMap.get(id));
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckBox checkBox = (CheckBox) view;
                    mMap.put(id, checkBox.isChecked());
                }
            });
        }
    }

    public void setAll(){
        for(int i = 0,n= mMap.size(); i < n; i++) {
            int key = mMap.keyAt(i);
            mMap.put(key,true);
        }
        notifyDataSetChanged();
    }

    public void setCancelAll(){
        for(int i = 0,n= mMap.size(); i < n; i++) {
            int key = mMap.keyAt(i);
            mMap.put(key,false);
        }
        notifyDataSetChanged();
    }

    /**
     * 标记被选中的位置为true，遍历取出mList对应true的位置的值。
     * @return
     */
    public String getEmailStr() {
        StringBuffer mBuffer = new StringBuffer();
        for(int i = 0,n= mMap.size(); i < n; i++) {
            if(mMap.valueAt(i)){
                mBuffer.append(mList.get(i) + ",");
            }
        }
        return mBuffer.toString();
    }

}
