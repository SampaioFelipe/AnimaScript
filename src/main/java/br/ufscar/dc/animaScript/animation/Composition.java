package br.ufscar.dc.animaScript.animation;

import java.util.HashMap;

public class Composition {

    private int width, height;
    private int fps;
    private int total_frames;
    private String bgcolor;

    private HashMap<String, Attribute> attributes;

    public Composition() {
        this.width = 500;
        this.height = 500;
        this.fps = 25;
        this.total_frames = 1500;
        this.bgcolor = "'#ffffff'";
    }

    @Override
    public String toString() {
        StringBuilder state = new StringBuilder();

        state.append("widht: " + this.getWidth() + "\n");
        state.append("height: " + this.getHeight() + "\n");
        state.append("FPS: " + this.getFPS() + "\n");
        state.append("TotalFrame: " + this.getTotalFrames());

        return state.toString();
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
            System.out.println("setando duração");
            setDuration(attr.getValue());
        } else if (name.equals("background")) {
            setBgcolor(attr.getValue());
        } else {
            this.attributes.put(name, attr);
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

    public void setDuration(String total_frames) {
        this.total_frames = Integer.decode(total_frames);
    }

    public void setBgcolor(String bgcolor) {
        this.bgcolor = bgcolor;
    }


    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getTotalFrames() {
        return this.total_frames;
    }

    public int getFPS() {
        return this.fps;
    }

    public String getBgcolor() {
        return bgcolor;
    }

    public Attribute getAttribute(String identfier) {
        return this.attributes.get(identfier);
    }
}
