// This snippet creates a new standard menu

//Title:
//Version:
//Copyright:
//Author:
//Company:
//Description:

package com.garyclayburg.spider;

import javax.swing.*;


public class StandardMenu extends JMenuBar {


  JMenu fileMenu = new JMenu();
  JMenu editMenu = new JMenu();
  JMenu helpMenu = new JMenu();
  JMenuItem menuItem1 = new JMenuItem();
  JMenuItem menuItem2 = new JMenuItem();
  JMenuItem menuItem3 = new JMenuItem();
  JMenuItem menuItem4 = new JMenuItem();
  JMenuItem menuItem5 = new JMenuItem();
  JMenuItem menuItem6 = new JMenuItem();
  JMenuItem menuItem7 = new JMenuItem();
  JMenuItem menuItem8 = new JMenuItem();
  JMenuItem menuItem9 = new JMenuItem();
  JMenuItem menuItem10 = new JMenuItem();
  JMenuItem menuItem11 = new JMenuItem();
  JMenuItem menuItem12 = new JMenuItem();
  JMenuItem menuItem13 = new JMenuItem();
  JMenuItem menuItem14 = new JMenuItem();
  JMenuItem menuItem15 = new JMenuItem();
  JMenuItem menuItem16 = new JMenuItem();
  JMenuItem menuItem17 = new JMenuItem();
  JMenuItem menuItem18 = new JMenuItem();
  JMenuItem menuItem19 = new JMenuItem();
  JMenuItem jMnewSpider = new JMenuItem();
  JMenu spiderMenu = new JMenu();
  JMenuItem jMenuItem1 = new JMenuItem();

public StandardMenu() {
    try {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    fileMenu.setText("File");
    editMenu.setText("Edit");
    helpMenu.setText("Help");


    fileMenu.setMnemonic('F');
    editMenu.setMnemonic('E');
    helpMenu.setMnemonic('H');

    menuItem1.setText("New");
    menuItem1.setActionCommand("New game");
    menuItem1.setMnemonic('N');
    menuItem2.setText("Open");
    menuItem2.setMnemonic('O');
    menuItem3.setText("Save");
    menuItem3.setMnemonic('S');
    menuItem4.setText("Save As...");
    menuItem4.setMnemonic('A');
    menuItem5.setText("Print");
    menuItem5.setMnemonic('P');
    menuItem6.setText("Print Setup");
    menuItem6.setMnemonic('i');
    menuItem7.setText("Exit");
    menuItem7.setMnemonic('x');
    menuItem8.setText("Undo");
    menuItem8.setMnemonic('U');
    menuItem9.setText("Redo");
    menuItem9.setMnemonic('R');
    menuItem10.setText("Cut");
    menuItem10.setMnemonic('t');
    menuItem11.setText("Copy");
    menuItem11.setMnemonic('C');
    menuItem12.setText("Paste");
    menuItem12.setMnemonic('P');
    menuItem13.setText("Find");
    menuItem13.setMnemonic('F');
    menuItem14.setText("Replace");
    menuItem14.setMnemonic('r');
    menuItem15.setText("Goto");
    menuItem15.setMnemonic('G');
    menuItem16.setText("Contents");
    menuItem16.setMnemonic('C');
    menuItem17.setText("Search for help on");
    menuItem17.setMnemonic('H');
    menuItem18.setText("How to use help");
    menuItem18.setMnemonic('u');
    menuItem19.setText("About");
    menuItem19.setMnemonic('A');

    jMnewSpider.setText("New Spider");
    spiderMenu.setText("Spider");
    jMenuItem1.setText("New Game");
    fileMenu.add(jMnewSpider);
    fileMenu.add(menuItem1);
    fileMenu.add(menuItem2);
    fileMenu.add(menuItem3);
    fileMenu.add(menuItem4);
    fileMenu.addSeparator();
    fileMenu.add(menuItem5);
    fileMenu.add(menuItem6);
    fileMenu.addSeparator();
    fileMenu.add(menuItem7);
    editMenu.add(menuItem8);
    editMenu.add(menuItem9);
    editMenu.addSeparator();
    editMenu.add(menuItem10);
    editMenu.add(menuItem11);
    editMenu.add(menuItem12);
    editMenu.addSeparator();
    editMenu.add(menuItem13);
    editMenu.add(menuItem14);
    editMenu.add(menuItem15);
    helpMenu.add(menuItem16);
    helpMenu.add(menuItem17);
    helpMenu.add(menuItem18);
    helpMenu.add(menuItem19);

    this.add(fileMenu);
    this.add(editMenu);
    this.add(spiderMenu);
    this.add(helpMenu);
    spiderMenu.add(jMenuItem1);

  }

}

