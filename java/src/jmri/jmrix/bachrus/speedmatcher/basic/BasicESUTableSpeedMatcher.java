package jmri.jmrix.bachrus.speedmatcher.basic;

import jmri.jmrix.bachrus.speedmatcher.SpeedMatcherConfig;

/**
 *
 * @author toddt
 */
public class BasicESUTableSpeedMatcher extends BasicSpeedMatcher {

    public BasicESUTableSpeedMatcher(SpeedMatcherConfig config) {
        super(config);
    }

    @Override
    public boolean Start() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void Stop() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean IsIdle() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void programmingOpReply(int value, int status) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}