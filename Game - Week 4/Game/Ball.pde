class Ball {
  PVector location;
  PVector velocity;

  PVector gravity;
  float gravityConstant = 0.15;

  PVector friction;
  float normalForce = 1;
  float mu = 0.03;
  float frictionMagnitude = normalForce * mu;
  
  float sphereRadius = 20;

  Ball() {
    location = new PVector(0, -sphereRadius/2, 0);
    velocity = new PVector(0, 0, 0);
    gravity  = new PVector(0, 0, 0);
  }

  void update() {
    
      friction = velocity.get();
      friction.mult(-1);
      friction.normalize();
      friction.mult(frictionMagnitude);
    
      gravity.set(sin(rotationZ)*gravityConstant, 0, -sin(rotationX) * gravityConstant);    
      
      velocity.add(gravity);
      velocity.add(friction);
      location.add(velocity);
  }  
  
  void display() {
    stroke(0);
    strokeWeight(2);
    fill(127);
    
    pushMatrix();
    
    translate(location.x, location.y - sphereRadius, location.z);
    sphere(sphereRadius);  
    
    popMatrix();
}
  
  void checkEdges() {
    
    if ( location.x  > boxe.getWidth()/2 ) {
        velocity.x = -1.0 * velocity.x;
        location.x =  boxe.getWidth()/2 ;
    }
    else if (location.x < -boxe.getWidth()/2 ) {
        velocity.x = -1.0 * velocity.x;
        location.x = - boxe.getWidth()/2;
    }
    
    
    if (location.z  > (boxe.getWidth()/2  )){
        velocity.z = -1.0 * velocity.z;
        location.z =  boxe.getWidth()/2 ;
    }
    else if (location.z  < -1.0 * boxe.getWidth()/2  ) {
        velocity.z = -1.0 * velocity.z;
        location.z =  -boxe.getWidth()/2 ;
    } 
    
  }
  
  
}

