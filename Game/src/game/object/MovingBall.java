package game.object;

import game.Game;
import processing.core.PApplet;
import processing.core.PVector;

public class MovingBall {
    private Game parent;
    public PVector location;
    public PVector velocity;
    public final static float radius = 15;

    private PVector gravityForce = new PVector(0, 0, 0);
    private final float gravityConstant = 0.1f;
    private final float normalForce = 1;
    private final float mu = 0.01f;
    private final float frictionMagnitude = normalForce * mu;
    private PVector friction;


    private boolean sameScore = false;

    public MovingBall(Game parent) {
        location = new PVector(0, -17.5f, 0);
        velocity = new PVector(0, 0, 0);
        this.parent = parent;
    }

    // unused at the moment
    public void reset() {
        location = new PVector(0, -17.5f, 0);
        velocity = new PVector(0, 0, 0);
    }

    public void update(float rotX, float rotZ) {
        gravityForce.x = PApplet.sin(rotZ) * gravityConstant;
        gravityForce.z = -PApplet.sin(rotX) * gravityConstant;
        friction = velocity.get();
        friction.mult(-1);
        friction.normalize();
        friction.mult(frictionMagnitude);
        velocity.add(gravityForce);
        velocity.add(friction);
        location.add(velocity);
    }

    public void display() {
        parent.pushMatrix();
        parent.translate(location.x, location.y, location.z);
        parent.fill(12,119,136);
        parent.sphere(radius);
        parent.popMatrix();
    }

    public void checkCylinderCollision() {

        for (Arch arch : parent.archs) {
            float deltaX = location.x - arch.lColumn.x;
            float deltaY = location.z - arch.lColumn.y;

            float deltaX2 = location.x - arch.rColumn.x;
            float deltaY2 = location.z - arch.rColumn.y ;

            if (PApplet.sqrt((deltaX * deltaX) + (deltaY * deltaY)) <= Arch.columnRadius + radius) {
                PVector n = new PVector(deltaX, deltaY);
                n.normalize();
                PVector v = new PVector(velocity.x, velocity.z);
                float te = 2 * v.dot(n);
                n.mult(te);
                v.sub(n);

                n = new PVector(deltaX, deltaY);
                n.normalize();
                n.mult(arch.columnRadius + radius);


                location.x = arch.lColumn.x + n.x;
                location.z = arch.lColumn.y + n.y;
                velocity.x = v.x;
                velocity.z = v.y;
            } else if (PApplet.sqrt((deltaX2 * deltaX2) + (deltaY2 * deltaY2)) <= Arch.columnRadius) {
                PVector n = new PVector(deltaX2, deltaY2);
                n.normalize();
                PVector v = new PVector(velocity.x, velocity.z);
                float te = 2 * v.dot(n);
                n.mult(te);
                v.sub(n);

                n = new PVector(deltaX2, deltaY2);
                n.normalize();
                n.mult(arch.columnRadius + radius);


                location.x = arch.rColumn.x + n.x;
                location.z = arch.rColumn.y + n.y;

                velocity.x = v.x;
                velocity.z = v.y;
            }

            float deltaTime = 1.50f;  //Delta time for reUpdate all code.

            // TODO: SCORE DETECTION!
            // SEE: Check if point is inside rectangle
            boolean score =  ((location.x >= arch.location.x - Arch.archWidth) && (location.x < arch.location.x + Arch.archWidth ) && (location.z <= arch.location.y + deltaTime) && (location.z >= arch.location.y - deltaTime)  ) ;
            boolean diffArch = ( arch.archId != parent.lastArchId );
            if (parent.archNbr == 1) {
                diffArch = true; //If we have only one arch on the game, the lastArch is obviously the same.
            }

            if(score && !sameScore && diffArch){ //It's a win
                parent.successfulPasses = parent.successfulPasses + 1;
                sameScore = true;

                parent.lastArchId = arch.archId;
                parent.dataPanel.archSuccess();
            }
            else{
                sameScore = false;
            }
        }

    }

    public void checkEdges(float boundX, float boundZ) {
        if (location.x > boundX) {
            velocity.x = -velocity.x;
            location.x = boundX;
            parent.dataPanel.boundHit();
        } else if (location.x < -boundX) {
            velocity.x = -velocity.x;
            location.x = -boundX;
            parent.dataPanel.boundHit();
        }
        if (location.z > boundZ) {
            velocity.z = -velocity.z;
            location.z = boundZ;
            parent.dataPanel.boundHit();
        } else if (location.z < -boundZ) {
            velocity.z = -velocity.z;
            location.z = -boundZ;
            parent.dataPanel.boundHit();
        }
    }
}