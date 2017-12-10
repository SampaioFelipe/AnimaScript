# Isso é um comentario

@animation "Caso 2: Mario"
@authors "Grupo Animascript"
@description "Caso de teste para a disciplina de compiladores"

composition:
    width = 900
    height = 600
    fps = 30
    duration = 12s0f # 10f

elements:
    Mario
    {
        image = "../../src/main/resources/casos_de_teste/images/mario.png"

        width = 50
        height = 52

        action init(a, b) {
            this.x = a
            this.y = b
        }

        action walk(x){
            this.x += x
        }

        action jump_up(y){
            this.y -= y
        }

        action jump_down(y){
            this.y += y
        }
    }

    Background {
        image = "../../src/main/resources/casos_de_teste/images/mario_bg.jpg"

        x = 450
        y = 250
    }

scene:
    Background bg()
    Mario m1(0, 470)

storyboard:
[0f]:
    start m1.walk(3)

[4s0f]:
    stop m1.walk

[4s10f]:
    start m1.jump_up(5)

[4s20f]:
    stop m1.jump_up
    start m1.jump_down(5)

[5s00f]:
    stop m1.jump_down

[5s10f]:
    start m1.walk(3)

[6s10f]:
    start m1.jump_up(3)
[7s10f]:
    stop m1.jump_up
    start m1.jump_down(3)

[7s15f]:
    stop m1.jump_down

[9s15f]:
    start m1.jump_down(3)

[10s13f]:
    stop m1.jump_down

