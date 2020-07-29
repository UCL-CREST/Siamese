            @Override
            public void actionPerformed(ActionEvent e) {
                if (!Desktop.isDesktopSupported()) {
                    JOptionPane.showMessageDialog(WebsiteButton.this, "OpenGroove can't open the URL \"" + WebsiteButton.this.uri + "\" in your default browser. Try typing " + "in the uri yourself.");
                    return;
                }
                try {
                    Desktop.getDesktop().browse(WebsiteButton.this.uri);
                } catch (IOException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(WebsiteButton.this, "OpenGroove can't open the URL \"" + WebsiteButton.this.uri + "\" in your default browser. Try typing " + "in the uri yourself.");
                    return;
                }
            }
