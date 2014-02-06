package com.garyclayburg.spider;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AutoTestSpiderPiles {
    public final static int DUMMY_X = 2;
    public final static int DUMMY_Y = 3;
    private Card[] uCard;
    private Card sixClubs;
    private Card fiveClubs;
    private Card fourClubs;
    private Card threeClubs;
    private Card twoClubs;
    private Card aceClubs;
    private pile10up pile32clubs, pileAclubs, pile3;
    private PileMoving pileInMotion = null;
    private PileMoving pileInMotion2 = null;
    private PileMover pm = null;
    private static Logger log = Logger.getLogger(AutoTestSpiderPiles.class);

    @Before
    public void setUp() {
        log.debug("\n\nsetUp test!: " + this);
        sixClubs = new Card(Card.CLUBS,6);
        fiveClubs = new Card(Card.CLUBS,5);
        fourClubs = new Card(Card.CLUBS,4);
        threeClubs = new Card(Card.CLUBS,3);
        twoClubs = new Card(Card.CLUBS,2);
        aceClubs = new Card(Card.CLUBS,1);

        uCard = new Card[78];
        uCard[0] = threeClubs;
        uCard[1] = twoClubs;
        pile32clubs = new pile10up(2,uCard,DUMMY_X,DUMMY_Y);

        uCard = new Card[78];
        uCard[0] = aceClubs;
        pileAclubs = new pile10up(1,uCard,DUMMY_X,DUMMY_Y);

        uCard = new Card[78];
        uCard[0] = sixClubs;
        uCard[1] = fiveClubs;
        uCard[2] = fourClubs;
        pile3 = new pile10up(3,uCard,DUMMY_X,DUMMY_Y);
        pm = new PileMover();

    }

    private void moveCards(int numCards,pile pileFrom,pile pileTo) {
        //move numCards from pileForm to pileTo
        pileInMotion = new PileMoving(pileFrom,numCards);
//      pileInMotion = new PileMoving(pileFrom.popTopCard(numCards),pileFrom);

        pileInMotion.setPileTo(pileTo);
        pm.movePile(pileInMotion);
    }

    private void dumpPiles(String description) {
        log.debug("====pile dump: " + description);
        log.debug("pile 1 is " + pile32clubs);
        log.debug("pile 2 is " + pileAclubs);
        log.debug("pile 3 is " + pile3);
    }

    @Test
    public void testMove2Cards() {
        moveCards(2,pile32clubs,pile3);
        dumpPiles("  testMove2Cards(): after moving 2 cards");
        assertEquals(0,pile32clubs.length());
        assertPile3HasFiveCards();
    }

    private void assertPile3HasFiveCards() {
        assertEquals(5,pile3.length());
        assertEquals(sixClubs,pile3.peekCard(0));
        assertEquals(fiveClubs,pile3.peekCard(1));
        assertEquals(fourClubs,pile3.peekCard(2));
        assertEquals(threeClubs,pile3.peekCard(3));
        assertEquals(twoClubs,pile3.peekCard(4));
    }

    private void fullPile3() {
        assertEquals(sixClubs,pile3.peekCard(0));
        assertEquals(fiveClubs,pile3.peekCard(1));
        assertEquals(fourClubs,pile3.peekCard(2));
        assertEquals(threeClubs,pile3.peekCard(3));
        assertEquals(twoClubs,pile3.peekCard(4));
        assertEquals(aceClubs,pile3.peekCard(5));
    }

    @Test
    public void testMove3Piles() {
        moveCards(2,pile32clubs,pile3);
        moveCards(1,pileAclubs,pile3);
        fullPile3();
    }

    @Test
    public void testMove3PilesUndo() {
        moveCards(2,pile32clubs,pile3);
        moveCards(1,pileAclubs,pile3);
        fullPile3();
        pm.undoMove();
        assertPile3HasFiveCards();
        dumpPiles("before 2nd undo:");
        pm.undoMove();
        assertOrig();
    }

    @Test
    public void testMove3PilesSingleUndo() {
        pileInMotion = new PileMoving(pile32clubs,2,pile3);
        pileInMotion2 = new PileMoving(pileAclubs,1,pile3);
        PileMoving[] pilesInMotion = {pileInMotion,pileInMotion2};
        pm.movePile(pilesInMotion);
        fullPile3();
        pm.undoMove();
        assertOrig();
    }

    @Test
    public void testUndoPartialMove() {
        pileInMotion = new PileMoving(pile32clubs,2);
        pileInMotion.undo();
        assertOrig();
    }

    @Test
    public void testUndoMove2Cards() {
        moveCards(2,pile32clubs,pile3);
        dumpPiles("  testUndoMove2Cards(): after moving 2 cards");
        pm.undoMove();
        dumpPiles("  testUndoMove2Cards(): after undoing moving 2 cards");
        assertOrig();
    }

    @Test
    public void testMoveCard() {
        dumpPiles("before testMoveCard()");
        moveCards(1,pileAclubs,pile32clubs);
        log.debug("pile32clubs[0] == " + pile32clubs.peekCard(0));
        log.debug("threeClubs == " + threeClubs);
        dumpPiles("after moving testMoveCard()");

        assertEquals(threeClubs,pile32clubs.peekCard(0));
        assertEquals(twoClubs,pile32clubs.peekCard(1));
        assertEquals(aceClubs,pile32clubs.peekCard(2));
        assertEquals(3,pile32clubs.length());
        assertEquals(0,pileAclubs.length());
    }

    private void assertOrig() {
        dumpPiles("assertOrig()");
        assertEquals(threeClubs,pile32clubs.peekCard(0));
        assertEquals(twoClubs,pile32clubs.peekCard(1));
        assertEquals(2,pile32clubs.length());

        assertEquals(aceClubs,pileAclubs.peekCard(0));
        assertEquals(1,pileAclubs.length());

        assertEquals(sixClubs,pile3.peekCard(0));
        assertEquals(fiveClubs,pile3.peekCard(1));
        assertEquals(fourClubs,pile3.peekCard(2));
        assertEquals(3,pile3.length());
    }

    @Test
    public void testUndoMoveCard() {
        log.debug("testUndoMoveCard");
        assertOrig();
        moveCards(1,pileAclubs,pile32clubs);
        dumpPiles("after moving a card");
        pm.undoMove();
        log.debug("    end1 testUndoMoveCard");
        assertOrig();
        log.debug("    end testUndoMoveCard");
    }
}
