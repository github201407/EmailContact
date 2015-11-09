package com.example.administrator.emailcontact.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.administrator.emailcontact.R;
import com.example.administrator.emailcontact.activity.ModifyContact;
import com.example.administrator.emailcontact.model.ContactService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/22.
 */
public class RecyclerAdapter extends CursorAdapter {

    private List<String> mList = new ArrayList<String>();
    private SparseBooleanArray mMap = new SparseBooleanArray();
    private Context mCtx;
    public RecyclerAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        this.mCtx = context;
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
        View convertView = inflater.inflate(R.layout.contact_list_item, viewGroup, false);
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
        ImageView icon;
        TextView text1;
        TextView text2;
        CheckBox checkBox;
        Button delete, modify;

        public ViewHolder(View view) {
            icon = (ImageView) view.findViewById(R.id.icon);
            text1 = (TextView) view.findViewById(R.id.text1);
            text2 = (TextView) view.findViewById(R.id.text2);
            checkBox = (CheckBox) view.findViewById(R.id.checkbox);
            delete = (Button) view.findViewById(R.id.item_delete);
            modify = (Button) view.findViewById(R.id.item_modify);
        }

        public void setData(final int id, String displayName, final String email) {
            setImage("");
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
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContactService mService = new ContactService(v.getContext());
                    mService.delete(id);
                }
            });
            modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ModifyContact.Instance(v.getContext(), id, ModifyContact.SEARCH_MODIFY);
                }
            });
        }

        public void setImage(String url){
            Glide.with(mCtx).load(url).asBitmap().centerCrop().placeholder(R.mipmap.head_woman).animate(android.R.anim.fade_in).into(new BitmapImageViewTarget(icon) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(mCtx.getResources(), resource);
                    circularBitmapDrawable.setCornerRadius(50);
                    icon.setImageDrawable(circularBitmapDrawable);
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
            mMap.put(key,!mMap.valueAt(i));
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
