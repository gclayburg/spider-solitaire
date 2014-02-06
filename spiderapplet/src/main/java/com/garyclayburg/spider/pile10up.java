package com.garyclayburg.spider;

import java.awt.*;
import java.awt.image.ImageObserver;

public class pile10up extends pile {
  public static final int UPSEP = 25;
  public static final int UPSEP_SAMESUIT = 5;

  public pile10up(int maxUp, Card[] cUp, int x, int y){
    /*
      PRE: (x,y) is top-left of down pile
      POST: a "good" pile7up is created
      cUp is the lone up card
      cDown is an array of down cards
      */
    // create down pile
    super(maxUp,cUp,x,y);  //this is dumb.
  }
  public pile10up(int maxUp, Card[] cUp, Point topLeft){
    super(maxUp,cUp,topLeft.x,topLeft.y);
  }

  public pile popTopCard(int num){
  // pre: there are at least num cards to pop off
  // post: num cards are returned and popped off pile (or as many as we can pop off)
    Point pointOfTop;
    //    pileint owner = pi;  // Interface for calling pile subclass - should be pile7up or pile10up
    //    if (spider.debug) System.out.println("pile10up: popTopCard(int)");
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
    if (spider.debug) System.out.println("pile10up: popTopCard(int) returning pile" + p);
    return p;
  }

  private int cardsUnderMouse(int x, int y){
    // returns: number of card at x,y counted from the top of the pile.  
    //          Card closest to the top of the screen is #1.
    int ret = 0;
    int prevSuit = 0;
    int prevSuit2 = 0;
    int prevVal = 0;
    int prevVal2 = 0;
    int prev_y = this.y;
    int inc =0;
    //    if (spider.debug) System.out.println("(x,y) at cardUnderMouse = (" + x + "," + y + ")  this = ( "+ this.x + "," +this.y);
    if (x >= this.x && x <= (this.x + Card.CARDWIDTH) && (y >= this.y)){
//       if ((maxCards ==0) && (y <= this.y + Card.CARDHEIGHT))
// 	ret =0;  // found an empty pile
      
      for (int i = 0; i <maxCards; i++){
	//	if (spider.debug) System.out.println("Start for, i =" + i);

	if ((prevSuit == cardPile[i].getSuit() ) && ( (prevVal -1) == cardPile[i].getValue()) &&
	    (maxCards -1 != i) && (cardPile[i].getSuit() == cardPile[i+1].getSuit()) &&
	    ((cardPile[i].getValue() -1) == cardPile[i+1].getValue() ))

	  inc = UPSEP_SAMESUIT;
	else
	  inc = UPSEP;

	if ( i == (maxCards -1))
	  if (y <= (prev_y +Card.CARDHEIGHT)){
	    ret = i + 1;
	    //	    if (spider.debug) System.out.println("got card on the end: ret =" + ret);
	  }
	prev_y += inc;
	prevVal2 = prevVal;
	prevVal = cardPile[i].getValue();
	prevSuit2 = prevSuit;
	prevSuit = cardPile[i].getSuit();

	if (y <= prev_y ){
	  //	  if (spider.debug) System.out.println("found card: " + i);
	  ret = i +1;  // we found the card!
	  break;
	}
      }
    }
    //    System.out.println(ret + " cardsUnderMouse.");

    return ret;
  }	     

  public Point topCardPoint(){
    // returns: number of card at x,y counted from the top of the pile.  
    //          Card closest to the top of the screen is #1.
    Point ret = new Point(0,0);
    int prevSuit = 0;
    int prevSuit2 = 0;
    int prevVal = 0;
    int prevVal2 = 0;
    int prev_y = this.y;
    int inc =0;
    //    if (spider.debughole) System.out.println("(x,y) at topCardPoint = (" + x + "," + y + ")  this = ( "+ this.x + "," +this.y);
    for (int i = 0; i <maxCards; i++){
      //      if (spider.debughole) System.out.println("topCardPoint: Start for, i =" + i);

      if ((prevSuit == cardPile[i].getSuit() ) && ( (prevVal -1) == cardPile[i].getValue()) &&
	  (maxCards -1 != i) && (cardPile[i].getSuit() == cardPile[i+1].getSuit()) &&
	  ((cardPile[i].getValue() -1) == cardPile[i+1].getValue() ))

	inc = UPSEP_SAMESUIT;
      else
	inc = UPSEP;

      if ( i == (maxCards -1)){
	ret = new Point(x,prev_y);
	//	if (spider.debughole) System.out.println("got card on the end: ret =" + ret);
      }
      prev_y += inc;
      prevVal2 = prevVal;
      prevVal = cardPile[i].getValue();
      prevSuit2 = prevSuit;
      prevSuit = cardPile[i].getSuit();

    }
    //    if (spider.debughole) System.out.println(ret + " cardsUnderMouse.");
    return ret;
  }	     

//   public Point topCardPoint(){
//     Point p = new Point(x,(y + (maxCards -1) * UPSEP));
//     return p;
//   }

