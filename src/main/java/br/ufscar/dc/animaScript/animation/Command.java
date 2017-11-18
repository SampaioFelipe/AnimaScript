package br.ufscar.dc.animaScript.animation;

public class Command {
    private Attribute attr;
    private String operation;

    public Command(Attribute attr, String operation) {
        this.attr = attr;
        this.operation = operation;
    }

    public void setAttr(Attribute attr) {
        this.attr = attr;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getAttrName() {
        return attr.getName();
    }

    public String getAttrValue() {
        return attr.getValue();
    }

    public String getOperation() {
        return operation;
    }
}
