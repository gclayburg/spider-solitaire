package com.garyclayburg.spider;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PileServ extends Remote {
  public String himan() throws RemoteException;
  void AddPlayer(PileClientInterface newPlayer) throws RemoteException;
  void PileToPile(pile newPile) throws RemoteException;
}

