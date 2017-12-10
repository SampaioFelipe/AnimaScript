# Isso é um comentario

@animation "Caso 1: Sistema Solar"
@authors "Grupo Animascript"
@description "Caso de teste para a disciplina de compiladores"

composition:
    width = 900
    height = 500
    fps = 30
    duration = 1m15s0f # 10f
    background = "#00000"

elements:
    Moon
    {
        image = "../../src/main/resources/casos_de_teste/images/Canvas_moon.png"

        action init(a, b) {
            this.x = a
            this.y = b
        }
    }

    Earth
    {
        image = "../../src/main/resources/casos_de_teste/images/Canvas_earth.png"
        x = 80
        y = 80

        Moon lua(30,30)

        action rotaciona(r) {
            this.rotation += r
        }
    }

    Sun {

        image = "../../src/main/resources/casos_de_teste/images/sol.png"
        x = 450
        y = 250
        width = 100
        height = 100

        Earth terra()

        action rotaciona() {
            this.rotation += 0.01
        }
    }

    Background {
        image = "../../src/main/resources/casos_de_teste/images/background.jpg"

        x = 450
        y = 250
    }

scene:
    Background bg()
    Sun sol()

storyboard:
[0f]:
    start sol.rotaciona()
    start sol.terra.rotaciona(0.03)

