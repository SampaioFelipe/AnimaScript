package br.ufscar.dc.animaScript.animation;

import java.util.ArrayList;

public class Command {

    enum TYPE {
        attr, action
    }

    private TYPE type;

    private String identifier;

    private String value;

    private String params;

    private int numberParams;

    private String op;

    @Override
    public String toString() {
        StringBuilder state = new StringBuilder();

        if (this.type == TYPE.attr) {
            state.append(identifier + " " + op + " " + value);
        } else {
            state.append(op + " " + identifier + "("+ params +")");
        }

        return state.toString();
    }

    public Command(){
        this.params = "";
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getValue() {
        return this.value;
    }

    public String getParams(){
        return this.params;
    }

    public String getOp() {
        return op;
    }

    public int getOpId(){
        if(this.op.equals("start")) {
            return 0;
        } else if(this.op.equals("stop")){
            return 1;
        } else {
            return 2;
        }
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public boolean isAttribute() {
        return this.type == TYPE.attr;
    }

    public boolean isCallAction() {
        return this.type == TYPE.action;
    }

    public void buildAction(String op, String identifier, ArrayList<String> params) {
        this.type = TYPE.action;
        this.op = op;
        this.identifier = identifier;

        this.setParams(params);
    }

    public void setParams(ArrayList<String> params){
        this.numberParams = params.size();

        if(this.numberParams > 0){
            this.params = params.get(0);

            for(int i = 1; i < this.numberParams; i++){
                this.params += "," + params.get(i);
            }
        }
    }

    public int getNumberParams() {
        return numberParams;
    }

    public void buildAttribute(String identifier, String op, String value) {
        this.type = TYPE.attr;
        this.identifier = identifier;
        this.op = op;
        this.value = value;
    }


}
