        @Override
        public void mouseClicked(final MouseEvent e) {
            log.debug(e);
            HyperlinkLabel self = (HyperlinkLabel) e.getSource();
            if (self.url == null) {
                return;
            }
            if (SwingUtilities.isRightMouseButton(e)) {
                log.debug("Right Mouse Click. " + e.getModifiersEx());
                popupMenu.show(e.getComponent(), e.getX(), e.getY());
            } else {
                try {
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().browse(new URI(self.url.toString()));
                    } else {
                        log.info("Cant't open link.");
                    }
                } catch (IOException e1) {
                    log.error(e1);
                } catch (URISyntaxException ex) {
                    log.error(ex);
                } catch (Exception ex) {
                    log.error(ex);
                }
            }
        }
