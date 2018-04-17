    public ThrongMacAboutHandler(JFrame owner) {
        setBounds(100, 100, 260, 165);
        aboutTextPane = new JLabel();
        aboutTextPane.setBounds(6, 6, 255, 100);
        aboutTextPane.setText("<html><center><strong>Throng</strong> " + Constants.THRONG_VERSION + "</center><br/>" + Constants.FULL_NAME + "<br/><br/>For help look in<html>");
        lbljohannesLuderschmidt = new JLabel("<html><a href=''>" + Constants.HOMEPAGE_TEXT + "</a></html>");
        lbljohannesLuderschmidt.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lbljohannesLuderschmidt.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent e) {
                java.net.URI uri = null;
                try {
                    uri = new java.net.URI(Constants.HOMEPAGE_LINK);
                } catch (URISyntaxException e1) {
                    e1.printStackTrace();
                }
                if (java.awt.Desktop.isDesktopSupported()) {
                    try {
                        if (uri != null) {
                            java.awt.Desktop.getDesktop().browse(uri);
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }

            public void mouseEntered(MouseEvent arg0) {
            }

            public void mouseExited(MouseEvent arg0) {
            }

            public void mousePressed(MouseEvent arg0) {
            }

            public void mouseReleased(MouseEvent arg0) {
            }
        });
        lbljohannesLuderschmidt.setBounds(6, 93, 248, 40);
        aboutPanel = new JPanel();
        aboutPanel.add(aboutTextPane);
        aboutPanel.add(lbljohannesLuderschmidt);
    }
