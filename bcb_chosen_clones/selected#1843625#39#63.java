        public void hyperlinkUpdate(javax.swing.event.HyperlinkEvent e) {
            if (e.getEventType() != HyperlinkEvent.EventType.ACTIVATED) return;
            if (!Desktop.isDesktopSupported()) return;
            URI uri = null;
            if (e.getURL() == null) {
                File f = new File("gpl-3.0.html");
                if (!f.exists()) try {
                    uri = new URI("http://www.gnu.org/licenses/");
                } catch (URISyntaxException e1) {
                    Log.logger.error(e1);
                    return;
                }
                uri = f.toURI();
            } else try {
                uri = e.getURL().toURI();
            } catch (URISyntaxException e2) {
                Log.logger.error(e2);
                return;
            }
            try {
                Desktop.getDesktop().browse(uri);
            } catch (IOException e1) {
                Log.logger.error(e1);
            }
        }
