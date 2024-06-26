package jmri.jmrit.display.controlPanelEditor.configurexml;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.List;
import javax.swing.JFrame;
import jmri.ConfigureManager;
import jmri.InstanceManager;
import jmri.configurexml.AbstractXmlAdapter;
import jmri.configurexml.XmlAdapter;
import jmri.jmrit.catalog.NamedIcon;
import jmri.jmrit.display.Editor;
import jmri.jmrit.display.EditorManager;
import jmri.jmrit.display.Positionable;
import jmri.jmrit.display.controlPanelEditor.ControlPanelEditor;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handle configuration for {@link ControlPanelEditor} panes.
 *
 * @author Bob Jacobsen Copyright: Copyright (c) 2002
 */
public class ControlPanelEditorXml extends AbstractXmlAdapter {

    public ControlPanelEditorXml() {
    }

    /**
     * Default implementation for storing the contents of a ControlPanelEditor.
     *
     * @param o Object to store, of type ControlPanelEditor
     * @return Element containing the complete info
     */
    @Override
    public Element store(Object o) {
        ControlPanelEditor p = (ControlPanelEditor) o;
        Element panel = new Element("paneleditor");

        JFrame frame = p.getTargetFrame();
        Dimension size = frame.getSize();
        Point posn = frame.getLocation();

        panel.setAttribute("class", "jmri.jmrit.display.controlPanelEditor.configurexml.ControlPanelEditorXml");
        panel.setAttribute("name", "" + frame.getName());
        panel.setAttribute("x", "" + posn.x);
        panel.setAttribute("y", "" + posn.y);
        panel.setAttribute("height", "" + size.height);
        panel.setAttribute("width", "" + size.width);
        panel.setAttribute("editable", "" + (p.isEditable() ? "yes" : "no"));
        panel.setAttribute("positionable", "" + (p.allPositionable() ? "yes" : "no"));
        //panel.setAttribute("showcoordinates", ""+(p.showCoordinates()?"yes":"no"));
        panel.setAttribute("showtooltips", "" + (p.showToolTip() ? "yes" : "no"));
        panel.setAttribute("controlling", "" + (p.allControlling() ? "yes" : "no"));
        panel.setAttribute("hide", p.showHidden() ? "no" : "yes");  // hide=no means showHidden enabled
        panel.setAttribute("panelmenu", p.isPanelMenuVisible() ? "yes" : "no");
        panel.setAttribute("scrollable", p.getScrollable());
        if (p.getBackgroundColor() != null) {
            panel.setAttribute("redBackground", "" + p.getBackgroundColor().getRed());
            panel.setAttribute("greenBackground", "" + p.getBackgroundColor().getGreen());
            panel.setAttribute("blueBackground", "" + p.getBackgroundColor().getBlue());
        }
        panel.setAttribute("state", "" + p.getExtendedState());
        panel.setAttribute("shapeSelect", "" + (p.getShapeSelect() ? "yes" : "no"));

        String family = p.getPortalIconFamily();
        if (family != null && !family.equals("Standard")) { // don't store the default map name
        Element elem = new Element("icons");
            elem.setAttribute("portalFamily", family);
            panel.addContent(elem);
        }

        // include contents
        List<Positionable> contents = p.getContents();
        log.debug("N elements: {}", contents.size());
        for (Positionable sub : contents) {
            if (sub != null && sub.storeItem()) {
                try {
                    Element e = jmri.configurexml.ConfigXmlManager.elementFromObject(sub);
                    if (e != null) {
                        panel.addContent(e);
                    }
                } catch (RuntimeException e) {
                    log.error("Error storing panel element", e);
                }
            }
        }
        return panel;
    }

    public Element storeIcon(String elemName, NamedIcon icon) {
        if (icon == null) {
            return null;
        }
        Element element = new Element(elemName);
        element.addContent(new Element("url").addContent(icon.getURL()));
        return element;
    }

