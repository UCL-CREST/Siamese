                @Override
                public void mouseClicked(MouseEvent e) {
                    if (!e.isPopupTrigger() && SwingUtilities.isLeftMouseButton(e)) {
                        if (Desktop.isDesktopSupported()) {
                            try {
                                StyledDocument doc = (StyledDocument) getJtpChannelText().getDocument();
                                String url = (String) doc.getCharacterElement(getJtpChannelText().viewToModel(e.getPoint())).getAttributes().getAttribute(TextStyle.IDENTIFIER_URL);
                                if (url != null) {
                                    Desktop.getDesktop().browse(new java.net.URI(StringHelper.addProcotol(url, "http://")));
                                }
                            } catch (Exception ex) {
                            }
                        }
                    }
                }
