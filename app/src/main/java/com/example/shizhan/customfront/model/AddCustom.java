package com.example.shizhan.customfront.model;

/**
 * Created by shizhan on 16/7/31.
 */
public class AddCustom {
    private int ImageId;
    private String label;
    private String show_text;
    private int enterId;

    public AddCustom(int imageId, String label, String show_text, int enterId) {
        this.ImageId = imageId;
        this.label = label;
        this.show_text = show_text;
        this.enterId = enterId;
    }

    public int getImageId() {
        return ImageId;
    }

    public void setImageId(int imageId) {
        ImageId = imageId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String category) {
        this.label = category;
    }

    public String getShow_text() {
        return show_text;
    }

    public void setShow_text(String show_text) {
        this.show_text = show_text;
    }

    public int getEnterId() {
        return enterId;
    }

    public void setEnterId(int enterId) {
        this.enterId = enterId;
    }
}
