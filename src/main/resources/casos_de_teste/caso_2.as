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
            position.x += 5
            position.y += 5
        }

        action rotaciona() {
            rotation += 10 deg
        }

        action escala() {
            scale = 0.5
        }
    }

scene:
    Earth terra
    terra.position = (15,15)

storyboard:
[0f]:
    start terra.translada()
    start terra.rotaciona()

[100f]:
    stop terra.translada()

[200f]:
    stop terra.rotaciona()



#[0]:
    #addAction(terra.rotate_terra())
    #addAction(jupiter.flip())
#[20]:
    #removeAction(jupiter.flip())

