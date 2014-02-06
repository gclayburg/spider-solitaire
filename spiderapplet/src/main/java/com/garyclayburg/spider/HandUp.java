package com.garyclayburg.spider;

import java.awt.*;
import java.awt.image.ImageObserver;

public class HandUp extends pile{
  public HandUp(int maxCards, Card[] ca, int x, int y){
    super(maxCards,ca,x,y);
  }

  public void pileToPile(pile newPile){
    if (newPile == null) 
      return;
    doReversePileToPile(newPile);
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

  //dummy to get this to compile
  public boolean addPile(pile newPile){
    return true;
  }

  public int paintPile(Graphics g, ImageObserver eyes, boolean blank){
    return 0;
  }

  public int paintPile(Graphics g, ImageObserver eyes){
    /* 
       post:	The hand is drawn to the screen in the right place.
       */
    if (maxCards == 1) {
      cardPile[maxCards-1].drawCard(g,x,y,eyes,true);
    }
    if (maxCards == 2) {
      cardPile[maxCards-2].drawCard(g,x,y,eyes,true);
      cardPile[maxCards-1].drawCard(g,x+solitaire.HANDSEP,y,eyes,true);
    }
    if (maxCards >= 3) {
      cardPile[maxCards-3].drawCard(g,x,y,eyes,true);
      cardPile[maxCards-2].drawCard(g,x+solitaire.HANDSEP,y,eyes,true);
      cardPile[maxCards-1].drawCard(g,x+solitaire.HANDSEP+solitaire.HANDSEP,y,eyes,true);
    }
    return 0;
  }

  public boolean mouseOnTopCard(int x, int y){
    switch(maxCards){
    case 0:
      if ( x>= this.x && x <= (this.x + Card.CARDWIDTH) &&
	   y >= this.y && y <= (this.y + Card.CARDHEIGHT))
	return true;
      break;
    case 1:
      if ( x>= this.x && x <= (this.x + Card.CARDWIDTH) &&
	   y >= this.y && y <= (this.y + Card.CARDHEIGHT))
	return true;
      break;
    case 2:
      if ( x >= (this.x + solitaire.HANDSEP) && 
	   x<= (this.x + solitaire.HANDSEP + Card.CARDWIDTH) &&
	   y >= this.y && y <= (this.y + Card.CARDHEIGHT))
	return true;
      break;
    case 3:
      if ( x >= (this.x + solitaire.HANDSEP + solitaire.HANDSEP) && 
	   x<= (this.x + solitaire.HANDSEP + solitaire.HANDSEP + Card.CARDWIDTH) &&
	   y >= this.y && y <= (this.y + Card.CARDHEIGHT))
	return true;
      break;
    default:
      if ( x >= (this.x + solitaire.HANDSEP + solitaire.HANDSEP) && 
	   x<= (this.x + solitaire.HANDSEP + solitaire.HANDSEP + Card.CARDWIDTH) &&
	   y >= this.y && y <= (this.y + Card.CARDHEIGHT))
	return true;
      break;
    }
    return false;
  }
  public Point topCardPoint(){
    Point p;
    switch(maxCards){
    case 0:
      p = new Point(x,y);
      break;
    case 1:
      p = new Point(x,y);
      break;
    case 2:
      p = new Point(x+solitaire.HANDSEP,y);
      break;
    case 3:
      p = new Point(x+solitaire.HANDSEP+solitaire.HANDSEP,y);
      break;
    default:
      p = new Point(x+solitaire.HANDSEP+solitaire.HANDSEP,y);
      break;
    }
    return p;
  }

  public String toString(){
    int i =0;
    String out =  "Hand Up: (" + maxCards + " cards): ";
    if (solitaire.debug) System.out.println("" + "up[0] =" + cardPile[0] );
    if (solitaire.debug) System.out.println("" + "up[1] =" + cardPile[1] );
    if (solitaire.debug) System.out.println("" + "up[2] =" + cardPile[2] );
    for (i=0; i < (maxCards ); i++) {
      if (cardPile[i] != null){
	out +=cardPile[i] + " ";
      }
    }
    out += "\n";
    return out;
  }

  private static Card[] handDown; // 0 is bottom of deck
  private static int handDownLength =0;
  private static Card[] handUp;  //  0 is bottom of deck
  private static int handUpLength =0;
}
