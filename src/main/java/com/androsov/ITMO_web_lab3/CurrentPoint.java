package com.androsov.ITMO_web_lab3;


import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class CurrentPoint implements Serializable {
    private float x = 0;
    private float y = 0;
    private float r = 1;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getR() {
        return r;
    }

    public void setR(float r) {
        this.r = r;
    }

    public float getPixelsOfY() {
        if(y > 1.25*r || y < -1.25*r) return -10000;

        return (float)((500 / 2.5) * (y / r) + 250);
    }

    public float getPixelsOfX() {
        if(x > 1.25*r || x < -1.25*r) return -10000;

        return (float)((500 / 2.5) * (x / r) + 250);
    }
}
