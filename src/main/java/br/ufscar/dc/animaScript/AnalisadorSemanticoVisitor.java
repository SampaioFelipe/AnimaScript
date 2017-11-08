package br.ufscar.dc.animaScript;

import br.ufscar.dc.animaScript.animacao.Composicao;

public class AnalisadorSemanticoVisitor extends AnimaScriptBaseVisitor <Object> {

    Composicao composicao;

    public AnalisadorSemanticoVisitor(Composicao composicao) {
        super();

        this.composicao = composicao;
    }

    @Override
    public Object visitPrograma(AnimaScriptParser.ProgramaContext ctx) {
        return super.visitPrograma(ctx);
    }

    @Override
    public Object visitComposition(AnimaScriptParser.CompositionContext ctx) {
        for(AnimaScriptParser.Decl_propContext decl : ctx.decls) {
            if (decl.prop.getText().equals("width")) {
                this.composicao.setWidth(Integer.decode(decl.valor().getText()));
            }
            System.out.println(decl.getText());
        }

            return super.visitComposition(ctx);
    }
}
