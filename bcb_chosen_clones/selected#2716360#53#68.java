    private void jAboutEditorPaneHyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {
        Desktop desktop = Desktop.getDesktop();
        if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
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
        }
    }
