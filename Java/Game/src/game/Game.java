package game;

import processing.core.*;

import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

public class Game extends PApplet {
    MovingBall ball;
    public ArrayList<Cylinder> cylinders;
    private float rotateY = 0;
    private float rotateX = 0;
    private float rotateZ = 0;
    private float prevMouseX;
    private float prevMouseY;
    public boolean birdView = false;
    private float boundX1;
    private float boundX2;
    private float boundZ1;
    private float boundZ2;
    public final float boardSize = 400;
    private final float boardHeight = 5;
    private float movMagnitude = 0.02f;
    private final float speedCoeff = 1.5f;
    private final float speedLowLimit = 0.01f;
    private final float speedUpperLimit = 0.1f;
    public Data dataPanel;

    public void setup() {
        size(800, 800, P3D);
        ball = new MovingBall(this);
        boundX1 = width / 2 - boardSize / 2;
        boundX2 = width / 2 + boardSize / 2;
        boundZ1 = height / 2 - boardSize / 2;
        boundZ2 = height / 2 + boardSize / 2;
        noStroke();
        noSmooth();
        cylinders = new ArrayList<Cylinder>();
        dataPanel = new Data(width, 120, this);
    }

    public void draw() {
        fill(255);
        background(0);
        lights();
        dataPanel.draw(0, height - 120);
        fill(255, 255, 255);
        textSize(15);
        text("rotationX: " + degrees(rotateX) + " rotationZ: " + degrees(rotateZ) + " rotation speed: " + movMagnitude + " cylinders: " + cylinders.size(), 10, 20, 0);
        translate(width / 2, height / 2, 0);
        if (birdView) {
            //view the board from the top to place cylinders
            textSize(25);
            text("SHIFT", boardSize / 2 + 20, boardSize / 2 - 20, 0);
            rotateX(-PI / 2);
            // Cylinder Preview
            if (cylinderCheckBoard() && cylinderCheckBall()) {
                fill(80, 98, 112);
            } else {
                fill(255, 0, 0);
            }
            Cylinder cylinderPrev = new Cylinder(mouseX - width / 2, mouseY - width / 2, this);
            cylinderPrev.display();
        } else {
            //normal view
            rotateY(rotateY);
            rotateZ(rotateZ);
            rotateX(rotateX);
            ball.checkEdges(boardSize / 2, boardSize / 2);
            ball.update(rotateX, rotateZ);
            ball.checkCylinderCollision();
        }
        fill(133, 158, 178);
        box(boardSize, boardHeight, boardSize);
        ball.display();
        fill(80, 98, 112);
        for (Cylinder cy : cylinders) {
            cy.display();
        }
    }

    @Override
    public void mouseClicked() {
        if (birdView) {
            //don't place cylinders outside of the board
            if (cylinderCheckBoard()) {
                //don't place cylinders on the ball
                if (cylinderCheckBall()) {
                    cylinders.add(new Cylinder(mouseX - width / 2, mouseY - width / 2, this));
                }
            }
        }
    }

    public void mouseDragged() {
        //don't rotate if in birdview
        if (birdView) return;

        if(dataPanel.bar.mouseOver || dataPanel.bar.locked) return;

        //rotation
        if (mouseY > prevMouseY) {
            rotateX -= movMagnitude;
        } else if (mouseY < prevMouseY) {
            rotateX += movMagnitude;
        }
        if (mouseX > prevMouseX) {
            rotateZ += movMagnitude;
        } else if (mouseX < prevMouseX) {
            rotateZ -= movMagnitude;
        }
        if (rotateX > PI / 3) {
            rotateX = PI / 3;
        } else if (rotateX < -PI / 3) {
            rotateX = -PI / 3;
        }
        if (rotateZ > PI / 3) {
            rotateZ = PI / 3;
        } else if (rotateZ < -PI / 3) {
            rotateZ = -PI / 3;
        }
        prevMouseY = mouseY;
        prevMouseX = mouseX;
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        float w = e.getWheelRotation();
        if (w < 0) {
            movMagnitude = movMagnitude * speedCoeff;
        } else if (w > 0) {
            movMagnitude = movMagnitude / speedCoeff;
        }
        if (movMagnitude < speedLowLimit) {
            movMagnitude = speedLowLimit;
        } else if (movMagnitude > speedUpperLimit) {
            movMagnitude = speedUpperLimit;
        }
    }

    @Override
    public void keyReleased() {
        if (key == CODED) {
            if (keyCode == SHIFT) {
                birdView = false;
            }
        }
    }

    public void keyPressed() {
        if (key == CODED) {
            if (keyCode == LEFT) {
                rotateY -= movMagnitude;
            } else if (keyCode == RIGHT) {
                rotateY += movMagnitude;
            } else if (keyCode == SHIFT) {
                birdView = true;
            }
        }
    }

    public boolean cylinderCheckBoard() {
        return (mouseX >= boundX1 + Cylinder.radius && mouseX <= boundX2 - Cylinder.radius && mouseY >= boundZ1 + Cylinder.radius && mouseY <= boundZ2 - Cylinder.radius);
    }

    public boolean cylinderCheckBall() {
        PVector mouse = new PVector(mouseX - width / 2, -22.5f, mouseY - width / 2);
        return (mouse.dist(ball.location) >= Cylinder.radius + MovingBall.radius);
    }

    static public void main(String[] args) {
        String[] appletArgs = new String[]{"game.Game"};
        PApplet.main(appletArgs);
    }

}
