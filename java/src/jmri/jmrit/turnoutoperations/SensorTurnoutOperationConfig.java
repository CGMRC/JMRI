package jmri.jmrit.turnoutoperations;

import jmri.TurnoutOperation;

/**
 * Configuration for SensorTurnoutOperation class.
 * All the work is done by the Common... class
 *
 * @author John Harper Copyright 2005
 */
public class SensorTurnoutOperationConfig extends CommonTurnoutOperationConfig {

    /**
     * Create the config JPanel, if there is one, to configure this operation
     * type
     * @param op turnout operation.
     */
    public SensorTurnoutOperationConfig(TurnoutOperation op) {
        super(op);
    }
}
