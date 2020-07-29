    public CharacterLink(String linkText, int leftGap, int rightGap, int vOffset) {
        super(new JLabel(linkText), leftGap, rightGap, vOffset);
        this.leftGap = leftGap;
        this.rightGap = rightGap;
        this.vOffset = vOffset;
        label = (JLabel) comp;
        allowLineBreak = false;
        label.setFont(FONT);
        label.setForeground(forgroundColor);
        label.setIconTextGap(1);
        label.setVerticalAlignment(JLabel.TOP);
        label.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                long delay = System.currentTimeMillis() - lastClicked;
                if (e.getButton() == MouseEvent.BUTTON1 && delay > 1000) {
                    e.consume();
                    lastClicked = System.currentTimeMillis();
                    if (Desktop.isDesktopSupported()) {
                        Desktop desktop = Desktop.getDesktop();
                        if (desktop.isSupported(Desktop.Action.BROWSE)) {
                            try {
                                desktop.browse(new URI("http://everquest2.com/Valor/" + URLEncoder.encode(label.getText(), "UTF-8") + "/"));
                            } catch (Exception ex) {
                                System.err.println(ex.getMessage());
                            }
                        }
                    }
                }
            }

            public void mouseEntered(MouseEvent e) {
                forgroundColor = label.getForeground();
                label.setForeground(mouseOverForgroundColor);
            }

            public void mouseExited(MouseEvent e) {
                label.setForeground(forgroundColor);
            }
        });
    }
