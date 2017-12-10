# Isso é um comentario

@animation "Título da Animação"
@authors "Nome dos autores"
@description "Descrição da animação"

composition:
    width = 500
    height = 500
    fps = 25
    duration = 1m15s # 10f
    background = "#0ff"

elements:

    Moon
    {
        image = "/home/felipe/intelliJProjects/AnimaScript/AnimaScript/src/main/resources/casos_de_teste/images/Canvas_moon.png"

        action init(a, b) {
            this.x = a
            this.y = b

            k = 0

            this.width = 7
            this.height = 7
        }
    }

    Earth
    {
        image = "/home/felipe/intelliJProjects/AnimaScript/AnimaScript/src/main/resources/casos_de_teste/images/Canvas_earth.png"

        Moon lua(20,20)

        action init(a,b){
            this.x = a
            this.y = b

#            width = 100
#            height = 100
        }

        action translada(a,b) { # Como colocar argumentos nos frames?
            this.x += a
            this.y += b
        }

        action rotaciona(r) {
            this.rotation += r
        }

        action escala() {
            this.width += 6
            this.height += 6
        }
    }

scene:
    Earth terra(200,200)

    Earth terra1(100,100)

storyboard:
[0f]:
    start terra.rotaciona()
[10s]:
    start terra1.rotaciona()
    terra.x = 10
    terra.y = 10
    terra1.lua.x = 40

[10s]:
    start terra.escala()

[12s]:
    stop terra.escala

[100f]:
    start terra.translada(2,2)

[150f]:
    stop terra.translada
