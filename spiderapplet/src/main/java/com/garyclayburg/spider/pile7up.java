package com.garyclayburg.spider;

import java.awt.*;
import java.awt.image.ImageObserver;

public class pile7up extends pile {
  public static final int UPSEP = 25;

  public pile7up(int maxUp, Card[] cUp, int x, int y){
    /*
      PRE: (x,y) is top-left of down pile
      POST: a "good" pile7up is created
      cUp is the lone up card
      cDown is an array of down cards
      */
    // create down pile
    super(maxUp,cUp,x,y);  //this is dumb.
  }
  public pile7up(int maxUp, Card[] cUp, Point topLeft){
    super(maxUp,cUp,topLeft.x,topLeft.y);
  }
//   public pile7up createpile(int num, Card[] c, Point p){
//     return new pile7up(num,c,p.x,p.y);
//   }

  //dummy to get this to compile
  public boolean addPile(pile newPile){
    return true;
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

  public int mouseOnACard(int x, int y){
    // returns: number of cards of this pile7up that is above the mouse.
    if (solitaire.debugking) System.out.println("" + "in mouseOnACard: (x,y) = " + x + "," + y );
    if (solitaire.debugking) System.out.println("thisxy = " + this.x + "," + this.y);
    if (maxCards >= 2){
      if ( x >= this.x && x <= (this.x + Card.CARDWIDTH)){  // found a good pile
	for (int i =maxCards;i>=1;i--){
	  if ( y >= (this.y + UPSEP * (i -1 ) ) &&
	       ( y <= (this.y + UPSEP * (i) ) )){
	    if (solitaire.debugking) System.out.println("" + "mouse is on a card under top card");
	    return (maxCards -i + 1);
	  }
	}

      }
      else{
	if (solitaire.debugking) System.out.println("not this pile");
      
	return 0;
      }
    }
    if (maxCards >= 1){
      if ( x >= this.x && x <= (this.x + Card.CARDWIDTH) &&
	   y >= (this.y + UPSEP * (maxCards -1 ) ) &&
	   y <= (this.y + UPSEP * (maxCards - 1) + Card.CARDHEIGHT) ){
	if (solitaire.debugking) System.out.println("" + "mouse is on this top card");
	return 1;
      }
      else {
	if (solitaire.debugking) System.out.println("mouse is not on this top card");
	return 0;
      }
    }
    
    return 0; 
  }

  public boolean mouseOnTopCard(int x, int y){
    /*
      returns: true if (x,y) is on a card at end of pileup
      */ 
    if (solitaire.debug) System.out.println("" + "in mouseOnTopCard: (x,y) = " + x + "," + y );
    if (solitaire.debug) System.out.println("thisxy = " + this.x + "," + this.y);
    if (maxCards >= 1){
      if ( x >= this.x && x <= (this.x + Card.CARDWIDTH) &&
	   y >= (this.y + UPSEP * (maxCards -1 ) ) &&
	   y <= (this.y + UPSEP * (maxCards - 1) + Card.CARDHEIGHT) ){
	if (solitaire.debug) System.out.println("" + "mouse is on this top card");
	return true;
      }
      else 
	return false;
    }
    else if (maxCards == 0) {
      if ( x >= this.x && x <= (this.x + Card.CARDWIDTH) &&
	   y >= (this.y ) &&
	   y <= (this.y + Card.CARDHEIGHT) ){
	if (solitaire.debug) System.out.println("" + "mouse is on this top EMPTY card");
	return true;
      }
    }
    return false; 
  }

  public boolean clickEgg(int x, int y){
    if (maxCards > 0)
      if ( x >= this.x && x <= (this.x + Card.CARDWIDTH) &&
	   y >= this.y && y <= (this.y ))
	return true;
    return false;
  }
    
  public void doCardToPile(Card c) {
    // actually adds the card without asking questions
    // this should only be called (externally) when time to flip card over
    if (solitaire.debug) System.out.println("" + " maxcards= " + maxCards);
    if (solitaire.debug) System.out.println("" + "card is " + c );
    cardPile[maxCards++] = c;
    if (solitaire.debug) System.out.println("" + "card is " + cardPile[maxCards -1] );
    if (solitaire.debug) System.out.println(""+  " maxcards= " + maxCards);
  }
  public boolean cardToPile(Card c){
    // post: pile has a new card on it if it fits.
    //  returns: true if card can be put on pile
    //           false if not
    if (maxCards == 0) { return false; }  // have empty pile here.
    if (c == null ) {return false; } // happens if drag from empty area to card.
    if (solitaire.debug) System.out.println("" + "cardToPile: maxCards is " + maxCards);
    if (solitaire.debug) System.out.println("" + "this pile is this" + this );
    if (solitaire.debug) System.out.println("" + "this card is this" + c );
    boolean alt_suit = ((c.isBlack() && cardPile[maxCards - 1].isRed()) ||
			(c.isRed() && cardPile[maxCards - 1].isBlack()) );
    if (alt_suit &&
	((c.getValue() + 1) == cardPile[maxCards -1].getValue()) ){
      doCardToPile(c);
      return true;
    }
    else{
      return false;
    }
  }

  public boolean pileToPile(pile newPile){
    // post: newPile is "dropped" on top of this pile if it fits, otherwise nothing happens.
    // returns: true if newPile can fit on this pile
    //          false if not
    //    if (newPile ==null) { return false; }
    if (solitaire.debugking) System.out.println("Checking for king.");
    if (maxCards == 0){
      if ( newPile.peekCard(0).getValue() != Card.KING) { return false; }
      if (solitaire.debugking) System.out.println("found a valid king!");
      doPileToPile(newPile);  // king pile to empty pile
      return true;
    }
    if (solitaire.debugking) System.out.println("done with king.");
    Card c = newPile.peekCard(0);
    if (solitaire.debug) System.out.println("" + "card on pileinmotion is " + c );
    boolean alt_suit = ((c.isBlack() && cardPile[maxCards - 1].isRed()) ||
			(c.isRed() && cardPile[maxCards - 1].isBlack()) );
    if (alt_suit &&
	((c.getValue() + 1) == cardPile[maxCards -1].getValue()) ){
      doPileToPile(newPile);
      return true;
    } 
    else{
      return false;
    }
  }

  public Point topCardPoint(){
    Point p = new Point(x,(y + (maxCards -1) * UPSEP));
    return p;
  }
  public int paintPile(Graphics g, ImageObserver eyes, boolean blank){
    return 0;
  }

  public int paintPile(Graphics g, ImageObserver eyes){
    /*
      pre:	pileNum is a valid pile (0-6)
      */
    int i =0;
    for (i=0; i < maxCards; i++){
      if (solitaire.debug) System.out.println("pile7up " + maxCards+ ": card is" + cardPile[i] + "num is" + i + " maxCards = " + maxCards);
      cardPile[i].drawCard(g,x,(y +0 + i * UPSEP),eyes, true);
    }
    return 0;
  }
     
}  
