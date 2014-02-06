package com.garyclayburg.spider;
//import corejava.*;

import java.applet.Applet;
import java.awt.*;
import java.net.URL;

public class spiderjar extends Applet{
  public Image gif = null;

  public void init(){
    setLayout(null); //needed for applet
    startmeup();
  }

  public void startmeup(){ 
    URL u = this.getClass().getResource("CardImages/AC.gif");
    System.out.println("URL is: " + u);
    gif = getImage(u); 
    repaint();
  }  
 
  public boolean handleEvent(Event evt){
    if (evt.id == Event.WINDOW_DESTROY)
      System.exit(0);
    return super.handleEvent(evt);
  }

  public void paint(Graphics g){
    System.out.println("painting!...");
    g.setColor(Color.green); 
    Dimension d = size();
    g.fillRect(0,0,d.width,d.height);
    g.setColor(Color.red);
    g.drawLine(1,1,30,30);
    g.drawImage(gif,10,10,this);

  }
}
