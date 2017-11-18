package br.ufscar.dc.animaScript.animation;

import java.util.HashMap;

import br.ufscar.dc.animaScript.utils.AttributeList;

public class Animation {

    private Composition composition;
    private AttributeList globals;
    private HashMap<String, Element> elements;

    public Animation() {
        this.globals = new AttributeList();
        this.composition = new Composition();
        this.elements = new HashMap<String, Element>();
    }

    public boolean addGlobalAttr(Attribute attr) {
        if(attr.getName().startsWith("@")) {
            attr.setName(attr.getName().substring(1));

            if(!globals.contains(attr.getName())){
                globals.add(attr);
                return true;
            } else {
                Attribute attribute = globals.get(attr.getName());
                attribute.setValue(attr.getValue());

                System.err.println("Redefinição de atributo");
                //TODO: relatar redefinição
            }
        }

        return false;
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

    public boolean addElement(Element element) {
        if(!this.elements.containsKey(element.getName())) {
            this.elements.put(element.getName(), element);
            return true;
        }

        return false;
    }
}
