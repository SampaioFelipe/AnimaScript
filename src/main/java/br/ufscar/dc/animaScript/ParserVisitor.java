package br.ufscar.dc.animaScript;

import java.util.ArrayList;
import java.util.List;

import br.ufscar.dc.animaScript.animation.Action;
import br.ufscar.dc.animaScript.animation.Animation;
import br.ufscar.dc.animaScript.animation.Attribute;
import br.ufscar.dc.animaScript.animation.Command;
import br.ufscar.dc.animaScript.animation.Element;
import org.antlr.v4.runtime.Token;

public class ParserVisitor extends AnimaScriptBaseVisitor<Object> {

    private Animation animation;
    private Element cur_element;

    private List<String> local_variables;

    public ParserVisitor(Animation animation) {
        super();

        local_variables = new ArrayList<String>();

        this.animation = animation;
    }

    @Override
    public Object visitDecl_global(AnimaScriptParser.Decl_globalContext ctx) {

        if (ctx.GLOBAL_ATTR() != null) {

            for (int i = 0; i < ctx.GLOBAL_ATTR().size(); i++) {
                Attribute attr = new Attribute(ctx.GLOBAL_ATTR().get(i).getText(), ctx.value().get(i).getText());
                animation.addGlobalAttr(attr);
            }
        }

        return null;
    }

    @Override
    public Object visitTime(AnimaScriptParser.TimeContext ctx) {
        if (ctx.NUM_INT() != null) {
            return ctx.NUM_INT().getText();
        } else {
            return String.valueOf(animation.horas2frames(ctx.HOUR_FORMAT().getText(), ctx.HOUR_FORMAT().getSymbol().getLine()));
        }
    }

    @Override
    public Object visitComposition(AnimaScriptParser.CompositionContext ctx) {
        for (AnimaScriptParser.Decl_attr_compContext attr : ctx.attrs) {

            Attribute attribute = (Attribute) visitDecl_attr_comp(attr);

            animation.getComposition().addAttribute(attribute);
        }

        return null;
    }

    @Override
    public Object visitDecl_attr(AnimaScriptParser.Decl_attrContext ctx) {
        String attr = ctx.attr().getText();
        String value = (String) visitValue(ctx.value());

        return new Attribute(attr, value);
    }

    @Override
    public Object visitValue(AnimaScriptParser.ValueContext ctx) {
        super.visitValue(ctx);
        return ctx.getText();
    }

    @Override
    public Object visitDecl_attr_comp(AnimaScriptParser.Decl_attr_compContext ctx) {

        if (ctx.value().time() != null) {
            return new Attribute(ctx.attr().getText(), (String) visitTime(ctx.value().time()));
        } else {
            return new Attribute(ctx.attr().getText(), ctx.value().getText());
        }
    }

    @Override
    public Object visitDecl_action(AnimaScriptParser.Decl_actionContext ctx) {

        ArrayList<String> params = new ArrayList<String>();

        for (Token param : ctx.params) {
            params.add(param.getText());
        }

        Action action = new Action(ctx.name.getText(), params);

        local_variables.addAll(action.getParams());

        for (AnimaScriptParser.CommandContext cmd : ctx.command()) {
            action.addCommand((Command) visitCommand(cmd));
        }

        local_variables.removeAll(action.getParams());

        return action;
    }

    @Override
    public Object visitExpr(AnimaScriptParser.ExprContext ctx) {
        if (ctx.attr() != null) {

            List<String> idents = new ArrayList<String>();

            for (Token ident : ctx.attr().idents) {
                idents.add(ident.getText());
            }

            if(!local_variables.contains(idents.get(0))){

                Element element_aux;

                if (idents.get(0).equals("this")) {
                    if (cur_element == null) {
                        Main.out.printErro(ctx.start.getLine(), "'this' nao permitido neste escopo");
                        return ctx.getText();
                    }

                    element_aux = cur_element;

                } else {
                    if (animation.getInst_element().containsKey(idents.get(0))) {
                        element_aux = animation.getInst_element().get(idents.get(0));
                    } else {
                        Main.out.printErro(ctx.start.getLine(), idents.get(0) + " nao declarado");
                        return ctx.getText();
                    }
                }

                if(!element_aux.verifyAttr(idents.subList(1, idents.size()))){
                    Main.out.printErro(ctx.start.getLine(), " expressao invalida ");
                }
            }
        }

        return super.visitExpr(ctx);
    }

