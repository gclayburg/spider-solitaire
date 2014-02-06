/*
 * Gary Cornell and Cay S. Horstmann, Core Java (Book/CD-ROM)
 * Published By SunSoft Press/Prentice-Hall
 * Copyright (C) 1996 Sun Microsystems Inc.
 * All Rights Reserved. ISBN 0-13-565755-5
 *
 * Permission to use, copy, modify, and distribute this 
 * software and its documentation for NON-COMMERCIAL purposes
 * and without fee is hereby granted provided that this 
 * copyright notice appears in all copies. 
 * 
 * THE AUTHORS AND PUBLISHER MAKE NO REPRESENTATIONS OR 
 * WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE, EITHER 
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. THE AUTHORS
 * AND PUBLISHER SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED 
 * BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING 
 * THIS SOFTWARE OR ITS DERIVATIVES.
 */

/**
 * an improved random number generator based on Algorithm B
 * in Knuth Vol 2 p32
 * gives a range of random integers that do not exhibit
 * as much correlation as the method used in Java
 * @version 1.01 15 Feb 1996 
 * @author Cay Horstmann
 */

//package corejava;
package com.garyclayburg.spider;

public class RandomIntGenerator {
    /**
     * Constructs the class that encapsulates the random integer
     *
     * @param l the lowest integer in the range
     * @param h the highest integer in the range
     */

    public RandomIntGenerator(int l,int h) {
        low = l;
        high = h;
    }

    /**
     * Used to return a random integer in the range constructed
     */

    public int draw() {
        int r = low + (int) ((high - low + 1) * nextRandom());
        if (r > high) r = high;
        return r;
    }

    /**
     * test stub for the class
     */

    public static void main(String[] args) {
        RandomIntGenerator r1 = new RandomIntGenerator(1,10);
        RandomIntGenerator r2 = new RandomIntGenerator(0,1);
        int i;
        for (i = 1; i <= 100; i++)
            System.out.println(r1.draw() + " " + r2.draw());
    }

    private static double nextRandom() {
        int pos = (int) (Math.random() * BUFFER_SIZE);
        if (pos == BUFFER_SIZE) pos = BUFFER_SIZE - 1;
        double r = buffer[pos];
        buffer[pos] = Math.random();
        return r;
    }

    private static final int BUFFER_SIZE = 101;
    private static double[] buffer = new double[BUFFER_SIZE];

    static {
        int i;
        for (i = 0; i < BUFFER_SIZE; i++)
            buffer[i] = Math.random();
    }

    private int low;
    private int high;
}
