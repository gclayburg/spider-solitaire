package com.garyclayburg.spider;

import java.awt.*;
import java.awt.image.ImageObserver;

public class pile10down extends pile {
    public static final int DOWNSEP = 20;

    public pile10down(int maxDown,Card[] cDown,int x,int y) {
    /*
      PRE: (x,y) is top-left of down pile
      POST: a "good" pile7down is created
      cUp is the lone up card
      cDown is an array of down cards
      */
        super(maxDown,cDown,x,y);
    }

    public boolean addPile(pile newPile) {
        return true;
    }

    public pile popTopCard(int num) {
        // pre: there are at least num cards to pop off
        // post: num cards are returned and popped off pile (or as many as we can pop off)
        Point pointOfTop;
        if (maxCards == 0) {
            return null;
        }
        if (maxCards < num) {
            num = maxCards; //pop all that we have.
        }
        Card c[] = new Card[num];
        for (int i = num - 1; i >= 1; i--) {
            c[i] = popTopCard();
        }
        pointOfTop =
                topCardPoint();  //tricky part.  should use polymorphism here to drill down to correct extended pile.
        c[0] = popTopCard();
        // could use callbacks via interfaces or make pile non-abstract and instantiate pile here.
        // or I could subclass this method to both pile7up and pile10up
        return new pile10up(num,c,pointOfTop);
    }

    public boolean clickEgg(int x,int y) {
        if (maxCards > 0) if (x >= this.x && x <= (this.x + Card.CARDWIDTH) &&
                              y >= this.y && y <= (this.y + DOWNSEP)) return true;
        return false;
    }

    public int paintPile(Graphics g,ImageObserver eyes) {
        return paintPile(g,eyes,false);
    }

    public int paintPile(Graphics g,ImageObserver eyes,boolean blank) {
    /*
      pre:	pileNum is a valid pile (0-6)
      The location of the cards on the bottom of piledown will not change
      when cards are flipped over.
      */
        for (int i = 1; i <= maxCards; i++) {
            cardPile[0].drawCard(g,x,(y + Spider.DOWNSEP * (i - 1)),eyes,false);
        }
        return maxCards; // used to make sure we paint() uppile in right spot
    }
}
