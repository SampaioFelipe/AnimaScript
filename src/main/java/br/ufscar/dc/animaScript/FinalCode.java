package br.ufscar.dc.animaScript;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FinalCode {
    private String inputName;
    private String outputFolderName;

    // Resultado das análises, se for vazio significa que não houveram erros
    private StringBuffer parserResult;
    // Caso haja erro, changed se torna true e resultado análise conterá o erro
    private boolean changed;

    // Conterá o conteúdo do código gerado
    private StringBuffer outputHtml;
    private StringBuffer outputJs;


    public FinalCode() {
        parserResult = new StringBuffer();
        changed = false;

        outputHtml = new StringBuffer();
        outputJs = new StringBuffer();
    }

    public String getInputName() {
        return inputName;
    }

    public String getOutputFolderName() {
        return outputFolderName;
    }

    public StringBuffer getOutputHtml() {
        return outputHtml;
    }

    public StringBuffer getOutputJs() {
        return outputJs;
    }

    public void setInputName(String arqEntrada) {
        this.inputName = arqEntrada;
    }

    public void setOutputFolderName(String arqSaida) {
        this.outputFolderName = arqSaida;
    }

    public void printErro(int linha, String erro) {
        if (!changed)
            changed = true;

        parserResult.append("Linha " + String.valueOf(linha) + ": ");
        parserResult.append(erro);
        parserResult.append("\n");
    }

    public boolean isChanged() {
        return changed;
    }

    public void printHtml(String codigo) {
        outputHtml.append(codigo);
        outputHtml.append("\n");
    }

    public void printJS(String codigo) {
        outputJs.append(codigo);
        outputJs.append("\n");
    }

    public void generate() {

        // Grava no arquivo de saida o resultado da compilacao
        try {
            if (isChanged()) {
                System.err.println(parserResult);
            } else {
                File file_html = new File(outputFolderName + "/main.html");
                file_html.getParentFile().mkdirs();
                file_html.createNewFile();
                PrintWriter pw = new PrintWriter(new FileWriter(file_html));
                pw.print(getOutputHtml());
                pw.flush();
                pw.close();

                File file_js = new File(outputFolderName + "/code.js");
                file_js.createNewFile();
                pw = new PrintWriter(new FileWriter(file_js));
                pw.print(getOutputJs());
                pw.flush();
                pw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        String tostring = "Resultado da Analise:\n" + parserResult;
        tostring += "\nHtml:\n" + outputHtml;
        tostring += "\nJavaScript:\n" + outputJs;

        return tostring;
    }
}