    /**
     * Create a ControlPanelEditor object, then register and fill it, then pop
     * it in a JFrame
     *
     * @param shared Top level Element to unpack.
     * @return true if successful
     */
    @Override
    public boolean load(Element shared, Element perNode) {
        if (java.awt.GraphicsEnvironment.isHeadless()) {
            return true;
        }

        boolean result = true;
        Attribute a;
        // find coordinates
        int x = 0;
        int y = 0;
        int height = 400;
        int width = 300;
        try {
            if ((a = shared.getAttribute("x")) != null) {
                x = a.getIntValue();
            }
            if ((a = shared.getAttribute("y")) != null) {
                y = a.getIntValue();
            }
            if ((a = shared.getAttribute("height")) != null) {
                height = a.getIntValue();
            }
            if ((a = shared.getAttribute("width")) != null) {
                width = a.getIntValue();
            }
        } catch (org.jdom2.DataConversionException e) {
            log.error("failed to convert ControlPanelEditor's attribute");
            result = false;
        }
        // find the name
        String name = "Control Panel";
        if (shared.getAttribute("name") != null) {
            name = shared.getAttribute("name").getValue();
        }
        // confirm that panel hasn't already been loaded
        if (InstanceManager.getDefault(EditorManager.class).contains(name)) {
            log.warn("File contains a panel with the same name ({}) as an existing panel", name);
            result = false;
        }

        // If available, override location and size with machine dependent values
        if (!InstanceManager.getDefault(jmri.util.gui.GuiLafPreferencesManager.class).isEditorUseOldLocSize()) {
            jmri.UserPreferencesManager prefsMgr = InstanceManager.getNullableDefault(jmri.UserPreferencesManager.class);
            if (prefsMgr != null) {
                String windowFrameRef = "jmri.jmrit.display.controlPanelEditor.ControlPanelEditor:" + name;

                java.awt.Point prefsWindowLocation = prefsMgr.getWindowLocation(windowFrameRef);
                if (prefsWindowLocation != null) {
                    x = (int) prefsWindowLocation.getX();
                    y = (int) prefsWindowLocation.getY();
                }

                java.awt.Dimension prefsWindowSize = prefsMgr.getWindowSize(windowFrameRef);
                if (prefsWindowSize != null && prefsWindowSize.getHeight() != 0 && prefsWindowSize.getWidth() != 0) {
                    height = (int) prefsWindowSize.getHeight();
                    width = (int) prefsWindowSize.getWidth();
                }
            }
        }

        ControlPanelEditor panel = new ControlPanelEditor(name);
        panel.getTargetFrame().setVisible(false);   // save painting until last
        InstanceManager.getDefault(EditorManager.class).add(panel);

        // Load editor option flags. This has to be done before the content
        // items are loaded, to preserve the individual item settings
        boolean value = true;
        if ((a = shared.getAttribute("editable")) != null && a.getValue().equals("no")) {
            value = false;
        }
        panel.setAllEditable(value);

        value = true;
        if ((a = shared.getAttribute("positionable")) != null && a.getValue().equals("no")) {
            value = false;
        }
        panel.setAllPositionable(value);

        /*
         value = false;
         if ((a = element.getAttribute("showcoordinates"))!=null && a.getValue().equals("yes"))
         value = true;
         panel.setShowCoordinates(value);
         */
        value = true;
        if ((a = shared.getAttribute("showtooltips")) != null && a.getValue().equals("no")) {
            value = false;
        }
        panel.setAllShowToolTip(value);

        value = true;
        if ((a = shared.getAttribute("controlling")) != null && a.getValue().equals("no")) {
            value = false;
        }
        panel.setAllControlling(value);

        value = true;
        if ((a = shared.getAttribute("hide")) != null && a.getValue().equals("yes")) {
            value = false;
        }
        panel.setShowHidden(value);

        value = true;
        if ((a = shared.getAttribute("panelmenu")) != null && a.getValue().equals("no")) {
            value = false;
        }
        panel.setPanelMenuVisible(value);

        value = true;
        if ((a = shared.getAttribute("shapeSelect")) != null && a.getValue().equals("no")) {
            value = false;
        }
        panel.setShapeSelect(value);

        if ((a = shared.getAttribute("state")) != null) {
            try {
                int xState = a.getIntValue();
                panel.setExtendedState(xState);
            } catch (org.jdom2.DataConversionException e) {
                log.error("failed to convert ControlPanelEditor's extended State");
                result = false;
            }
        }

        Element elem = shared.getChild("icons");
        if (elem != null) {
            Attribute attr = elem.getAttribute("portalFamily");
            if (attr != null) {
                panel.setPortalIconFamily(attr.getValue());
            }
        }

        String state = "both";
        if ((a = shared.getAttribute("scrollable")) != null) {
            state = a.getValue();
        }
        panel.setScroll(state);
        try {
            int red = shared.getAttribute("redBackground").getIntValue();
            int blue = shared.getAttribute("blueBackground").getIntValue();
            int green = shared.getAttribute("greenBackground").getIntValue();
            panel.setBackgroundColor(new Color(red, green, blue));
        } catch (org.jdom2.DataConversionException e) {
            log.warn("Could not parse color attributes!");
        } catch (NullPointerException e) {  // considered normal if the attributes are not present
        }

        shared.removeChild("icons");

        //set the (global) editor display widgets to their flag settings
        panel.initView();

        // load the contents
        List<Element> items = shared.getChildren();
        for (Element panelItem : items) {
            String adapterName = panelItem.getAttribute("class").getValue();
            log.debug("load via {}", adapterName);
            try {
                XmlAdapter adapter = (XmlAdapter) Class.forName(adapterName).getDeclaredConstructor().newInstance();
                // and do it
                adapter.load(panelItem, panel);
                if (!panel.loadOK()) {
                    result = false;
                }
            } catch (ClassNotFoundException | InstantiationException
                    | jmri.configurexml.JmriConfigureXmlException | IllegalAccessException
                    | NoSuchMethodException | java.lang.reflect.InvocationTargetException e) {
                log.error("Exception while loading {}", panelItem.getName(), e);
                result = false;
            }
        }
        panel.disposeLoadData();     // dispose of url correction data

        // display the results, with the editor in back
        panel.pack();
        panel.setAllEditable(panel.isEditable());

        // register the resulting panel for later configuration
        ConfigureManager cm = InstanceManager.getNullableDefault(jmri.ConfigureManager.class);
        if (cm != null) {
            cm.registerUser(panel);
        }

        // reset the size and position, in case the display caused it to change
        panel.getTargetFrame().setLocation(x, y);
        panel.getTargetFrame().setSize(width, height);
        panel.setTitle();
        panel.getTargetFrame().setVisible(true);    // always show the panel
        // do last to set putItem override - unused.
        panel.loadComplete();

        return result;
    }

    public NamedIcon loadIcon(String key, Element element, Editor ed) {
        Element elem = element.getChild(key);
        NamedIcon icon = null;
        if (elem != null) {
            Element e = elem.getChild("url");
            String iconName = e.getText();
            icon = NamedIcon.getIconByName(iconName);
            if (icon == null) {
                icon = ed.loadFailed(key, iconName);
                if (icon == null) {
                    log.info("{} removed for url= {}", key, iconName);
                }
            }
        }
        return icon;
    }

    @Override
    public int loadOrder() {
        return jmri.Manager.PANELFILES;
    }

    private static final Logger log = LoggerFactory.getLogger(ControlPanelEditorXml.class);

}
