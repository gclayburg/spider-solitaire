
//Title:       Spider GUI test
//Version:     
//Copyright:   Copyright (c) 1999
//Author:      Gary Clayburg
//Company:     hi
//Description: Just testing GUI elements to eventually incorporate into Spider applet
package com.garyclayburg.spider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Gui1 extends JApplet {
  boolean isStandalone = false;
  JPopupMenu jPopupMenu1 = new JPopupMenu();
  JPopupMenu jPopupMenu2 = new JPopupMenu();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel jPanel1 = new JPanel();
  JRadioButton jRadioButton3 = new JRadioButton();
  JRadioButton jRadioButton1 = new JRadioButton();
  JPanel jPanel2 = new JPanel();
  JButton jButton1 = new JButton();
  JTextField myTextField = new JTextField();
  JLabel jLabel1 = new JLabel();
  FlowLayout flowLayout1 = new FlowLayout();

  //Get a parameter value
  public String getParameter(String key, String def) {
    return isStandalone ? System.getProperty(key, def) :
      (getParameter(key) != null ? getParameter(key) : def);
  }

  //Construct the applet
  public Gui1() {
  }

  //Initialize the applet
  public void init() {
    try  {
      jbInit();
    }
    catch(Exception e)  {
      e.printStackTrace();
    }
  }

  //Component initialization
  private void jbInit() throws Exception {
    this.setSize(new Dimension(400, 180));
    this.getContentPane().setLayout(borderLayout1);
    jRadioButton3.setText("jRadioButton2");
    jRadioButton3.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        jRadioButton3_actionPerformed(e);
      }
    });
    jRadioButton1.setText("jRadioButton1");
    jRadioButton1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        jRadioButton1_actionPerformed(e);
      }
    });
    jButton1.setToolTipText("this is a tip");
    jButton1.setHorizontalTextPosition(SwingConstants.LEFT);
    jButton1.setText("push me");
    jButton1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        jButton1_actionPerformed(e);
      }
    });
    myTextField.setText("push something already");
    jLabel1.setText("this is a label");
    jPanel2.setLayout(flowLayout1);
    this.getContentPane().add(jPanel1, BorderLayout.SOUTH);
    jPanel1.add(jRadioButton3, null);
    jPanel1.add(jRadioButton1, null);
    jPanel1.add(jButton1, null);
    this.getContentPane().add(jPanel2, BorderLayout.CENTER);
    jPanel2.add(myTextField, null);
    jPanel2.add(jLabel1, null);
  }

  //Start the applet
  public void start() {
  }

  //Stop the applet
  public void stop() {
  }

  //Destroy the applet
  public void destroy() {
  }

  //Get Applet information
  public String getAppletInfo() {
    return "Applet Information";
  }

  //Get parameter info
  public String[][] getParameterInfo() {
    return null;
  }

  //Main method
  public static void main(String[] args) {
    Gui1 applet = new Gui1();
    applet.isStandalone = true;
    JFrame frame = new JFrame();
    frame.setTitle("Applet Frame");
    frame.getContentPane().add(applet, BorderLayout.CENTER);
    applet.init();
    applet.start();
    frame.setSize(400,320);
    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    frame.setLocation((d.width - frame.getSize().width) / 2, (d.height - frame.getSize().height) / 2);
    frame.setVisible(true);
  }
  // static initializer for setting look & feel
  static {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      //UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    }
    catch (Exception e) {}
  }

  void jButton1_actionPerformed(ActionEvent e) {
    myTextField.setText("button pushed");
  }

  void jRadioButton1_actionPerformed(ActionEvent e) {
    myTextField.setText("radio 1");
  }

  void jRadioButton3_actionPerformed(ActionEvent e) {
    myTextField.setText("radio 2");
  }

} 