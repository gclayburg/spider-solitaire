package com.garyclayburg.spider;

import java.rmi.RemoteException;

public interface PileClientInterface {
  public void UpdateForNewPlayer(pile[] newPiles) throws RemoteException;
}

