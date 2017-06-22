package com.TypeIt.gui;

/**
 * Created by admin on 22/06/2017.
 */
public class GuiNote {
    private double x;
    private double y;
    private double velocity;
    private int imageIndex;

    public GuiNote(double x, double y, double velocity, int image) {
        this.x = x;
        this.y = y;
        this.velocity = velocity;
        this.imageIndex = image;
    }

    public int getImageIndex() {
        return imageIndex;
    }
    public void move() {
        y -= velocity;
    }
    public double getVelocity() {
        return velocity;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
}
