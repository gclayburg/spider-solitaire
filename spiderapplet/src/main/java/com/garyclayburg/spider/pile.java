package com.garyclayburg.spider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.ImageObserver;

public abstract class pile extends Component {
    protected boolean isMoving;
    protected Card[] cardPile;
    protected int maxCards = 0;  // can we use .length instead? no.
    protected int x; // x coord of top-left of pile
    protected int y; // y coord of top-left of pile
    protected int offSetY = 0;
    protected Point topLeft;
    private static Logger log = LoggerFactory.getLogger(pile.class);

    public pile(int maxCards,Card[] ca,int x,int y) {
    /*  
        pre: 	ca is an array of maxCards Cards
	post:	A new pile is created with ca Cards
	returns: nothing
	*/
        cardPile = ca;
        this.maxCards = maxCards;
        this.x = x;
        this.y = y;
        topLeft = new Point(x,y);
        offSetY = y - Spider.YOFFSET;
        this.isMoving = false;
    }

    public pile popTopCard(int num) {
        // pre: there are at least num cards to pop off
        // post: num cards are returned and popped off pile (or as many as we can pop off)
        Point pointOfTop;
        log.debug("pile: popTopCard(int)");
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

    public Card popTopCard() {
    /*
      pre: there exists at least one card in pile
      post: top card is taken off stack and returned.
      */
        log.debug("trying to pop  " + maxCards);
        log.debug("popTopCard(): popping card: " + cardPile[maxCards - 1]);
        return cardPile[--maxCards];
    }

    public int length() {
        return maxCards;
    }

    public int getx() {
        return topLeft.x;
    }

    public int gety() {
        return topLeft.y;
    }

    public void move(int x,int y) {
        this.x = x;
        this.y = y;
        topLeft.x = x;
        topLeft.y = y;
    }

    public Card peekCard(int numFromBeginning) {
        return (cardPile[numFromBeginning]);
    }

    public Card getTopCard() {
        return (cardPile[maxCards - 1]);
    }

    public boolean mouseOnTopCard(int x,int y) {
    /* post: is point x,y on this top card?
     */
        return x >= topLeft.x && x <= (topLeft.x + Card.CARDWIDTH) &&
               y >= (topLeft.y) &&
               y <= (topLeft.y + Card.CARDHEIGHT);
    }

    public boolean mouseOnTopCard(Point p1) {
    /* pre: p1 is top left of card being moved
       post: is region bounded by p1 and  a subset of this top card?
       */
        Rectangle pileInMotionRect = new Rectangle(p1.x,p1.y,Card.CARDWIDTH,Card.CARDHEIGHT);
        Rectangle pileRect = new Rectangle(topLeft.x,topLeft.y,Card.CARDWIDTH,Spider.pileHeight);
        return pileInMotionRect.intersects(pileRect);
    }

    public void removeTopCard() {
    /*
      pre: there exists a card to remove
      */
        cardPile[maxCards--] = null;
    }

    public String toString() {  //debugging
        int i;
        String s = " ";
        for (i = 0; i < maxCards; i++) {
            s = s + cardPile[i];
        }
        return (s + "  " + maxCards + "maxCards");
    }

    public void doReversePileToPile(pile newPile) {
        // this is exactly backwards from HandUp.doPileToPile
        int l = newPile.length();
        for (int i = l - 1; i >= 0; i--) {
            cardPile[maxCards++] = newPile.peekCard(i);
        }
    }

    public void doPileToPile(pile newPile) {
        int l = newPile.length();
        for (int i = 0; i < l; i++) {
            cardPile[maxCards++] = newPile.peekCard(i);
        }
    }

    public void setIsMoving(boolean isMoving) {
        this.isMoving = isMoving;
    }

    public abstract boolean addPile(pile newPile);

    public abstract int paintPile(Graphics g,ImageObserver eyes);

    public abstract int paintPile(Graphics g,ImageObserver eyes,boolean blank);

    public Point topCardPoint() {
        return topLeft;
    }

    public Point getTopLeft() {
        return topLeft;
    }

}
