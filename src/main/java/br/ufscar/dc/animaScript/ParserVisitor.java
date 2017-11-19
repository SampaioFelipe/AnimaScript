package br.ufscar.dc.animaScript;

import java.util.ArrayList;

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
                //TODO: pegar de forma correta o valor
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
    public Object visitDecl_element(AnimaScriptParser.Decl_elementContext ctx) {
        Element element = new Element(ctx.IDENT_DECL_ELEMENT().getText());

        for (AnimaScriptParser.Decl_attrContext decl_attr : ctx.decl_attr()) {
            Attribute attribute = (Attribute) visitDecl_attr(decl_attr);
            System.out.println(attribute.getName());
            element.addAttribute(attribute);
        }

        for (AnimaScriptParser.Decl_actionContext decl_action : ctx.decl_action()) {
            System.out.println(decl_action.getText());
        }

        for (AnimaScriptParser.Element_instanceContext element_instance : ctx.element_instance()) {
            System.out.println(element_instance.getText());
        }

        animation.addDeclElement(element);

        return super.visitDecl_element(ctx);
    }

    @Override
    public Object visitElement_instance(AnimaScriptParser.Element_instanceContext ctx) {

        String elementType = ctx.IDENT_DECL_ELEMENT().getText();
        String elementName;
        for (Token ident : ctx.idents) {
            elementName = ident.getText();
            //TODO: lidar com os possíveis erros
            animation.addInstElement(elementType, elementName);
        }

        return super.visitElement_instance(ctx);
    }

    @Override
    public Object visitKeyframe(AnimaScriptParser.KeyframeContext ctx) {

        ArrayList<Command> cmds = new ArrayList<Command>();

        for (AnimaScriptParser.CommandContext cmdContext : ctx.cmds) {
            Command cmd = new Command();
            if (cmdContext.decl_attr() != null) {

                cmd.buildAttribute(cmdContext.decl_attr().attr().getText(),
                        cmdContext.decl_attr().OP_ATTRIB().getText(),
                        cmdContext.decl_attr().value().getText()); // TODO: verificar tipo da operação
            } else {
                cmd.buildAction(cmdContext.action_call().OP_ACTION().getText(),
                        cmdContext.action_call().attr().getText(), ""); //TODO: tratar os parametros
            }

            cmds.add(cmd);
        }

        animation.addFrame(ctx.time().getText(), cmds); // TODO: tratar conversão de frames

        return super.visitKeyframe(ctx);
    }

    @Override
    public Object visitCommand(AnimaScriptParser.CommandContext ctx) {
        return super.visitCommand(ctx);
    }
}
