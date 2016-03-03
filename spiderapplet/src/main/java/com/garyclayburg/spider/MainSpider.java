package com.garyclayburg.spider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: gclaybur
 * Date: 2/19/14
 * Time: 9:29 AM
 */
public class MainSpider extends Frame implements AppletStub, AppletContext{
    private static Logger log = LoggerFactory.getLogger(MainSpider.class);

    MainSpider(final Spider applet) {
        log.info("Constructing MainSpider");
        setTitle("Spider main");
        setSize(800,600);
        add("Center",applet);
        setVisible(true);
        applet.setStub(this);
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                applet.manualResize();

            }

        });
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        applet.init();
        applet.start();
    }
    public boolean handleEvent(Event evt){
        if (evt.id == Event.WINDOW_DESTROY) {
            System.exit(0);
        }
        return super.handleEvent(evt);
    }

    public static void main(String[] args) {
        log.info("MainSpider starting");
        Spider applet = new Spider();
        new MainSpider(applet);
    }

    public AudioClip getAudioClip(URL url) {
        return null;
    }

    public Image getImage(URL url) {
        return null;
    }

    public Applet getApplet(String name) {
        return null;
    }

    public Enumeration<Applet> getApplets() {
        return null;
    }

    public void showDocument(URL url) {

    }

    public void showDocument(URL url,String target) {

    }

    public void showStatus(String status) {

    }

    public void setStream(String key,InputStream stream) throws IOException {

    }

    public InputStream getStream(String key) {
        return null;
    }

    public Iterator<String> getStreamKeys() {
        return null;
    }

    public URL getDocumentBase() {
        return null;
    }

    public URL getCodeBase() {
        return null;
    }

    public String getParameter(String name) {
        return null;
    }

    public AppletContext getAppletContext() {
        return this;
    }

    public void appletResize(int width,int height) {

    }
}
