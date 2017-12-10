package br.ufscar.dc.animaScript;

import java.util.ArrayList;
import java.util.List;
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


        //Gera a parte HTML do código final
        StringBuffer html = new StringBuffer();

        html.append("<html>" +
                "<head>" +
                "<meta charset=\"utf-8\">");

        String title = animation.getTitle();

        html.append("<title>" + title.substring(1, title.length() - 1) + "</title>");

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
                "<div style=\"float:right; color:#fff;margin-right:10px;\">Tempo: <span id=\"current_time\">0:0:0</span></div>" +
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

    private void decodeComposition() {

        // Declaração globais da animação
        codeBuffer.append("var canvas = document.getElementById('canvas');\n" +
                "width = " + animation.getComposition().getWidth() + ";\n" +
                "height = " + animation.getComposition().getHeight() + ";\n" +
                "canvas.width = width;\n" +
                "canvas.height = height;\n" +
                "var ctx = canvas.getContext(\"2d\");\n" +
                "ctx.globalCompositeOperation = 'destination-over';\n" +
                "ctx.fillStyle = " + animation.getComposition().getBgcolor() + ";");

        // Controles de tempo
        codeBuffer.append("var current_frame = -1;\n" +
                "var total_frame = " + animation.getComposition().getTotalFrames() + ";\n" +
                "var fps = " + animation.getComposition().getFPS() + ";\n" +
                "var frame_duration = 1000/fps;\n" +
                "var now;\n " +
                "var then = Date.now();");

        // Controle do estado da animação

        codeBuffer.append("var paused = true;\n" +
                "var change_frame = true;" +
                "function pause(){\n" +
                "  paused = true;\n" +
                "  change_frame = true;\n" +
                "}\n" +
                "function play() {\n" +
                "    if(paused == true){\n" +
                "    paused = false;\n" +
                "    window.requestAnimationFrame(draw);\n" +
                "  }\n" +
                "}\n" +
                "function stop() {\n" +
                "  current_frame = -1;\n" +
                "  pause();\n" +
                "  init();\n" +
                "}\n" +
                "function show_time(){\n" +
                "  var seconds = Math.floor(current_frame/fps)\n" +
                "  var h = Math.floor(seconds / 3600)\n" +
                "  var resto = Math.floor(seconds%3600)\n" +
                "  var m = Math.floor(resto/60)\n" +
                "  var s = Math.floor(resto%60)\n" +
                "  \n" +
                "  document.getElementById(\"current_time\").innerHTML = h + \":\" + m + \":\" + s;\n" +
                "}\n");
    }

    private void decodedDeclElements() {

        codeBuffer.append("class Element {\n" +
                "  constructor() {\n" +
                "    this.x = 0;\n" +
                "    this.y = 0;\n" +
                "    this.rotation = 0;\n" +
                "    this.frames = {};\n" +
                "    this.last_frame = 0;\n" +
                "    this.cur_actions = [];" +
                "    this.child = this;" +
                "  }\n" +
                "update() {\n" +
                "    var cur = this.child.frames[current_frame];\n" +
                "    if (typeof cur != 'undefined') {\n" +
                "      for (let i = 0; i < cur.length; i++) {\n" +
                "        if(cur[i].op == 0) {\n" +
                "          this.child.cur_actions.push(cur[i])\n" +
                "        } else if(cur[i].op == 1) {\n" +
                "          \n" +
                "          for(let j = this.child.cur_actions.length - 1; j >= 0 ; j--) {\n" +
                "            if(this.child.cur_actions[j].id == cur[i].id) {\n" +
                "              this.child.cur_actions.splice(j,1);\n" +
                "              break;\n" +
                "            }\n" +
                "          }\n" +
                "        } else {\n" +
                "          cur[i].func();\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "    for (var i = 0; i < this.child.cur_actions.length; i++) {\n" +
                "      this.child.cur_actions[i].func();\n" +
                "    }\n" +
                "  }" +
                "}");

        for (Element element : animation.getDecl_elements().values()) {
            decodeElement(element);
        }
    }

    private void decodeScene() {

        for (Map.Entry<String, Element> inst : animation.getInst_element().entrySet()) {

            codeBuffer.append("var " + inst.getKey() + " = new " + inst.getValue().getName() + "(" + inst.getValue().decodeInitParams() + ");\n");
            codeBuffer.append(inst.getKey() + ".frames = {\n");

            for (Map.Entry<Integer, ArrayList<Command>> cmds : inst.getValue().getFrames().entrySet()) {
                codeBuffer.append(cmds.getKey() + ": [");
                String attributions = "";
                for (Command cmd : cmds.getValue()) {

                    if (cmd.getOpId() == 2) {
                        attributions += cmd.getIdentifier() + cmd.getOp() + cmd.getValue() + ";\n";
                    } else {
                        codeBuffer.append("{id:\"" + cmd.getIdentifier() + "\", op:" + cmd.getOpId() + ", " +
                                "func: function(){" + cmd.getIdentifier() + "(");
                        if (cmd.getNumberParams() > 0) {
                            codeBuffer.append(cmd.decodeParams());
                        }
                        codeBuffer.append(");}},");
                    }
                }

                if (attributions.length() > 0) {
                    codeBuffer.append("{op:2, func: function(){");

                    codeBuffer.append(attributions);

                    codeBuffer.append("}}");
                }

                codeBuffer.append("],");
            }
            codeBuffer.append("}\n");
        }

        codeBuffer.append("function init() {\n");

        for (Map.Entry<String, Element> elementEntry : animation.getInst_element().entrySet()) {
            codeBuffer.append(elementEntry.getKey() + ".init(" + elementEntry.getValue().decodeInitParams() + ");\n");
        }

        codeBuffer.append("window.requestAnimationFrame(draw);\n" +
                "}");

        codeBuffer.append("function draw() {\n" +
                "    requestAnimationFrame(draw);\n" +
                "    if (current_frame < total_frame) {\n" +
                "      now = Date.now();\n" +
                "      delta = now - then;\n" +
                "      if((!paused && (delta > frame_duration)) || current_frame < 1) {" +
                "    show_time();\n" +
                "    ctx.clearRect(0, 0," + animation.getComposition().getWidth() + ", " + animation.getComposition().getHeight() + ");\n");

        for (String obj : animation.getInst_element().keySet()) {
            codeBuffer.append(obj + ".draw(ctx);\n");
        }

        codeBuffer.append(
                "ctx.fillRect(0,0," + animation.getComposition().getWidth() + ", " + animation.getComposition().getHeight() + ");" +
                        "then = now - (delta % frame_duration);\n" +
                        "current_frame += 1;\n" +
                        "}\n" +
                        "}" +
                        "}\n" +
                        "init();");
    }

    private void decodeElement(Element element) {

        String image = element.getName() + "_image";

        codeBuffer.append("var " + image + " = new Image();\n");
        codeBuffer.append(image + ".src = " + element.getImage_path() + ";\n"); //TODO: devemos tratar a ordem
        codeBuffer.append(image + ".onload = function()\n" +
                "{\n" +
                "    init()\n" +
                "}\n");

        // Declaracao da class do elemento
        codeBuffer.append("class " + element.getName() + " extends Element {\n");


        // Construtor
        codeBuffer.append("constructor(){" +
                "super();\n");

        for (Command child : element.getChildren().values()) {

            codeBuffer.append("this." + child.getIdentifier() + "= new " + child.getOp() + "();\n");
        }

        codeBuffer.append("this.child = this;}\n");

        // Funcao init
        codeBuffer.append("init(" + element.decodeInitParams() + "){\n");

        for (Map.Entry<String, Attribute> attr : element.getAttributes().entrySet()) {
            codeBuffer.append("this." + attr.getKey() + "=");

            if (attr.getKey().equals("width")) {
                if(attr.getValue().getValue().equals("0"))
                    codeBuffer.append(image + ".naturalWidth;");
                else
                    codeBuffer.append(attr.getValue().getValue()+ ";");

            } else if (attr.getKey().equals("height")) {
                if(attr.getValue().getValue().equals("0"))
                    codeBuffer.append(image + ".naturalHeight;");
                else
                    codeBuffer.append(attr.getValue().getValue()+ ";");
            } else {
                codeBuffer.append(attr.getValue().getValue() + ";");
            }
        }

        for (Command child : element.getChildren().values()) {

            codeBuffer.append("this." + child.getIdentifier() + ".init(" + child.decodeParams() + ");\n");
        }

        Action initAction = element.getActions().remove("init");

        if (initAction != null) {

            for (Command command : initAction.getCommands()) {
                if(command.getIdentifier().contains("this"))
                    decodeCommand(command, "this");
                else
                    decodeCommand(command, "");
            }
        }

        codeBuffer.append("this.cur_actions = [];\n");

        codeBuffer.append("}\n");

        // Definição da função de desenho
        codeBuffer.append("  draw(ctx) {\n" +
                "    // atualiza estado\n" +
                "    this.update();" +
                "    ctx.save();\n" +
                "    ctx.translate(this.x, this.y);\n" +
                "    ctx.rotate(this.rotation);\n");

        for (Command child : element.getChildren().values()) {
            codeBuffer.append("ctx.save();\n");
            codeBuffer.append("this." + child.getIdentifier() + ".draw(ctx);\n");
            codeBuffer.append("ctx.restore();\n");
        }

        codeBuffer.append("ctx.drawImage(" + image + ", -this.width/2, -this.height/2, this.width, this.height);\n" + //TODO: ajustar posicao de desenho no meio
                "    ctx.restore();\n" +
                "  }\n");

        // Definição das actions
        for (Action action : element.getActions().values()) {
            decodeDeclAction(action);
        }


        codeBuffer.append("}\n");
    }

    private void decodeDeclAction(Action action) {

        codeBuffer.append(action.getName() + "("); // TODO: tratar os parametros

        if (action.getNumberParams() > 0) {
            codeBuffer.append(action.decodeParams());
        }

        codeBuffer.append("){\n");

        List<String> variables = new ArrayList<String>();

        for (Command cmd : action.getCommands()) {

            if (cmd.getIdentifier().contains("this") || cmd.getIdentifier().contains("\\."))
                decodeCommand(cmd, "this");
            else {
                if (!variables.contains(cmd.getIdentifier())) {
                    codeBuffer.append("var " + cmd.getIdentifier() + ";\n");
                    codeBuffer.append(cmd.getIdentifier() + "=" + cmd.getValue() + ";\n");
                    variables.add(cmd.getIdentifier());
                } else {
                    codeBuffer.append(cmd.getIdentifier() + cmd.getOp() + cmd.getValue() + ";\n");

                }

            }
        }

        codeBuffer.append("}\n");
    }

    private void decodeCommand(Command cmd, String prefix) {

        if (cmd.isAttribute()) {
            if (prefix.length() > 0)
                prefix = prefix + ".";

            codeBuffer.append(prefix + cmd.getIdentifier().replace("this.","") + cmd.getOp() + cmd.getValue() + ";\n");
        }
    }
}
