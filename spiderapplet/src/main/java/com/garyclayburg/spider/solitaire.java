//import corejava.*;
package com.garyclayburg.spider;

import java.applet.Applet;
import java.awt.*;

//public class solitaire extends Applet implements Runnable,PileClientInterface{
public class solitaire extends Applet implements Runnable{
  public static final int SLEEP = 1000;
  public static final int CARDWIDTH = 71;
  public static final int CARDHEIGHT = 96;
  public static final int PILESEP = 10;
  public static final int XOFFSET = 20;
  public static final int YOFFSET = 116; //original solitaire
  //  public static final int YOFFSET = 424;
  public static final int XACEOFFSET = 263;
  public static final int YACEOFFSET = 10;
  public static final int ACESEP = 10;
  public static final int DOWNSEP = 10;
  public static final int XHANDOFFSET = 10;
  public static final int YHANDOFFSET = 10;
  public static final int HANDSEP = 10;
  public static int iter=0;
  public static final boolean debug = true;
  public static final boolean debughand = false;
  public static final boolean debugking = false;
  public static final boolean debugace = false;
  public Applet ac;

  public void init(){
    int i=0;
    int j=0;
    setLayout(new BorderLayout()); //needed for applet
    //    setTitle("Clayburg Solitaire");
    ac = this;  // reference to applet context (needed for Card
    d = size();
    offScrImage = createImage(d.width,d.height);    // double buffer paint()
    offScrGr = offScrImage.getGraphics();

    String sleeps = getParameter("sleeptime");
    try { sleeptime = Integer.parseInt(sleeps);}
    catch (NumberFormatException e) { sleeptime = SLEEP; }
    if (sleeptime == 0){
      sleeptime = SLEEP;
    }
    System.out.println("sleep time is " + sleeptime);
    solDeck = new CardDeck(ac);
    System.out.println("Entire Deck is: " + solDeck);
    pileListUp = new pile7up[7];
    pileListDown = new pile7down[7];
    // fill 7 piles with cards
    for (i = 0; i < 7 ;i++){
      Card[] aCard = new Card[i];
      Card[] uCard = new Card[13];  // room for face up cards on piles
      uCard[0] = new Card(ac,solDeck.draw());
      for (j = 0; j < i; j++){
	aCard[j] = new Card(ac,solDeck.draw());
      }
      
      pileListUp[i] = new pile7up(1, uCard,
			      (XOFFSET + CARDWIDTH * i + PILESEP * i),(YOFFSET + DOWNSEP));
      pileListDown[i] = new pile7down(i, aCard,
			      (XOFFSET + CARDWIDTH * i + PILESEP * i),YOFFSET);
    }
    Card[] hCard = new Card[24]; // cards to put in hand
    for (j = 0; j <24; j++){
      hCard[j] = solDeck.draw();  // don't need a new Card(), do I?
    }
    handDown = new HandDown(24,hCard,XHANDOFFSET,YHANDOFFSET);
    if (solitaire.debug) System.out.println("" + "handdown is" + handDown );
    Card[] handUpCard = new Card[24];
    handUp = new HandUp(0,handUpCard,XHANDOFFSET + Card.CARDWIDTH + HANDSEP,YHANDOFFSET);
    if (solitaire.debug) System.out.println("" + "handUp is" + handUp );
    Card[] caaa;
    acesList = new AcePile[4];  //assume max of 4 players
    //    acesList = new AcePile[16];  //assume max of 4 players
    for (i=0;i<4;i++){
      caaa = new Card[13];
      acesList[i] = new AcePile(ac, 0, 
 				caaa ,
 				(XACEOFFSET + CARDWIDTH * i + ACESEP * i),
 				YACEOFFSET);
      if (solitaire.debug) printPile();
    }
  }
  
  public void start(){
    if (runner == null){
      runner = new Thread(this);
      runner.start();
    }
  }
  public void stop(){
    if (runner != null && runner.isAlive())
      runner.stop();
    runner = null;
  }

  public void run(){

    if (!Card.checkImages(this)) // should block and wait for images
      return;
    this.showStatus("Loaded all cards...");
    // MediaTracker should have loaded all cards now...
    loaded = true;
    while (runner != null){
      repaint();
      try {
	Thread.sleep(sleeptime);
      }
      catch (InterruptedException e) {}
    }
  }

//   public UpdateForNewPlayer(pile[] newPiles){
//     //pre: length(newPiles) is multiple of 4
//     int i,j,acegroup;
//     int l = length(newPiles);
//     int numNewPlayers = l / 4; // better be a multiple of 4!
//     for (i=(numPlayers*4) ;i< ((numPlayers*l) + 4);i++){
//       caaa = new Card[13];
//       j = i % 4;  // 4 aces per player
//       acegroup = i / 4;
//       acesList[i] = new AcePile(ac, 0, 
// 				caaa ,
// 				(XACEOFFSET + CARDWIDTH * j + ACESEP * j),
// 				YACEOFFSET + (4 - acegroup +1) * (CARDHEIGHT + YACEOFFSET),
// 				newPiles[i - (numPlayers*4)]);  
//       if (solitaire.debug) printPile();
//     }
//     numPlayers += numNewPlayers;
//   }
    
