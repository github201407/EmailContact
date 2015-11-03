package com.example.administrator.emailcontact.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.administrator.emailcontact.R;
import com.example.administrator.emailcontact.activity.ModifyContact;
import com.example.administrator.emailcontact.model.Contact;
import com.example.administrator.emailcontact.model.ContactService;
import com.example.administrator.emailcontact.model.Group;
import com.example.administrator.emailcontact.model.GroupService;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/11/2.
 */
public class GroupExpandAdapter extends BaseExpandableListAdapter {

    ArrayList<Group> mGroups = new ArrayList<>();
    ArrayList<Group> mGChilds = new ArrayList<>();
    ArrayList<Contact> mContacts = new ArrayList<>();
    HashMap<Integer, ArrayList<Contact>> mGroupId2Contacts = new HashMap<>();
    private Context mCtx;
    private GroupService mGroupService;
    private ContactService mContactService;

    public GroupExpandAdapter(Context context) {
        this.mCtx = context;
        mGroupService = new GroupService(context);
        mContactService = new ContactService(context);
        mGroups = mGroupService.queryTopParent(-1);
    }

    /**
     * Gets the number of groups.
     *
     * @return the number of groups
     */
    @Override
    public int getGroupCount() {
        return mGroups.size();
    }

    /**
     * Gets the number of children in a specified group.
     *
     * @param groupPosition the position of the group for which the children
     *                      count should be returned
     * @return the children count in the specified group
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        Group mGroup = mGroups.get(groupPosition);
        int groupId = mGroup.getId();
        mContacts = mContactService.queryContactByGroupId(groupId);
        mGroupId2Contacts.put(groupPosition, mContacts);
        return mContacts.size();
    }

    /**
     * Gets the data associated with the given group.
     *
     * @param groupPosition the position of the group
     * @return the data child for the specified group
     */
    @Override
    public Group getGroup(int groupPosition) {
        return mGroups.get(groupPosition);
    }

    /**
     * Gets the data associated with the given child within the given group.
     *
     * @param groupPosition the position of the group that the child resides in
     * @param childPosition the position of the child with respect to other
     *                      children in the group
     * @return the data of the child
     */
    @Override
    public Contact getChild(int groupPosition, int childPosition) {
        return mGroupId2Contacts.get(groupPosition).get(childPosition);
    }

    /**
     * Gets the ID for the group at the given position. This group ID must be
     * unique across groups. The combined ID (see
     * {@link #getCombinedGroupId(long)}) must be unique across ALL items
     * (groups and all children).
     *
     * @param groupPosition the position of the group for which the ID is wanted
     * @return the ID associated with the group
     */
    @Override
    public long getGroupId(int groupPosition) {
        return getGroup(groupPosition).getId();
    }

