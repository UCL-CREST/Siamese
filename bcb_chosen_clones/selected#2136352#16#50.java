    public AboutBox(java.awt.Frame parent, String appName, String appVersion) {
        super(parent);
        try {
            initComponents();
            getRootPane().setDefaultButton(closeButton);
            final String resourceName = "resources/about.html";
            java.net.URL aboutTextUrl = getClass().getResource(resourceName);
            if (aboutTextUrl == null) throw new Exception("Cannot load resource \"" + resourceName + "\".");
            String aboutText = myutils.Misc.readHtmlResourceAsString(aboutTextUrl);
            jEditorPaneAbout.setContentType("text/html");
            aboutText = aboutText.replace("$(APP_NAME)", appName);
            aboutText = aboutText.replace("$(APP_VERSION)", appVersion);
            jEditorPaneAbout.setText(aboutText);
            jEditorPaneAbout.setCaretPosition(0);
            GuiUtils.addStandardPopupMenuAndUndoSupport(jEditorPaneAbout);
            jEditorPaneAbout.addHyperlinkListener(new HyperlinkListener() {

                @Override
                public void hyperlinkUpdate(HyperlinkEvent hlinkEvt) {
                    try {
                        if (hlinkEvt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                            if (!Desktop.isDesktopSupported()) throw new Exception("Cannot open link: this system does not support opening web links.");
                            Desktop desktop = Desktop.getDesktop();
                            desktop.browse(hlinkEvt.getURL().toURI());
                        }
                    } catch (Throwable e) {
                        ErrDialog.errorDialog(getContentPane(), ErrUtils.getExceptionMessage(e));
                    }
                }
            });
        } catch (Throwable e) {
            setVisible(false);
            throw new RuntimeException("Error initialising the about window: " + ErrUtils.getExceptionMessage(e));
        }
    }
