package br.ufscar.dc.animaScript.animation;

import java.util.ArrayList;

public class Command {

    enum TYPE {
        attr, action
    }

    private TYPE type;

    private String identifier;

    private String value_or_params;

    private String op;

    @Override
    public String toString() {
        StringBuilder state = new StringBuilder();

        if (this.type == TYPE.attr) {
            state.append(identifier + " " + op + " " + value_or_params);
        } else {
            state.append(op + " " + identifier + "("+ value_or_params +")");
        }

        return state.toString();
    }

    public void buildAction(String op, String identifier, String params) {
        this.type = TYPE.action;
        this.op = op;
        this.identifier = identifier;
        this.value_or_params = params;
    }

    public void buildAttribute(String identifier, String op, String value) {
        this.type = TYPE.attr;
        this.identifier = identifier;
        this.op = op;
        this.value_or_params = value;
    }
}
