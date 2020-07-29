    private JTextPane getJtpChannelText() {
        if (jtpChannelText == null) {
            jtpChannelText = new JTextPane();
            jtpChannelText.setAutoscrolls(true);
            jtpChannelText.setEditable(false);
            jtpChannelText.addKeyListener(new KeyAdapter() {

                /**
				 * Called when a key is typed.
				 * 
				 * @param e The KeyEvent.
				 */
                @Override
                public void keyPressed(KeyEvent e) {
                    if ((e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK || (e.getModifiers() & InputEvent.ALT_MASK) == InputEvent.ALT_MASK || (e.getModifiers() & InputEvent.META_MASK) == InputEvent.META_MASK) {
                        return;
                    }
                    parent.focusTextInput(e.getKeyChar());
                }
            });
            jtpChannelText.addMouseListener(new MouseAdapter() {

                /**
				 * Handle the mouse clicking the control.
				 * 
				 * @param e The MouseEvent.
				 */
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
            });
            jtpChannelText.addMouseMotionListener(new MouseMotionAdapter() {

                /**
				 * Called when the mouse is moved.
				 * 
				 * @param e The MouseEvent.
				 */
                @Override
                public void mouseMoved(MouseEvent e) {
                    try {
                        StyledDocument doc = (StyledDocument) getJtpChannelText().getDocument();
                        String url = (String) doc.getCharacterElement(getJtpChannelText().viewToModel(e.getPoint())).getAttributes().getAttribute(TextStyle.IDENTIFIER_URL);
                        if (url != null) {
                            setCursor(new Cursor(Cursor.HAND_CURSOR));
                        } else {
                            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        }
                    } catch (Exception ex) {
                    }
                }
            });
        }
        return jtpChannelText;
    }
