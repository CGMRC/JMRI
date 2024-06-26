package jmri.jmrix.can.adapters.gridconnect.sproggen5.serialdriver.configurexml;

import jmri.jmrix.can.adapters.gridconnect.sproggen5.serialdriver.Sprog3PlusConnectionConfig;
import jmri.util.JUnitUtil;

import org.junit.jupiter.api.*;

/**
 * Sprog3PlusConnectionConfigXmlTest.java
 * 
 * Test for the Sprog3PlusConnectionConfigXml class
 *
 * @author  Andrew Crosland  Copyright (C) 2020
 */
public class Sprog3PlusConnectionConfigXmlTest extends jmri.jmrix.configurexml.AbstractSerialConnectionConfigXmlTestBase {

    @Test
    public void testBothConstructors() {
        Assertions.assertNotNull(xmlAdapter, "xmlAdapter exists");
        Assertions.assertNotNull(cc, "cc exists");
    }
    
    @BeforeEach
    @Override
    public void setUp() {
        JUnitUtil.setUp();
        xmlAdapter = new Sprog3PlusConnectionConfigXml();
        cc = new Sprog3PlusConnectionConfig();
    }

    @AfterEach
    @Override
    public void tearDown() {
        JUnitUtil.tearDown();
        xmlAdapter = null;
        cc = null;
    }
}
