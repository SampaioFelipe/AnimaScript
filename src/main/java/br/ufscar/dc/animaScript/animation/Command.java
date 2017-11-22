package br.ufscar.dc.animaScript.animation;

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

    public String getIdentifier() {
        return identifier;
    }

    public String getValue_or_params() {
        return value_or_params;
    }

    public String getOp() {
        return op;
    }

    public boolean isAttribute() {
        return this.type == TYPE.attr;
    }

    public boolean isCallAction() {
        return this.type == TYPE.action;
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
