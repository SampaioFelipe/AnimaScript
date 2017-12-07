package br.ufscar.dc.animaScript.animation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.ufscar.dc.animaScript.Main;

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

                System.err.println("Redefinição de atributo global");
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

    public boolean addInstElement(String elementType, Command element, int line) {
        if (this.decl_elements.containsKey(elementType)) {

            Element newInstance = new Element(this.decl_elements.get(elementType));

            if (newInstance.getNumberParamsConstructor() == element.getNumberParams()) {

                newInstance.setInitParams(element.getParams());
                this.inst_element.put(element.getIdentifier(), newInstance);
                return true;
            } else {
                Main.out.printErro(line, "numero de parametros incompativel com o tipo de elemento");
            }
        } else {
            Main.out.printErro(line, "tipo \"" + elementType + "\" nao declarado");
        }
        return false;
    }

    public boolean addFrame(String frame, ArrayList<Command> cmds, int line) {
        for (Command cmd : cmds) {
            // TODO: tratar quando não acha o "."
            String[] identifiers = cmd.getIdentifier().split("\\.");
            if (this.inst_element.containsKey(identifiers[0])) {
                Element obj = this.inst_element.get(identifiers[0]);

                if((obj.getActions().containsKey(identifiers[1]) || obj.getAttributes().containsKey(identifiers[1]) || obj.getChildren().containsKey(identifiers[1]))){
                    cmd.setIdentifier(identifiers[1]);
                } else {
                    if(!identifiers[1].equals("x") && !identifiers[1].equals("y")) {
                        Main.out.printErro(line, identifiers[1] + " não declarado");
                    }
                }

                 // TODO: tratar aninhamento
                obj.addFrame(Integer.decode(frame), cmd);
            } else {
                Main.out.printErro(line, identifiers[0] + "nao declarado");
                return false;
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

    public HashMap<String, Attribute> getGlobals() {
        return globals;
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

    public int horas2frames(String horas, int linha) {

        int frames = 0;
        int fps = this.composition.getFPS();
        int valor;

        String msg_erro = "formato de tempo inválido";

        String[] componentes = horas.split("h");

        if (componentes.length > 1) {
            // TODO: definir limite de hora?
            valor = Integer.parseInt(componentes[0]);
            frames += fps * 360 * valor;
            horas = componentes[1];
        }

        componentes = horas.split("m");

        if (componentes.length > 1) {
            valor = Integer.parseInt(componentes[0]);

            if (valor > 59) {
                Main.out.printErro(linha, msg_erro);
                return 0;
            }

            frames += fps * 60 * valor;
            horas = componentes[1];
        }

        componentes = horas.split("s");

        if (componentes.length > 0) {
            valor = Integer.parseInt(componentes[0]);

            if (valor > 59) {
                Main.out.printErro(linha, msg_erro);
                return 0;
            }

            frames += fps * valor;
        }

        return frames;
    }
}
