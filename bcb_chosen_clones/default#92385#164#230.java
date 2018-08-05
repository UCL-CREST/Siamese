    protected void DoAction(String actionString, int tabIndex) {
        if (actionString.equals("Add Tab")) {
            OptionUI oui = new OptionUI();
            String result = oui.promptString(_myTabs.getComponentAt(tabIndex >= 0 ? tabIndex : 0), "Enter the name of the tab to add.  Prefer brevity.", "Add New Tab", "");
            if (result == null) return;
            result = result.trim();
            if (result.length() == 0) return;
            FilterManager.getInstance().addTab(result);
            return;
        }
        String tabName = _myTabs.getTitleAt(tabIndex);
        if (actionString.charAt(0) == '~') {
            boolean result = FilterManager.getInstance().toggleField(tabName, actionString.substring(1));
            if (tabToProperties != null) {
                JTabProperties properties = (JTabProperties) tabToProperties.get(tabName);
                if (properties != null) {
                    properties.setColumnStatus(actionString.substring(1), result);
                }
            }
        }
        if (actionString.equals("Properties")) {
            JFrame jf = getFrame(tabName);
            jf.setState(Frame.NORMAL);
            jf.setVisible(true);
        }
        if (actionString.equals("Export")) {
            JFileChooser jfc = new JFileChooser();
            jfc.setApproveButtonText("Export");
            int result = jfc.showSaveDialog(null);
            switch(result) {
                case JFileChooser.APPROVE_OPTION:
                    String fname = jfc.getSelectedFile().getAbsolutePath();
                    if (!FilterManager.getInstance().exportTab(tabName, fname)) {
                        JOptionPane.showMessageDialog(null, "Could not export tab [" + tabName + "].", "Export error", JOptionPane.PLAIN_MESSAGE);
                    }
                    return;
                case JFileChooser.ERROR_OPTION:
                case JFileChooser.CANCEL_OPTION:
                default:
                    return;
            }
        }
        if (actionString.equals("Print")) {
            if (tabIndex == -1) {
                ErrorManagement.logDebug("Can't print unknown tab, must prompt...");
            } else {
                if (!FilterManager.getInstance().printTab(tabName)) {
                    JOptionPane.showMessageDialog(null, "Could not print tab [" + tabName + "].", "Print error", JOptionPane.PLAIN_MESSAGE);
                }
            }
        }
        boolean eraseEntries = false;
        if (actionString.equals("Tab & All Entries")) {
            eraseEntries = true;
            actionString = "Just Tab";
        }
        if (actionString.equals("Just Tab")) {
            if (tabIndex == -1) {
                ErrorManagement.logDebug("Prompting for Delete...\n");
            } else {
                ErrorManagement.logDebug("Deleting tab [" + tabName + "]...\n");
                if (!FilterManager.getInstance().deleteTab(tabName, eraseEntries)) {
                    JOptionPane.showMessageDialog(null, "Could not delete tab [" + tabName + "].", "Tab deletion error", JOptionPane.PLAIN_MESSAGE);
                }
            }
        }
    }
