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
            x = a
            y = b

            width = 7
            height = 7
        }
    }

    Earth
    {
        image = "/home/felipe/intelliJProjects/AnimaScript/AnimaScript/src/main/resources/casos_de_teste/images/Canvas_earth.png"

        Moon lua(20,20)

        action init(a,b){
            x = a
            y = b

            width = 24
            height = 24
        }

        action translada() { # Como colocar argumentos nos frames?
            x += 1
            y += 1
        }

        action deg_to_rad() {

        }

        action rotaciona() {
            rotation += 0.05
        }

#        action escala() {
#            scale = 0.5
#        }
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
[100f]:
    start terra.translada()

