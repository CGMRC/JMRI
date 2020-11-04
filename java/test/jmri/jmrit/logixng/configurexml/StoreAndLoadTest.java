package jmri.jmrit.logixng.configurexml;

import java.beans.PropertyVetoException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import jmri.*;
import jmri.jmrit.logixng.*;
import jmri.jmrit.logixng.analog.actions.*;
import jmri.jmrit.logixng.analog.expressions.*;
import jmri.jmrit.logixng.digital.actions.*;
import jmri.jmrit.logixng.digital.expressions.*;
import jmri.jmrit.logixng.string.actions.*;
import jmri.jmrit.logixng.string.expressions.*;
import jmri.util.FileUtil;
import jmri.util.JUnitUtil;

import org.junit.*;

/**
 * Creates a LogixNG with all actions and expressions to test store and load.
 * <P>
 * It uses the Base.printTree(PrintWriter writer, String indent) method to
 * compare the LogixNGs before and after store and load.
 */
public class StoreAndLoadTest {
    
    public void setupInitialConditionalNGTree(ConditionalNG conditionalNG) {
        try {
            DigitalActionManager digitalActionManager =
                    InstanceManager.getDefault(DigitalActionManager.class);
            
            FemaleSocket femaleSocket = conditionalNG.getFemaleSocket();
            MaleDigitalActionSocket actionManySocket =
                    InstanceManager.getDefault(DigitalActionManager.class)
                            .registerAction(
                                    new jmri.jmrit.logixng.digital.actions.Many(
                                            digitalActionManager.getAutoSystemName(), null));
            femaleSocket.connect(actionManySocket);
            femaleSocket.setLock(Base.Lock.HARD_LOCK);

            femaleSocket = actionManySocket.getChild(0);
            MaleDigitalActionSocket actionIfThenSocket =
                    InstanceManager.getDefault(DigitalActionManager.class)
                            .registerAction(new IfThenElse(digitalActionManager.getAutoSystemName(), null, IfThenElse.Type.TRIGGER_ACTION));
            femaleSocket.connect(actionIfThenSocket);
        } catch (SocketAlreadyConnectedException e) {
            // This should never be able to happen.
            throw new RuntimeException(e);
        }
    }
    
