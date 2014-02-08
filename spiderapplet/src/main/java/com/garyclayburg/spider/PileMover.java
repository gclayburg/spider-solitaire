package com.garyclayburg.spider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;

public class PileMover {
    Stack<PileMoving> stackMovingPile = null;
    private static Logger log = LoggerFactory.getLogger(PileMover.class);

    public PileMover() {
        stackMovingPile = new Stack<PileMoving>();

    }

    public boolean movePile(PileMoving movingPile) {
        boolean moved = movingPile.getPileTo().addPile(movingPile.getPileInMotion());
        if (moved) {
            stackMovingPile.push(null); //spacer
            stackMovingPile.push(movingPile);
        }
        return moved;
    }

    public void movePile(PileMoving[] movingPiles) {
        stackMovingPile.push(null); //spacer
        for (PileMoving movingPile : movingPiles) {
            movingPile.getPileTo().doPileToPile(movingPile.getPileInMotion());
            stackMovingPile.push(movingPile);
        }
    }

    public void cancelMovePile(PileMoving[] movingPiles) {
        for (int i = 9; i >= 0; i--) {     // put cards back in hand, for example; do not record anything
            movingPiles[i].getPileFrom().doPileToPile(movingPiles[i].getPileInMotion());
        }
    }

    public void undoMove() {
        log.debug("undo stack: " + this.toString());
        if (!stackMovingPile.empty()) {
            PileMoving movingPile = stackMovingPile.pop();
            while (movingPile != null) {
                int len = movingPile.getPileInMotion().length();
                movingPile.getPileTo().popTopCard(len);
                movingPile.getPileFrom().doPileToPile(movingPile.getPileInMotion());
                movingPile = stackMovingPile.pop();
            }
        }
    }

    @Override
    public String toString() {
//        log.debug("stackMovingPile: " + stackMovingPile);
        StringBuilder sb = new StringBuilder();
        for (PileMoving pileMoving : stackMovingPile) {
            if (pileMoving != null) {
                sb.append(stackMovingPile.size()).append(" undo elements\n");
                sb.append(" moving").append(pileMoving.getPileInMotion());
                sb.append(" from: ").append(pileMoving.getPileFrom());
                sb.append(" to: ").append(pileMoving.getPileTo());
            } else {
                sb.append("\n -- spacer --");
            }
        }
        return sb.toString();
    }
}
