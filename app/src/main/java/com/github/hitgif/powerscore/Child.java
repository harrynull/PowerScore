package com.github.hitgif.powerscore;


public class Child {
    private String name;
    private boolean isChecked;

    public Child(String name) {
        this.name = name;
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

    public String getName() {
        return name;
    }
}
