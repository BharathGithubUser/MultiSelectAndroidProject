package com.belivnat.multiselect.multisearch;

public class MultiSelectModel {
    public boolean isSelected;
    public String dataString;
    public boolean isSection;

    public MultiSelectModel(String dataString, boolean isSelected, boolean isSection) {
        this.isSection = isSection;
        this.isSelected = isSelected;
        this.dataString = dataString;
    }

    public MultiSelectModel(String dataString, boolean isSelected) {
        this(dataString, isSelected, false);
    }

    public MultiSelectModel(String dataString) {
        this(dataString, false);
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getDataString() {
        return dataString;
    }

    public void setDataString(String dataString) {
        this.dataString = dataString;
    }
}
