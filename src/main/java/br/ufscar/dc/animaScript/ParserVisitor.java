package br.ufscar.dc.animaScript;

import java.util.ArrayList;

import br.ufscar.dc.animaScript.animation.Action;
import br.ufscar.dc.animaScript.animation.Animation;
import br.ufscar.dc.animaScript.animation.Attribute;
import br.ufscar.dc.animaScript.animation.Command;
import br.ufscar.dc.animaScript.animation.Element;
import org.antlr.v4.runtime.Token;

public class ParserVisitor extends AnimaScriptBaseVisitor<Object> {

    private Animation animation;

    public ParserVisitor(Animation animation) {
        super();

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

        //TODO: temos que ver o tipo de atribuicao, o que fazer com ela?

        return new Attribute(ctx.attr().getText(), ctx.value().getText());
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

        System.out.println(action.getParams());

        for (AnimaScriptParser.CommandContext cmd : ctx.command()) {
            action.addCommand((Command) visitCommand(cmd));
        }

        return action;
    }

    @Override
    public Object visitDecl_element(AnimaScriptParser.Decl_elementContext ctx) {
        Element element = new Element(ctx.IDENT_DECL_ELEMENT().getText());

        for (AnimaScriptParser.Decl_attrContext decl_attr : ctx.decl_attr()) {
            Attribute attribute = (Attribute) visitDecl_attr(decl_attr);
            element.addAttribute(attribute);
        }

        for (AnimaScriptParser.Decl_actionContext decl_action : ctx.decl_action()) {
            Action action = (Action) visitDecl_action(decl_action);
            System.out.println("-----------");
            System.out.println(action.getParams());

            element.addAction(action);
        }

        for (AnimaScriptParser.Element_instanceContext instanceContext : ctx.element_instance()) {
            Command childElement = (Command) visitElement_instance(instanceContext);
            element.addChild(childElement);
        }

        animation.addDeclElement(element);

        return null;
    }

    @Override
    public Object visitScene(AnimaScriptParser.SceneContext ctx) {

        for (AnimaScriptParser.Element_instanceContext instanceContext : ctx.element_instance()) {
            Command element = (Command) visitElement_instance(instanceContext);
            if (element != null) {
                System.out.println("ahhhhhhhhhhhhhhhhhhhh");
                System.out.println(element.getParams());

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
                cmd.buildAttribute(ctx.decl_attr().attr().getText(),
                        ctx.decl_attr().OP_ATTRIB().getText(),
                        ctx.decl_attr().value().getText()); // TODO: verificar tipo da operação
            } else {
                cmd.buildAttribute(ctx.decl_attr().attr().getText(),
                        ctx.decl_attr().OP_ATTRIB2().getText(),
                        ctx.decl_attr().value().getText()); // TODO: verificar tipo da operação
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
