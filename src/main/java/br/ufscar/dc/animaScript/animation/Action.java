package br.ufscar.dc.animaScript.animation;

import java.util.ArrayList;

public class Action {
    String name;

    ArrayList<Command> commands;

    int numberParams;
    String params;

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
        this.setParams(params);
        this.commands = new ArrayList<Command>();
        this.setParams(params);
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
        return numberParams;
    }

    public void setParams(ArrayList<String> params){
        this.numberParams = params.size();

        if(this.numberParams > 0){
            this.params = params.get(0);

            for(int i = 1; i < this.numberParams; i++){
                this.params += "," + params.get(i);
            }
        } else {
            this.params = "";
        }
    }

    public String getParams() {
        return params;
    }
}
