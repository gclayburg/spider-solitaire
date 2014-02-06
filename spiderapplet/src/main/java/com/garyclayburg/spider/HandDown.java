package com.garyclayburg.spider;

import java.awt.*;
import java.awt.image.ImageObserver;

public class HandDown extends pile{
  private static final int MAXWIDTH = 5* Spider.HANDSEP;

  public HandDown(int maxCards, Card[] ca, int x, int y){
    super(maxCards,ca,x,y);
  }

  public boolean mouseOnTopCard(int x, int y){
//      if ( x >= this.x + Spider.HANDSEP*(5 - maxCards / 10) &&
    if ( x >= this.x  && 
	 x <= (this.x + Spider.HANDSEP*(5 - maxCards / 10) + Card.CARDWIDTH) &&
	 y >= (this.y ) &&
	 y <= (this.y + Card.CARDHEIGHT) )
      return true;
    return false;
  }

  public boolean mouseOnTopCard(Point p1){
    /* pre: p1 is top left of card being moved
       post: is region bounded by p1 and  a subset of this top card?
       */ 
//      System.out.println("pile.mouseOnTopCard(" + p1 +")");
    Rectangle pileInMotionRect = new Rectangle(p1.x + Spider.PILESEP,0,Card.CARDWIDTH + Spider.PILESEP,Card.CARDHEIGHT);
    Rectangle handDownRect= new Rectangle(topLeft.x,topLeft.y,Card.CARDWIDTH + MAXWIDTH,Card.CARDHEIGHT);
//      Rectangle r2= new Rectangle(this.x,this.y,Card.CARDWIDTH,Card.CARDHEIGHT);
    return pileInMotionRect.intersects(handDownRect);
  }

  
  public boolean addPile(pile newPile){
    pileToPile(newPile);
    return true;
  }

  public void pileToPile(pile newPile){
    if (newPile == null) 
      return;
    //    if (solitaire.debughand) System.out.println("doing doReverse PileToPile");
    this.doReversePileToPile(newPile);
  }

  public Point topCardPoint(){
    Point p = new Point(x,y);
    return p;
  } 

  public pile popTopCard(int num){
  // pre: there are at least num cards to pop off
  // post: num cards are returned and popped off pile (or as many as we can pop off)
    Point pointOfTop;
    //    pileint owner = pi;  // Interface for calling pile subclass - should be pile7up or pile10up
    if (maxCards == 0) { return null; }
    if (maxCards < num) {
      num = maxCards; //pop all that we have.
    }
    Card c[] = new Card[num];
    for (int i =num -1;i>=1;i--){
      c[i] = popTopCard();
    }
    pointOfTop = topCardPoint();  //tricky part.  should use polymorphism here to drill down to correct extended pile.
    c[0] = popTopCard();
    pile p = new pile10up(num,c,pointOfTop);  // can't use plain old pile. Can't instantiate abstract class.
    // could use callbacks via interfaces or make pile non-abstract and instantiate pile here.
    // or I could subclass this method to both pile7up and pile10up
    return p;
  }

  public int paintPile(Graphics g, ImageObserver eyes,boolean junk){
    return 0;
  }
  public int paintPile(Graphics g, ImageObserver eyes,boolean junk, boolean junk2){
    return 0;
  }
    
  public int paintPile(Graphics g, ImageObserver eyes){    
    /* 
       post:	The hand is drawn to the screen in the right place.
       */
    int i =5; // 10 cards at a time go to 10 piles (5 sets in hand to start)
    if (maxCards !=0) {
      for(;i >(5-(maxCards / 10));i--)
	cardPile[0].drawCard(g,x+ Spider.HANDSEP*(i-1),y,eyes,false);
      //	cardPile[0].drawCard(g,x,y-2*(i-1),eyes,false);
    }
    return 0;
  }

  public String toString(){
    int i =0;
    //    if (solitaire.debug) System.out.println("" + "maxCards = " + maxCards );
    String out = "HandDown : (" + maxCards + " cards): ";
    for (i=0; i < (maxCards ); i++){
      if (cardPile[i] != null){
	out += cardPile[i] + " " ;
      }
    }
    out += "\n";
    return out;
  }
  // no variables.
}
