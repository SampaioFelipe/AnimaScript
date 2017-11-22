package br.ufscar.dc.animaScript.animation;

import java.util.ArrayList;

public class Action {
    String name;

    ArrayList<String> params;

    ArrayList<Command> commands;

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

    public Action(String name){
        this.name = name;
        params = new ArrayList<String>();
        commands = new ArrayList<Command>();
    }

    public boolean addParam(String param) {
        if(!params.contains(param)) {
            params.add(param);
            return true;
        }

        return false;
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
}