    /**
     * Gets the ID for the given child within the given group. This ID must be
     * unique across all children within the group. The combined ID (see
     * {@link #getCombinedChildId(long, long)}) must be unique across ALL items
     * (groups and all children).
     *
     * @param groupPosition the position of the group that contains the child
     * @param childPosition the position of the child within the group for which
     *                      the ID is wanted
     * @return the ID associated with the child
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return getChild(groupPosition, childPosition).getId();
    }

    /**
     * Indicates whether the child and group IDs are stable across changes to the
     * underlying data.
     *
     * @return whether or not the same ID always refers to the same object
     * @see Adapter#hasStableIds()
     */
    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * Gets a View that displays the given group. This View is only for the
     * group--the Views for the group's children will be fetched using
     * {@link #getChildView(int, int, boolean, View, ViewGroup)}.
     *
     * @param groupPosition the position of the group for which the View is
     *                      returned
     * @param isExpanded    whether the group is expanded or collapsed
     * @param convertView   the old view to reuse, if possible. You should check
     *                      that this view is non-null and of an appropriate type before
     *                      using. If it is not possible to convert this view to display
     *                      the correct data, this method can create a new view. It is not
     *                      guaranteed that the convertView will have been previously
     *                      created by
     *                      {@link #getGroupView(int, boolean, View, ViewGroup)}.
     * @param parent        the parent that this view will eventually be attached to
     * @return the View corresponding to the group at the specified position
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder mGroupHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mCtx).inflate(R.layout.expand_group_item, parent, false);
            mGroupHolder = new GroupHolder(convertView);
            convertView.setTag(mGroupHolder);
        } else {
            mGroupHolder = (GroupHolder) convertView.getTag();
        }
        mGroupHolder.setData(getGroup(groupPosition));
        return convertView;
    }

    /**
     * Gets a View that displays the data for the given child within the given
     * group.
     *
     * @param groupPosition the position of the group that contains the child
     * @param childPosition the position of the child (for which the View is
     *                      returned) within the group
     * @param isLastChild   Whether the child is the last child within the group
     * @param convertView   the old view to reuse, if possible. You should check
     *                      that this view is non-null and of an appropriate type before
     *                      using. If it is not possible to convert this view to display
     *                      the correct data, this method can create a new view. It is not
     *                      guaranteed that the convertView will have been previously
     *                      created by
     *                      {@link #getChildView(int, int, boolean, View, ViewGroup)}.
     * @param parent        the parent that this view will eventually be attached to
     * @return the View corresponding to the child at the specified position
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ContactHolder mContactHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mCtx).inflate(R.layout.contact_list_item, parent, false);
            mContactHolder = new ContactHolder(convertView);
            convertView.setTag(mContactHolder);
        } else {
            mContactHolder = (ContactHolder) convertView.getTag();
        }
        mContactHolder.setData(getChild(groupPosition, childPosition));
        return convertView;
    }

    /**
     * Whether the child at the specified position is selectable.
     *
     * @param groupPosition the position of the group that contains the child
     * @param childPosition the position of the child within the group
     * @return whether the child is selectable.
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    class GroupHolder {
        private int groupId;
        private TextView textView;

        public GroupHolder(View view) {
            textView = (TextView) view.findViewById(android.R.id.text1);
        }

        public void setData(Group group) {
            groupId = group.getId();
            textView.setText(group.getName());
        }
    }

    class ContactHolder {
        ImageView icon;
        int id;
        TextView text1;
        TextView text2;
        CheckBox checkBox;
        Button delete, modify;

        public ContactHolder(View view) {
            icon = (ImageView) view.findViewById(R.id.icon);
            text1 = (TextView) view.findViewById(R.id.text1);
            text2 = (TextView) view.findViewById(R.id.text2);
            checkBox = (CheckBox) view.findViewById(R.id.checkbox);
            delete = (Button) view.findViewById(R.id.item_delete);
            modify = (Button) view.findViewById(R.id.item_modify);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                    if (b) {
//                        if (!mEmails.contains(getEmail()))
//                            mEmails.add(getEmail());
//                        ExpandList.checkedId = getId();
//                        mCheckedHistory.push(getId());
//                    } else {
//                        if (mEmails.contains(getEmail()))
//                            mEmails.remove(getEmail());
//                        if(!mCheckedHistory.isEmpty() && mCheckedHistory.peek() == getId()){
//                            mCheckedHistory.pop();
//                            if(!mCheckedHistory.isEmpty())
//                                ExpandList.checkedId = mCheckedHistory.peek();
//                        }
//
//                    }

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
                    ModifyContact.Instance(v.getContext(), id, ModifyContact.CONTACT_MODIFY);
                }
            });
        }

        public void setImage(String url) {
            Glide.with(mCtx).load(url).asBitmap().centerCrop().placeholder(R.mipmap.head_man).animate(android.R.anim.fade_in).into(new BitmapImageViewTarget(icon) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(mCtx.getResources(), resource);
                    circularBitmapDrawable.setCornerRadius(50);
                    icon.setImageDrawable(circularBitmapDrawable);
                }
            });
        }

        public void setData(Contact contact) {
            this.id = contact.getId();
            text1.setText(contact.getName());
            text2.setText(contact.getEmail());
            setImage(contact.getImageUrl());
        }
    }
}