  public int mouseOnACard(int x, int y){
    // returns: number of cards of this pile10up that is above the mouse.
    return (maxCards - cardsUnderMouse(x,y) + 1);
  }

//     if (solitaire.debugking) System.out.println("" + "in mouseOnACard: (x,y) = " + x + "," + y );
//     if (solitaire.debugking) System.out.println("thisxy = " + this.x + "," + this.y);
//     if (maxCards >= 2){
//       if ( x >= this.x && x <= (this.x + Card.CARDWIDTH)){  // found a good pile
// 	for (int i =maxCards;i>=1;i--){
// 	  if ( y >= (this.y + UPSEP * (i -1 ) ) &&
// 	       ( y <= (this.y + UPSEP * (i) ) )){
// 	    if (solitaire.debugking) System.out.println("" + "mouse is on a card under top card");
// 	    return (maxCards -i + 1);
// 	  }
// 	}

//       }
//       else{
// 	if (solitaire.debugking) System.out.println("not this pile");
      
// 	return 0;
//       }
//     }
//     if (maxCards >= 1){
//       if ( x >= this.x && x <= (this.x + Card.CARDWIDTH) &&
// 	   y >= (this.y + UPSEP * (maxCards -1 ) ) &&
// 	   y <= (this.y + UPSEP * (maxCards - 1) + Card.CARDHEIGHT) ){
// 	if (solitaire.debugking) System.out.println("" + "mouse is on this top card");
// 	return 1;
//       }
//       else {
// 	if (solitaire.debugking) System.out.println("mouse is not on this top card");
// 	return 0;
//       }
//     }
    
//     return 0; 
//   }

  public boolean moveableCard(int x, int y){
    // returns: whether or not the card under the mouse pos on a spider pile can be moved.
    int i;
    int pos = cardsUnderMouse(x,y);
    if (spider.debughole) System.out.println("pos     = " + pos);
    if (spider.debughole) System.out.println("maxCards = " + maxCards);
    if (pos == 0 ) return false;
    if (maxCards <= 1) return true;
    boolean ret = true; //innocent until proven guily!
    for (i = pos -1; i < (maxCards -1); i++){
      if (spider.debughole) System.out.println("loop = " + i);

      if ((cardPile[i].getSuit() != cardPile[i+1].getSuit()) ||
	  (cardPile[i].getValue() -1 != cardPile[i+1].getValue()) ){
	ret = false;
	break;
      }
    }
    return ret;
  }

  public boolean mouseOnTopCard(int x, int y){
    /*
      returns: true if (x,y) is on a card at end of pileup
      */ 
    if (spider.debug) System.out.println("pile10up.mouseOnTopCard(" + x + "," + y +")");
    if ((maxCards ==0) && (x >= this.x && x <= (this.x + Card.CARDWIDTH)) &&
	(y >= this.y) && (y >= this.y && y <= (this.y + Card.CARDWIDTH)))
      return true;
    if (cardsUnderMouse(x,y) == 0) return false;
    return (maxCards == cardsUnderMouse(x,y));
  }

//     if (solitaire.debug) System.out.println("" + "in mouseOnTopCard: (x,y) = " + x + "," + y );
//     if (solitaire.debug) System.out.println("thisxy = " + this.x + "," + this.y);
//     if (maxCards >= 1){
//       if ( x >= this.x && x <= (this.x + Card.CARDWIDTH) &&
// 	   y >= (this.y + UPSEP * (maxCards -1 ) ) &&
// 	   y <= (this.y + UPSEP * (maxCards - 1) + Card.CARDHEIGHT) ){
// 	if (solitaire.debug) System.out.println("" + "mouse is on this top card");
// 	return true;
//       }
//       else 
// 	return false;
//     }
//     else if (maxCards == 0) {
//       if ( x >= this.x && x <= (this.x + Card.CARDWIDTH) &&
// 	   y >= (this.y ) &&
// 	   y <= (this.y + Card.CARDHEIGHT) ){
// 	if (solitaire.debug) System.out.println("" + "mouse is on this top EMPTY card");
// 	return true;
//       }
//     }
//     return false; 
//   }