  public boolean handleEvent(Event evt){
    if (evt.id == Event.WINDOW_DESTROY)
      System.exit(0);
    return super.handleEvent(evt);
  }

  public void update(Graphics g) {  // force repaint() to not erase first...
    paint(g);
  }

  public void paint(Graphics g){
    /* 
       pre:	none.
       post:	The entire solitaire screen is painted.
       */
    int i;
    if (! loaded)
      return;
    offScrGr.setColor(Color.green);
    offScrGr.fillRect(0,0,d.width,d.height);
    if (solitaire.debug) printPile();
    for (i =0; i < 7; i++){
      if (debug) System.out.println("painting " + i + "th piledown");
      pileListDown[i].paintPile(offScrGr,this);
      if (debug) System.out.println("painting " + i + "th pileup");
      pileListUp[i].paintPile(offScrGr,this);
    }
    //    theHand.paintHand(offScrGr,this);
    handDown.paintPile(offScrGr,this);
    handUp.paintPile(offScrGr,this);
    for (i=0;i<4;i++)
      acesList[i].paintPile(offScrGr,this);
    if (pileInMotion != null && pileFrom != handDown && !moveHandUpDown)
      pileInMotion.paintPile(offScrGr,this);
    
    if (winner) {
      offScrGr.setColor(Color.black);
      offScrGr.drawRect(200,200,100,100);
      offScrGr.setColor(Color.white);
      offScrGr.fillRect(200,200,100,100);
      offScrGr.setColor(Color.red);
      offScrGr.setFont(new Font("TimesRoman", Font.PLAIN, 24));
      offScrGr.drawString("You",225,250);
      offScrGr.drawString("Win!",225,276);
    }
    g.drawImage(offScrImage,0,0,this);

  }

  public boolean keyDown(Event evt, int key){
    if ((evt.modifiers & Event.CTRL_MASK ) != 0){
      special = true;
      if (solitaire.debug) System.out.println("" + "ctrlkey down" );      
    }
    else
      if (solitaire.debug) System.out.println("" + "not ctrl" );
    return true;
  }

  public boolean mouseDown(Event evt, int x, int y) {
    //    currentMousePos = find(x,y);
    int i =0;
    nPileMoved = -1;
    boolean ca;
    if (handDown.mouseOnTopCard(x,y)) { // need to flip 3 cards of hand.
      if (solitaire.debughand) System.out.println("" + "mousedown on hand" );
      if (solitaire.debughand) printPile();

      pileInMotion = handDown.popTopCard(3);
      pileFrom = handDown;
      if (pileInMotion == null  && handUp.length() != 0 ){ // need to move handUp to handDown upon mouseUp
	if (solitaire.debughand) System.out.println("marking HandUp pile to flip over.." );
	moveHandUpDown = true;
	pileInMotion = handUp.popTopCard(handUp.length());
	pileFrom = handUp;
      }
    }

    if (handUp.mouseOnTopCard(x,y) && handUp.length() !=0){   // start to drag card from HandUp
      pileInMotion = handUp.popTopCard(1);
      pileFrom = handUp;
    }
    for (i=0; i < 4; i++){
      if (acesList[i].mouseOnTopCard(x,y)) { // begin to move top card from aces
	pileInMotion = acesList[i].popTopCard(1);
	pileFrom = acesList[i];
      }
    }
    for (i =0; i < 7;i++){
      if ( pileListUp[i].mouseOnACard(x,y) > 0){// try to move a pile of cards
	pileInMotion = pileListUp[i].popTopCard(pileListUp[i].mouseOnACard(x,y));
	pileFrom = pileListUp[i];	  
	if (solitaire.debugking) System.out.println("poping a pile...");
      }
      else{  // flip up a new card for a pile7
	if (pileListUp[i].length() == 0 && pileListDown[i].mouseOnTopCard(x,y)){ 
	  flipPile = i;
	}       
      }
    }
    last_point.x = -1;  // kludge to get mouseDrag to be ok.
    if (last_point.x == -1) {
      last_point.setLocation(x,y);
      if (debug) System.out.println("" + "setting last_point");
    }
    return true;
  }

  public boolean mouseDrag(Event evt, int x, int y) {  // called automatically
    Color c;
    if (pileInMotion != null){
      pileInMotion.setLocation((pileInMotion.getx() + x - last_point.x),
			(pileInMotion.gety() + y - last_point.y));
      last_point.setLocation(x,y);
    }
    else
      if (solitaire.debug) System.out.println("bad drag");    
    return true;
  }

