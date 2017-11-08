package br.ufscar.dc.animaScript;

import br.ufscar.dc.animaScript.animacao.Composicao;

public class GeradorCodigo {
    private Composicao composicao;
    public GeradorCodigo(Composicao composicao){
        this.composicao = composicao;

        StringBuffer html = new StringBuffer();

        html.append("<html>\n" +
                    "<head>\n" +
                    "<meta charset=\"utf-8\">");

        html.append("<title>Main Animation</title>");
        html.append("  <style media=\"screen\">\n" +
                "    .wrapper {\n" +
                "      padding: 0;\n" +
                "      margin: auto;\n" +
                "      display: block;\n" +
                "      width: 500px;\n" +
                "      height: 500px;\n" +
                "      position: absolute;\n" +
                "      top: 0;\n" +
                "      bottom: 0;\n" +
                "      left: 0;\n" +
                "      right: 0;\n" +
                "    }\n" +
                "\n" +
                "    .controles {\n" +
                "      \n" +
                "      width: 500px;\n" +
                "      background-color: #333;\n" +
                "    }\n" +
                "  </style>");

        html.append("</head>");

        html.append("<body>\n" +
                "  <div class=\"wrapper\">\n" +
                "    <canvas id=\"canvas\"></canvas>\n" +
                "    <div class=\"controles\">\n" +
                "      <button id=\"play\" type=\"button\" onclick=\"play()\">Play</button>\n" +
                "      <button id=\"play\" type=\"button\" onclick=\"pause()\">Pause</button>\n" +
                "      <button id=\"play\" type=\"button\" onclick=\"stop()\">Stop</button>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "  <script src=\"code.js\"></script>\n" +
                "</body>");
        html.append("</html>");

        Main.saida.printHtml(html.toString());

        System.out.println(composicao.getWidth());
    }
}
