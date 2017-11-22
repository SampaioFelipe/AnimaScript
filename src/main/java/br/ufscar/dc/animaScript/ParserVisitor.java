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
    public Object visitProgram(AnimaScriptParser.ProgramContext ctx) {
        return super.visitProgram(ctx);
    }

    @Override
    public Object visitDecl_global(AnimaScriptParser.Decl_globalContext ctx) {

        if (ctx.GLOBAL_ATTR() != null) {

            for (int i = 0; i < ctx.GLOBAL_ATTR().size(); i++) {
                //TODO: pegar de forma correta o valor, verificando tipo e atribuição
                Attribute attr = new Attribute(ctx.GLOBAL_ATTR().get(i).getText(), ctx.value().get(i).getText());

                animation.addGlobalAttr(attr);
            }
        }

        return super.visitDecl_global(ctx);
    }

    @Override
    public Object visitComposition(AnimaScriptParser.CompositionContext ctx) {
        for (AnimaScriptParser.Decl_attrContext attr : ctx.attrs) {
            Attribute attribute = (Attribute) visitDecl_attr(attr);

            animation.getComposition().addAttribute(attribute);
        }
        return super.visitComposition(ctx);
    }

    @Override
    public Object visitDecl_attr(AnimaScriptParser.Decl_attrContext ctx) {

        //TODO: temos que ver o tipo de atribuicao, o que fazer com ela?

        return new Attribute(ctx.attr().getText(), ctx.value().getText());
    }

    @Override
    public Object visitDecl_action(AnimaScriptParser.Decl_actionContext ctx) {

        Action action = new Action(ctx.name.getText());

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
            System.out.println(action);
            element.addAction(action);
        }

        for (AnimaScriptParser.Element_instanceContext instanceContext : ctx.element_instance()) {
            ArrayList<Attribute> elements = (ArrayList<Attribute>) visitElement_instance(instanceContext);

            for(Attribute childElement : elements) {
                element.addChild(childElement);
            }
        }

        animation.addDeclElement(element);

        return super.visitDecl_element(ctx);
    }

    @Override
    public Object visitScene(AnimaScriptParser.SceneContext ctx) {

        for (AnimaScriptParser.Element_instanceContext instanceContext : ctx.element_instance()) {
            ArrayList<Attribute> elements = (ArrayList<Attribute>) visitElement_instance(instanceContext);

            for(Attribute childElement : elements) {
                System.out.println(childElement.getName());
                animation.addInstElement(childElement.getType(), childElement.getName());
            }
        }
        // TODO: processar as decl_attr e atribuir ao objeto certo

        for(AnimaScriptParser.Decl_attrContext attrContext : ctx.decl_attr()) {
            Attribute att = (Attribute) visitDecl_attr(attrContext);

            System.out.println(att.getName());
        }

        return super.visitScene(ctx);
    }

    @Override
    public Object visitElement_instance(AnimaScriptParser.Element_instanceContext ctx) {
        ArrayList<Attribute> elements = new ArrayList<Attribute>();

        String elementType = ctx.IDENT_DECL_ELEMENT().getText();
        // TODO: Verificar se já foi declarado antes o tipo

        for (Token ident : ctx.idents) {
            Attribute attr = new Attribute(ident.getText());
            attr.setType(elementType);
            elements.add(attr);
            //TODO: lidar com os possíveis erros ()
        }

        return elements;
    }

    @Override
    public Object visitKeyframe(AnimaScriptParser.KeyframeContext ctx) {

        ArrayList<Command> cmds = new ArrayList<Command>();

        for (AnimaScriptParser.CommandContext cmdContext : ctx.cmds) {

            cmds.add((Command) visitCommand(cmdContext));
        }

        animation.addFrame(ctx.time().getText(), cmds); // TODO: tratar conversão de frames

        return super.visitKeyframe(ctx);
    }

    @Override
    public Object visitCommand(AnimaScriptParser.CommandContext ctx) {
        Command cmd = new Command();
        if (ctx.decl_attr() != null) {

            cmd.buildAttribute(ctx.decl_attr().attr().getText(),
                    ctx.decl_attr().OP_ATTRIB().getText(),
                    ctx.decl_attr().value().getText()); // TODO: verificar tipo da operação
        } else {
            cmd.buildAction(ctx.action_call().OP_ACTION().getText(),
                    ctx.action_call().attr().getText(), ""); //TODO: tratar os parametros
        }

        return cmd;
    }
}
