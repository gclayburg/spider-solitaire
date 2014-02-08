package com.garyclayburg.spider;

public class PileMoving {
    private pile _pileInMotion = null;
    private pile _pileFrom = null;
    private pile _pileTo = null;

    public PileMoving(pile pileFrom,int numCards) {
        _pileInMotion = pileFrom.popTopCard(numCards);
        _pileFrom = pileFrom;
    }

    public PileMoving(pile pileFrom,int numCards,pile pileTo) {
        _pileInMotion = pileFrom.popTopCard(numCards);
        _pileFrom = pileFrom;
        _pileTo = pileTo;
    }

    public void undo() {
        _pileFrom.doPileToPile(_pileInMotion);
        _pileFrom = null;
        _pileInMotion = null;
    }

    @Override
    public String toString() {
        return "from: " + _pileFrom != null ? _pileFrom.toString() :
               "null" + "  inMotion: " + _pileInMotion != null ? _pileInMotion.toString() : "null";
    }

    public void setPileTo(pile pileTo) {
        _pileTo = pileTo;
    }

    public pile getPileTo() {
        return _pileTo;
    }

    public pile getPileFrom() {
        return _pileFrom;
    }

    public pile getPileInMotion() {
        return _pileInMotion;
    }
}
