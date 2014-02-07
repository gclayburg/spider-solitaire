/*--- formatted by Jindent 2.1, (www.c-lab.de/~jindent) ---*/

package com.garyclayburg.spider;

// import corejava.*;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

public class Spider extends Applet implements Runnable {
    public static final int CARDWIDTH = 71;
    public static final int CARDHEIGHT = 96;
    public static final int PILESEP = 5;
    public static final int XOFFSET = 5;
    public static final int YOFFSET = 116;
    public static final int XACEOFFSET = 157;
    public static final int YACEOFFSET = 10;
    public static final int ACESEP = 5;
    public static final int DOWNSEP = 5;    // separation between each down card
    public static final int XHANDOFFSET = 5;
    public static final int YHANDOFFSET = 10;
    public static final int HANDSEP = 5;
    public static int iter = 0;
    public static int pileHeight;
    public static final boolean debug = true;
    public static final boolean debughole = true;
    public static final boolean debugTiming = true;
    public Applet ac;
    private static Logger log = LoggerFactory.getLogger(Spider.class);

    static {
    }

    public static void main(String[] args) {
        Spider s = new Spider();

        s.init();
    }    // end of main ()

    public Spider() {
        System.out.println("Spider()");
    }

    public void init() {
        System.out.println("ok");
        configureLog4j();
        log.info("Spider() starting up...");

        log.debug("init from Spider");
        setLayout(null);    // needed for applet

        // setLayout(new BorderLayout(0,0)); //needed for applet
        // spiderTable = new Canvas();
        // add("North",new Button("himan"));
        // add("Center",spiderTable);
        // setTitle("Clayburg Solitaire");
        restart = new Button("Restart");
        restart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cheating=false;
                startmeup();
            }
        });
        add(restart);
        cheat = new Button("Cheat");
        cheat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cheating=true;
                startmeup();
            }
        });
        add(cheat);
        undo = new Button("Undo");
        undo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pileMover.undoMove();
                repaint();
            }
        });
        add(undo);

        // add(spiderTable);
        ac = this;               // reference to applet context (needed for Card
        d = getSize();
        log.debug("size is: " + d);
        offScrImage = createImage(d.width,d.height);    // double buffer paint()
        offScrGr = offScrImage.getGraphics();
        offScrGr.setColor(Color.green);
        offScrGr.fillRect(0,0,d.width,d.height);
        pileHeight = d.height - YOFFSET;
        String sleeps = getParameter("sleeptime");

        try {
            sleeptime = Integer.parseInt(sleeps);
        } catch (NumberFormatException e) {
            sleeptime = 250;
        }
        if (sleeptime == 0) {
            sleeptime = 250;
        }

        // log.debug("sleep time is " + sleeptime);
        startmeup();
    }

    private void configureLog4j() {
        System.out.println("configuring log4j using log4j.xml");
        BasicConfigurator.resetConfiguration();

        InputStream isLog4jXML = this.getClass().getResourceAsStream("/log4j.xml");

//    InputStream isLog4jXML = Thread.currentThread().getContextClassLoader().getResourceAsStream("/log4j.xml");
        if (isLog4jXML != null) {
            System.out.println("trying input stream first");
            new DOMConfigurator().doConfigure(isLog4jXML,LogManager.getLoggerRepository());
        } else {

            try {

                URI uri = ac.getCodeBase().toURI();
                URI combined = new URI(uri.toString() + "log4j.xml");
                System.out.println("loading from codebase: " + combined);
                InputStream fileStream = new FileInputStream(new File(combined));
                if (fileStream != null) {
                    new DOMConfigurator().doConfigure(fileStream,LogManager.getLoggerRepository());

                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            System.out.println("cannot use input stream for log4j.xml");
            DOMConfigurator.configure("log4j.xml");

        }
        log.debug("done configuring basic log4j");
    }

    public void startmeup() {

        // This will start a new game - either from the beginning or when
        // someone clicks, "restart"
        int i = 0;
        int j = 0;

        winner = false;
        if (debug) {
            log.debug("startmeup()");
        }
        solDeck = new CardDeck(ac,2);    // Spider uses two decks of cards
        if (cheating){
            solDeck.fill(ac,2);
        }else{
            solDeck.shuffle();  // ok fine. so we shuffle the deck twice for the first game. I can live with that.
        }
        pileListUp = new pile10up[10];
        pileListDown = new pile10down[10];
        motionPiles = new PileMoving[10];

        // fill 10 piles with cards
        for (i = 0; i < 10; i++) {
            Card[] aCard = new Card[5];    // max of 5 cards face down on a pile
            Card[] uCard = new Card[78];    // Spider's face up piles can be huge.  Worst case scenario. (6*13)

            uCard[0] = solDeck.draw();
            for (j = 3; j >= 0; j--) {      // put 4 cards face down to start
                aCard[j] = solDeck.draw();
            }
            int downcards = 4;

            if (i % 3 == 0) {         // every third pile needs an extra card
                aCard[4] = solDeck.draw();
                downcards++;
            }
            pileListUp[i] =
                    new pile10up(1,uCard,(XOFFSET + CARDWIDTH * i + PILESEP * i),(YOFFSET + (DOWNSEP * downcards)));
            pileListUp[i].setNumCardsDown(downcards);
            pileListDown[i] = new pile10down(downcards,aCard,(XOFFSET + CARDWIDTH * i + PILESEP * i),YOFFSET);
        }
        Card[] hCard = new Card[50];    // cards to put in hand

        for (j = 0; j < 50; j++) {
            hCard[j] = solDeck.draw();
        }
        handDown = new HandDown(50,hCard,XHANDOFFSET,YHANDOFFSET);
        acesList = new AcePileSpider[8];
        Card[] caaa;

        for (i = 0; i < 8; i++) {
            caaa = new Card[13];
            acesList[i] = new AcePileSpider(ac,0,caaa,(XACEOFFSET + CARDWIDTH * i + ACESEP * i),YACEOFFSET);

            // if (solitaire.debug) printPile();
        }
        pileMover = new PileMover();
        current_ace = 0;
        if (Spider.debug) {
            log.debug("time to paint\n");
        }
        repaint();
    }

    public void start() {
        if (runner == null) {
            runner = new Thread(this);
            runner.start();
        }
    }

    public void stop() {
        if (runner != null && runner.isAlive()) {
            runner.stop();
        }
        runner = null;
    }

    public void run() {
        if (!Card.checkImages(this)) {    // should block and wait for images
            return;
        }
        this.showStatus("Loaded all cards...");
        repaint();

        // MediaTracker should have loaded all cards now...
        loaded = true;
        while (runner != null) {

            // repaint();
            try {
                Thread.sleep(sleeptime);
            } catch (InterruptedException e) {
            }
        }
    }

    public boolean handleEvent(Event evt) {
        if (evt.id == Event.WINDOW_DESTROY) {
            System.exit(0);
        }
        return super.handleEvent(evt);
    }

    public void update(Graphics g) {    // force repaint() to not erase first...
        paint(g);
    }

    public void paint(Graphics g) {

    /*
     * pre:	none.
     * post:	The entire solitaire screen is painted.
     */
        int i;
        int score = 0;
        long beginPaint = System.currentTimeMillis();

        if (!loaded) {
            return;
        }
        if (!flipped) {
            return;
        }
        if (debugTiming) {
            log.debug("painting...");
        }
        offScrGr.setColor(Color.green);
        offScrGr.fillRect(0,0,d.width,d.height);
        for (i = 0; i < 10; i++) {
            int numCardsDown = pileListDown[i].paintPile(offScrGr,this);
            pileListUp[i].setNumCardsDown(numCardsDown);
            score += pileListUp[i].paintPile(offScrGr,this);
        }
        handDown.paintPile(offScrGr,this);
        for (i = 0; i < 8; i++) {
            score += acesList[i].paintPile(offScrGr,this);
        }
        if (motionPile != null && motionPile.getPileInMotion() != null && pileFrom != handDown && !moveHandUpDown) {
            score += motionPile.getPileInMotion().paintPile(offScrGr,this);
        }
        if (winner) {
            offScrGr.setColor(Color.black);
            offScrGr.drawRect(200,200,100,100);
            offScrGr.setColor(Color.white);
            offScrGr.fillRect(200,200,100,100);
            offScrGr.setColor(Color.red);
            offScrGr.setFont(new Font("TimesRoman",Font.PLAIN,20));
            offScrGr.drawString("You",225,250);
            offScrGr.drawString("Win!",225,276);
        }
        offScrGr.setColor(Color.blue);
        offScrGr.setFont(ftr);
        offScrGr.drawString("Score: ",XHANDOFFSET + Card.CARDWIDTH + HANDSEP * 5 + 5,20);
        offScrGr.drawString("" + score,XHANDOFFSET + Card.CARDWIDTH + HANDSEP * 5 + 5,35);
        g.drawImage(offScrImage,0,0,this);

        cheat  .setBounds(XHANDOFFSET + Card.CARDWIDTH + HANDSEP * 5,YHANDOFFSET + 30,50,20);
        restart.setBounds(XHANDOFFSET + Card.CARDWIDTH + HANDSEP * 5,YHANDOFFSET + 50,50,20);
        undo   .setBounds(XHANDOFFSET + Card.CARDWIDTH + HANDSEP * 5,YHANDOFFSET + 75,50,20);
        long endPaint = System.currentTimeMillis();

        if (debugTiming) {
            log.debug("Hello! Painting() time: " + (endPaint - beginPaint));
        }
        log.debug("painted");
    }

    public boolean keyDown(Event evt,int key) {
        int flags = evt.modifiers;

        log.debug("" + "key " + key + "is down!");
        if (evt.id == Event.KEY_PRESS) {
            showStatus("Key Down:");
        }
        if (key == Event.RIGHT && (evt.modifiers & Event.CTRL_MASK) != 0) {
            special = true;
            log.debug("" + "ctrl-r down");
            startmeup();
            return true;
        } else {
            log.debug("" + "not ctrl");
        }
        return false;
    }

    public boolean mouseDown(Event evt,int x,int y) {

        // currentMousePos = find(x,y);
        int i = 0;

        nPileMoved = -1;
        boolean ca;

        if (evt.modifiers == Event.META_MASK) {
            log.debug("right mouse button up");
            return true;    // This eliminates cheating via right mouse clicks
        }

        if (handDown.mouseOnTopCard(x,y)) {    // need to flip 10 cards of hand to piles.
            handleHandClick();
        } else{
            for (int pileNum = 0; pileNum < 10; pileNum++) {
                handlePileClick(x,y,pileNum);
            }
        }
        last_point.x = -1;    // kludge to get mouseDrag to be ok.
        if (last_point.x == -1) {
            last_point.setLocation(x,y);
        }
        return true;
    }

    private void handleHandClick() {
        motionPiles = new PileMoving[10];
        for (int j = 0; j < 10; j++) {
            if (Spider.debug) {
                log.debug("create motionPiles " + j);
            }
            motionPiles[j] = new PileMoving(handDown,1,pileListUp[j]);
        }

        pileFrom = handDown;

        this.showStatus("");
    }

    private void handlePileClick(int x,int y,int pileNum) {
        if (pileListUp[pileNum].moveableCard(x,y)) {    // user clicked on a pile; try to start dragging(moving) a pile of cards
            pIMxOffset = x - (XOFFSET + pileNum * CARDWIDTH + pileNum * PILESEP);
            motionPile = new PileMoving(pileListUp[pileNum],pileListUp[pileNum].mouseOnACard(x,y));
            motionPile.getPileInMotion().setIsMoving(true);
            pileFrom = pileListUp[pileNum];
        } else {          // flip up a new card for a pile10
            if (pileListUp[pileNum].length() == 0 && pileListDown[pileNum].mouseOnTopCard(x,y)) {
                flipPile = pileNum;
            }
        }
    }

    private void wait(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
        }
    }

    public boolean mouseDrag(Event evt,int x,int y) {    // called automatically
        Color c;
        Graphics g = null;
        Graphics bigGraphicsBuffer = null;
        long beginTime = System.currentTimeMillis();
        long getGraphicsTime;

        if (evt.modifiers == Event.META_MASK) {
            log.debug("right mouse button up");
            return true;    // This eliminates cheating via right mouse clicks
        }
        if (motionPile != null && motionPile.getPileInMotion() != null) {
            g = getGraphics();
            getGraphicsTime = System.currentTimeMillis();
            if (pileFrom != handDown && !moveHandUpDown) {
                motionPile.getPileInMotion().paintPile(offScrGr,this,true);
                for (int i = 0; i < 10; i++) {    // paint changed piles
                    if (pileListUp[i].mouseOnTopCard(motionPile.getPileInMotion().getTopLeft()) ||
                        pileListDown[i].mouseOnTopCard(motionPile.getPileInMotion().getTopLeft())) {
                        pileListDown[i].paintPile(offScrGr,this);
                        pileListUp[i].paintPile(offScrGr,this);
                    }
                }
                for (int i = 0; i < 8; i++) {     // paint changed aces
                    if (acesList[i].mouseOnTopCard(motionPile.getPileInMotion().getTopLeft())) {
                        acesList[i].paintPile(offScrGr,this);
                    }
                }
                if (handDown.mouseOnTopCard(motionPile.getPileInMotion().getTopLeft())) {
                    handDown.paintPile(offScrGr,this);
                }
            }
            long endLoopTime = System.currentTimeMillis();

            motionPile.getPileInMotion().setLocation((motionPile.getPileInMotion().getx() + x - last_point.x),

                                                     // pileInMotion.move((pileInMotion.getx() + x - last_point.x),
                                                     (motionPile.getPileInMotion().gety() + y - last_point.y));
            last_point.setLocation(x,y);

            // last_point.move(x,y);
            if (pileFrom != handDown && !moveHandUpDown) {
                motionPile.getPileInMotion().paintPile(offScrGr,this,false);
                g.drawImage(offScrImage,0,0,this);

                // g.drawImage(offScrImage,pileInMotion.getx(),0,this);
            }
            long endTime = System.currentTimeMillis();

            if (debugTiming) {
                log.debug("MouseDrag: " + (endTime - beginTime));
            }
            if (debugTiming) {
                log.debug("MouseDrag getGraphics: " + (getGraphicsTime - beginTime));
            }
            if (debugTiming) {
                log.debug("MouseDrag endLoop: " + (endLoopTime - beginTime));
            }
            return true;
        } else {

            // if (solitaire.debug) log.debug("bad drag");
            return true;
        }
    }

    public boolean mouseUp(Event evt,int x,int y) {
        int i;
        int origPileMoved = nPileMoved;

        log.debug("" + "mouseup! " + evt.id);
        log.debug("modifiers = " + evt.modifiers);
        log.debug("meta mask = " + Event.META_MASK);
        if (evt.modifiers == Event.META_MASK) {
            log.debug("right mouse button up");
            return true;    // This eliminates cheating via right mouse clicks
        }
        if (debug) {
            printPile();
        }
        flipped = false;
        if (motionPiles != null && handDown.mouseOnTopCard(x,y) &&
            (pileFrom == handDown)) {    // flip up the pileInMotion cards.
            mouseUpHand();
        }

        // if (pileFrom == handDown ) { return true;}  //don't try to drag three cards to pile7!
        else {
            for (i = 0; i < 10; i++) {
                if (i != origPileMoved && pileListUp[i].mouseOnTopCard(new Point(x - pIMxOffset,y)) &&
                    motionPile != null && pileFrom != handDown) {
                    if (handleMouseUpOnPile(i)) break;
                } else {
                    if (flipPile == i && pileListDown[i].mouseOnTopCard(x,y) && pileListDown[i].length() != 0 &&
                        pileFrom != handDown) {
                        flipUpFaceDownCard(i);
                    }
                }
            }
        }
        if (motionPile != null && motionPile.getPileFrom() != null){  //pile was dropped in an invalid location
            motionPile.getPileFrom().doPileToPile(motionPile.getPileInMotion());
            motionPile.getPileInMotion().setIsMoving(false);
            motionPile = null;

        }
        flipped = true;
        log.debug("down with mouseup");
        repaint();
        return true;
    }

    private void flipUpFaceDownCard(int i) {
        flipPile = -1;
        if (debug) {
            log.debug("" + "clicked to flip");
        }
//        Card cTmp = pileListDown[i].popTopCard();
//        pileListUp[i].doCardToPile(cTmp,DOWNSEP);

        PileMoving moving = new PileMoving(pileListDown[i],1);
        moving.setPileTo(pileListUp[i]);
        pileMover.movePile(moving);
    }

    private boolean handleMouseUpOnPile(int i) {
        // move motionPile to a pile
        if (Spider.debughole) {
            log.debug("trying to dump pile on pile");
        }
        motionPile.setPileTo(pileListUp[i]);
//        if ((pileListUp[i].length() != 0 && pileListUp[i].pileToPile(motionPile.getPileInMotion())) ||
//            pile moved to valid pile
//            (pileListUp[i].length() == 0 && pileListDown[i].length() == 0 && pileListUp[i].pileToPile(motionPile.getPileInMotion()))) {
        if ((pileListUp[i].length() != 0 && pileMover.movePile(motionPile) ||
            // pile moved to valid pile
            (pileListUp[i].length() == 0 && pileListDown[i].length() == 0 && pileMover.movePile(motionPile)))) {
            motionPile.getPileInMotion().setIsMoving(false);

            pileInMotion = null;
            pileFrom = null;
            motionPile = null;  //todo keep list of motionPIle for undo
            pile p = pileListUp[i].checkCompleteSuit();

            if (p != null) {        // We found a complete pile A-King
                acesList[current_ace++].pileToPile(p);
                if (current_ace == 8) {
                    winner = true;
                }
                return true;
            }
        }
        if (Spider.debughole) {
            log.debug("done checking for valid pile - hole");
        }
        return false;
    }

    private void mouseUpHand() {
        int i;
        boolean all_filled = true;
        // if (solitaire.debug) log.debug("" + "mouseup on handdown" );
        int totCards = 0;

        for (i = 0; i < 10; i++) {
            totCards += pileListUp[i].length();
            totCards += pileListDown[i].length();

            // repaint();
            if (pileListUp[i].length() == 0) {
                all_filled = false;
            }
        }
        if (all_filled ) {    // all piles must have at least one card before filling from hand
            pileMover.movePile(motionPiles);
        } else{
            pileMover.cancelMovePile(motionPiles);
            this.showStatus("All pile spaces must have at least one card.  Move any card to the empty space(s)");
        }
        pileInMotion = null;
        motionPile = null;
        motionPiles = null;
        pileFrom = null;
        return;
    }

    // removed for making a applet.
    // public static void main(String[] args){
    // int i,j;
    // Frame f = new solitaire();
    // f.setSize(600,400);
    // f.setVisible(true);
    // }
    public String[][] getParameterInfo() {
        String[][] info = {{"sleeptime","integer time in ms","time that the Graphics thread waits to redraw"}};

        return info;
    }

    public String getAppletInfo() {
        return "Clayburg Solitaire v0.921  Written by Gary Clayburg. (gclaybur@h2net.net)";
    }

    public static void printPile() {

    /*
     * pre: 	none
     * post: 	pile, hand printed to stdout
     * returns: 	none
     */
        int i = 0;

        // if (debug) log.debug("Here is the PileUp List:");
        // for (i =0; i< 7; i++){
        // if (debug) log.debug(i + ": " +pileListUp[i].toString());
        // }
        // if (debug) log.debug("Here is the PileDown List:");
        // for (i =0; i< 7; i++){
        // if (debug) log.debug(i + ": " +pileListDown[i].toString());
        // }
        // output hand (facedown part)
        // if (solitaire.debughand) log.debug("try..." + handDown.toString() + "why? ");
        // if (solitaire.debughand) log.debug("" + handUp );
    }

    private Canvas spiderTable;
    private Font ftr = new Font("TimesRoman",Font.PLAIN,18);
    private int sleeptime;
    private Button restart;
    private Button cheat;
    private Button undo;
    private static CardDeck solDeck;
    private static pile pileInMotion;
    private static PileMoving motionPile;
    private static PileMoving[] motionPiles;
    private static int pIMxOffset;
    private static pile pileFrom;
    private static Point pointCardInMotion = new Point(-1,-1);
    private static Point last_point = new Point(-1,-1);    // for moving a card.
    private static pile10up[] pileListUp;       // L-R 0-6
    private static pile10down[] pileListDown;       // L-R 0-6
    private static int nPileMoved;
    private static HandDown handDown;

    // private static HandUp handUp;
    private static pile pileHand;
    private static AcePileSpider[] acesList;
    private static boolean special = false;    // easter egg.
    private static boolean moveHandUpDown = false;
    private Thread runner;
    private Dimension d;
    private boolean winner = false;
    public Image offScrImage;
    public PileMover pileMover;
    public Graphics offScrGr;
    public Graphics g2;
    private int flipPile;    // which pileup to flip a new card over from piledown
    private boolean loaded = false;     // are Images loaded yet?
    private boolean flipped = true;
    private int current_ace;
    private boolean cheating = false;
}

/*--- formatting done in "Gary Java Convention" style on 11-28-2001 ---*/