    @Test
    public void testLogixNGs() throws PropertyVetoException, Exception {
/*        
        // FOR TESTING ONLY. REMOVE LATER.
        if (1==0) {
            if (!GraphicsEnvironment.isHeadless()) {

                for (jmri.Logix l : InstanceManager.getDefault(jmri.LogixManager.class).getNamedBeanSet()) {
                    String sysName = l.getSystemName();
                    if (!sysName.equals("SYS") && !sysName.startsWith("RTX")) {
                        jmri.jmrit.logixng.tools.ImportLogix il = new jmri.jmrit.logixng.tools.ImportLogix(l);
                        il.doImport();
                    }
                }
            }
        }
*/        
        Light light1 = InstanceManager.getDefault(LightManager.class).provide("IL1");
        light1.setCommandedState(Light.OFF);
        Light light2 = InstanceManager.getDefault(LightManager.class).provide("IL2");
        light2.setCommandedState(Light.OFF);
        Sensor sensor1 = InstanceManager.getDefault(SensorManager.class).provide("IS1");
        sensor1.setCommandedState(Sensor.INACTIVE);
        Sensor sensor2 = InstanceManager.getDefault(SensorManager.class).provide("IS2");
        sensor2.setCommandedState(Sensor.INACTIVE);
        Turnout turnout1 = InstanceManager.getDefault(TurnoutManager.class).provide("IT1");
        turnout1.setCommandedState(Turnout.CLOSED);
        Turnout turnout2 = InstanceManager.getDefault(TurnoutManager.class).provide("IT2");
        turnout2.setCommandedState(Turnout.CLOSED);
        Turnout turnout3 = InstanceManager.getDefault(TurnoutManager.class).provide("IT3");
        turnout3.setCommandedState(Turnout.CLOSED);
        Turnout turnout4 = InstanceManager.getDefault(TurnoutManager.class).provide("IT4");
        turnout4.setCommandedState(Turnout.CLOSED);
        Turnout turnout5 = InstanceManager.getDefault(TurnoutManager.class).provide("IT5");
        turnout5.setCommandedState(Turnout.CLOSED);

        Memory memory1 = InstanceManager.getDefault(MemoryManager.class).provide("IM1");
        Memory memory2 = InstanceManager.getDefault(MemoryManager.class).provide("IM2");
        Memory memory3 = InstanceManager.getDefault(MemoryManager.class).provide("IM3");
        Memory memory4 = InstanceManager.getDefault(MemoryManager.class).provide("IM4");

        LogixNG_Manager logixNG_Manager = InstanceManager.getDefault(LogixNG_Manager.class);
        ConditionalNG_Manager conditionalNGManager = InstanceManager.getDefault(ConditionalNG_Manager.class);
        AnalogActionManager analogActionManager = InstanceManager.getDefault(AnalogActionManager.class);
        AnalogExpressionManager analogExpressionManager = InstanceManager.getDefault(AnalogExpressionManager.class);
        DigitalActionManager digitalActionManager = InstanceManager.getDefault(DigitalActionManager.class);
        DigitalBooleanActionManager digitalBooleanActionManager = InstanceManager.getDefault(DigitalBooleanActionManager.class);
        DigitalExpressionManager digitalExpressionManager = InstanceManager.getDefault(DigitalExpressionManager.class);
        StringActionManager stringActionManager = InstanceManager.getDefault(StringActionManager.class);
        StringExpressionManager stringExpressionManager = InstanceManager.getDefault(StringExpressionManager.class);
        
        // Create an empty LogixNG
        logixNG_Manager.createLogixNG("An empty logixNG");
        
        // Create a LogixNG with an empty ConditionalNG
        LogixNG logixNG = logixNG_Manager.createLogixNG("A logixNG with an empty conditionlNG");
        ConditionalNG conditionalNG =
                conditionalNGManager.createConditionalNG("An empty conditionalNG");
        logixNG.addConditionalNG(conditionalNG);
        logixNG.setEnabled(false);
        conditionalNG.setEnabled(false);
        
        
        // Create LogixNG with a tree of stuff
        logixNG = logixNG_Manager.createLogixNG("A logixNG");
        conditionalNG =
                conditionalNGManager.createConditionalNG("A conditionalNG");
        
        
        
        
        
        
        setupInitialConditionalNGTree(conditionalNG);
        
        logixNG.addConditionalNG(conditionalNG);
        logixNG.setEnabled(true);
        conditionalNG.setEnabled(true);

        logixNG = logixNG_Manager.createLogixNG("Another logixNG");
        conditionalNG =
                conditionalNGManager.createConditionalNG("");
        setupInitialConditionalNGTree(conditionalNG);
        logixNG.addConditionalNG(conditionalNG);

        logixNG = logixNG_Manager.createLogixNG("Yet another logixNG");
        conditionalNG =
                conditionalNGManager.createConditionalNG("Yet another conditionalNG");
        
        
        
        
        
        
        if (1==0) {
        
        
        
        
        
        
        setupInitialConditionalNGTree(conditionalNG);
        logixNG.addConditionalNG(conditionalNG);

        MaleSocket socketMany = conditionalNG.getChild(0).getConnectedSocket();
        MaleSocket socketIfThen = socketMany.getChild(0).getConnectedSocket();

        Or expressionOr = new Or(logixNG_Manager.getSystemNamePrefix()+"DE:AUTO:00001", null);
        MaleSocket socketOr = digitalExpressionManager.registerExpression(expressionOr);
        socketIfThen.getChild(0).connect(socketOr);

        int index = 0;

        Or expressionOr2 = new Or(logixNG_Manager.getSystemNamePrefix()+"DE:AUTO:00002", "My Or expression");
        MaleSocket socketOr2 = digitalExpressionManager.registerExpression(expressionOr2);
        socketOr.getChild(index++).connect(socketOr2);

        And expressionAnd = new And(logixNG_Manager.getSystemNamePrefix()+"DE:AUTO:00003", null);
        MaleSocket socketAnd = digitalExpressionManager.registerExpression(expressionAnd);
        socketOr.getChild(index++).connect(socketAnd);

        And expressionAnd2 = new And(logixNG_Manager.getSystemNamePrefix()+"DE:AUTO:00004", "My And expression");
        MaleSocket socketAnd2 = digitalExpressionManager.registerExpression(expressionAnd2);
        socketOr.getChild(index++).connect(socketAnd2);

        ExpressionTurnout expressionTurnout3 = new ExpressionTurnout(logixNG_Manager.getSystemNamePrefix()+"DE:AUTO:00005", null);
        expressionTurnout3.setTurnout(turnout3);
        expressionTurnout3.setTurnoutState(ExpressionTurnout.TurnoutState.THROWN);
        MaleSocket socketTurnout3 = digitalExpressionManager.registerExpression(expressionTurnout3);
        expressionAnd.getChild(0).connect(socketTurnout3);

        ExpressionTurnout expressionTurnout4 = new ExpressionTurnout(logixNG_Manager.getSystemNamePrefix()+"DE:AUTO:00006", "My new turnout");
        expressionTurnout4.setTurnout(turnout4);
        expressionTurnout4.setTurnoutState(ExpressionTurnout.TurnoutState.CLOSED);
        expressionTurnout4.set_Is_IsNot(Is_IsNot_Enum.IS);
        MaleSocket socketTurnout4 = digitalExpressionManager.registerExpression(expressionTurnout4);
        expressionAnd.getChild(1).connect(socketTurnout4);

        ExpressionTurnout expressionTurnout5 = new ExpressionTurnout(logixNG_Manager.getSystemNamePrefix()+"DE:AUTO:00007", null);
        expressionTurnout5.setTurnout(turnout5);
        expressionTurnout5.setTurnoutState(ExpressionTurnout.TurnoutState.OTHER);
        expressionTurnout5.set_Is_IsNot(Is_IsNot_Enum.IS_NOT);
        MaleSocket socketTurnout5 = digitalExpressionManager.registerExpression(expressionTurnout5);
        expressionAnd.getChild(2).connect(socketTurnout5);

        Antecedent expressionAntecedent = new Antecedent(logixNG_Manager.getSystemNamePrefix()+"DE:AUTO:00008", null);
        expressionAntecedent.setAntecedent("R1");
        MaleSocket socketAntecedent = digitalExpressionManager.registerExpression(expressionAntecedent);
        socketOr.getChild(index++).connect(socketAntecedent);

        Antecedent expressionAntecedent2 = new Antecedent(logixNG_Manager.getSystemNamePrefix()+"DE:AUTO:00009", "My Antecedent expression");
        expressionAntecedent2.setAntecedent("R1");
        MaleSocket socketAntecedent2 = digitalExpressionManager.registerExpression(expressionAntecedent2);
        socketOr.getChild(index++).connect(socketAntecedent2);

        Antecedent expressionAntecedent3 = new Antecedent(logixNG_Manager.getSystemNamePrefix()+"DE:AUTO:00010", "My Antecedent expression");
        expressionAntecedent3.setAntecedent("R1");
        MaleSocket socketAntecedent3 = digitalExpressionManager.registerExpression(expressionAntecedent3);
        socketAntecedent2.getChild(0).connect(socketAntecedent3);

        False expressionFalse = new False(logixNG_Manager.getSystemNamePrefix()+"DE:AUTO:00011", null);
        MaleSocket socketFalse = digitalExpressionManager.registerExpression(expressionFalse);
        socketOr.getChild(index++).connect(socketFalse);

        False expressionFalse2 = new False(logixNG_Manager.getSystemNamePrefix()+"DE:AUTO:00012", "My False expression");
        MaleSocket socketFalse2 = digitalExpressionManager.registerExpression(expressionFalse2);
        socketOr.getChild(index++).connect(socketFalse2);

        Hold expressionHold = new Hold(logixNG_Manager.getSystemNamePrefix()+"DE:AUTO:00013", null);
        MaleSocket socketHold = digitalExpressionManager.registerExpression(expressionHold);
        socketOr.getChild(index++).connect(socketHold);

        Hold expressionHold2 = new Hold(logixNG_Manager.getSystemNamePrefix()+"DE:AUTO:00014", "My Hold expression");
        MaleSocket socketHold2 = digitalExpressionManager.registerExpression(expressionHold2);
        socketOr.getChild(index++).connect(socketHold2);

        Hold expressionHold3 = new Hold(logixNG_Manager.getSystemNamePrefix()+"DE:AUTO:00015", "My other Hold expression");
        MaleSocket socketHold3 = digitalExpressionManager.registerExpression(expressionHold3);
        socketHold2.getChild(0).connect(socketHold3);

        Hold expressionHold4 = new Hold(logixNG_Manager.getSystemNamePrefix()+"DE:AUTO:00016", "My other Hold expression");
        MaleSocket socketHold4 = digitalExpressionManager.registerExpression(expressionHold4);
        socketHold2.getChild(1).connect(socketHold4);

        ResetOnTrue expressionResetOnTrue = new ResetOnTrue(logixNG_Manager.getSystemNamePrefix()+"DE:AUTO:00017", null);
        MaleSocket socketResetOnTrue = digitalExpressionManager.registerExpression(expressionResetOnTrue);
        socketOr.getChild(index++).connect(socketResetOnTrue);

        ResetOnTrue expressionResetOnTrue2 = new ResetOnTrue(logixNG_Manager.getSystemNamePrefix()+"DE:AUTO:00018", "My ResetOnTrue expression");
        MaleSocket socketResetOnTrue2 = digitalExpressionManager.registerExpression(expressionResetOnTrue2);
        expressionResetOnTrue.getChild(0).connect(socketResetOnTrue2);

        ResetOnTrue expressionResetOnTrue3 = new ResetOnTrue(logixNG_Manager.getSystemNamePrefix()+"DE:AUTO:00019", "My ResetOnTrue expression");
        MaleSocket socketResetOnTrue3 = digitalExpressionManager.registerExpression(expressionResetOnTrue3);
        expressionResetOnTrue.getChild(1).connect(socketResetOnTrue3);

        ExpressionTimer expressionTimer = new ExpressionTimer(logixNG_Manager.getSystemNamePrefix()+"DE:AUTO:00020", null);
        MaleSocket socketTimer = digitalExpressionManager.registerExpression(expressionTimer);
        socketOr.getChild(index++).connect(socketTimer);

        ExpressionTimer expressionTimer2 = new ExpressionTimer(logixNG_Manager.getSystemNamePrefix()+"DE:AUTO:00021", "My Timer expression");
        MaleSocket socketTimer2 = digitalExpressionManager.registerExpression(expressionTimer2);
        socketOr.getChild(index++).connect(socketTimer2);

        TriggerOnce expressionTriggerOnce = new TriggerOnce(logixNG_Manager.getSystemNamePrefix()+"DE:AUTO:00022", null);
        MaleSocket socketTriggerOnce = digitalExpressionManager.registerExpression(expressionTriggerOnce);
        socketOr.getChild(index++).connect(socketTriggerOnce);

        TriggerOnce expressionTriggerOnce2 = new TriggerOnce(logixNG_Manager.getSystemNamePrefix()+"DE:AUTO:00023", "My TriggerOnce expression");
        MaleSocket socketTriggerOnce2 = digitalExpressionManager.registerExpression(expressionTriggerOnce2);
        expressionTriggerOnce.getChild(0).connect(socketTriggerOnce2);

        True expressionTrue = new True(logixNG_Manager.getSystemNamePrefix()+"DE:AUTO:00024", null);
        MaleSocket socketTrue = digitalExpressionManager.registerExpression(expressionTrue);
        socketOr.getChild(index++).connect(socketTrue);

        True expressionTrue2 = new True(logixNG_Manager.getSystemNamePrefix()+"DE:AUTO:00025", "My True expression");
        MaleSocket socketTrue2 = digitalExpressionManager.registerExpression(expressionTrue2);
        socketOr.getChild(index++).connect(socketTrue2);

        ExpressionLight expressionLight = new ExpressionLight(logixNG_Manager.getSystemNamePrefix()+"DE:AUTO:00026", null);
        expressionLight.setLight(light1);
        expressionLight.set_Is_IsNot(Is_IsNot_Enum.IS);
        expressionLight.setLightState(ExpressionLight.LightState.ON);
        MaleSocket socketLight = digitalExpressionManager.registerExpression(expressionLight);
        socketOr.getChild(index++).connect(socketLight);

        ExpressionMemory expressionMemory = new ExpressionMemory(logixNG_Manager.getSystemNamePrefix()+"DE:AUTO:11026", null);
        expressionMemory.setMemory(memory1);
        expressionMemory.setConstantValue("Some constant value");
        expressionMemory.setCompareTo(ExpressionMemory.CompareTo.VALUE);
        expressionMemory.setMemoryOperation(ExpressionMemory.MemoryOperation.GREATER_THAN);
        expressionMemory.setCaseInsensitive(true);
        MaleSocket socketMemory = digitalExpressionManager.registerExpression(expressionMemory);
        socketOr.getChild(index++).connect(socketMemory);

        expressionMemory = new ExpressionMemory(logixNG_Manager.getSystemNamePrefix()+"DE:AUTO:12026", null);
        expressionMemory.setMemory(memory2);
        expressionMemory.setOtherMemory(memory3);
        expressionMemory.setCompareTo(ExpressionMemory.CompareTo.MEMORY);
        expressionMemory.setMemoryOperation(ExpressionMemory.MemoryOperation.LESS_THAN);
        expressionMemory.setCaseInsensitive(false);
        socketMemory = digitalExpressionManager.registerExpression(expressionMemory);
        socketOr.getChild(index++).connect(socketMemory);

        ExpressionLight expressionLight2 = new ExpressionLight(logixNG_Manager.getSystemNamePrefix()+"DE:AUTO:00027", "My light");
        expressionLight2.removeLight();
        expressionLight2.set_Is_IsNot(Is_IsNot_Enum.IS);
        expressionLight2.setLightState(ExpressionLight.LightState.ON);
        MaleSocket socketLight2 = digitalExpressionManager.registerExpression(expressionLight2);
        socketOr.getChild(index++).connect(socketLight2);

        ExpressionSensor expressionSensor = new ExpressionSensor(logixNG_Manager.getSystemNamePrefix()+"DE:AUTO:00028", null);
        expressionSensor.setSensor(sensor1);
        expressionSensor.set_Is_IsNot(Is_IsNot_Enum.IS);
        expressionSensor.setSensorState(ExpressionSensor.SensorState.ACTIVE);
        MaleSocket socketSensor = digitalExpressionManager.registerExpression(expressionSensor);
        socketOr.getChild(index++).connect(socketSensor);

        ExpressionSensor expressionSensor2 = new ExpressionSensor(logixNG_Manager.getSystemNamePrefix()+"DE:AUTO:00029", "My sensor");
        expressionSensor2.removeSensor();
        expressionSensor2.set_Is_IsNot(Is_IsNot_Enum.IS);
        expressionSensor2.setSensorState(ExpressionSensor.SensorState.ACTIVE);
        MaleSocket socketSensor2 = digitalExpressionManager.registerExpression(expressionSensor2);
        socketOr.getChild(index++).connect(socketSensor2);

        ExpressionTurnout expressionTurnout = new ExpressionTurnout(logixNG_Manager.getSystemNamePrefix()+"DE:AUTO:00030", null);
        expressionTurnout.setTurnout(turnout1);
        expressionTurnout.set_Is_IsNot(Is_IsNot_Enum.IS);
        expressionTurnout.setTurnoutState(ExpressionTurnout.TurnoutState.THROWN);
        MaleSocket socketTurnout = digitalExpressionManager.registerExpression(expressionTurnout);
        socketOr.getChild(index++).connect(socketTurnout);

        ExpressionTurnout expressionTurnout2 = new ExpressionTurnout(logixNG_Manager.getSystemNamePrefix()+"DE:AUTO:00031", "My turnout");
        expressionTurnout2.removeTurnout();
        expressionTurnout2.set_Is_IsNot(Is_IsNot_Enum.IS);
        expressionTurnout2.setTurnoutState(ExpressionTurnout.TurnoutState.THROWN);
        MaleSocket socketTurnout2 = digitalExpressionManager.registerExpression(expressionTurnout2);
        socketOr.getChild(index++).connect(socketTurnout2);



        // If then
        jmri.jmrit.logixng.digital.actions.Many expressionMany =
                new jmri.jmrit.logixng.digital.actions.Many(logixNG_Manager.getSystemNamePrefix()+"DA:AUTO:00010", null);
        MaleSocket socketSecondMany = digitalActionManager.registerAction(expressionMany);
        socketIfThen.getChild(1).connect(socketSecondMany);

        // If else
        jmri.jmrit.logixng.digital.actions.Many expressionMany2 =
                new jmri.jmrit.logixng.digital.actions.Many(logixNG_Manager.getSystemNamePrefix()+"DA:AUTO:10010", null);
        MaleSocket socketSecondMany2 = digitalActionManager.registerAction(expressionMany2);
        socketIfThen.getChild(2).connect(socketSecondMany2);

        index = 0;
/*                    
        HoldAnything actionHoldAnything = new HoldAnything(logixNG_Manager.getSystemNamePrefix()+"DA:AUTO:00011", "My hold anything");
        MaleSocket socketHoldAnything = digitalActionManager.registerAction(actionHoldAnything);
        socketSecondMany.getChild(index++).connect(socketHoldAnything);
*/                    
        IfThenElse actionIfThen2 = new IfThenElse(logixNG_Manager.getSystemNamePrefix()+"DA:AUTO:00012", "My if then", IfThenElse.Type.TRIGGER_ACTION);
        MaleSocket socketIfThen2 = digitalActionManager.registerAction(actionIfThen2);
        socketSecondMany.getChild(index++).connect(socketIfThen2);

        jmri.jmrit.logixng.digital.actions.Many actionMany =
                new jmri.jmrit.logixng.digital.actions.Many(logixNG_Manager.getSystemNamePrefix()+"DA:AUTO:00013", "My many");
        MaleSocket socketMany2 = digitalActionManager.registerAction(actionMany);
        socketSecondMany.getChild(index++).connect(socketMany2);

        ShutdownComputer actionShutdownComputer = new ShutdownComputer(logixNG_Manager.getSystemNamePrefix()+"DA:AUTO:00014", "My shutdown computer");
        MaleSocket socketShutdownComputer = digitalActionManager.registerAction(actionShutdownComputer);
        socketSecondMany.getChild(index++).connect(socketShutdownComputer);

        DoAnalogAction actionDoAnalogAction = new DoAnalogAction(logixNG_Manager.getSystemNamePrefix()+"DA:AUTO:00015", null);
        MaleSocket socketDoAnalogAction = digitalActionManager.registerAction(actionDoAnalogAction);
        socketSecondMany.getChild(index++).connect(socketDoAnalogAction);

        DoStringAction actionDoStringAction = new DoStringAction(logixNG_Manager.getSystemNamePrefix()+"DA:AUTO:00016", null);
        MaleSocket socketDoStringAction = digitalActionManager.registerAction(actionDoStringAction);
        socketSecondMany.getChild(index++).connect(socketDoStringAction);

        ShutdownComputer expressionShutdownComputer = new ShutdownComputer(logixNG_Manager.getSystemNamePrefix()+"DA:AUTO:00017", null);
        MaleSocket socketShutdownComputer2 = digitalActionManager.registerAction(expressionShutdownComputer);
        socketSecondMany.getChild(index++).connect(socketShutdownComputer2);

        ActionLight actionLight = new ActionLight(logixNG_Manager.getSystemNamePrefix()+"DA:AUTO:00018", null);
//                    actionLight.setLight(light2);
        actionLight.setLightState(ActionLight.LightState.ON);
        socketLight2 = digitalActionManager.registerAction(actionLight);
        socketSecondMany.getChild(index++).connect(socketLight2);

        actionLight = new ActionLight(logixNG_Manager.getSystemNamePrefix()+"DA:AUTO:10019", "My light action");
        actionLight.setLight(light2);
        actionLight.setLightState(ActionLight.LightState.ON);
        socketLight2 = digitalActionManager.registerAction(actionLight);
        socketSecondMany.getChild(index++).connect(socketLight2);

        ActionSensor actionSensor = new ActionSensor(logixNG_Manager.getSystemNamePrefix()+"DA:AUTO:00020", null);
//                    actionSensor.setSensor(sensor2);
        actionSensor.setSensorState(ActionSensor.SensorState.ACTIVE);
        socketSensor2 = digitalActionManager.registerAction(actionSensor);
        socketSecondMany.getChild(index++).connect(socketSensor2);

        actionSensor = new ActionSensor(logixNG_Manager.getSystemNamePrefix()+"DA:AUTO:00021", "My sensor action");
        actionSensor.setSensor(sensor2);
        actionSensor.setSensorState(ActionSensor.SensorState.ACTIVE);
        socketSensor2 = digitalActionManager.registerAction(actionSensor);
        socketSecondMany.getChild(index++).connect(socketSensor2);

        ActionTurnout actionTurnout = new ActionTurnout(logixNG_Manager.getSystemNamePrefix()+"DA:AUTO:00022", null);
//                    actionTurnout.setTurnout(turnout2);
        actionTurnout.setTurnoutState(ActionTurnout.TurnoutState.THROWN);
        socketTurnout2 = digitalActionManager.registerAction(actionTurnout);
        socketSecondMany.getChild(index++).connect(socketTurnout2);

        actionTurnout = new ActionTurnout(logixNG_Manager.getSystemNamePrefix()+"DA:AUTO:00023", "My turnout action");
        actionTurnout.setTurnout(turnout2);
        actionTurnout.setTurnoutState(ActionTurnout.TurnoutState.THROWN);
        socketTurnout2 = digitalActionManager.registerAction(actionTurnout);
        socketSecondMany.getChild(index++).connect(socketTurnout2);

        AnalogExpressionConstant locoConstant = new AnalogExpressionConstant(logixNG_Manager.getSystemNamePrefix()+"AE:AUTO:00001", null);
        locoConstant.setValue(10);
        MaleSocket socketLocoConstant = analogExpressionManager.registerExpression(locoConstant);

        AnalogExpressionConstant speedConstant = new AnalogExpressionConstant(logixNG_Manager.getSystemNamePrefix()+"AE:AUTO:00002", null);
        speedConstant.setValue(0.5);
        MaleSocket socketSpeedConstant = analogExpressionManager.registerExpression(speedConstant);

        Sensor sensorDirection = InstanceManager.getDefault(SensorManager.class).provide("IS100");
        sensorDirection.setCommandedState(Sensor.ACTIVE);
        ExpressionSensor directionSensor = new ExpressionSensor(logixNG_Manager.getSystemNamePrefix()+"DE:AUTO:10028", null);
        expressionSensor.setSensor(sensorDirection);
        expressionSensor.set_Is_IsNot(Is_IsNot_Enum.IS);
        expressionSensor.setSensorState(ExpressionSensor.SensorState.ACTIVE);
        MaleSocket socketDirectionSensor = digitalExpressionManager.registerExpression(directionSensor);

        ActionThrottle actionThrottle = new ActionThrottle(logixNG_Manager.getSystemNamePrefix()+"DA:AUTO:10023", "My throttle action");
        actionThrottle.getChild(0).connect(socketLocoConstant);
        actionThrottle.getChild(1).connect(socketSpeedConstant);
        actionThrottle.getChild(2).connect(socketDirectionSensor);
        MaleSocket socketThrottle = digitalActionManager.registerAction(actionThrottle);
        socketSecondMany.getChild(index++).connect(socketThrottle);

        actionThrottle = new ActionThrottle(logixNG_Manager.getSystemNamePrefix()+"DA:AUTO:10024", "My other throttle action");
        socketThrottle = digitalActionManager.registerAction(actionThrottle);
        socketSecondMany.getChild(index++).connect(socketThrottle);

        AnalogExpressionMemory analogExpressionMemory = new AnalogExpressionMemory(logixNG_Manager.getSystemNamePrefix()+"AE:AUTO:00003", null);
        analogExpressionMemory.setMemory(memory1);
        MaleSocket socketAnalogExpressionMemory = analogExpressionManager.registerExpression(analogExpressionMemory);

        AnalogActionMemory analogActionMemory = new AnalogActionMemory(logixNG_Manager.getSystemNamePrefix()+"AA:AUTO:00001", null);
        analogActionMemory.setMemory(memory2);
        MaleSocket socketAnalogActionMemory = analogActionManager.registerAction(analogActionMemory);

        DoAnalogAction doAnalogAction = new DoAnalogAction(logixNG_Manager.getSystemNamePrefix()+"DA:AUTO:00101", "My do analog action");
        doAnalogAction.getChild(0).connect(socketAnalogExpressionMemory);
//DANIEL                    doAnalogAction.setAnalogExpressionSocketSystemName(socketAnalogExpressionMemory.getSystemName());
        doAnalogAction.getChild(1).connect(socketAnalogActionMemory);
//DANIEL                    doAnalogAction.setAnalogActionSocketSystemName(socketAnalogActionMemory.getSystemName());
        MaleSocket socket = digitalActionManager.registerAction(doAnalogAction);
        socketSecondMany.getChild(index++).connect(socket);

        analogExpressionMemory = new AnalogExpressionMemory(logixNG_Manager.getSystemNamePrefix()+"AE:AUTO:00004", "My expression");
        socketAnalogExpressionMemory = analogExpressionManager.registerExpression(analogExpressionMemory);

        analogActionMemory = new AnalogActionMemory(logixNG_Manager.getSystemNamePrefix()+"AA:AUTO:00002", "My action");
        socketAnalogActionMemory = analogActionManager.registerAction(analogActionMemory);

        doAnalogAction = new DoAnalogAction(logixNG_Manager.getSystemNamePrefix()+"DA:AUTO:00102", null);
//                    doAnalogAction.setAnalogExpressionSocketSystemName(socketAnalogExpressionMemory.getSystemName());
        doAnalogAction.getChild(0).connect(socketAnalogExpressionMemory);
//                    doAnalogAction.setAnalogActionSocketSystemName(socketAnalogActionMemory.getSystemName());
        doAnalogAction.getChild(1).connect(socketAnalogActionMemory);
        socket = digitalActionManager.registerAction(doAnalogAction);
        socketSecondMany.getChild(index++).connect(socket);

        StringExpressionMemory stringExpressionMemory = new StringExpressionMemory(logixNG_Manager.getSystemNamePrefix()+"SE:AUTO:00001", null);
        stringExpressionMemory.setMemory(memory3);
        MaleSocket socketStringExpressionMemory = stringExpressionManager.registerExpression(stringExpressionMemory);

        StringActionMemory stringActionMemory = new StringActionMemory(logixNG_Manager.getSystemNamePrefix()+"SA:AUTO:00001", null);
        stringActionMemory.setMemory(memory4);
        MaleSocket socketStringActionMemory = stringActionManager.registerAction(stringActionMemory);

        DoStringAction doStringAction = new DoStringAction(logixNG_Manager.getSystemNamePrefix()+"DA:AUTO:00103", "My do string action");
        doStringAction.getChild(0).connect(socketStringExpressionMemory);
//DANIEL                    doStringAction.setStringExpressionSocketSystemName(socketStringExpressionMemory.getSystemName());
        doStringAction.getChild(1).connect(socketStringActionMemory);
//DANIEL                    doStringAction.setStringActionSocketSystemName(socketStringActionMemory.getSystemName());
        socket = digitalActionManager.registerAction(doStringAction);
        socketSecondMany.getChild(index++).connect(socket);

        stringExpressionMemory = new StringExpressionMemory(logixNG_Manager.getSystemNamePrefix()+"SE:AUTO:00002", "My expression");
        socketStringExpressionMemory = stringExpressionManager.registerExpression(stringExpressionMemory);

        stringActionMemory = new StringActionMemory(logixNG_Manager.getSystemNamePrefix()+"SA:AUTO:00002", "My action");
        socketStringActionMemory = stringActionManager.registerAction(stringActionMemory);

        doStringAction = new DoStringAction(logixNG_Manager.getSystemNamePrefix()+"DA:AUTO:00104", null);
        doStringAction.getChild(0).connect(socketStringExpressionMemory);
//DANIEL                    doStringAction.setStringExpressionSocketSystemName(socketStringExpressionMemory.getSystemName());
        doStringAction.getChild(1).connect(socketStringActionMemory);
//DANIEL                    doStringAction.setStringActionSocketSystemName(socketStringActionMemory.getSystemName());
        socket = digitalActionManager.registerAction(doStringAction);
        socketSecondMany.getChild(index++).connect(socket);

        logixNG_Manager.resolveAllTrees();
        logixNG_Manager.setupAllLogixNGs();

        logixNG.setEnabled(true);
        conditionalNG.setEnabled(true);
        
        
        
        
        
        
        
        }
        
        
        
        
        
        
        
        
        
        

        // Store panels
        jmri.ConfigureManager cm = InstanceManager.getNullableDefault(jmri.ConfigureManager.class);
        if (cm == null) {
            log.error("Unable to get default configure manager");
        } else {
            FileUtil.createDirectory(FileUtil.getUserFilesPath() + "temp");
            File firstFile = new File(FileUtil.getUserFilesPath() + "temp/" + "LogixNG_temp.xml");
            File secondFile = new File(FileUtil.getUserFilesPath() + "temp/" + "LogixNG.xml");
            log.info("Temporary first file: %s%n", firstFile.getAbsoluteFile());
            log.info("Temporary second file: %s%n", secondFile.getAbsoluteFile());
            
            final String treeIndent = "   ";
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            logixNG.printTree(Locale.ENGLISH, printWriter, treeIndent);
            final String originalTree = stringWriter.toString();
            
            boolean results = cm.storeUser(firstFile);
            log.debug(results ? "store was successful" : "store failed");
            if (!results) {
                log.error("Failed to store panel");
                throw new RuntimeException("Failed to store panel");
            }
            
            // Add the header comment to the xml file
            addHeader(firstFile, secondFile);
            
            
            //**********************************
            // Delete all the LogixNGs, ConditionalNGs, and so on before reading the file.
            //**********************************
            
            java.util.Set<LogixNG> logixNG_Set = new java.util.HashSet<>(logixNG_Manager.getNamedBeanSet());
            for (LogixNG aLogixNG : logixNG_Set) {
                logixNG_Manager.deleteLogixNG(aLogixNG);
            }
            
            java.util.Set<ConditionalNG> conditionalNGSet = new java.util.HashSet<>(conditionalNGManager.getNamedBeanSet());
            for (ConditionalNG aConditionalNG : conditionalNGSet) {
                conditionalNGManager.deleteConditionalNG(aConditionalNG);
            }
            
            java.util.Set<MaleAnalogActionSocket> analogActionSet = new java.util.HashSet<>(analogActionManager.getNamedBeanSet());
            for (MaleAnalogActionSocket aAnalogAction : analogActionSet) {
                analogActionManager.deleteAnalogAction(aAnalogAction);
            }
            
            java.util.Set<MaleAnalogExpressionSocket> analogExpressionSet = new java.util.HashSet<>(analogExpressionManager.getNamedBeanSet());
            for (MaleAnalogExpressionSocket aAnalogExpression : analogExpressionSet) {
                analogExpressionManager.deleteAnalogExpression(aAnalogExpression);
            }
            
            java.util.Set<MaleDigitalActionSocket> digitalActionSet = new java.util.HashSet<>(digitalActionManager.getNamedBeanSet());
            for (MaleDigitalActionSocket aDigitalActionSocket : digitalActionSet) {
                digitalActionManager.deleteDigitalAction(aDigitalActionSocket);
            }
            
            java.util.Set<MaleDigitalBooleanActionSocket> digitalBooleanActionSet = new java.util.HashSet<>(digitalBooleanActionManager.getNamedBeanSet());
            for (MaleDigitalBooleanActionSocket aDigitalBooleanAction : digitalBooleanActionSet) {
                digitalBooleanActionManager.deleteDigitalBooleanAction(aDigitalBooleanAction);
            }
            
            java.util.Set<MaleDigitalExpressionSocket> digitalExpressionSet = new java.util.HashSet<>(digitalExpressionManager.getNamedBeanSet());
            for (MaleDigitalExpressionSocket aDigitalExpression : digitalExpressionSet) {
                digitalExpressionManager.deleteDigitalExpression(aDigitalExpression);
            }
            
            java.util.Set<MaleStringActionSocket> stringActionSet = new java.util.HashSet<>(stringActionManager.getNamedBeanSet());
            for (MaleStringActionSocket aStringAction : stringActionSet) {
                stringActionManager.deleteStringAction(aStringAction);
            }
            
            java.util.Set<MaleStringExpressionSocket> stringExpressionSet = new java.util.HashSet<>(stringExpressionManager.getNamedBeanSet());
            for (MaleStringExpressionSocket aStringExpression : stringExpressionSet) {
                stringExpressionManager.deleteStringExpression(aStringExpression);
            }
            
            Assert.assertEquals(0, logixNG_Manager.getNamedBeanSet().size());
            Assert.assertEquals(0, conditionalNGManager.getNamedBeanSet().size());
            Assert.assertEquals(0, analogActionManager.getNamedBeanSet().size());
            Assert.assertEquals(0, analogExpressionManager.getNamedBeanSet().size());
            Assert.assertEquals(0, digitalActionManager.getNamedBeanSet().size());
            Assert.assertEquals(0, digitalExpressionManager.getNamedBeanSet().size());
            Assert.assertEquals(0, stringActionManager.getNamedBeanSet().size());
            Assert.assertEquals(0, stringExpressionManager.getNamedBeanSet().size());
            
            
            
            //**********************************
            // Try to load file
            //**********************************
            
            java.util.Set<ConditionalNG> conditionalNG_Set =
                    new java.util.HashSet<>(conditionalNGManager.getNamedBeanSet());
            for (ConditionalNG aConditionalNG : conditionalNG_Set) {
                conditionalNGManager.deleteConditionalNG(aConditionalNG);
            }
            java.util.SortedSet<MaleAnalogActionSocket> set1 = analogActionManager.getNamedBeanSet();
            List<MaleSocket> l = new ArrayList<>(set1);
            for (MaleSocket x1 : l) {
                analogActionManager.deleteBean((MaleAnalogActionSocket)x1, "DoDelete");
            }
            java.util.SortedSet<MaleAnalogExpressionSocket> set2 = analogExpressionManager.getNamedBeanSet();
            l = new ArrayList<>(set2);
            for (MaleSocket x2 : l) {
                analogExpressionManager.deleteBean((MaleAnalogExpressionSocket)x2, "DoDelete");
            }
            java.util.SortedSet<MaleDigitalActionSocket> set3 = digitalActionManager.getNamedBeanSet();
            l = new ArrayList<>(set3);
            for (MaleSocket x3 : l) {
                digitalActionManager.deleteBean((MaleDigitalActionSocket)x3, "DoDelete");
            }
            java.util.SortedSet<MaleDigitalExpressionSocket> set4 = digitalExpressionManager.getNamedBeanSet();
            l = new ArrayList<>(set4);
            for (MaleSocket x4 : l) {
                digitalExpressionManager.deleteBean((MaleDigitalExpressionSocket)x4, "DoDelete");
            }
            java.util.SortedSet<MaleStringActionSocket> set5 = stringActionManager.getNamedBeanSet();
            l = new ArrayList<>(set5);
            for (MaleSocket x5 : l) {
                stringActionManager.deleteBean((MaleStringActionSocket)x5, "DoDelete");
            }
            java.util.SortedSet<MaleStringExpressionSocket> set6 = stringExpressionManager.getNamedBeanSet();
            l = new ArrayList<>(set6);
            for (MaleSocket x6 : l) {
                stringExpressionManager.deleteBean((MaleStringExpressionSocket)x6, "DoDelete");
            }

            results = cm.load(secondFile);
            log.debug(results ? "load was successful" : "store failed");
            if (results) {
                logixNG_Manager.resolveAllTrees();
                logixNG_Manager.setupAllLogixNGs();

                logixNG = logixNG_Manager.getLogixNG("Yet another logixNG");
                stringWriter = new StringWriter();
                printWriter = new PrintWriter(stringWriter);
                logixNG.printTree(Locale.ENGLISH, printWriter, treeIndent);

                if (!originalTree.equals(stringWriter.toString())) {
                    log.error("--------------------------------------------");
                    log.error("Old tree:");
                    log.error("XXX"+originalTree+"XXX");
                    log.error("--------------------------------------------");
                    log.error("New tree:");
                    log.error("XXX"+stringWriter.toString()+"XXX");
                    log.error("--------------------------------------------");

//                    log.error(conditionalNGManager.getBySystemName(originalTree).getChild(0).getConnectedSocket().getSystemName());

                    throw new RuntimeException("tree has changed");
                }
            } else {
                throw new RuntimeException("Failed to load panel");
            }
        }
    }
    
    
    private void addHeader(File inFile, File outFile) throws FileNotFoundException, IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), StandardCharsets.UTF_8));
                PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), StandardCharsets.UTF_8)))) {
            
            String line = reader.readLine();
            writer.println(line);
            
            writer.println("<!--");
            writer.println("*****************************************************************************");
            writer.println();
            writer.println("DO NOT EDIT THIS FILE!!!");
            writer.println();
            writer.println("This file is created by jmri.jmrit.logixng.configurexml.StoreAndLoadTest");
            writer.println("and put in the temp/temp folder. LogixNG uses both the standard JMRI load");
            writer.println("and store test, and a LogixNG specific store and load test.");
            writer.println();
            writer.println("After adding new stuff to StoreAndLoadTest, copy the file temp/temp/LogixNG.xml");
            writer.println("to the folder java/test/jmri/jmrit/logixng/configurexml/load");
            writer.println();
            writer.println("******************************************************************************");
            writer.println("-->");
            
            while ((line = reader.readLine()) != null) {
                writer.println(line);
            }
        }
    }
    
    
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        JUnitUtil.resetInstanceManager();
        JUnitUtil.resetProfileManager();
        JUnitUtil.initConfigureManager();
        JUnitUtil.initInternalTurnoutManager();
        JUnitUtil.initInternalLightManager();
        JUnitUtil.initInternalSensorManager();
        
        JUnitUtil.initInternalSignalHeadManager();
        JUnitUtil.initDefaultSignalMastManager();
        JUnitUtil.initSignalMastLogicManager();
        JUnitUtil.initOBlockManager();
        JUnitUtil.initWarrantManager();
        
        JUnitUtil.initLogixNGManager();
   }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }
    
    
    private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(StoreAndLoadTest.class);

}
