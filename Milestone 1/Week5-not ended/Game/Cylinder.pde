class Cylinder{
  
  float cylinderBaseSize = 20;
  float cylinderHeight = 40;
  int cylinderResolution = 20;
  
  float x;
  float y;
  
  PShape closedCylinder;
  
  Cylinder(float x, float y){
     closedCylinder = new PShape(); 
     this.x = x;
     this.y = y;
  }
  
  
  void initialize() {
    
    float angle;
    float[] x = new float[cylinderResolution + 1];
    float[] y = new float[cylinderResolution + 1];
    
    //get the x and y position on a circle for all the sides
    for(int i = 0; i < x.length; i++) {
      angle = (TWO_PI / cylinderResolution) * i;
      x[i] = sin(angle) * cylinderBaseSize;
      y[i] = cos(angle) * cylinderBaseSize;
    }
      
    closedCylinder = createShape();
    
    // bottom surface of the cylinder
    closedCylinder.beginShape(TRIANGLES);
    for (int i = 0; i < x.length -1; i++){
      closedCylinder.vertex(x[i],y[i],0);
      closedCylinder.vertex(x[i+1],y[i+1],0);
      closedCylinder.vertex(0,0,0);
    }
    //faire le dernier manuellement
    closedCylinder.vertex(x[x.length-1],y[x.length-1],0);
    closedCylinder.vertex(x[0],y[0],0);
    closedCylinder.vertex(0,0,0);
    closedCylinder.endShape();
    
    // top surface of the cylinder
    closedCylinder.beginShape(TRIANGLES);
    for (int i = 0; i < x.length -1; i++){
      closedCylinder.vertex(x[i],y[i],cylinderHeight);
      closedCylinder.vertex(x[i+1],y[i+1],cylinderHeight);
      closedCylinder.vertex(0,0,cylinderHeight);
    }
    //faire le dernier manuellement
    closedCylinder.vertex(x[x.length-1],y[x.length-1],cylinderHeight);
    closedCylinder.vertex(x[0],y[0],cylinderHeight);
    closedCylinder.vertex(0,0,cylinderHeight);
    closedCylinder.endShape();  
    
    
    //draw the border of the cylinder
    closedCylinder.beginShape(QUAD_STRIP);
    for(int i = 0; i < x.length; i++) {
      closedCylinder.vertex(x[i], y[i] , 0);
      closedCylinder.vertex(x[i], y[i], cylinderHeight);
    }
    closedCylinder.endShape();
    
  }
  
  
  void display() {
    pushMatrix();
    
    translate(x, y, 0);
    shape(closedCylinder);
  
    popMatrix();  
}
  
  
}



