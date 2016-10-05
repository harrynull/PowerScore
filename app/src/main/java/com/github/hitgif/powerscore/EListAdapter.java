package com.github.hitgif.powerscore;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;

import android.view.View.OnClickListener;

import android.view.ViewGroup;

import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;


public class EListAdapter extends BaseExpandableListAdapter implements ExpandableListView.OnChildClickListener {
    private Context context;
    private ArrayList<Group> groups;

    public EListAdapter(Context context) {
        this.context = context;
        this.groups = getDataArray();
    }

    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).getChildItem(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).getChildrenCount();
    }

    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    public int getGroupCount() {
        return groups.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        Group group = (Group) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.group_layout, null);
        }

        TextView tv = (TextView) convertView.findViewById(R.id.tvGroup);
        tv.setText(group.getTitle());

        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.chbGroup);
        checkBox.setChecked(group.getChecked());

        checkBox.setOnClickListener(new Group_CheckBox_Click(groupPosition));

        return convertView;
    }

    /** ?? Children ?? */
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        Child child = groups.get(groupPosition).getChildItem(childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.child_layout, null);
        }

        TextView tv = (TextView) convertView.findViewById(R.id.tvChild);
        tv.setText(child.getName());

        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.chbChild);
        checkBox.setChecked(child.getChecked());

        checkBox.setOnClickListener(new Child_CheckBox_Click(groupPosition,
                childPosition));

        return convertView;
    }

    public void handleClick(int childPosition, int groupPosition) {
        groups.get(groupPosition).getChildItem(childPosition).toggle();

        int childrenCount = groups.get(groupPosition).getChildrenCount();
        boolean childrenAllIsChecked = true;

        for (int i = 0; i < childrenCount; i++) {
            if (!groups.get(groupPosition).getChildItem(i).getChecked()) {
                childrenAllIsChecked = false;
            }
        }

        groups.get(groupPosition).setChecked(childrenAllIsChecked);

        notifyDataSetChanged();
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
                                int groupPosition, int childPosition, long id) {
        handleClick(childPosition, groupPosition);

        return true;
    }

    public ArrayList<Group> getGroups(){
        return groups;
    }

    public static ArrayList<Group> getDataArray() {
        ArrayList<Group> g = new ArrayList<Group>();

        for (final String key : MainActivity.classes.keySet()) {

            ClassData c = MainActivity.classes.get(key);
            EListAdapter.Group group = new EListAdapter.Group(key, c.name);

            for (int j = 0; j < c.members.length; j++) {
                Child child = new Child(c.members[j]);
                group.addChildrenItem(child);
            }

            g.add(group);
        }

        return g;
    }

    public static class Group {
        private String id;
        private String title;
        private ArrayList<Child> children;
        private boolean isChecked;

        public Group(String id, String title) {
            this.id = id;
            this.title = title;
            children = new ArrayList<Child>();
            this.isChecked = false;
        }

        public void setChecked(boolean isChecked) {
            this.isChecked = isChecked;
        }

        public void toggle() {
            this.isChecked = !this.isChecked;
        }

        public boolean getChecked() {
            return this.isChecked;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public void addChildrenItem(Child child) {
            children.add(child);
        }

        public int getChildrenCount() {
            return children.size();
        }

        public Child getChildItem(int index) {
            return children.get(index);
        }
    }

    class Group_CheckBox_Click implements OnClickListener {
        private int groupPosition;

        Group_CheckBox_Click(int groupPosition) {
            this.groupPosition = groupPosition;
        }

        public void onClick(View v) {
            groups.get(groupPosition).toggle();

            int childrenCount = groups.get(groupPosition).getChildrenCount();
            boolean groupIsChecked = groups.get(groupPosition).getChecked();

            for (int i = 0; i < childrenCount; i++)
                groups.get(groupPosition).getChildItem(i)
                        .setChecked(groupIsChecked);

            notifyDataSetChanged();
        }
    }

    class Child_CheckBox_Click implements OnClickListener {
        public int groupPosition;
        public int childPosition;

        Child_CheckBox_Click(int groupPosition, int childPosition) {
            this.groupPosition = groupPosition;
            this.childPosition = childPosition;
        }

        public void onClick(View v) {
            handleClick(childPosition, groupPosition);
        }
    }
}
