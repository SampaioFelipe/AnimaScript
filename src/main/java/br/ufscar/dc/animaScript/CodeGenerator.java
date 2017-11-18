package br.ufscar.dc.animaScript;

import br.ufscar.dc.animaScript.animation.Animation;

public class CodeGenerator {

    private Animation animation;

    public CodeGenerator(Animation animation) {

        this.animation = animation;

        StringBuffer html = new StringBuffer();

        html.append("<html>" +
                "<head>" +
                "<meta charset=\"utf-8\">");

        html.append("<title>"+ animation.getTitle()+"</title>");

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

        this.geraJs();
    }

    public void geraJs() {
        StringBuffer js = new StringBuffer();

        decodeComposicao(js);

        decodeDeclaracoes(js);

        Main.out.printJS(js.toString());

        // Declaração dos elementos
    }

    private void decodeDeclaracoes(StringBuffer buffer) {
        buffer.append("var sun = new Image();\n" +
                "sun.src = 'https://mdn.mozillademos.org/files/1456/Canvas_sun.png';\n" +
                "var moon = new Image();\n" +
                "moon.src = 'https://mdn.mozillademos.org/files/1443/Canvas_moon.png';\n" +
                "var moon_speed_rotation = 0.05\n" +
                "var earth = new Image();\n" +
                "earth.src = 'https://mdn.mozillademos.org/files/1429/Canvas_earth.png';\n" +
                "var earth_speed_rotation = 0.01\n" +
                "// Inicialização das propriedades da animation\n" +
                "function init() {\n" +
                "  // Inicio da animação\n" +
                "  window.requestAnimationFrame(draw);\n" +
                "}\n" +
                "function draw(timestamp) {\n" +
                "  update(timestamp);\n" +
                "  ctx.clearRect(0, 0, width, height); // limpa o canvas\n" +
                "  ctx.fillStyle = 'rgba(0, 0, 0, 0.4)';\n" +
                "  ctx.strokeStyle = 'rgba(0, 153, 255, 0.4)';\n" +
                "  ctx.save(); // salva o estado do canvas\n" +
                "  ctx.translate(width/2, height/2); // translada o canvas para o meio (nova origem)\n" +
                "  ctx.rotate(current_frame * earth_speed_rotation);\n" +
                "  ctx.translate(105, 2);\n" +
                "  ctx.fillRect(0, -12, 50, 24); // Shadow\n" +
                "  ctx.rotate(current_frame * earth_speed_rotation);\n" +
                "  ctx.drawImage(earth, -12, -12);\n" +

                "  ctx.save();\n" +
                "  ctx.rotate(current_frame * moon_speed_rotation);\n" +
                "  ctx.translate(0, 28.5);\n" +
                "  ctx.drawImage(moon, -3.5, -3.5);\n" +
                "  ctx.restore();\n" +

                "  ctx.restore();\n" +
                "  ctx.beginPath();\n" +
                "  ctx.arc(width/2, height/2, 105, 0, Math.PI * 2, false); // Earth orbit\n" +
                "  ctx.stroke();\n" +
                "  ctx.drawImage(sun, 0, 0, width, height);\n" +
                "  last_frame_update_time = timestamp;\n" +
                "  window.requestAnimationFrame(draw);\n" +
                "}\n" +
                "init();");
    }

    private void decodeComposicao(StringBuffer buffer) {

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
