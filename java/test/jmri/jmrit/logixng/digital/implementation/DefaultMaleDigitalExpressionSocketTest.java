package jmri.jmrit.logixng.digital.implementation;

import java.util.Locale;

import jmri.InstanceManager;
import jmri.JmriException;
import jmri.jmrit.logixng.*;
import jmri.util.JUnitUtil;

import jmri.jmrit.logixng.digital.expressions.AbstractDigitalExpression;
import jmri.jmrit.logixng.digital.expressions.And;
import jmri.jmrit.logixng.digital.expressions.ExpressionTurnout;
import jmri.jmrit.logixng.digital.implementation.DefaultMaleDigitalExpressionSocket.DigitalExpressionDebugConfig;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

/**
 * Test ExpressionTimer
 * 
 * @author Daniel Bergqvist 2018
 */
public class DefaultMaleDigitalExpressionSocketTest extends MaleSocketTestBase {

    @Override
    protected String getNewSystemName() {
        return InstanceManager.getDefault(DigitalExpressionManager.class)
                .getAutoSystemName();
    }
    
    @Test
    public void testCtor() {
        DigitalExpressionBean expression = new And("IQDE321", null);
        Assert.assertNotNull("exists", new DefaultMaleDigitalExpressionSocket(expression));
    }
    
    @org.junit.jupiter.api.Test
    public void testEvaluate() throws JmriException {
        MyDigitalExpression expression = new MyDigitalExpression("IQDE321");
        DefaultMaleDigitalExpressionSocket socket = new DefaultMaleDigitalExpressionSocket(expression);
        Assert.assertNotNull("exists", socket);
        
        socket.setEnabled(true);
        socket.setErrorHandlingType(MaleSocket.ErrorHandlingType.THROW);
        
        expression.je = null;
        expression.re = null;
        expression.result = true;
        Assert.assertTrue(socket.evaluate());
        expression.result = false;
        Assert.assertFalse(socket.evaluate());
        
        expression.je = new JmriException("Test JmriException");
        expression.re = null;
        Throwable thrown = catchThrowable( () -> socket.evaluate());
        assertThat(thrown)
                .withFailMessage("Evaluate throws an exception")
                .isNotNull()
                .isInstanceOf(JmriException.class)
                .hasMessage("Test JmriException");
        
        expression.je = null;
        expression.re = new RuntimeException("Test RuntimeException");
        thrown = catchThrowable( () -> socket.evaluate());
        assertThat(thrown)
                .withFailMessage("Evaluate throws an exception")
                .isNotNull()
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Test RuntimeException");
        
        // If the socket is not enabled, it shouldn't do anything
        socket.setEnabled(false);
        expression.re = new RuntimeException("Test RuntimeException");
        thrown = catchThrowable( () -> socket.evaluate());
        assertThat(thrown)
                .withFailMessage("Evaluate does nothing")
                .isNull();
        
        // Test debug config
        socket.setEnabled(true);
        DigitalExpressionDebugConfig config = new DigitalExpressionDebugConfig();
        socket.setDebugConfig(config);
        expression.je = null;
        expression.re = null;
        config._forceResult = true;
        config._result = true;
        expression.result = false;
        Assert.assertTrue(socket.evaluate());
        config._forceResult = false;
        Assert.assertFalse(socket.evaluate());
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
        
        DigitalExpressionBean expressionA = new ExpressionTurnout("IQDE321", null);
        Assert.assertNotNull("exists", expressionA);
        DigitalExpressionBean expressionB = new MyDigitalExpression("IQDE322");
        Assert.assertNotNull("exists", expressionA);
        
        maleSocketA =
                InstanceManager.getDefault(DigitalExpressionManager.class)
                        .registerExpression(expressionA);
        Assert.assertNotNull("exists", maleSocketA);
        
        maleSocketB =
                InstanceManager.getDefault(DigitalExpressionManager.class)
                        .registerExpression(expressionB);
        Assert.assertNotNull("exists", maleSocketB);
    }

    @AfterEach
    @Override
    public void tearDown() {
        JUnitUtil.tearDown();
    }
    
    
    /**
     * This action is different from action And and is used to test the
     * male socket.
     */
    private class MyDigitalExpression extends AbstractDigitalExpression {
        
        JmriException je = null;
        RuntimeException re = null;
        boolean result = false;
        
        MyDigitalExpression(String sysName) {
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
        public boolean evaluate() throws JmriException {
            if (je != null) throw je;
            if (re != null) throw re;
            return result;
        }
        
        @Override
        public void reset() {
            throw new UnsupportedOperationException("Not supported.");
        }
        
    }
    
}
