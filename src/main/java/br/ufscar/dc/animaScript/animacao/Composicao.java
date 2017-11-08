package br.ufscar.dc.animaScript.animacao;

public class Composicao {
    private int width, height;
    private float fps;
    private int total_frames;

    public Composicao() {
        this.width = 500;
        this.height = 500;
    }
    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}
