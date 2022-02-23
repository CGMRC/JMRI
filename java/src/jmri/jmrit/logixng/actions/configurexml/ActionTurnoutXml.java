package jmri.jmrit.logixng.actions.configurexml;

import jmri.*;
import jmri.configurexml.JmriConfigureXmlException;
import jmri.jmrit.logixng.DigitalActionManager;
// import jmri.jmrit.logixng.NamedBeanAddressing;
import jmri.jmrit.logixng.actions.ActionTurnout;
import jmri.jmrit.logixng.util.configurexml.LogixNG_SelectNamedBeanXml;
import jmri.jmrit.logixng.util.configurexml.LogixNG_SelectEnumXml;
// import jmri.jmrit.logixng.util.parser.ParserException;

import org.jdom2.Element;

/**
 * Handle XML configuration for ActionLightXml objects.
 *
 * @author Bob Jacobsen Copyright: Copyright (c) 2004, 2008, 2010
 * @author Daniel Bergqvist Copyright (C) 2019
 */
public class ActionTurnoutXml extends jmri.managers.configurexml.AbstractNamedBeanManagerConfigXML {

    public ActionTurnoutXml() {
    }

    /**
     * Default implementation for storing the contents of a ActionTurnout
     *
     * @param o Object to store, of type TripleTurnoutSignalHead
     * @return Element containing the complete info
     */
    @Override
    public Element store(Object o) {
        ActionTurnout p = (ActionTurnout) o;

        var selectNamedBeanXml = new LogixNG_SelectNamedBeanXml<Turnout>();
        var selectEnumXml = new LogixNG_SelectEnumXml();

        Element element = new Element("ActionTurnout");
        element.setAttribute("class", this.getClass().getName());
        element.addContent(new Element("systemName").addContent(p.getSystemName()));

        storeCommon(p, element);

        element.addContent(selectNamedBeanXml.store(p.getSelectNamedBean(), "namedBean"));
        element.addContent(selectEnumXml.store(p.getSelectEnum(), "state"));

        return element;
    }

    @Override
    public boolean load(Element shared, Element perNode) throws JmriConfigureXmlException {     // Test class that inherits this class throws exception
        String sys = getSystemName(shared);
        String uname = getUserName(shared);
        ActionTurnout h = new ActionTurnout(sys, uname);

        var selectNamedBeanXml = new LogixNG_SelectNamedBeanXml<Turnout>();
        var selectEnumXml = new LogixNG_SelectEnumXml();

        loadCommon(h, shared);

        selectNamedBeanXml.load(shared.getChild("namedBean"), h.getSelectNamedBean());
        selectNamedBeanXml.loadLegacy(shared, h.getSelectNamedBean(), "turnout");

        selectEnumXml.load(shared.getChild("state"), h.getSelectEnum());
        selectEnumXml.loadLegacy(shared, h.getSelectEnum(), "turnoutState");
/*
        try {
            Element elem = shared.getChild("stateAddressing");
            if (elem != null) {
                h.setStateAddressing(NamedBeanAddressing.valueOf(elem.getTextTrim()));
            }

            Element turnoutState = shared.getChild("turnoutState");
            if (turnoutState != null) {
                h.setBeanState(ActionTurnout.TurnoutState.valueOf(turnoutState.getTextTrim()));
            }

            elem = shared.getChild("stateReference");
            if (elem != null) h.setStateReference(elem.getTextTrim());

            elem = shared.getChild("stateLocalVariable");
            if (elem != null) h.setStateLocalVariable(elem.getTextTrim());

            elem = shared.getChild("stateFormula");
            if (elem != null) h.setStateFormula(elem.getTextTrim());

        } catch (ParserException e) {
            throw new JmriConfigureXmlException(e);
        }
*/
        InstanceManager.getDefault(DigitalActionManager.class).registerAction(h);
        return true;
    }

//    private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ActionTurnoutXml.class);
}
