package com.garyclayburg.spider;

import java.util.Stack;

public class PileMover {
    Stack stackMovingPile = null;

    public PileMover() {
        stackMovingPile = new Stack();
    }

    public boolean movePile(PileMoving movingPile) {
        boolean moved = movingPile.getPileTo().addPile(movingPile.getPileInMotion());
        if (moved){
            stackMovingPile.push(null); //spacer
            stackMovingPile.push(movingPile);
        }
        return moved;
    }

    public void movePile(PileMoving[] movingPiles) {
        stackMovingPile.push(null); //spacer
        for (int i = 0; i < movingPiles.length; i++) {
            movingPiles[i].getPileTo().doPileToPile(movingPiles[i].getPileInMotion());
            stackMovingPile.push(movingPiles[i]);
        }
    }

    public void cancelMovePile(PileMoving[] movingPiles){
        for (int i = 9; i >=0; i--) {     // put cards back in hand, for example; do not record anything
            movingPiles[i].getPileFrom().doPileToPile(movingPiles[i].getPileInMotion());
        }
    }

    public void undoMove() {
        if (!stackMovingPile.empty()) {
            PileMoving movingPile = (PileMoving) stackMovingPile.pop();
            while (movingPile != null) {
                int len = movingPile.getPileInMotion().length();
                movingPile.getPileTo().popTopCard(len);
                movingPile.getPileFrom().doPileToPile(movingPile.getPileInMotion());
                movingPile = (PileMoving) stackMovingPile.pop();
            }
        }
    }
}
