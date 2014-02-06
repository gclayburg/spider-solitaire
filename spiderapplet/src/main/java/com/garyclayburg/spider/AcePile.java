package com.garyclayburg.spider;

import java.applet.Applet;
import java.awt.*;
import java.awt.image.ImageObserver;

public class AcePile extends pile{

  private Card empty;
  private pile master;  // the REAL deal
  public AcePile(Applet ac,int maxCards, Card[] ca, int x, int y,pile theMan){
    super(maxCards,ca,x,y);
    empty = new Card(ac);
    master = theMan;
  }
  public AcePile(Applet ac,int maxCards, Card[] ca, int x, int y){
    super(maxCards,ca,x,y);
    empty = new Card(ac);
  }

  public AcePile(Card[] ca){
    // used for PileServImpl only
    super(0,ca,0,0);
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

  public int paintPile(Graphics g, ImageObserver eyes){
    //  post: The ace pile is painted.
    if (maxCards >0)
      cardPile[maxCards -1].drawCard(g,x,y,eyes,true);
    else{ 
      System.out.println("I'm at AcePile.paintPile with no aces Z" + empty + "Z"); 
      empty.drawCard(g,x,y,eyes,true);
      System.out.println("blank card is there now.");
    }
    return 0;
  }
  //dummy to get this to compile
  public boolean addPile(pile newPile){
    return true;
  }

  public int paintPile(Graphics g, ImageObserver eyes, boolean blank){
    return 0;
  }

  public boolean pileToPile(pile newPile){
    // pre: newPile is a valid pile with one card in it.
    // post: newPile is added to this acePile, if it is a valid card
    //                                         else nothing.
    boolean possibleMatch = false;
    if (maxCards == 0) {
      if ( newPile.peekCard(0).getValue() != Card.ACE) { return false; }
      possibleMatch = true;
      //      doPileToPile(newPile);
      //      return true;
    }
    else{
      if (((newPile.peekCard(0).getValue() -1) >= this.peekCard(maxCards -1).getValue()) &&
	  (newPile.peekCard(0).getSuit() == this.peekCard(maxCards -1).getSuit() ) ){
      possibleMatch = true;
// 	doPileToPile(newPile);
// 	return true;
      }
    }
    if(possibleMatch){
      doPileToPile(newPile);
      return true;
//        return master.PileToPile(newPile); // rely on server to callback to update pile
	//    return false;
  }
    return false;
 }
}


