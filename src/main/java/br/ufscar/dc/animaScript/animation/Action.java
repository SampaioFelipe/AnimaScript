package br.ufscar.dc.animaScript.animation;

import java.util.ArrayList;
import java.util.List;

public class Action {
    String name;

    ArrayList<Command> commands;

    List<String> params;

    @Override
    public String toString() {
        StringBuilder state = new StringBuilder();

        state.append("action " + name + "{\n");
        for(Command cmd: commands) {
            state.append(cmd + "\n");
        }

        state.append("\n}");

        return state.toString();
    }

    public Action(String name, ArrayList<String> params){
        this.name = name;
        this.params = new ArrayList<String>();

        this.setParams(params);

        this.commands = new ArrayList<Command>();
    }

    public void addCommand(Command command) {
        commands.add(command);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }

    public int getNumberParams() {
        return this.params.size();
    }

    public void setParams(List<String> params){
        this.params.addAll(params);
    }

    public List<String> getParams() {
        return params;
    }

    public String decodeParams(){

        String paramsString = "";
        if(this.params.size() > 0){
            paramsString = this.params.get(0);

            for(int i = 1; i < this.params.size(); i++){
                paramsString += "," + params.get(i);
            }
        }

        return paramsString;
    }
}
