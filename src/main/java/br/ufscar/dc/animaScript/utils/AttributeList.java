package br.ufscar.dc.animaScript.utils;

import java.util.ArrayList;

import br.ufscar.dc.animaScript.animation.Attribute;

public class AttributeList extends ArrayList<Attribute> {

    public boolean contains(String name) {
        for(int i = 0; i < super.size(); i++) {
            if(super.get(i).getName().equals(name)) {
                return true;
            }
        }

        return false;
    }

    public Attribute get(String name) {
        for(int i = 0; i < super.size(); i++) {
            if(super.get(i).getName().equals(name)) {
                return super.get(i);
            }
        }

        return null;
    }
}
