package br.ufscar.dc.animaScript;

import java.util.ArrayList;
import java.util.Map;

import br.ufscar.dc.animaScript.animation.Action;
import br.ufscar.dc.animaScript.animation.Animation;
import br.ufscar.dc.animaScript.animation.Attribute;
import br.ufscar.dc.animaScript.animation.Command;
import br.ufscar.dc.animaScript.animation.Element;

public class CodeGenerator {

    private Animation animation;

    private StringBuffer codeBuffer;

    public CodeGenerator(Animation animation) {

        this.animation = animation;

        this.codeBuffer = new StringBuffer();

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
    }

    public void geraJs() {

        decodeComposition();

        decodedDeclElements();

        decodeScene();

        Main.out.printJS(this.codeBuffer.toString());
    }

    private void decodeElement(Element element) {

        String image = element.getName() + "_image";

        codeBuffer.append("var " + image + " = new Image();\n");
        codeBuffer.append(image + ".src = " + element.getImage_path() + ";\n"); //TODO: devemos tratar a ordem

        // Declaracao da class do elemento
        codeBuffer.append("class " + element.getName() + " extends Element {\n");

        // Construtor
        codeBuffer.append("  constructor() {\n" +
                "    super();\n" +
                "  }\n");
        // Definição da função de desenho
        codeBuffer.append("  draw(ctx) {\n" +
                "    // atualiza estado\n" +
                "    this.update(this);" +
                "    ctx.save();\n" +
                "    ctx.translate(this.x, this.y);\n" +
                "    ctx.rotate(this.rotation);" + // TODO: temos que definir a função de desenho dos filhos
                "    ctx.drawImage(" + image + ", -12, -12);\n" + //TODO: ajustar posicao de desenho no meio
                "    ctx.restore();\n" +
                "  }\n");

        // Definição das actions
        for (Action action : element.getActions().values()) {
            decodeDeclAction(action);
        }


        codeBuffer.append("}\n");
    }

    private void decodeDeclAction(Action action) {

        codeBuffer.append(action.getName() + "(obj){\n"); // TODO: tratar os parametros

        for (Command cmd : action.getCommands()) {
            decodeCommand(cmd, "obj");
        }

        codeBuffer.append("}\n");
    }

    private void decodeCommand(Command cmd, String prefix) {
        if (cmd.isAttribute()) {
            codeBuffer.append(prefix + "." + cmd.getIdentifier() + cmd.getOp() + cmd.getValue_or_params() + ";\n");
        }
    }

    private void decodeComposition() {

        // Declaração globais da animação
        codeBuffer.append("var canvas = document.getElementById('canvas');\n" +
                "width = " + animation.getComposition().getWidth() + ";\n" +
                "height = " + animation.getComposition().getHeight() + ";\n" +
                "canvas.width = width;\n" +
                "canvas.height = height;\n" +
                "var ctx = canvas.getContext(\"2d\");\n" +
                "ctx.globalCompositeOperation = 'destination-over';\n");

        // Controles de tempo
        codeBuffer.append("var current_frame = -1;\n" +
                "var total_frame = " + animation.getComposition().getTotalFrames() + ";\n" +
                "var fps = " + animation.getComposition().getFPS() + ";\n" +
                "var frame_duration = 1000/fps;\n" +
                "var loop = false;\n" + // TODO: tratar escolha de loop
                "var elapsed_time = 0;\n" +
                "var last_frame_update_time = 0;\n");

        // Controle do estado da animação

        codeBuffer.append("var paused = true;\n" +
                "var change_frame = true;" +
                "function pause(){\n" +
                "  paused = true;\n" +
                "  change_frame = true;\n" +
                "}\n" +
                "function play() {\n" +
                "  paused = false;\n" +
                "  window.requestAnimationFrame(draw);\n" +
                "}\n" +
                "function stop() {\n" +
                "  current_frame = -1;\n" +
                "  elapsed_time = 0;\n" +
                "  last_frame_update_time = 0;\n" +
                "  pause();\n" +
                "  init();\n" +
                "}\n");

        // Controle da taxa de atualização do frame
        codeBuffer.append("function update(timestamp) {\n" +
                "  if (!paused || (current_frame < 0)) {\n" +
                "      var dt = timestamp - last_frame_update_time;\n" +
                "      if (loop || current_frame < total_frame - 1) {\n" +
                "        elapsed_time += dt;\n" +
                "        if (elapsed_time > frame_duration) {\n" +
                "          current_frame += 1;\n" +
                "          current_frame = current_frame % total_frame;\n" +
                "          elapsed_time -= frame_duration;\n" +
                "          change_frame = true;\n" +
                "          return true;\n" +
                "        } else {\n" +
                "          change_frame = false;\n" +
                "        }\n" +
                "      }\n" +
                "  }\n" +
                "  return false;\n" +
                "}");
    }

