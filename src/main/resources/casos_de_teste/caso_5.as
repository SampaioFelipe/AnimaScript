# Isso é um comentario

@animation "Título da Animação"
@authors "Nome dos autores"
@description "Descrição da animação"

composition:
    width = 500
    height = 500
    fps = 25
    duration = 1m15s0f # 10f
    background = "#0ff"

elements:

    Earth
    {
        image = "casos_de_teste/images/Canvas_earth.png"

        action init(a,b){
            this.x = a
            this.y = b
        }

        action translada(a,b) {
            this.x += a
            this.y += b
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
    start terra.translada()
[10s0f]:
    start terra1.rotaciona()
    terra.x = 10
    terra.y = 10