    @Override
    public Object visitDecl_element(AnimaScriptParser.Decl_elementContext ctx) {
        Element element = new Element(ctx.IDENT_DECL_ELEMENT().getText());

        cur_element = element;

        for (AnimaScriptParser.Decl_attrContext decl_attr : ctx.decl_attr()) {
            Attribute attribute = (Attribute) visitDecl_attr(decl_attr);
            element.addAttribute(attribute);
        }

        if (element.getImage_path() == null) {
            Main.out.printErro(ctx.IDENT_DECL_ELEMENT().getSymbol().getLine(),
                    element.getName() + " nao possui o atributo \'image\'");
        }

        for (AnimaScriptParser.Decl_actionContext decl_action : ctx.decl_action()) {
            Action action = (Action) visitDecl_action(decl_action);

            if(action.getName().equals("init")) {
                for(Command cmd : action.getCommands()){
                    String[] ident = cmd.getIdentifier().split("\\.");
                    if(ident[0].equals("this")){
                        System.out.println(ident[1]);
                        element.getAttributes().remove(ident[1]);
                        System.out.println(element.getAttributes());
                    }
                }
            }

            element.addAction(action);
        }

        for (AnimaScriptParser.Element_instanceContext instanceContext : ctx.element_instance()) {
            Command childElement = (Command) visitElement_instance(instanceContext);
            element.addChild(childElement);
        }

        animation.addDeclElement(element);

        cur_element = null;

        return null;
    }

    @Override
    public Object visitScene(AnimaScriptParser.SceneContext ctx) {

        for (AnimaScriptParser.Element_instanceContext instanceContext : ctx.element_instance()) {
            Command element = (Command) visitElement_instance(instanceContext);
            if (element != null) {

                animation.addInstElement(element.getOp(),
                        element, instanceContext.start.getLine());
            }
        }

        return null;
    }

    @Override
    public Object visitElement_instance(AnimaScriptParser.Element_instanceContext ctx) {

        ArrayList<String> params = new ArrayList<String>();

        for (AnimaScriptParser.ExprContext expr : ctx.params) {
            params.add(expr.getText());
        }

        Command instance = new Command();
        instance.buildAction(ctx.IDENT_DECL_ELEMENT().getText(), ctx.name.getText(), params);

        return instance;
    }

    @Override
    public Object visitKeyframe(AnimaScriptParser.KeyframeContext ctx) {

        ArrayList<Command> cmds = new ArrayList<Command>();

        for (AnimaScriptParser.CommandContext cmdContext : ctx.cmds) {

            cmds.add((Command) visitCommand(cmdContext));
        }

        animation.addFrame((String) visitTime(ctx.time()), cmds, ctx.getStart().getLine());

        return null;
    }

    @Override
    public Object visitCommand(AnimaScriptParser.CommandContext ctx) {
        if (ctx.decl_attr() != null) {

            Command cmd = new Command();


            if (ctx.decl_attr().OP_ATTRIB() != null) {
                visitDecl_attr(ctx.decl_attr());
                cmd.buildAttribute(ctx.decl_attr().attr().getText(),
                        ctx.decl_attr().OP_ATTRIB().getText(),
                        ctx.decl_attr().value().getText());
            } else {
                cmd.buildAttribute(ctx.decl_attr().attr().getText(),
                        ctx.decl_attr().OP_ATTRIB2().getText(),
                        ctx.decl_attr().value().getText());
            }
            return cmd;
        } else {

            return visitAction_call(ctx.action_call());
        }
    }

    @Override
    public Object visitAction_call(AnimaScriptParser.Action_callContext ctx) {
        Command cmd = new Command();
        ArrayList<String> params = new ArrayList<String>();

        for (AnimaScriptParser.ExprContext expr : ctx.params) {
            params.add(expr.getText());
        }

        //TODO: tratar o número de parametros

        cmd.buildAction(ctx.OP_ACTION().getText(),
                ctx.attr().getText(), params);

        return cmd;
    }
}
