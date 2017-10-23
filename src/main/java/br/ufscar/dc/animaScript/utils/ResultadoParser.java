package br.ufscar.dc.animaScript.utils;

public class ResultadoParser {
    private String nomeEntrada;
    private String nomeSaida;

    // Resultado das análises, se for vazio significa que não houveram erros
    private StringBuffer resultadoAnalise;
    // Caso haja erro, modificado se torna true e resultado análise conterá o erro
    private boolean modificado;

    // Conterá o conteúdo do código gerado
    private StringBuffer saidaHtml;
    private StringBuffer saidaJs;


    public ResultadoParser() {
        resultadoAnalise = new StringBuffer();
        modificado = false;

        saidaHtml = new StringBuffer();
        saidaJs = new StringBuffer();
    }

    public String getNomeEntrada() {
        return nomeEntrada;
    }

    public String getNomeSaida() {
        return nomeSaida;
    }

    public StringBuffer getSaidaHtml() {
        return saidaHtml;
    }

    public StringBuffer getSaidaJs() {
        return saidaJs;
    }

    public void setNomeEntrada(String arqEntrada) {
        this.nomeEntrada = arqEntrada;
    }

    public void setNomeSaida(String arqSaida) {
        this.nomeSaida = arqSaida;
    }

    public void printErro(int linha, String erro) {
        if (!modificado)
            modificado = true;
        resultadoAnalise.append("Linha " + String.valueOf(linha) + ": ");
        resultadoAnalise.append(erro);
        resultadoAnalise.append("\n");
    }

    public boolean isModificado() {
        return modificado;
    }

    public void printHtml(String codigo) {
        saidaHtml.append(codigo);
        saidaHtml.append("\n");
    }

    public void printJS(String codigo) {
        saidaJs.append(codigo);
        saidaJs.append("\n");
    }

    @Override
    public String toString() {
        String tostring = "Resultado da Analise:\n" + resultadoAnalise;
        tostring += "\nHtml:\n" + saidaHtml;
        tostring += "\nJavaScript:\n" + saidaJs;

        return tostring;
    }
}
