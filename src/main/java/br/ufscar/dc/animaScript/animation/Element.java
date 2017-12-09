package br.ufscar.dc.animaScript.animation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Element {

    public static HashMap<String, Element> decl_elements;

    private String name;
    private String image_path;
    private HashMap<Integer, ArrayList<Command>> frames;

    private HashMap<String, Attribute> attributes;

    private HashMap<String, Command> children;

    private HashMap<String, Action> actions;

    private List<String> initParams;

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

        attributes.put("x", new Attribute("x", "0"));
        attributes.put("y", new Attribute("y", "0"));
        attributes.put("width", new Attribute("width", "0"));
        attributes.put("height", new Attribute("height", "0"));
        attributes.put("rotation", new Attribute("rotation", "0"));

        this.initParams = new ArrayList<String>();
    }

    public Element(Element old) {
        this.name = old.name;

//        attributes = new HashMap<String, Attribute>();
        // Definição dos atributos padrões
        attributes = (HashMap<String, Attribute>) old.attributes.clone();
        children = (HashMap<String, Command>) old.children.clone();

        actions = old.actions;
        frames = new HashMap<Integer, ArrayList<Command>>();
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

    public boolean verifyAttr(List<String> attrs){
        String first = attrs.get(0);
        if(attributes.containsKey(first)){
            return true;
        }
        else if(children.containsKey(first)){
            return decl_elements.get(first).verifyAttr(attrs.subList(1, attrs.size()));
        }

        return false;
    }

    public void setInitParams(List<String> params) {
        this.initParams = params;
    }

    public List<String> getInitParams() {
        return initParams;
    }

    public String decodeInitParams(){
        String paramsString = "";
        if(this.initParams.size() > 0){
            paramsString = this.initParams.get(0);

            for(int i = 1; i < this.initParams.size(); i++){
                paramsString += "," + initParams.get(i);
            }
        }

        return paramsString;
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
        return initParams.size();
    }
}
