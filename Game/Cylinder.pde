public class Cylinder {

    private Game parent;
    public final static float radius = 15;
    private final float cylinderHeight = 35;
    private final int cylinderResolution = 40;
    public PVector location;
    PShape cylinder = new PShape();
    

    public Cylinder(float posX, float posY, Game parent) {
        location = new PVector(posX, posY);
        this.parent = parent;
        float angle;
        float[] x = new float[cylinderResolution + 1];
        float[] y = new float[cylinderResolution + 1];

        for (int i = 0; i < x.length; i++) {
            angle = (PConstants.TWO_PI / cylinderResolution) * i;
            x[i] = (PApplet.sin(angle) * radius) + posX;
            y[i] = (PApplet.cos(angle) * radius) + posY;
        }

        // apparently we can't use two createShape in the same shape? It works though
        // if needed we can fix it by building the shapes separately
        cylinder = createShape();

        //Bottom part
        cylinder.beginShape(PConstants.TRIANGLES);
        for (int i = 0; i < x.length - 1; i++) {
            cylinder.vertex(x[i], y[i], 0);
            cylinder.vertex(x[i + 1], y[i + 1], 0);
            cylinder.vertex(posX, posY, 0);
         }
        cylinder.vertex(x[x.length-1], y[x.length-1], 0);
        cylinder.vertex(x[0], y[0], 0);
        cylinder.vertex(posX, posY, 0);
        cylinder.endShape();
        
        //Top part
        cylinder.beginShape(PConstants.TRIANGLES);
        for (int i = 0; i < x.length - 1; i++) {
            cylinder.vertex(x[i], y[i], cylinderHeight);
            cylinder.vertex(x[i + 1], y[i + 1], cylinderHeight);
            cylinder.vertex(posX, posY, cylinderHeight);
        }
        cylinder.vertex(x[x.length-1], y[x.length-1], cylinderHeight);
        cylinder.vertex(x[0], y[0], cylinderHeight);
        cylinder.vertex(posX, posY, cylinderHeight);
        cylinder.endShape();
      
        //Border part
        cylinder.beginShape(PConstants.QUAD_STRIP);
        for (int i = 0; i < x.length; i++) {
            cylinder.vertex(x[i], y[i], 0);
            cylinder.vertex(x[i], y[i], cylinderHeight);
        }
        cylinder.endShape();

    }

    public void display() {
        parent.pushMatrix();
        parent.rotateX(PConstants.PI / 2);
        parent.shape(cylinder);
        parent.popMatrix();
    }
}
