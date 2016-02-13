package com.github.hitgif.powerscore;


import java.util.ArrayList;

public class Group {
    private String id;
    private String title;
    private ArrayList<Child> children;
    private boolean isChecked;

    public Group(String id, String title) {
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
