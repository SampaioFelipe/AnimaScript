package br.ufscar.dc.animaScript;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import br.ufscar.dc.animaScript.AnimaScriptParser;
import br.ufscar.dc.animaScript.AnimaScriptLexer;
import br.ufscar.dc.animaScript.animation.Animation;
import br.ufscar.dc.animaScript.animation.Composition;
import br.ufscar.dc.animaScript.utils.ErrorParserListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

public class Main {

    public static FinalCode out = new FinalCode();

    public static void main(String[] args) {

        if (validateArgs(args)) {
            try {
                // Abre o arquivo com o codigo a ser compilado
                ANTLRInputStream input = new ANTLRInputStream(new FileInputStream(out.getInputName()));

                // Inicializa a estrutura de tokenizacao do antlr
                AnimaScriptLexer lexer = new AnimaScriptLexer(input);

                // Inicializa o analisador lexico/sintatico
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                AnimaScriptParser parser = new AnimaScriptParser(tokens);

//                parser.removeErrorListeners();
                // Adiciona estrutra para a manipulação de erros sintaticos
                parser.addErrorListener(new ErrorParserListener(out));

                // Realizacao da analise e geracao da arvore sintatica
                AnimaScriptParser.ProgramContext parserTree = parser.program();

                // Se não houve nenhum erro léxico ou sintático, prossegue para a analise semântica
                if (!out.isChanged()) {
                    Animation animation = new Animation();
                    ParserVisitor parserVisitor = new ParserVisitor(animation);

                    parserVisitor.visitProgram(parserTree);

                    // Se houve erro semantico o processo e terminado e reportado o erro
                    if (!out.isChanged()) {
                        CodeGenerator codeGenerator = new CodeGenerator(animation);
                        codeGenerator.geraJs();
                    }
                }
            } catch (FileNotFoundException pce) {
                if (pce.getMessage() != null) {
                    out.printErro(0, pce.getMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            out.generate();

        } else {
            printHelp();
        }
    }

    public static void printHelp() {
        String texto = "Uso: java -jar anima [OPTIONS] fileA fileB\n" +
                "Compila código em animaScript...\n" +
                "Options:\n" +
                "-v...  ";

        System.out.println(texto);
    }

    public static boolean validateArgs(String[] args) {
        if (args == null || args.length < 2) {
            return false;
        }

        String arg = args[0];
        int i = 1;
        while (arg.startsWith("-")) {
            if (arg.equalsIgnoreCase("-v")) {

            } else {
                return false;
            }

            arg = args[i++];
        }

        if ((args.length != i + 1)) {
            return false;
        }

        out.setInputName(args[i - 1]);
        out.setOutputFolderName(args[i]);

        return true;
    }
}
