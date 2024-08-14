package jmri.jmrit.permission.swing;

import java.awt.event.ActionEvent;

import javax.swing.Icon;

import jmri.InstanceManager;
import jmri.PermissionManager;
import jmri.util.swing.*;


/**
 * Let a user login to the permission manager.
 *
 * @author Daniel Bergqvist (C) 2024
 */
public class ChangePasswordAction extends JmriAbstractAction {

    public ChangePasswordAction(String s, WindowInterface wi) {
        super(s, wi);
        checkPermission();
    }

    public ChangePasswordAction(String s, Icon i, WindowInterface wi) {
        super(s, i, wi);
        checkPermission();
    }

    public ChangePasswordAction() {
        super(Bundle.getMessage("ChangePasswordAction_Title"));
        checkPermission();
    }

    private void checkPermission() {
        var permissionManager = InstanceManager.getDefault(PermissionManager.class);
        if (permissionManager.isEnabled()) {
            setEnabled(permissionManager.isLoggedIn());
            permissionManager.addLoginListener((isLogin) -> {
                setEnabled(isLogin);
            });
        } else {
            setEnabled(false);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new ChangePasswordDialog().setVisible(true);
    }

    @Override
    public JmriPanel makePanel() {
        throw new IllegalArgumentException("Should not be invoked");
    }

}