    private void decodedDeclElements() {

        codeBuffer.append("// Declaração dos elementos\n" +
                "class Element {\n" +
                "  constructor() {\n" +
                "    this.x = 0;\n" +
                "    this.y = 0;\n" +
                "    this.rotation = 0;\n" +
                "    this.frames = {};\n" +
                "    this.last_frame = 0;\n" +
                "    this.cur_actions = [];\n" +
                "  }\n" +
                "  update(obj) {\n" +
                "    var cur = obj.frames[current_frame];\n" +
                "    if (typeof cur != 'undefined') {\n" +
                "      for (var i = 0; i < cur.length; i++) {\n" +
                "        if(cur[i].op == 0) { // adiciona\n" +
                "          obj.cur_actions.push(cur[i])\n" +
                "        } else {\n" +
                "          for(var j = obj.cur_actions.length - 1; j >= 0 ; j--) {\n" +
                "            if(obj.cur_actions[j].id == cur[i].id) {\n" +
                "              obj.cur_actions.splice(j,1);\n" +
                "              break;\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "    for (var i = 0; i < obj.cur_actions.length; i++) {\n" +
                "      obj.cur_actions[i].func(this);\n" +
                "    }\n" +
                "  }\n" +
                "}");

        for (Element element : animation.getDecl_elements().values()) {
            decodeElement(element);
        }
    }

    private void decodeScene() {

        for (Map.Entry<String, Element> inst : animation.getInst_element().entrySet()) {
            codeBuffer.append("var " + inst.getKey() + " = new " + inst.getValue().getName() + "();\n");
            codeBuffer.append(inst.getKey()+".frames = {\n");

            for(Map.Entry<Integer, ArrayList<Command>> cmds : inst.getValue().getFrames().entrySet()) {
                codeBuffer.append(cmds.getKey() + ": [");
                int j = 0;
                for(Command cmd : cmds.getValue()) {
                    //TODO: tratar quando o comando não for action
                    codeBuffer.append("{id:\"" + cmd.getIdentifier() + "\", op:" + cmd.getOpId() + ", func:" + cmd.getIdentifier() + "}");

                    if(j < cmds.getValue().size() - 1) {
                        codeBuffer.append(",");
                    }

                    j++;
                }

                codeBuffer.append("],");
            }
            codeBuffer.append("}\n");
        }

        //TODO: ajeitar inicialização da animação
        codeBuffer.append("function init() {\n");

        for (Map.Entry<String, Element> elementEntry : animation.getInst_element().entrySet()) {

            for (Attribute attr : elementEntry.getValue().getAttributes().values()) {
                codeBuffer.append(elementEntry.getKey() + "." + attr.getName() + "=" + attr.getValue() + ";\n");
            }

            codeBuffer.append(elementEntry.getKey()+".cur_actions = [];\n");

        }

        codeBuffer.append("window.requestAnimationFrame(draw);\n" +
                "}");

        codeBuffer.append("function draw(timestamp) {\n" +
                "    if(update(timestamp)) {\n" +
                "    ctx.clearRect(0, 0," + animation.getComposition().getWidth() + ", " + animation.getComposition().getHeight() + ");\n");

        for (String obj : animation.getInst_element().keySet()) {
            codeBuffer.append(obj + ".draw(ctx);\n");
        }

        codeBuffer.append("}\n" +
                "last_frame_update_time = timestamp;\n" +
                "    if(!paused) {\n" +
                "      window.requestAnimationFrame(draw);\n" +
                "    }\n" +
                "}\n" +
                "init();");
    }
}
