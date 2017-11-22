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

        # Elementos também possuem ações, que podem alterar suas propriedades
        action translada() {
            x += 1
            y += 1
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

[100f]:
    start terra.translada()

[150f]:
    start terra2.rotaciona()
    stop terra.rotaciona()

[200f]:
    stop terra.translada()



#[0]:
    #addAction(terra.rotate_terra())
    #addAction(jupiter.flip())
#[20]:
    #removeAction(jupiter.flip())

