package com.example.meet_workshop;

public class InterestData {


    private String name;

    private boolean selected;
    public InterestData(String name) {
        this.name = name;
    }

    public InterestData(boolean selected) {
        this.selected = selected;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
