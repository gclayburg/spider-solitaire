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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.applet.Applet;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

public final class Card {
    public static final int CARDWIDTH = 71;
    public static final int CARDHEIGHT = 96;
    public static final int ACE = 1;
    public static final int JACK = 11;
    public static final int QUEEN = 12;
    public static final int KING = 13;

    public static final int CLUBS = 1;
    public static final int DIAMONDS = 2;
    public static final int HEARTS = 3;
    public static final int SPADES = 4;
    private static boolean gotEmpty;
    private static Image backImg;
    private static Image emptyStaticImg;
    private static MediaTracker tracker;

    private Applet ac;
    private int value;
    private int suit;
    private static HashMap<String, Image> hCardImages = new HashMap<String, Image>();
    private Image gifImg;
    private static Logger log = LoggerFactory.getLogger(Card.class);

    public Card(int s,int v) {
        //pre:  v is a valid card value
        //      s is a valid card suit
        // used only for the AcePileServerthing so far... 5/4/97
        value = v;
        suit = s;
    }

    private Image loadImageFromFile(String imageName) {
        log.debug("loading Image from codebase: " + ac.getCodeBase() + imageName);
        return ac.getImage(ac.getCodeBase(),imageName);
    }

    private Image loadImageFromResource(String imageName) {
        // needed for Appletviewer,jdk 1.3 (also works with IE)
        // Netscape 4.x barfs at this
        Image retVal = null;
        URL u = this.getClass().getResource(imageName);
        if (u != null) {
            retVal = ac.getImage(u);
        }
        return retVal;
    }

    private Image loadImageFromResourceStream(String imageName) throws IOException {
        Image image = null;
        //needed for Netscape 4.x (also works with IE)
        // This loading method creates partially loaded images with jdk 1.3 (appletviewer, netscape 6)

        //do we still need this method for loading images?
        log.debug("attempting to load from stream: " + imageName);
        InputStream in = getClass().getResourceAsStream(imageName);
        if (in != null) {
            int avail = in.available();
            byte buffer[] = new byte[in.available()];
            in.read(buffer);  //todo fix this to either verify image is completely read, or load some other way
            image = Toolkit.getDefaultToolkit().createImage(buffer);
        }
        return image;
    }

    private Image loadImage(String imageName) {
        log.debug("loading image: " + imageName);
        Image cardImage;
        if (hCardImages.containsKey(imageName)) {
            cardImage = hCardImages.get(imageName);
        } else {
            // Be careful.  Order of trying different loading methods makes a difference!
            try {
                log.debug("try from resource");
                cardImage = loadImageFromResource(imageName);
                if (cardImage == null) {
                    try {
                        log.debug("load this from resource stream");
                        cardImage = loadImageFromResourceStream(imageName);
                    } catch (IOException e) {
                        log.error("can't even load resource stream");
                    }
                    if (cardImage == null) {
                        log.debug("try from file");
                        cardImage = loadImageFromFile(imageName);
                    }
                }
            } catch (NullPointerException npe) { //Netscape 4.x will throw this
                npe.printStackTrace();
                try {
                    log.debug("try from resourceStream");
                    cardImage = loadImageFromResourceStream(imageName);
                } catch (IOException e) {  //Jar file doesn't have the image?
                    log.debug("try from file");
                    cardImage = loadImageFromFile(imageName);
                } catch (NullPointerException e) {  //No jar?
                    cardImage = loadImageFromFile(imageName);
                }
            }
            hCardImages.put(imageName,cardImage);
        }
        return cardImage;
    }

    // Internet Explorer way to get image from jar:
    //      u = this.getClass().getResource("CardImages/empty.gif");
    //      emptyStaticImg = ac.getImage(u);

    // Get image directly via http:
    //        emptyStaticImg = ac.getImage(ac.getCodeBase(),"jbuilderspider/CardImages/empty.gif");
//    }