  public boolean mouseUp(Event evt, int x, int y){
    int i;
    int origPileMoved = nPileMoved;
    if (debug) System.out.println("" + "mouse is up!");
    if (debug) printPile();

    for (i=0; i < 4; i++){
      if (acesList[i].mouseOnTopCard(x,y) && pileInMotion != null &&
	  pileInMotion.length() ==1) {
	if (solitaire.debugace) System.out.println("can I put my card on this ace pile?");
	if (acesList[i].pileToPile(pileInMotion)){  // put a new card on ace pile
	  if (solitaire.debugace) System.out.println("should have a card on aces now!");
	  pileInMotion = null;
	  pileFrom = null;
	  if (acesList[0].length() == 13 &&
	      acesList[1].length() == 13 &&
	      acesList[2].length() == 13 &&
	      acesList[3].length() == 13 ) 
	    winner = true;
	  break;
	}
      }
    }
    if (moveHandUpDown) {  
      moveHandUpDown = false;
      if (solitaire.debughand) printPile();
      if (debughand) System.out.println("filling up handDown.");
      handDown.pileToPile(pileInMotion);
      pileInMotion = null;
      pileFrom = null;
      repaint();
      if (solitaire.debughand) printPile();
    }
    if (pileInMotion != null  && handDown.mouseOnTopCard(x,y) && (pileFrom == handDown)) { // flip up the pileInMotion cards.
      if (solitaire.debug) System.out.println("" + "mouseup on handdown" );
      handUp.pileToPile(pileInMotion); // shouldn't fail!
      pileInMotion = null;
      pileFrom = null;
    }
    if (pileFrom == handDown ) { return true;}  //don't try to drag three cards to pile7!
    for (i=0; i < 7; i++){
      if (i != origPileMoved && pileListUp[i].mouseOnTopCard(x,y) && pileInMotion != null){
	if (solitaire.debugking) System.out.println("trying to dump pile on pile");
	if (pileListUp[i].pileToPile(pileInMotion)) {  //pile moved to valid pile
	  if (solitaire.debugking) System.out.println("found a good pile to dump pile on!");
	  pileInMotion = null;
	  pileFrom = null;
	  break;
	}
	if (solitaire.debugking) System.out.println("done checking for valid pile");
      }
      else{
	if (flipPile == i && pileListDown[i].mouseOnTopCard(x,y) && pileListDown[i].length() != 0){ // flip up a new card for a pile7
	  flipPile = -1;
	  if (solitaire.debug) System.out.println("" + "clicked to flip" );
	  Card cTmp = pileListDown[i].popTopCard();
	  pileListUp[i].doCardToPile(cTmp);
	}
      }
    }
    if (pileFrom != null && pileInMotion != null) {
      // pileInMotion should never be null, just a precaution
      if (solitaire.debugking) System.out.println("LOSER! putting bad pile back" );
      if (solitaire.debugking) System.out.println("" + "pileInMotion is:" + pileInMotion );
      pileFrom.doPileToPile(pileInMotion);
      pileFrom = null;
      pileInMotion = null;
    }    
    repaint();
    return true;
  }
    
    
  // removed for making a applet.
//     public static void main(String[] args){
//     int i,j;
//     Frame f = new solitaire();
//     f.setSize(600,400);
//     f.setVisible(true);
//   }
    
  public String[][] getParameterInfo(){
    String[][] info = {
      {"sleeptime","integer time in ms","time that the Graphics thread waits to redraw"}
    };
    return info;
  }

  public String getAppletInfo(){
    return "Clayburg Solitaire v0.921  Written by Gary Clayburg. (gclaybur@h2net.net)";
  }
  public static void printPile(){
    /*
      pre: 	none
      post: 	pile, hand printed to stdout
      returns: 	none
      */
    int i = 0;
//     if (debug) System.out.println("Here is the PileUp List:");
//     for (i =0; i< 7; i++){
//       if (debug) System.out.println(i + ": " +pileListUp[i].toString());
//     }

//     if (debug) System.out.println("Here is the PileDown List:");
//     for (i =0; i< 7; i++){
//       if (debug) System.out.println(i + ": " +pileListDown[i].toString()); 
//     } 

    //output hand (facedown part)
     if (solitaire.debughand) System.out.println("try..." + handDown.toString() + "why? ");
     if (solitaire.debughand) System.out.println("" + handUp );
  }   

  private int numPlayers = 0;
  private int sleeptime;
  private static CardDeck solDeck;
  private static pile pileInMotion;
  private static pile pileFrom;
  private static Point pointCardInMotion = new Point(-1,-1);
  private static Point last_point = new Point(-1,-1);  // for moving a card.
  private static pile7up[]  pileListUp;  // L-R 0-6
  private static pile7down[]  pileListDown;  // L-R 0-6
  private static int nPileMoved;

  private static HandDown handDown;
  private static HandUp handUp;
  private static pile pileHand;

  private static AcePile[] acesList;
  private static boolean special = false;  //easter egg.
  private static boolean moveHandUpDown = false;
  private Thread runner;
  private Dimension d;
  private boolean winner = false;
  public Image offScrImage;
  public Graphics offScrGr;
  private int flipPile; // which pileup to flip a new card over from piledown
  private boolean loaded = false; // are Images loaded yet?
}

  
