package br.ufscar.dc.animaScript;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import br.ufscar.dc.animaScript.utils.ErrorParserListener;
import br.ufscar.dc.animaScript.utils.ResultadoParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

public class Main {

    public static ResultadoParser saida = new ResultadoParser();

    public static void main(String[] args) {

         if (validateArgs(args)){
            try {
                // Abre o arquivo com o codigo a ser compilado
                ANTLRInputStream input = new ANTLRInputStream(new FileInputStream(saida.getNomeEntrada()));

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
             try {
                 File saidaCasoTeste = new File(saida.getNomeSaida());
                 saidaCasoTeste.createNewFile();
                 PrintWriter pw = new PrintWriter(new FileWriter(saidaCasoTeste));

                 pw.print(saida.getSaidaHtml());
                 pw.flush();
                 pw.close();
             } catch (IOException E){

             }

        } else {
             printHelp();
         }
    }

    public static void printHelp(){
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

        if((args.length != i + 1)) {
            return false;
        }

        saida.setNomeEntrada(args[i-1]);
        saida.setNomeSaida(args[i]);

        return true;
    }

    private static void inicializaArquivos(){

    }
}
