package object;

import game.Game;
import processing.core.*;

public class Arch {
    public static PShape archPreview;
    public static PShape arch;
    public PVector location;
    private Game parent;

    public final static float columnCenter = 36;
    public final static float columnBoard = 40;
    public final static float columnHeight = 0;
    public final static float columnRadius = 10;
    public final static float archWidth = 52;
    public final static float sizeForPrev = 15;

    public PVector lColumn;
    public PVector rColumn;

    public int archId;
    public float rotation;


    public Arch(float posX, float posY, int id, float rotation,  Game parent) {
        location = new PVector(posX , posY );
        this.parent = parent;
        this.archId = id;
        this.rotation = rotation;

        lColumn = new PVector(- columnCenter, 0);
        rColumn = new PVector(columnCenter, 0);

        lColumn.rotate(rotation);
        rColumn.rotate(rotation);

        lColumn.add(location);
        rColumn.add(location);


    }

    public void preview(boolean isPlaceable, float rotation) {
        PVector loc = new PVector(parent.mouseX - parent.width / 2, parent.mouseY - parent.height / 2);
        parent.pushMatrix();

        parent.translate(loc.x, -37, loc.y);

        parent.rotateX((float) Math.PI);
        parent.rotateY(rotation);
        if(!isPlaceable) {
            parent.shape(archPreview);
        } else {
            parent.shape(arch);
        }
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