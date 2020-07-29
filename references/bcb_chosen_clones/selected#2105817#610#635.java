    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            if (e.getURL().getPath().endsWith("loadConfiguration")) {
                getLoadConfigurationAction().actionPerformed(new ActionEvent(this, 1, "loadConfiguration"));
            } else if (e.getURL().getPath().endsWith("createConfiguration")) {
                getCreateConfigurationAction().actionPerformed(new ActionEvent(this, 1, "createConfiguration"));
            } else if (e.getURL().getPath().endsWith("editConfiguration")) {
                getEditConfigurationAction().actionPerformed(new ActionEvent(this, 1, "editConfiguration"));
            } else if (e.getURL().getPath().endsWith("runConfiguration")) {
                getRunConfigurationAction().actionPerformed(new ActionEvent(this, 1, "runConfiguration"));
            } else {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(e.getURL().toURI());
                    } catch (IOException e1) {
                        Logging.getLogger(this.getClass()).warn("{}", e1.getLocalizedMessage());
                    } catch (URISyntaxException e1) {
                        Logging.getLogger(this.getClass()).warn("{}", e1.getLocalizedMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please view your results at " + e.getURL().toString());
                }
            }
        }
    }
