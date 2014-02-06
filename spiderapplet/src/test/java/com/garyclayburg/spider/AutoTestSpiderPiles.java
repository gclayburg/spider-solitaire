package com.garyclayburg.spider;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.log4j.Logger;

public class AutoTestSpiderPiles extends TestCase {
    public final static int DUMMY_X = 2;
    public final static int DUMMY_Y = 3;
    private Card[] uCard;
    private Card sixClubs;
    private Card fiveClubs;
    private Card fourClubs;
    private Card threeClubs;
    private Card twoClubs;
    private Card aceClubs;
    private pile10up pile1, pile2, pile3;
    private PileMoving pileInMotion = null;
    private PileMoving pileInMotion2 = null;
    private PileMover pm = null;
    private static Logger log = Logger.getLogger(AutoTestSpiderPiles.class);

    public AutoTestSpiderPiles(String name) {
        super(name);
    }

    public void setUp() {
        log.debug("\n\nsetUp test!: " + this);
        sixClubs = new Card(Card.CLUBS, 6);
        fiveClubs = new Card(Card.CLUBS, 5);
        fourClubs = new Card(Card.CLUBS, 4);
        threeClubs = new Card(Card.CLUBS, 3);
        twoClubs = new Card(Card.CLUBS, 2);
        aceClubs = new Card(Card.CLUBS, 1);

        uCard = new Card[78];
        uCard[0] = threeClubs;
        uCard[1] = twoClubs;
        pile1 = new pile10up(2, uCard, DUMMY_X, DUMMY_Y);

        uCard = new Card[78];
        uCard[0] = aceClubs;
        pile2 = new pile10up(1, uCard, DUMMY_X, DUMMY_Y);

        uCard = new Card[78];
        uCard[0] = sixClubs;
        uCard[1] = fiveClubs;
        uCard[2] = fourClubs;
        pile3 = new pile10up(3, uCard, DUMMY_X, DUMMY_Y);
        pm = new PileMover();

    }

    private void moveCards(int numCards, pile pileFrom, pile pileTo) {
        //move numCards from pileForm to pileTo
        pileInMotion = new PileMoving(pileFrom, numCards);
//      pileInMotion = new PileMoving(pileFrom.popTopCard(numCards),pileFrom);

        pileInMotion.setPileTo(pileTo);
        pm.movePile(pileInMotion);
    }

    private void dumpPiles(String description) {
        log.debug("====pile dump: " + description);
        log.debug("pile 1 is " + pile1);
        log.debug("pile 2 is " + pile2);
        log.debug("pile 3 is " + pile3);
    }

    public void testMove2Cards() {
        moveCards(2, pile1, pile3);
        dumpPiles("  testMove2Cards(): after moving 2 cards");
        assert (pile1.length() == 0);
        assertPile3HasFiveCards();
    }

    private void assertPile3HasFiveCards() {
        assert (pile3.length() == 5);
        assert (pile3.peekCard(0).equals(sixClubs));
        assert (pile3.peekCard(1).equals(fiveClubs));
        assert (pile3.peekCard(2).equals(fourClubs));
        assert (pile3.peekCard(3).equals(threeClubs));
        assert (pile3.peekCard(4).equals(twoClubs));
    }

    private void fullPile3() {
        assert (pile3.peekCard(0).equals(sixClubs));
        assert (pile3.peekCard(1).equals(fiveClubs));
        assert (pile3.peekCard(2).equals(fourClubs));
        assert (pile3.peekCard(3).equals(threeClubs));
        assert (pile3.peekCard(4).equals(twoClubs));
        assert (pile3.peekCard(5).equals(aceClubs));
    }

    public void testMove3Piles() {
        moveCards(2, pile1, pile3);
        moveCards(1, pile2, pile3);
        fullPile3();
    }

    public void testMove3PilesUndo() {
        moveCards(2, pile1, pile3);
        moveCards(1, pile2, pile3);
        fullPile3();
        pm.undoMove();
        assertPile3HasFiveCards();
        dumpPiles("before 2nd undo:");
        pm.undoMove();
        assertOrig();
    }

    public void testMove3PilesSingleUndo() {
        pileInMotion = new PileMoving(pile1, 2, pile3);
        pileInMotion2 = new PileMoving(pile2, 1, pile3);
        PileMoving[] pilesInMotion = {pileInMotion, pileInMotion2};
        pm.movePile(pilesInMotion);
        fullPile3();
        pm.undoMove();
        assertOrig();
    }

    public void testUndoPartialMove() {
        pileInMotion = new PileMoving(pile1, 2);
        pileInMotion.undo();
        assertOrig();
    }

    public void testUndoMove2Cards() {
        moveCards(2, pile1, pile3);
        dumpPiles("  testUndoMove2Cards(): after moving 2 cards");
        pm.undoMove();
        dumpPiles("  testUndoMove2Cards(): after undoing moving 2 cards");
        assertOrig();
    }


    public void testMoveCard() {
        dumpPiles("before testMoveCard()");
        moveCards(1, pile2, pile1);
        log.debug("pile1[0] == " + pile1.peekCard(0));
        log.debug("threeClubs == " + threeClubs);
        dumpPiles("after moving testMoveCard()");

        assert (pile1.peekCard(0) == threeClubs);
        assert (pile1.peekCard(1) == twoClubs);
        assert (pile1.peekCard(2) == aceClubs);
        assert (pile1.length() == 3);
        assert (pile2.length() == 0);
    }

    private void assertOrig() {
        dumpPiles("assertOrig()");
        assert (pile1.peekCard(0) == threeClubs);
        assert (pile1.peekCard(1) == twoClubs);
        assert (pile1.length() == 2);

        assert (pile2.peekCard(0) == aceClubs);
        assert (pile2.length() == 1);

        assert (pile3.peekCard(0) == sixClubs);
        assert (pile3.peekCard(1) == fiveClubs);
        assert (pile3.peekCard(2) == fourClubs);
        assert (pile3.length() == 3);
    }

    public void testUndoMoveCard() {
        log.debug("testUndoMoveCard");
        assertOrig();
        moveCards(1, pile2, pile1);
        dumpPiles("after moving a card");
        pm.undoMove();
        log.debug("    end1 testUndoMoveCard");
        assertOrig();
        log.debug("    end testUndoMoveCard");
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new AutoTestSpiderPiles("testMoveCard"));
        suite.addTest(new AutoTestSpiderPiles("testUndoMoveCard"));
        suite.addTest(new AutoTestSpiderPiles("testMove2Cards"));
        suite.addTest(new AutoTestSpiderPiles("testUndoMove2Cards"));
        suite.addTest(new AutoTestSpiderPiles("testMove3Piles"));
        suite.addTest(new AutoTestSpiderPiles("testMove3PilesUndo"));
        suite.addTest(new AutoTestSpiderPiles("testMove3PilesSingleUndo"));
        return suite;
    }

    public static void main(String args[]) {
        junit.textui.TestRunner.run(suite());
    }
}
