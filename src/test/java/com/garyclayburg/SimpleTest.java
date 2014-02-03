package com.garyclayburg;

import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertTrue;
/**
 * Created by maven archtype: trident-simple-archetype
 * Date: 9/18/12
 * Time: 10:37 AM
 */
public class SimpleTest {

    private static Logger log = LoggerFactory.getLogger(SimpleTest.class);

    @BeforeClass
    public static void switchOn() {
        log.debug("Switch on");
    }

    @AfterClass
    public static void switchOff() {
        log.debug("Switch off");
    }

    @Before
    public void setUp() {
        log.debug("setUp test");
    }

    @After
    public void tearDown() {
        log.debug("teardown test");
    }

    @Test
    public void bareBones(){
        assertTrue(true);
    }
}