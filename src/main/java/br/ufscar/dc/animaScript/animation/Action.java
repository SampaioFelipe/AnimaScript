package br.ufscar.dc.animaScript.animation;

import java.util.ArrayList;

public class Action {
    String name;
    ArrayList<String> params;
    ArrayList<Command> commands; // TODO: como representar os comandos?

    public Action(String name){
        params = new ArrayList<String>();
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
}
