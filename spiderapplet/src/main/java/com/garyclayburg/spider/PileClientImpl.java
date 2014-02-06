package com.garyclayburg.spider;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;

public class PileClientImpl{
  public static void main(String args[]){
    System.setSecurityManager(new RMISecurityManager());
    try{
      PileServ p = (PileServ) Naming.lookup("hi");
      String message = p.himan();
      System.out.println("here it goes");
      System.out.println("PileClientImpl: " + message);
    }
    catch (Exception e){
      System.out.println("Exception in main: " + e);
    }
  }
}
