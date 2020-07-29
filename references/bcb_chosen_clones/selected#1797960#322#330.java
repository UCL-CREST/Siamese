                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!Desktop.isDesktopSupported()) return;
                    try {
                        Desktop.getDesktop().browse(URI.create(Slideshow.this.items.elementAt(imageIndex).link));
                    } catch (Exception e2) {
                        JOptionPane.showMessageDialog(Slideshow.this, e2.getMessage());
                    }
                }
