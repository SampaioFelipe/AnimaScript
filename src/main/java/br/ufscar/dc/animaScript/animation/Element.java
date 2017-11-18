package br.ufscar.dc.animaScript.animation;

import java.util.ArrayList;
import java.util.HashMap;

import br.ufscar.dc.animaScript.utils.AttributeList;

public class Element {
    private String name;
    private String image_path;
    private int width, height;
    private int pos_x, pos_y;
    private int rotation;

    AttributeList attributes;

    HashMap<String, Element> children;

    ArrayList<Action> actions;

    public Element(String name) {
        this.name = name;
        attributes = new AttributeList();
        children = new HashMap<String, Element>();
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public void setAction(Action action) {
        this.actions.add(action);
        //TODO: tratar actions duplicadas
    }

    public void setPosition(int pos_x, int pos_y) {
        this.pos_x = pos_x;
        this.pos_y = pos_y;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public String getName() {
        return name;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getRotation() {
        return rotation;
    }
}
