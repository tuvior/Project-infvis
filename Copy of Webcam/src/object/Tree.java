package object;

import game.Game;
import processing.core.*;

import java.awt.*;

public class Tree {
    public final static float radius = 10;
    PShape tree;
    float rotX = 0;
    public PVector location;
    Game parent;

    public Tree(float posX, float posY, Game parent) {
        location = new PVector(posX, posY);
        this.parent = parent;
        tree = parent.loadShape("simpleTree.obj");
        tree.scale(-40);
    }

    public void display(boolean isPlaceable) {
        parent.pushMatrix();
        parent.translate(location.x, 0, location.y);
        if(!isPlaceable) tree.setFill(parent.color(255,0,0));
        parent.shape(tree);
        parent.popMatrix();
    }

    public void display() {
        parent.pushMatrix();
        parent.translate(location.x, 0, location.y);
        parent.shape(tree);
        parent.popMatrix();
    }


}
