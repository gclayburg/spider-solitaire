package com.garyclayburg.spider;

import java.awt.*;
import java.awt.image.ImageObserver;

public class pile7down extends pile {
  public static final int DOWNSEP = 10;
  
  public pile7down(int maxDown, Card[] cDown, int x, int y){
    /*
      PRE: (x,y) is top-left of down pile
      POST: a "good" pile7down is created
      cUp is the lone up card
      cDown is an array of down cards
      */
    // create down pile
    super(maxDown,cDown,x,y);
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
    pile p = new pile7up(num,c,pointOfTop);  // can't use plain old pile. Can't instantiate abstract class.
    // could use callbacks via interfaces or make pile non-abstract and instantiate pile here.
    // or I could subclass this method to both pile7up and pile10up
    return p;
  }

  public boolean clickEgg(int x, int y){
    if (maxCards > 0)
      if ( x >= this.x && x <= (this.x + Card.CARDWIDTH) &&
	   y >= this.y && y <= (this.y + DOWNSEP))
	return true;
    return false;
  }
 
  public int paintPile(Graphics g, ImageObserver eyes){
    /*
      pre:	pileNum is a valid pile (0-6)
      */
    int i =0;
    if (maxCards != 0 ) {
      cardPile[0].drawCard(g,x,y,eyes, false);
      //      if (solitaire.debug) System.out.print("" + "down at (" + x +"," +y +")    " );
    }
    return 0;
  }
    
  public int paintPile(Graphics g, ImageObserver eyes, boolean blank){
    return 0; // dummy
  }

  //dummy to get this to compile
  public boolean addPile(pile newPile){
    return true;
  }

  private Card[] pileUp;
  private int numPileUp;
}  
