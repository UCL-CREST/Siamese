    private void jHelpEditorPaneHyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {
        if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            if (evt.getURL().toString().equals("https://sourceforge.net/projects/javapoint/")) {
                Desktop desktop = Desktop.getDesktop();
                if (!desktop.isDesktopSupported()) {
                    System.err.println("Desktop is not supported!!");
                }
                if (!desktop.isSupported(Desktop.Action.BROWSE)) {
                    System.err.println("Desktop doesn't support the browse action!!");
                }
                try {
                    desktop.browse(new URI("https://sourceforge.net/projects/javapoint/"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    jHelpEditorPane.setPage(this.getClass().getResource("sample3.html"));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                jHelpEditorPane.addHyperlinkListener(new javax.swing.event.HyperlinkListener() {

                    @Override
                    public void hyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {
                        jHelpEditorPaneHyperlinkUpdate(evt);
                    }
                });
            }
        }
    }
