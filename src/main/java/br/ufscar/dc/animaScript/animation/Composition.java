package br.ufscar.dc.animaScript.animation;

import br.ufscar.dc.animaScript.utils.AttributeList;

public class Composition {

    private int width, height;
    private int fps;
    private int durationSeconds;

    private AttributeList attributes;

    public Composition() {
        this.width = 500;
        this.height = 500;
        this.fps = 25;
        this.durationSeconds = 60;
    }

    public boolean addAttribute(Attribute attr) {
        String name = attr.getName();
        if (name.equals("width")) {
            setWidth(Integer.decode(attr.getValue()));
        } else if (name.equals("height")) {
            setHeight(Integer.decode(attr.getValue()));
        } else if (name.equals("fps")) {
            setFps(Integer.decode(attr.getValue()));
        } else if (name.equals("duration")) {
            setDuration(attr.getValue());
        }

        return true;
        //TODO: tratar erros de tipos
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public void setDuration(String duration) {
//        this.durationSeconds ;
    }

    public void setDurationSeconds(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getTotalFrames() {
        return this.durationSeconds * this.fps;
    }

    public int getFPS() {
        return this.fps;
    }
}
