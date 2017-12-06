# Isso é um comentario

@animation "Título da Animação"
@authors "Nome dos autores"
@description "Descrição da animação"

composition:
    width = 500
    height = 500
    fps = 30
    duration = 1h10m15s # 10f
    background = "#00000"

elements:
    Moon
    {
        image = "https://mdn.mozillademos.org/files/1443/Canvas_moon.png"
        width = 50 # ao configurar um dos atributos de tamanho, o outro mantém a
    }

    Earth
    {
        image = "https://mdn.mozillademos.org/files/1429/Canvas_earth.png"

        Moon lua()

        # Elementos também possuem ações, que podem alterar suas propriedades
        action rotate_lua() {
            lua.rotation += 5
        }

        action self_rotation() {
            rotation += 10
        }
    }

    Sun {
        image = "https://mdn.mozillademos.org/files/1456/Canvas_sun.png"

        action init(a, b){
            x = a
            y = b
        }
        scale = 75

        Earth terra()

        action rotate_terra() {
            terra.rotation += 5
        }
    }

scene:
    Sun sol(200,200)

storyboard:


