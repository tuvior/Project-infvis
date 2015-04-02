import processing.core.PApplet;
import processing.core.PVector;

public class MovingBall {
    private Game parent;
    public PVector location;
    public PVector velocity;
    public final static float radius = 20;

    private PVector gravityForce = new PVector(0, 0, 0);
    private final float gravityConstant = 0.1f;
    private final float normalForce = 1;
    private final float mu = 0.01f;
    private final float frictionMagnitude = normalForce * mu;
    private PVector friction;


private boolean sameScore = false;

    public MovingBall(Game parent) {
        location = new PVector(0, -22.5f, 0);
        velocity = new PVector(0, 0, 0);
        this.parent = parent;
    }

    // unused at the moment
    public void reset() {
        location = new PVector(0, -22.5f, 0);
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
            float deltaX = location.x - arch.location.x + Arch.columnCenter;
            float deltaY = location.z - arch.location.y;
                                  
          if (PApplet.sqrt((deltaX * deltaX) + (deltaY * deltaY)) <= Arch.columnRadius) {
                PVector n = new PVector(deltaX, deltaY);
                n.normalize();
                PVector v = new PVector(velocity.x, velocity.z);
                float te = 2 * v.dot(n);
                n.mult(te);
                v.sub(n);
             
                velocity.x = v.x;
                velocity.z = v.y;
                parent.dataPanel.archHit();
            }
            
            float delta2X = location.x - arch.location.x - Arch.columnCenter;
            float delta2Y = location.z - arch.location.y;
            
            if (PApplet.sqrt((delta2X * delta2X) + (delta2Y * delta2Y)) <= Arch.columnRadius) {
                PVector n = new PVector(delta2X, delta2Y);
                n.normalize();
                PVector v = new PVector(velocity.x, velocity.z);
                float te = 2 * v.dot(n);
                n.mult(te);
                v.sub(n);
               
                velocity.x = v.x;
                velocity.z = v.y;
                parent.dataPanel.archHit();
            }
            
            float deltaTime = 1.50f;  //Delta time for reUpdate all code. 
            boolean score =  ((location.x >= arch.location.x - Arch.archWidth) && (location.x < arch.location.x + Arch.archWidth ) && (location.z <= arch.location.y + deltaTime) && (location.z >= arch.location.y - deltaTime)  ) ;
                      
            if(score && !sameScore){
              parent.scoreScore = parent.scoreScore + 1;
              sameScore = true;
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