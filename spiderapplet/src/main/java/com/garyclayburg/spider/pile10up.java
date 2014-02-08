package com.garyclayburg.spider;

import java.awt.*;
import java.awt.image.ImageObserver;

public class pile10up extends pile {
    public static final int UPSEP = 25;
    public static final int UPSEP_SAMESUIT = 5;

    public pile10up(int maxUp,Card[] cUp,int x,int y) {
    /*
      PRE: (x,y) is top-left of down pile
      POST: a "good" pile7up is created
      cUp is the lone up card
      cDown is an array of down cards
      */
        // create down pile
        super(maxUp,cUp,x,y);  //this is dumb.
    }

    public pile10up(int maxUp,Card[] cUp,Point topLeft) {
        super(maxUp,cUp,topLeft.x,topLeft.y);
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

    private int cardsUnderMouse(int x,int y) {
        // returns: number of card at x,y counted from the top of the pile.
        //          Card closest to the top of the screen is #1.
        int ret = 0;
        int prevSuit = 0;
        int prevVal = 0;
        int prev_y = this.y;
        int inc;

        if (x >= this.x && x <= (this.x + Card.CARDWIDTH) && (y >= this.y)) {

            for (int i = 0; i < maxCards; i++) {
                if ((prevSuit == cardPile[i].getSuit()) && ((prevVal - 1) == cardPile[i].getValue()) &&
                    (maxCards - 1 != i) && (cardPile[i].getSuit() == cardPile[i + 1].getSuit()) &&
                    ((cardPile[i].getValue() - 1) == cardPile[i + 1].getValue()))

                    inc = UPSEP_SAMESUIT;
                else inc = UPSEP;

                if (i == (maxCards - 1)) if (y <= (prev_y + Card.CARDHEIGHT)) {
                    ret = i + 1;
                }
                prev_y += inc;
                prevVal = cardPile[i].getValue();
                prevSuit = cardPile[i].getSuit();
                if (y <= prev_y) {
                    ret = i + 1;  // we found the card!
                    break;
                }
            }
        }
        return ret;
    }

    public Point topCardPoint() {
        // returns: number of card at x,y counted from the top of the pile.
        //          Card closest to the top of the screen is #1.
        Point ret = new Point(0,0);
        int prevSuit = 0;
        int prevVal = 0;
        int prev_y = this.y;
        int inc;
        for (int i = 0; i < maxCards; i++) {

            if ((prevSuit == cardPile[i].getSuit()) && ((prevVal - 1) == cardPile[i].getValue()) &&
                (maxCards - 1 != i) && (cardPile[i].getSuit() == cardPile[i + 1].getSuit()) &&
                ((cardPile[i].getValue() - 1) == cardPile[i + 1].getValue()))

                inc = UPSEP_SAMESUIT;
            else inc = UPSEP;

            if (i == (maxCards - 1)) {
                ret = new Point(x,prev_y);
            }
            prev_y += inc;
            prevVal = cardPile[i].getValue();
            prevSuit = cardPile[i].getSuit();

        }
        return ret;
    }

    public int mouseOnACard(int x,int y) {
        // returns: number of cards of this pile10up that is above the mouse.
        return (maxCards - cardsUnderMouse(x,y) + 1);
    }


    public boolean moveableCard(int x,int y) {
        // returns: whether or not the card under the mouse pos on a Spider pile can be moved.
        int i;
        int pos = cardsUnderMouse(x,y);
        if (pos == 0) return false;
        if (maxCards <= 1) return true;
        boolean ret = true; //innocent until proven guily!
        for (i = pos - 1; i < (maxCards - 1); i++) {
            if ((cardPile[i].getSuit() != cardPile[i + 1].getSuit()) ||
                (cardPile[i].getValue() - 1 != cardPile[i + 1].getValue())) {
                ret = false;
                break;
            }
        }
        return ret;
    }

    public boolean mouseOnTopCard(int x,int y) {
    /*
      returns: true if (x,y) is on a card at end of pileup
      */
        if ((maxCards == 0) && (x >= this.x && x <= (this.x + Card.CARDWIDTH)) &&
            (y >= this.y) && (y >= this.y && y <= (this.y + Card.CARDWIDTH))) return true;
        if (cardsUnderMouse(x,y) == 0) return false;
        return (maxCards == cardsUnderMouse(x,y));
    }


    public boolean clickEgg(int x,int y) {
        if (maxCards > 0) if (x >= this.x && x <= (this.x + Card.CARDWIDTH) &&
                              y >= this.y && y <= (this.y)) return true;
        return false;
    }

    public void doCardToPile(Card c) {
        // actually adds the card without asking questions
        // this should only be called (externally) when time to flip card over
        cardPile[maxCards++] = c;
    }

    public boolean cardToPile(Card c) {
        // post: pile has a new card on it if it fits.
        //  returns: true if card can be put on pile
        //           false if not
        if (maxCards == 0) {
            return false;
        }  // have empty pile here.
        if (c == null) {
            return false;
        } // happens if drag from empty area to card.
        boolean alt_suit =
                ((c.isBlack() && cardPile[maxCards - 1].isRed()) || (c.isRed() && cardPile[maxCards - 1].isBlack()));
        if (alt_suit && ((c.getValue() + 1) == cardPile[maxCards - 1].getValue())) {
            doCardToPile(c);
            return true;
        } else {
            return false;
        }
    }

    public boolean addPile(pile newPile) {
        return pileToPile(newPile);
    }

    public boolean pileToPile(pile newPile) {
        // pre: newPile is a valid Spider pile
        // post: newPile is "dropped" on top of this pile if it fits, otherwise nothing happens.
        // returns: true if newPile can fit on this pile
        //          false if not

        if (maxCards == 0) {
            doPileToPile(newPile);  // put anything on empty pile
            return true;
        }
        Card c = newPile.peekCard(0);
        if ((c.getValue() + 1) == cardPile[maxCards - 1].getValue()) {
            doPileToPile(newPile);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int paintPile(Graphics g,ImageObserver eyes) {
        return paintPile(g,eyes,false);
    }

    public int paintPile(Graphics g,ImageObserver eyes,boolean blank) {
    /*
      pre:	pileNum is a valid pile (0-6)
      post:     pile is drawn
      returns:  score for this pile- 2 sequential same-suit cards   2 pts
                                     3     ""        ""      ""     2+3
				     4     ""        ""      ""     2+3+4
      */
        int score = 2;
        int totscore = 0;
        int i;
        int prevSuit = 0;
        int prevSuit2 = 0;
        int prevVal = 0;
        int prevVal2 = 0;
        int prev_y;
        prev_y = y - UPSEP;
        int inc = 0;
        for (i = 0; i < maxCards; i++) {

            if ((prevSuit == cardPile[i].getSuit()) && ((prevVal - 1) == cardPile[i].getValue())) { // 2 in a row
                if ((prevSuit2 == cardPile[i].getSuit()) && ((prevVal2 - 1) == prevVal)) {  // 3 in a row

                    inc = UPSEP_SAMESUIT;
                    score++;
                    totscore += score;
                } else {
                    totscore += score;
                }
            } else {
                score = 2;
                inc = UPSEP;
            }
            prev_y += inc;
            prevVal2 = prevVal;
            prevVal = cardPile[i].getValue();
            prevSuit2 = prevSuit;
            prevSuit = cardPile[i].getSuit();

            cardPile[i].drawCard(g,x,prev_y,eyes,true,blank);
        }
        return totscore;
    }

    public boolean isCompleteSuit() {
        boolean isCompleteSuit = false;
        int i;
        int prevSuit = 0;
        int prevVal = 0;
        int sum = 1;
        for (i = 0; i < maxCards; i++) {
            if ((prevSuit == cardPile[i].getSuit()) && ((prevVal - 1) == cardPile[i].getValue())) {
                sum++;
                if (sum == 13) { // we just found a complete suit
                    isCompleteSuit = true;
                }
            } else sum = 1;
            prevSuit = cardPile[i].getSuit();
            prevVal = cardPile[i].getValue();
        }
        return isCompleteSuit;
    }

    public void setNumCardsDown(int numCardsDown) {
        y = Spider.YOFFSET + (Spider.DOWNSEP * numCardsDown);
    }

}
      
