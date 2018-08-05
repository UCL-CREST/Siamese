    @Override
    public void hyperlinkUpdate(final HyperlinkEvent e) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    if (e.getDescription().startsWith("ovtk2://")) {
                        ActionEvent ae = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, e.getDescription().substring(8));
                        desktop.actionPerformed(ae);
                    } else if (e.getDescription().equals("close")) {
                    } else {
                        Desktop desktop = null;
                        if (Desktop.isDesktopSupported()) {
                            desktop = Desktop.getDesktop();
                            try {
                                desktop.browse(new URI(e.getDescription()));
                            } catch (IOException ioe) {
                                ioe.printStackTrace();
                            } catch (URISyntaxException use) {
                                use.printStackTrace();
                            }
                        } else {
                            JOptionPane.showInputDialog(desktop, "Hyperlinks not supported by OS.");
                        }
                    }
                }
            }
        });
    }
