package com.garyclayburg.spider;
import java.util.Stack;

public class PileMover{
  Stack stackMovingPile = null;

  public PileMover(){
    stackMovingPile = new Stack();
  }

  public void movePile(PileMoving movingPile){
    movingPile.getPileTo().addPile(movingPile.getPileInMotion());
    stackMovingPile.push(null); //spacer
    stackMovingPile.push(movingPile);
  }

  public void movePile(PileMoving[] movingPiles){
    stackMovingPile.push(null); //spacer
    for(int i = 0;i<movingPiles.length;i++){
      movingPiles[i].getPileTo().addPile(movingPiles[i].getPileInMotion());
      stackMovingPile.push(movingPiles[i]);
    }
  }

  public void undoMove(){
    if (!stackMovingPile.empty()){
      PileMoving movingPile = (PileMoving) stackMovingPile.pop();
      while (movingPile != null){
	int len = movingPile.getPileInMotion().length();
	movingPile.getPileTo().popTopCard(len);
	movingPile.getPileFrom().addPile(movingPile.getPileInMotion());
	movingPile = (PileMoving) stackMovingPile.pop();
      }
    }
  }
}
