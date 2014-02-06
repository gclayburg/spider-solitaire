package com.garyclayburg.spider;

import java.applet.Applet;
import java.awt.*;
import java.awt.image.ImageObserver;
//  import com.protomatter.syslog.*;

public class AcePileSpider extends pile{

  public AcePileSpider(Applet ac,int maxCards, Card[] ca, int x, int y){
    super(maxCards,ca,x,y);
    empty = new Card(ac);
  }
  public int paintPile(Graphics g, ImageObserver eyes,boolean dummy){
    return paintPile(g,eyes);
  }

  public int paintPile(Graphics g, ImageObserver eyes){
    //  post: The ace pile is painted.
    if (maxCards >0){
      cardPile[maxCards -1].drawCard(g,x,y,eyes,true);
      return (2 + 3 + 4 + 5 + 6 + 7 + 8 + 9 + 10 + 11 + 12 + 13 + 14); // score for a full pile
    }
    else{ 
//        Syslog.debug(this, "I'm at AcePile.paintPile with no aces Z" + empty + "Z"); 
      //      System.out.println("I'm at AcePile.paintPile with no aces Z" + empty + "Z"); 
      empty.drawCard(g,x,y,eyes,true);
      return 0;
      //      System.out.println("blank card is there now.");
    }
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

  public boolean addPile(pile newPile){
    return pileToPile(newPile);
  }

  public boolean pileToPile(pile newPile){
    // pre: newPile is a valid pile with 13 cards of the same suit, in order.
    //      This pile must also be empty
    // post: newPile is added to this acePile, if it is a valid card
    //                                         else nothing.
    if (maxCards == 0) {
      doPileToPile(newPile);
      return true;
    }
    else{
      return false;
    }
  }
  private Card empty;
}


