        public void hyperlinkUpdate(HyperlinkEvent evt) {
            if (evt.getSource() == jta) {
                if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    try {
                        if (Desktop.isDesktopSupported()) Desktop.getDesktop().browse(new URI(evt.getURL().toString()));
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (URISyntaxException e2) {
                        JOptionPane.showMessageDialog(null, e2.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
