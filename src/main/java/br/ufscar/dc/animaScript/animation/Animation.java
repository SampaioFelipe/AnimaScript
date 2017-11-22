package br.ufscar.dc.animaScript.animation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Animation {

    private Composition composition;
    private HashMap<String, Attribute> globals;

    private HashMap<String, Element> decl_elements;
    private HashMap<String, Element> inst_element;

    public Animation() {
        this.globals = new HashMap<String, Attribute>();
        this.composition = new Composition();

        this.decl_elements = new HashMap<String, Element>();
        this.inst_element = new HashMap<String, Element>();

    }

    @Override
    public String toString() {
        StringBuilder state = new StringBuilder();
        state.append("Animation:\n");

        for (Map.Entry<String, Attribute> attr : this.globals.entrySet()) {
            state.append(attr.getKey() + ": " + attr.getValue().getValue() + "\n");
        }

        state.append("\nComposition:\n" + this.composition.toString());

        state.append("\n\nElements:\n");

        for (Map.Entry<String, Element> element : this.decl_elements.entrySet()) {
            state.append(element.getValue().toString() + "\n");
        }

        state.append("\nScene:\n");

        for (Map.Entry<String, Element> element : this.inst_element.entrySet()) {
            state.append(element.getValue().getName() + " " + element.getKey() + "\n");
        }

        state.append("\nStoryboard\n");

//        for(Map.Entry<String, ArrayList<Command>> frame : this.frames.entrySet()) {
//            state.append(frame.getKey() + ":\n");
//
//            for(Command command: frame.getValue()){
//                state.append(command.toString() + "\n");
//            }
//        }

        return state.toString();
    }

    public boolean addGlobalAttr(Attribute attr) {
        if (attr.getName().startsWith("@")) {
            attr.setName(attr.getName().substring(1));

            if (!globals.containsKey(attr.getName())) {
                globals.put(attr.getName(), attr);
                return true;
            } else {
                Attribute attribute = globals.get(attr.getName());
                attribute.setValue(attr.getValue());

                System.err.println("Redefinição de atributo");
                //TODO: relatar redefinição (warnings)
            }
        }

        return false;
    }

    public boolean addDeclElement(Element element) {
        if (!this.decl_elements.containsKey(element.getName())) {
            this.decl_elements.put(element.getName(), element);
            return true;
        }

        return false;
    }

    public boolean addInstElement(String elementType, String name) {
        //TODO: tratar o caso em que não exite o tipo do elemento
        if (this.decl_elements.containsKey(elementType)) {
            // TODO: verificar se dois elementos com o msm nome
            Element newInstance = new Element(this.decl_elements.get(elementType));
            this.inst_element.put(name, newInstance);
            return true;
        }

        return false;
    }

    public boolean addFrame(String frame, ArrayList<Command> cmds) {
        for (Command cmd: cmds) {
            // TODO: tratar quando não acha o "."
            String objName = cmd.getIdentifier().split("\\.")[0];
            System.out.println("---------");
            System.out.println(objName);
            if (this.inst_element.containsKey(objName)) {
                Element obj = this.inst_element.get(objName);
                obj.addFrame(Integer.decode(frame.split("f")[0]), cmd);
            } else {
                return false; //TODO: tratar o caso em que não encontrou o elemnto declarado
            }
        }

        return true;
    }

    public String getTitle() {
        Attribute title = globals.get("animation");

        if (title != null) {
            return title.getValue();
        }

        return "AnimaScript Animation";
    }

    public Composition getComposition() {
        return composition;
    }

    public HashMap<String, Element> getDecl_elements() {
        return decl_elements;
    }

    public HashMap<String, Element> getInst_element() {
        return inst_element;
    }

    public boolean addElement(Element element) {
        if (!this.decl_elements.containsKey(element.getName())) {
            this.decl_elements.put(element.getName(), element);
            return true;
        }

        return false;
    }

    public Element getInstElement(String name) {
        return this.inst_element.get(name);
    }
}
