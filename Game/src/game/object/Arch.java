package game.object;

import game.Game;
import processing.core.*;

public class Arch {
    PShape arch;
    public PVector location;
    Game parent;

    public final static float columnCenter = 36;
    public final static float columnBoard = 40;
    public final static float columnHeight = 0;
    public final static float columnRadius = 10;
    public final static float archWidth = 52;
    public final static float sizeForPrev = 15;

    PVector lColumn;
    PVector rColumn;

    public int archId;
    public float rotation;

    public Arch(float posX, float posY, int id, float rotation,  Game parent) {
        location = new PVector(posX , posY );
        this.parent = parent;
        arch = parent.loadShape("data/arco_nuovo.obj");
        this.archId = id;
        this.rotation = rotation;

        lColumn = new PVector(- columnCenter, 0);
        rColumn = new PVector(columnCenter, 0);

        lColumn.rotate(rotation);
        rColumn.rotate(rotation);

        lColumn.add(location);
        rColumn.add(location);


    }

    public void display(boolean isPlaceable) {
        parent.pushMatrix();

        parent.translate(location.x, -37, location.y);

        parent.rotateX((float) Math.PI);
        parent.rotateY(rotation);
        if(!isPlaceable) arch.setFill(parent.color(255,0,0));
        parent.shape(arch);
        parent.popMatrix();
    }

    public void display() {
        parent.pushMatrix();

        parent.translate(location.x, -37, location.y);
        parent.rotateX((float) Math.PI);

        parent.rotateY(rotation);
        parent.shape(arch);
        parent.popMatrix();
    }


}