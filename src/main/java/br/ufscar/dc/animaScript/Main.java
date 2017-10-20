package br.ufscar.dc.animaScript;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;

import br.ufscar.dc.animaScript.utils.ErrorParserListener;
import br.ufscar.dc.animaScript.utils.ResultadoParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

public class Main {

    public static ResultadoParser saida = new ResultadoParser();

    public static void main(String[] args) {
        // Verifica se ha argumentos suficientes para a execucao do compilador
        if (args == null || args.length < 2) {
            System.out.println("USO: java -jar anima " +
                    "'arquivo a ser compilado' " +
                    "'nome do arquivo destino'");
        } else {

            try {
                // Abre o arquivo com o codigo a ser compilado
                ANTLRInputStream input = new ANTLRInputStream(new FileInputStream(args[0]));

                // Inicializa a estrutura de tokenizacao do antlr
                AnimaScriptLexer lexer = new AnimaScriptLexer(input);

                // Inicializa o analisador lexico/sintatico
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                AnimaScriptParser parser = new AnimaScriptParser(tokens);

                // Adiciona estrutra para a manipulação de erros sintaticos
                parser.addErrorListener(new ErrorParserListener(saida));

                // Realizacao da analise e geracao da arvore sintatica
                AnimaScriptParser.ProgramaContext arvoreSintatica = parser.programa();

                // Se não houve nenhum erro léxico ou sintático, prossegue para a analise semântica
                if (!saida.isModificado()) {
                    AnalisadorSemanticoVisitor analisadorSemantico = new AnalisadorSemanticoVisitor();
                    analisadorSemantico.visitPrograma(arvoreSintatica);

                } else {
                    // Se houve algum erro lexico ele estara em lexicalErro
//                    if (lexicalError != null) {
//                        out.conteudo = new StringBuffer();
//                        out.println(lexicalError);
//                    }
                }
            } catch (FileNotFoundException pce) {
                if (pce.getMessage() != null) {
                    saida.printErro(0, pce.getMessage());
                }
            } catch (Exception e){

            }

            // Grava no arquivo de saida o resultado da compilacao
//            File saidaCasoTeste = new File(args[1]);
//            saidaCasoTeste.createNewFile();
//            PrintWriter pw = new PrintWriter(new FileWriter(saidaCasoTeste));
//
//            pw.print(out);
//            pw.flush();
//            pw.close();
        }
    }
}
