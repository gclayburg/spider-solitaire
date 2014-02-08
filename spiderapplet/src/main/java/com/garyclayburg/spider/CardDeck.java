/*
 * Gary Cornell and Cay S. Horstmann, Core Java (Book/CD-ROM)
 * Published By SunSoft Press/Prentice-Hall
 * Copyright (C) 1996 Sun Microsystems Inc.
 * All Rights Reserved. ISBN 0-13-565755-5
 *
 * Permission to use, copy, modify, and distribute this 
 * software and its documentation for NON-COMMERCIAL purposes
 * and without fee is hereby granted provided that this 
 * copyright notice appears in all copies. 
 * 
 * THE AUTHORS AND PUBLISHER MAKE NO REPRESENTATIONS OR 
 * WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE, EITHER 
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. THE AUTHORS
 * AND PUBLISHER SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED 
 * BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING 
 * THIS SOFTWARE OR ITS DERIVATIVES.
 */

/**
 * @version 1.00 07 Feb 1996 
 * @author Cay Horstmann
 */
package com.garyclayburg.spider;

import java.applet.Applet;

public class CardDeck {

    public CardDeck(Applet ac) {
        deck = new Card[52];
        fill(ac,1);
        shuffle();
    }

    public CardDeck(Applet ac,int numDecks) {
        deck = new Card[(52 * numDecks)];
        fill(ac,numDecks);
        shuffle();
    }

    public int len() {
        return deck.length;
    }

    public void fill(Applet ac,int numDecks) {
        int i;
        int j;
        int h;
        for (j = 1; j <= 4; j++)
            for (i = 1; i <= 13; i++)
                deck[13 * (j - 1) + i - 1] = new Card(ac,i,j);
        for (h = 1; h <= ((numDecks - 1) * 52); h++)
            deck[52 + h - 1] = deck[h - 1]; // use card image that we already have a reference to.
        cards = 52 * numDecks;
    }

    public void shuffle() {
        int next;
        for (next = 0; next < cards - 1; next++) {
            int r = new RandomIntGenerator(next,cards - 1).draw();
            Card temp = deck[next];
            deck[next] = deck[r];
            deck[r] = temp;
        }
    }


    public String toString() {
        String s = "";
        for (int i = 0; i < cards; i++) {
            s += deck[i];
        }
        return s;
    }

    public final Card draw() {
        if (cards == 0) return null;
        cards--;
        return deck[cards];
    }

    private Card[] deck;
    private int cards;
}
