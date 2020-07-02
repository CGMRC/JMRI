package jmri.jmrit.logixng.digital.implementation;

import java.util.Locale;

import jmri.InstanceManager;
import jmri.JmriException;
import jmri.jmrit.logixng.*;
import jmri.util.JUnitUtil;

import jmri.jmrit.logixng.digital.actions.AbstractDigitalAction;
import jmri.jmrit.logixng.digital.actions.ActionTurnout;
import jmri.jmrit.logixng.digital.actions.Many;
import jmri.jmrit.logixng.digital.implementation.DefaultMaleDigitalActionSocket.DigitalActionDebugConfig;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.catchThrowable;

/**
 * Test ExpressionTimer
 * 
 * @author Daniel Bergqvist 2018
 */
public class DefaultMaleDigitalActionSocketTest extends MaleSocketTestBase{

    @Override
    protected String getNewSystemName() {
        return InstanceManager.getDefault(DigitalActionManager.class)
                .getAutoSystemName();
    }
    
    @Test
    public void testCtor() {
        DigitalActionBean action = new Many("IQDA321", null);
        Assert.assertNotNull("exists", new DefaultMaleDigitalActionSocket(action));
    }
    
    @org.junit.jupiter.api.Test
    public void testExecute() throws JmriException {
        MyDigitalAction action = new MyDigitalAction("IQDA321");
        DefaultMaleDigitalActionSocket socket = new DefaultMaleDigitalActionSocket(action);
        Assert.assertNotNull("exists", socket);
        
        socket.setEnabled(true);
        socket.setErrorHandlingType(MaleSocket.ErrorHandlingType.THROW);
        
        action.je = null;
        action.re = null;
        action._hasExecuted = false;
        socket.execute();
        Assert.assertTrue(action._hasExecuted);
        
        action.je = new JmriException("Test JmriException");
        action.re = null;
        Throwable thrown = catchThrowable( () -> socket.execute());
        assertThat(thrown)
                .withFailMessage("Evaluate throws an exception")
                .isNotNull()
                .isInstanceOf(JmriException.class)
                .hasMessage("Test JmriException");
        
        action.je = null;
        action.re = new RuntimeException("Test RuntimeException");
        thrown = catchThrowable( () -> socket.execute());
        assertThat(thrown)
                .withFailMessage("Evaluate throws an exception")
                .isNotNull()
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Test RuntimeException");
        
        // If the socket is not enabled, it shouldn't do anything
        socket.setEnabled(false);
        action.re = new RuntimeException("Test RuntimeException");
        thrown = catchThrowable( () -> socket.execute());
        assertThat(thrown)
                .withFailMessage("Evaluate does nothing")
                .isNull();
        
        // Test debug config
        socket.setEnabled(true);
        DigitalActionDebugConfig config = new DigitalActionDebugConfig();
        socket.setDebugConfig(config);
        action.je = null;
        action.re = null;
        config._dontExecute = true;
        action._hasExecuted = false;
        socket.execute();
        Assert.assertFalse(action._hasExecuted);
        config._dontExecute = false;
        action._hasExecuted = false;
        socket.execute();
        Assert.assertTrue(action._hasExecuted);
    }
    
    // The minimal setup for log4J
    @BeforeEach
    @Override
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.resetProfileManager();
        JUnitUtil.initConfigureManager();
        JUnitUtil.initInternalSensorManager();
        JUnitUtil.initInternalTurnoutManager();
        JUnitUtil.initLogixNGManager();
        
        DigitalActionBean actionA = new ActionTurnout("IQDA321", null);
        Assert.assertNotNull("exists", actionA);
        DigitalActionBean actionB = new MyDigitalAction("IQDA322");
        Assert.assertNotNull("exists", actionA);
        
        maleSocketA =
                InstanceManager.getDefault(DigitalActionManager.class)
                        .registerAction(actionA);
        Assert.assertNotNull("exists", maleSocketA);
        
        maleSocketB =
                InstanceManager.getDefault(DigitalActionManager.class)
                        .registerAction(actionB);
        Assert.assertNotNull("exists", maleSocketB);
    }

    @AfterEach
    @Override
    public void tearDown() {
        JUnitUtil.tearDown();
    }
    
    
    /**
     * This action is different from MyStringAction and is used to test the
     * male socket.
     */
    private class MyDigitalAction extends AbstractDigitalAction {
        
        JmriException je = null;
        RuntimeException re = null;
        boolean _hasExecuted = false;
        
        MyDigitalAction(String sysName) {
            super(sysName, null);
        }

        @Override
        protected void registerListenersForThisClass() {
            // Do nothing
        }

        @Override
        protected void unregisterListenersForThisClass() {
            // Do nothing
        }

        @Override
        protected void disposeMe() {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public String getShortDescription(Locale locale) {
            return "My short description";
        }

        @Override
        public String getLongDescription(Locale locale) {
            return "My long description";
        }

        @Override
        public FemaleSocket getChild(int index) throws IllegalArgumentException, UnsupportedOperationException {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public int getChildCount() {
            return 0;
        }

        @Override
        public Category getCategory() {
            return Category.COMMON;
        }

        @Override
        public boolean isExternal() {
            return false;
        }

        @Override
        public void setup() {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public void execute() throws JmriException {
            if (je != null) throw je;
            if (re != null) throw re;
            _hasExecuted = true;
        }
        
    }
    
}
