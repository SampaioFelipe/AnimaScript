# Isso é um comentario

@animation "Título da Animação"
@authors "Nome dos autores"
@description "Descrição da animação"

composition:
    width = 600
    height = 600
    fps = 30
    duration = 1h10m15s # 10f
    background = "#00000"

elements:

    Earth
    {
        image = "/home/felipe/intelliJProjects/AnimaScript/AnimaScript/src/main/resources/casos_de_teste/images/Canvas_earth.png"

        Moon lua
        # Elementos também possuem ações, que podem alterar suas propriedades

        action init(x,y){
            lua.x = 200
            x = 0
            y = 0
        }

        action translada() {
            x += 1
            y += 1
        }

        action deg_to_rad() {

        }

        action rotaciona() {
            rotation += 0.05 #deg
        }

#        action escala() {
#            scale = 0.5
#        }
    }

scene:
    Earth terra
    terra.x = 300
    terra.y = 300

    Earth terra2
    terra2.x = 100
    terra2.y = 300

storyboard:
[0f]:
    start terra.rotaciona()
[50f]:
    start terra2.translada()
    terra.x = 10
    terra.y = 10
[100f]:
    start terra.translada()

[150f]:
    start terra2.rotaciona()
    stop terra.rotaciona()

[200f]:
    stop terra.translada()

