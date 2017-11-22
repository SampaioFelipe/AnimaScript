package br.ufscar.dc.animaScript.animation;

import java.util.ArrayList;
import java.util.HashMap;

public class Element {
    private String name;
    private String image_path;
    private int width, height;
    private int pos_x, pos_y;
    private float rotation;
    private HashMap<Integer, ArrayList<Command>> frames;

    private HashMap<String, Attribute> attributes;

    private HashMap<String, String> children;

    private HashMap<String, Action> actions;

    @Override
    public String toString() {
        StringBuilder state = new StringBuilder();
        state.append("Element: " + name);

        return state.toString();
    }

    public Element(String name) {
        this.name = name;
        attributes = new HashMap<String, Attribute>();
        children = new HashMap<String, String>();
        actions = new HashMap<String, Action>();
        frames = new HashMap<Integer, ArrayList<Command>>();
    }

    public Element(Element old) {
        this.name = old.name;
        attributes = old.attributes;
        children = old.children;
        actions = old.actions;
        frames = old.frames;
    }

    public boolean addAttribute(Attribute attr) {
        this.attributes.put(attr.getName(), attr);
        return true;
    }

    public boolean addChild(Attribute child) {
        this.children.put(child.getName(), child.getType());
        return true; //TODO: verificar se não existe já a msm chave
    }

    public void addAction(Action action) {
        this.actions.put(action.name, action);
        //TODO: tratar actions duplicadas
    }

    public void addFrame(int frame, Command cmd) {
//        this.frames.put(frame, cmd); // TODO: deve verificar se o frame já foi declarado e adicionar na lista de funçoes
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

    public float getRotation() {
        return rotation;
    }

    public String getImage_path() {
        return this.attributes.get("image").getValue();
    }

    public HashMap<String, Action> getActions() {
        return actions;
    }
}
