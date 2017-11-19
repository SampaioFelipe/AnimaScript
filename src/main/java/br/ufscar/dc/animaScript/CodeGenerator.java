package br.ufscar.dc.animaScript;

import java.util.Map;

import br.ufscar.dc.animaScript.animation.Animation;
import br.ufscar.dc.animaScript.animation.Element;

public class CodeGenerator {

    private Animation animation;

    public CodeGenerator(Animation animation) {

        this.animation = animation;

        System.out.println(animation);

        StringBuffer html = new StringBuffer();

        html.append("<html>" +
                "<head>" +
                "<meta charset=\"utf-8\">");

        html.append("<title>" + animation.getTitle() + "</title>");

        html.append("<style media=\"screen\">" +
                ".wrapper {" +
                "padding: 0;" +
                "margin: auto;" +
                "display: block;" +
                "width: " + animation.getComposition().getWidth() + "px;" +
                "height: " + animation.getComposition().getHeight() + "px;" +
                "position: absolute;" +
                "top: 0;" +
                "bottom: 0;" +
                "left: 0;" +
                "right: 0;" +
                "}" +
                ".controles {" +
                "width: " + animation.getComposition().getWidth() + "px;" +
                "background-color: #333;" +
                "}</style></head>");

        html.append("<body>" +
                "<div class=\"wrapper\">" +
                "<canvas id=\"canvas\"></canvas>" +
                "<div class=\"controles\">" +
                "<button type=\"button\" onclick=\"play()\">Play</button>" +
                "<button type=\"button\" onclick=\"pause()\">Pause</button>" +
                "<button type=\"button\" onclick=\"stop()\">Stop</button>" +
                "</div>" +
                "</div>" +
                "<script src=\"code.js\"></script>" +
                "</body></html>");

        Main.out.printHtml(html.toString());

//        this.geraJs();
    }

    public void geraJs() {
        StringBuffer js = new StringBuffer();

        decodeComposition(js);

        decodedDeclElements(js);

        Main.out.printJS(js.toString());

        // Declaração dos elementos
    }

    private void decodedDeclElements(StringBuffer buffer) {

        buffer.append("// Declaração dos elementos\n" +
                "class Element {\n" +
                "  constructor() {\n" +
                "    // this.x = x;\n" +
                "    // this.y = y;\n" +
                "    this.frames = {};\n" +
                "    this.cur_actions = [];\n" +
                "  }\n" +
                "}\n");

        for (Element decl : animation.getDecl_elements().values()) {
            String image = decl.getName() + "_image";

            buffer.append("var " + image + " = new Image();\n");
            buffer.append(image + ".src = " + decl.getImage_path() + ";\n"); //TODO: devemos tratar a ordem

            buffer.append("class " + decl.getName() + " extends Element {\n" +
                    "  constructor() {\n" +
                    "    super();\n" +
                    "  }\n" +
                    "  draw(ctx) {\n" + // TODO: temos que definir a função de desenho de acordo com os frames
                    "    ctx.save();\n" +
                    "    ctx.drawImage(" + image + ", 0, 0);\n" + //TODO: ajustar posicao de desenho no meio
                    "    ctx.restore();\n" +
                    "  }\n" +
                    "}\n");
        }

        for (Map.Entry<String, Element> inst : animation.getInst_element().entrySet()) {
            buffer.append("var " + inst.getKey() + " = new " + inst.getValue().getName() + "();\n");
        }

        //TODO: ajeita inicialização da animação
        buffer.append("function init() {\n" +
                "    // Inicio da animação\n" +
                "    window.requestAnimationFrame(draw);\n" +
                "}");

        buffer.append("function draw(timestamp) {\n" +
                "  update(timestamp);\n" +
                "  ctx.clearRect(0, 0," + animation.getComposition().getWidth() + ", " + animation.getComposition().getHeight() + ");\n" +
                "  //Desenha-se as camadas superiores primeiro\n" +
                "  ctx.fillStyle = 'rgba(0, 0, 0, 0.4)';\n" +
                "  ctx.strokeStyle = 'rgba(0, 153, 255, 0.4)';\n");
        for (String obj : animation.getInst_element().keySet()) {
            buffer.append(obj + ".draw(ctx);\n");
        }

        buffer.append("last_frame_update_time = timestamp;\n" +
                "  window.requestAnimationFrame(draw);\n" +
                "}" +
                "init();\n");
    }

    private void decodeComposition(StringBuffer buffer) {

        // Declaração globais da animação
        buffer.append("var canvas = document.getElementById('canvas');\n" +
                "width = " + animation.getComposition().getWidth() + ";\n" +
                "height = " + animation.getComposition().getHeight() + ";\n" +
                "canvas.width = width;\n" +
                "canvas.height = height;\n" +
                "var ctx = canvas.getContext(\"2d\");\n" +
                "ctx.globalCompositeOperation = 'destination-over';\n");

        // Controles de tempo
        buffer.append("var current_frame = 0;\n" +
                "var change_frame = true;\n" +
                "var total_frame = " + animation.getComposition().getTotalFrames() + ";\n" +
                "var fps = " + animation.getComposition().getFPS() + ";\n" +
                "var frame_duration = 1000/fps;\n" +
                "var loop = true;\n" + // TODO: tratar escolha de loop
                "var elapsed_time = 0;\n" +
                "var last_frame_update_time = 0;\n");

        // Controle do estado da animação
        buffer.append("var paused = true;\n" +
                "function pause(){\n" +
                "  paused = true;\n" +
                "  change_frame = false;\n" +
                "}\n" +
                "function play() {\n" +
                "  paused = false;\n" +
                "}\n" +
                "function stop() {\n" +
                "  current_frame = 0;\n" +
                "  pause();\n" +
                "}");

        // Controle da taxa de atualização do frame
        buffer.append("function update(timestamp) {\n" +
                "  if (!paused) {\n" +
                "      var dt = timestamp - last_frame_update_time;\n" +
                "      if (loop || current_frame < total_frame - 1) {\n" +
                "        elapsed_time += dt;\n" +
                "        if (elapsed_time > frame_duration) {\n" +
                "          current_frame += 1;\n" +
                "          current_frame = current_frame % total_frame;\n" +
                "          elapsed_time -= frame_duration;\n" +
                "          change_frame = true;\n" +
                "        } else {\n" +
                "          change_frame = false;\n" +
                "        }\n" +
                "      }\n" +
                "  }\n" +
                "}");
    }
}
