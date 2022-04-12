import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Trilateration_animation extends PApplet {



PImage blmap;
PImage antenna;

public void setup() {
  //size(1200, 700);
  
  textSize(26);
  
  blmap = loadImage("BanjaLukaMap.png");
  //blmap.resize(1200, 700);
  blmap.resize(width, height);
  
  antenna = loadImage("transmitter.png");
  antenna.resize(50, 50);
}

public class Point {
  private float x;
  private float y;
  
  private float incrementalCounuter = 0;
  
  public Point(float x, float y) {
    this.x = x;
    this.y = y;
  }
  
  public void checkForMovement(){
   if(mousePressed == true && abs(mouseX - x) < 50 && abs(mouseY - y) < 50){
     x = mouseX;
     y = mouseY;
   }
  }
  
  public void setX(float x) {
    this.x = x;
  }
  
  public void setY(float y) {
    this.y = y;
  }
  
  public float getX() {
    return x;
  }
  
  public float getY() {
    return y;
  }
  
  public float getDistanceTo(Point p) {
    return sqrt( (x - p.x) * (x - p.x) + (y - p.y) * (y - p.y));
  }
  
  public String toString() {
   return "X: " + x + "  Y: " + y; 
  }
  
  public void drawPoint(){
    image(antenna, x - 25, y - 25);
    incrementalCounuter += 1.2f;
    if(incrementalCounuter > 200) incrementalCounuter = 0;
    
    noFill();
    stroke(0);
    
    
    for(int i = 0; i < 5; i ++){
     float r = ((int)(incrementalCounuter + 50 *  i)) % 200;
     strokeWeight(2);
     circle(x, y, r); 
    }
    strokeWeight(1);
  }
  
}

Point a = new Point(random(50, 1000), random(50, 700));
Point b = new Point(random(50, 1000), random(50, 700));
Point c = new Point(random(50, 1000), random(50, 700));

public void draw() {
  //background(76);
  background(blmap);
  
  textSize(30);
  text("Simulacija algoritma trijangulacije", 4 * width / 10, 50);
  
  a.checkForMovement();
  b.checkForMovement();
  c.checkForMovement();
  
  Point mouseP = new Point(mouseX, mouseY);
  
  float da = a.getDistanceTo(mouseP);//sqrt((mouseX - a_x) * (mouseX - a_x) + (mouseY - a_y) * (mouseY - a_y));
  float db = b.getDistanceTo(mouseP);//sqrt((mouseX - b_x) * (mouseX - b_x) + (mouseY - b_y) * (mouseY - b_y));
  float dc = c.getDistanceTo(mouseP);//sqrt((mouseX - c_x) * (mouseX - c_x) + (mouseY - c_y) * (mouseY - c_y));
  
  Point a_of = new Point(0, 0);
  Point b_of = new Point(b.getX() - a.getX(), b.getY() - a.getY());
  Point c_of = new Point(c.getX() - a.getX(), c.getY() - a.getY());
  
  println("a: " + a_of);
  println("b: " + b_of);
  println("c: " + c_of);
  
  float angle = atan2(b_of.getY(), b_of.getX());
  
  println("Ugao " + angle * 180 / 3.1415f);
  
  Point b_rot = new Point(b_of.getX() * cos(angle) + b_of.getY() * sin(angle), - b_of.getX() * sin(angle) + b_of.getY() * cos(angle));
  Point c_rot = new Point(c_of.getX() * cos(angle) + c_of.getY() * sin(angle), - c_of.getX() * sin(angle) + c_of.getY() * cos(angle));
  
  println("Nakon rotacije a " + a_of);
  println("Nakon rotacije b " + b_rot);
  println("Nakon rotacije c " + c_rot);
  
  float mouse_X = (da * da - db * db + b_rot.getX() * b_rot.getX()) / (2 * b_rot.getX());
  float mouse_Y = (da * da - dc * dc + c_rot.getX() * c_rot.getX() + c_rot.getY() * c_rot.getY() - 2 * c_rot.getX() * mouse_X) / (2 * c_rot.getY());
  
  println("mouse X prije antirotacije " + mouse_X);
  println("mouse y prije antirotacije " + mouse_Y);
  
  float mouse_X_rot_back = mouse_X * cos(-angle) + mouse_Y * sin(-angle);
  float mouse_Y_rot_back = - mouse_X * sin(-angle) + mouse_Y * cos(-angle);
  
  Point calc_mouse = new Point(mouse_X_rot_back + a.getX(), mouse_Y_rot_back + a.getY());
  
  //translate(100, 100);
  
  line(a.getX(), a.getY(), mouseX, mouseY);
  line(b.getX(), b.getY(), mouseX, mouseY);
  line(c.getX(), c.getY(), mouseX, mouseY);
  
  textSize(15);
  text("Udaljenost od prve predajne stanice: " + da / 25.0f + " km.", a.getX() - 100, a.getY() + 25);
  text("Udaljenost od druge predajne stanice: " + db / 25.0f + " km.", b.getX() - 100, b.getY() + 25);
  text("Udaljenost od trece predajne stanice: " + dc / 25.0f + " km.", c.getX() - 100, c.getY() + 25);
  
  textSize(20);
  
  fill(255);
  text("Stvarna pozicija: " + (mouseX * (44.58360f - 45.02073f) / width + 45.02019f) + " " + (mouseY * (17.82118f - 16.55340f) / height + 16.55234f), mouseX + 10, mouseY + 10);
  
  text("Izracunata pozicija: " + (calc_mouse.getX() * (44.58360f - 45.02073f) / width + 45.02019f) + " " + (calc_mouse.getY() * (17.82118f - 16.55234f) / height + 16.55340f), mouseX + 10, mouseY - 10);
  textSize(26);
  
  //fill(255, 0, 0);
  //ellipse(a.getX(), a.getY(), 10, 10);
  //ellipse(b.getX(), b.getY(), 10, 10);
  //ellipse(c.getX(), c.getY(), 10, 10);
  a.drawPoint();
  b.drawPoint();
  c.drawPoint();
  
  noFill();
  stroke(220, 220, 255);
  circle(a.getX(), a.getY(), 2 * da);
  stroke(255, 220, 220);
  circle(b.getX(), b.getY(), 2 * db);
  stroke(220, 255, 220);
  circle(c.getX(), c.getY(), 2 * dc);
  
  stroke(0);
  if (mousePressed) {
    fill(0);
  } else {
    fill(255);
  }
  ellipse(mouseX, mouseY, 10, 10);
  
  text("Pritisni na predajne stanice da ih pomjeras", 7 * width / 10, 14 * height / 15);
  
}
  public void settings() {  fullScreen(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Trilateration_animation" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
