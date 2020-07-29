            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (toLowerCase(e.getKeyChar()) == 'o') {
                    int returnVal = fc.showOpenDialog(AniPlayerWindow);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        BufferedImage img = null;
                        try {
                            img = ImageIO.read(fc.getSelectedFile());
                        } catch (IOException ie) {
                        }
                        BrowseWindow.setImg(img);
                        AniPlayerWindow.repaint();
                    }
                }
            }
