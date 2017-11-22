package br.ufscar.dc.animaScript.animation;

import java.util.ArrayList;
import java.util.HashMap;

public class Element {
    private String name;
    private String image_path;
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
        children = new HashMap<String, String>();
        actions = new HashMap<String, Action>();
        frames = new HashMap<Integer, ArrayList<Command>>();

        attributes = new HashMap<String, Attribute>();
        // Definição dos atributos padrões
        attributes.put("x", new Attribute("x", "0"));
        attributes.put("y", new Attribute("y", "0"));
        attributes.put("rotation", new Attribute("rotation", "0"));

        attributes.put("width", new Attribute("width", "200")); // TODO: qual valor default?
        attributes.put("height", new Attribute("height", "200")); // TODO: qual valor default?
    }

    public Element(Element old) {
        this.name = old.name;
//        attributes = (HashMap<String, Attribute>) old.attributes.clone();

        attributes = new HashMap<String, Attribute>();
        // Definição dos atributos padrões
        attributes.put("x", new Attribute("x", "0"));
        attributes.put("y", new Attribute("y", "0"));
        attributes.put("rotation", new Attribute("rotation", "0"));

        attributes.put("width", new Attribute("width", "200")); // TODO: qual valor default?
        attributes.put("height", new Attribute("height", "200")); // TODO: qual valor default?

        children = (HashMap<String, String>) old.children.clone();
        actions =  old.actions;
        frames = new HashMap<Integer, ArrayList<Command>>();
    }

    public boolean addAttribute(Attribute attr) {
        if (attr.getName().equals("image")) {
            setImage_path(attr.getValue());
        } else {
            if (this.attributes.containsKey(attr.getName())) {
                Attribute attribute = this.attributes.get(attr.getName());
                attribute.setValue(attr.getValue());
            } else {
                this.attributes.put(attr.getName(), attr);
            }
        }

        return true; // TODO: verificar casos em que isso dá errado
    }

    public boolean changeAttribute(String name, String value) {

        if (this.attributes.containsKey(name)) {
            Attribute attr = this.attributes.get(name);
            attr.setValue(value);
            return true;
        }

        return false;
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
        if (this.frames.containsKey(frame)) {
            ArrayList<Command> cmds = this.frames.get(frame);
            cmds.add(cmd);
        } else {
            ArrayList<Command> cmds = new ArrayList<Command>();
            cmds.add(cmd);
            this.frames.put(frame, cmds);
        }
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getName() {
        return name;
    }

    public int getHeight() {
        return Integer.decode(this.attributes.get("height").getValue());
    }

    public int getWidth() {
        return Integer.decode(this.attributes.get("width").getValue());
    }

    public float getRotation() {
        return Float.parseFloat(this.attributes.get("rotation").getValue());
    }

    public String getImage_path() {
        return this.image_path;
    }

    public HashMap<String, Action> getActions() {
        return actions;
    }

    public HashMap<String, Attribute> getAttributes() {
        return attributes;
    }

    public HashMap<Integer, ArrayList<Command>> getFrames() {
        return frames;
    }
}