  public boolean clickEgg(int x, int y){
    if (maxCards > 0)
      if ( x >= this.x && x <= (this.x + Card.CARDWIDTH) &&
	   y >= this.y && y <= (this.y ))
	return true;
    return false;
  }
  public void doCardToPile(Card c,int new_y){
    cardPile[maxCards++] = c;
    y -= new_y;
    offSetY -= new_y;
  }
  public void doCardToPile(Card c) {
    // actually adds the card without asking questions
    // this should only be called (externally) when time to flip card over
//     if (solitaire.debug) System.out.println("" + " maxcards= " + maxCards);
//     if (solitaire.debug) System.out.println("" + "card is " + c );
    cardPile[maxCards++] = c;
//     if (solitaire.debug) System.out.println("" + "card is " + cardPile[maxCards -1] );
//     if (solitaire.debug) System.out.println(""+  " maxcards= " + maxCards);
  }
  public boolean cardToPile(Card c){
    // post: pile has a new card on it if it fits.
    //  returns: true if card can be put on pile
    //           false if not
    if (maxCards == 0) { return false; }  // have empty pile here.
    if (c == null ) {return false; } // happens if drag from empty area to card.
//     if (solitaire.debug) System.out.println("" + "cardToPile: maxCards is " + maxCards);
//     if (solitaire.debug) System.out.println("" + "this pile is this" + this );
//     if (solitaire.debug) System.out.println("" + "this card is this" + c );
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
  
  public boolean addPile(pile newPile){
    return pileToPile(newPile);
  }

  public boolean pileToPile(pile newPile){
    // pre: newPile is a valid Spider pile
    // post: newPile is "dropped" on top of this pile if it fits, otherwise nothing happens.
    // returns: true if newPile can fit on this pile
    //          false if not

    //    System.out.println("hi");
    //    if (spider.debughole) System.out.println("Checking for empty pile: " + maxCards + " maxCards");
    if (maxCards == 0){

      //      if (spider.debughole) System.out.println("found a valid king!");
      doPileToPile(newPile);  // put anything on empty pile
      return true;
    }
    Card c = newPile.peekCard(0);
    //    if (spider.debughole) System.out.println("" + "card on pileinmotion is " + c );
    if ( (c.getValue() + 1) == cardPile[maxCards -1].getValue()) {
      doPileToPile(newPile);
      return true;
    }  
    else{
      return false;
    }
  }

  public int paintPile(Graphics g, ImageObserver eyes){
    return paintPile(g,eyes,false);
  }

  public int paintPile(Graphics g, ImageObserver eyes,boolean blank){
    /*
      pre:	pileNum is a valid pile (0-6)
      post:     pile is drawn
      returns:  score for this pile- 2 sequential same-suit cards   2 pts
                                     3     ""        ""      ""     2+3
				     4     ""        ""      ""     2+3+4
      */
    int score =2;
    int totscore =0;
    int i =0;
    int prevSuit = 0;
    int prevSuit2 = 0;
    int prevVal = 0;
    int prevVal2 = 0;
    int prev_y = y - UPSEP;
    int inc =0;
    for (i=0; i < maxCards; i++){

      if ((prevSuit == cardPile[i].getSuit() ) && ( (prevVal -1) == cardPile[i].getValue())){ // 2 in a row
	if ((prevSuit2 == cardPile[i].getSuit()) && ( (prevVal2 -1) == prevVal)) {  // 3 in a row
	  
	  inc = UPSEP_SAMESUIT;
	  score++;
	  totscore += score;
	}else{
	  totscore += score; 
	}
      }
      else{
	score = 2;
	inc = UPSEP;
      }
      prev_y += inc;
      prevVal2 = prevVal;
      prevVal = cardPile[i].getValue();
      prevSuit2 = prevSuit;
      prevSuit = cardPile[i].getSuit();
      
      cardPile[i].drawCard(g,x,prev_y,eyes, true,blank);
    }
    return totscore;
  }

  public pile checkCompleteSuit(){
    int i =0;
    int prevSuit =0;
    int prevVal =0;
    int sum =1;
    pile ret = null;
    for (i=0;i<maxCards;i++){
      if ((prevSuit == cardPile[i].getSuit()) && ( (prevVal -1) == cardPile[i].getValue())){
	sum++;
	//	System.out.println("sum is " + sum);
	if (sum == 13) { // we just found a complete suit
	  ret = popTopCard(13);
	}
      }
      else
	sum =1;
      prevSuit = cardPile[i].getSuit();
      prevVal = cardPile[i].getValue();
    }
    return ret;
  }
}  
      