    public Card(Applet ac,int v,int s) {
        value = v;
        suit = s;
        this.ac = ac;
        if (!gotEmpty) {
            tracker = new MediaTracker(ac);
            emptyStaticImg = loadImage("/CardImages/empty.gif");
            tracker.addImage(emptyStaticImg,1);
            backImg = loadImage("/CardImages/back.gif");
            tracker.addImage(backImg,2);
            gotEmpty = true;
        }
        gifImg = loadImage("/CardImages/" + this + ".gif");
        String str = v + "" + s;
        tracker.addImage(gifImg,Integer.parseInt(str));
        if (Spider.debug) log.debug(
                "" + this + " card being loaded with code base " + ac.getCodeBase() + "  Image name is: " + gifImg);
    }

    public Card(Applet ac) {
        this.ac = ac;
        value = 1;
        suit = 1;
        gifImg = emptyStaticImg;
    }

    public Card(Applet ac,Card c) {
        // This constructor doesn't need to get the image
        //    log.debug("card constructor with a card: " + c);
        this.ac = ac;
        gifImg = ac.getImage(ac.getCodeBase(),"CardImages/" + c + ".gif");
        value = c.getValue();
        suit = c.getSuit();
    }

    public boolean equals(Card c) {
        boolean out;
        out = this.getValue() == c.getValue() && this.getSuit() == c.getSuit();
        if (Spider.debug) log.debug("Card.equals(): " + out);
        return out;
    }

    public static boolean checkImages(Applet ac) {
        String str;
        int cardnum;
        int i = 0;
        // wait for backImg and emptyStaticImg
        try {
            tracker.waitForID(1);
        } catch (InterruptedException e) {
            log.debug("why did tracker get interrupted on 1?");
        }

        try {
            tracker.waitForID(2);
        } catch (InterruptedException e) {
            log.debug("why did tracker get interrupted on 2?");
        }

        for (int val = 1; val <= 13; val++) {
            for (int suit = 1; suit <= 4; suit++) {
                i++;
                str = "" + val + suit;
                cardnum = Integer.parseInt(str);
                ac.showStatus("Loading Card Images: " + (i * 100 / 52) + "% complete");
                if (Spider.debug) log.debug("loading card: " + i);
                try {
                    tracker.waitForID(cardnum);
                } catch (InterruptedException e) {
                    log.debug("tracker got exception " + e);
                }
                if (tracker.isErrorID(cardnum)) {
                    ac.showStatus("Error loading image " + str + "; Come back another day.");
                    return false;
                }
            }
        }
        return true;
    }

    public int getValue() {
        return value;
    }

    public int getSuit() {
        return suit;
    }

    public boolean isRed() {
        return (suit == DIAMONDS || suit == HEARTS);
    }

    public boolean isBlack() {
        return (suit == SPADES || suit == CLUBS);
    }

    public int rank() {
        if (value == 1) return 4 * 13 + suit;
        else return 4 * (value - 1) + suit;
    }

    public void drawCard(Graphics g,Point p,ImageObserver eyes,boolean front) {
        drawCard(g,p.x,p.y,eyes,front);
    }

    public void drawCard(Graphics g,int x,int y,ImageObserver eyes,boolean front,boolean blank) {
        if (blank) {
            g.setColor(Color.green);
            g.fillRect(x,y,Spider.CARDWIDTH,Spider.CARDHEIGHT);
        } else {
            drawCard(g,x,y,eyes,front);
        }
    }

    public void drawCard(Graphics g,int x,int y,ImageObserver eyes,boolean front) {
        if (front) {
            g.drawImage(gifImg,x,y,eyes);
        } else {
            g.drawImage(backImg,x,y,eyes);
        }
    }

    public String toString() {
        String v;
        String s;
        if (value == ACE) v = "A";
        else if (value == JACK) v = "J";
        else if (value == QUEEN) v = "Q";
        else if (value == KING) v = "K";
        else v = String.valueOf(value);
        if (suit == DIAMONDS) s = "D";
        else if (suit == HEARTS) s = "H";
        else if (suit == SPADES) s = "S";
        else /* suit == CLUBS */ s = "C";
        return v + s;
    }

}
