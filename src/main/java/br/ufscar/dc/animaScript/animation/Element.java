package br.ufscar.dc.animaScript.animation;

import java.util.ArrayList;
import java.util.HashMap;

public class Element {
    private String name;
    private String image_path;
    private HashMap<Integer, ArrayList<Command>> frames;

    private HashMap<String, Attribute> attributes;

    private HashMap<String, Command> children;

    private HashMap<String, Action> actions;

    private int numberParamsConstructor;

    private String initParams;

    @Override
    public String toString() {
        StringBuilder state = new StringBuilder();
        state.append("Element: " + name);

        return state.toString();
    }

    public Element(String name) {
        this.name = name;
        children = new HashMap<String, Command>();
        actions = new HashMap<String, Action>();
        frames = new HashMap<Integer, ArrayList<Command>>();

        attributes = new HashMap<String, Attribute>();

        this.numberParamsConstructor = 0;
        this.initParams = "";
    }

    public Element(Element old) {
        this.name = old.name;

        attributes = new HashMap<String, Attribute>();
        // Definição dos atributos padrões
        attributes = (HashMap<String, Attribute>) old.attributes.clone();
        children = (HashMap<String, Command>) old.children.clone();

        actions = old.actions;
        frames = new HashMap<Integer, ArrayList<Command>>();
        this.numberParamsConstructor = old.numberParamsConstructor;
        this.initParams = old.initParams;
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

    public boolean setInitParams(ArrayList<String> params) {
        if (params.size() == this.numberParamsConstructor) {
            this.initParams = params.get(0);

            for (int i = 1; i < params.size(); i++) {
                this.initParams += "," + params.get(i);
            }

            return true;
        }

        return false;
    }

    public void setInitParams(String params) {
        this.initParams = params;
    }

    public String getInitParams() {
        return initParams;
    }

    public boolean addChild(Command child) {

        if (!this.children.containsKey(child.getIdentifier())) {
            this.children.put(child.getIdentifier(), child);
            return true;
        }

        return false;
    }

    public boolean addAction(Action action) {

        if (!this.getActions().containsKey(action.getName())) {

            if (action.getName().equals("init")) {
                this.numberParamsConstructor = action.getNumberParams();
                this.initParams = action.getParams();
            }

            this.actions.put(action.getName(), action);

            return true;
        }

        return false;
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

    public HashMap<String, Command> getChildren() {
        return children;
    }

    public int getNumberParamsConstructor() {
        return numberParamsConstructor;
    }
}
