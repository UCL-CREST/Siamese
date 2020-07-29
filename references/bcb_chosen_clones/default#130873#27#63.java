    public AniPlayer() {
        AniPlayerWindow.setFocusable(true);
        AniPlayerWindow.setPreferredSize(new Dimension(640, 480));
        BrowseWindow = new CBrowseWindow();
        BrowseWindow.setLocation(0, 0);
        BrowseWindow.setPreferredSize(new Dimension(640, 480));
        AniPlayerWindow.add(BrowseWindow);
        fc = new JFileChooser();
        AniPlayerWindow.addKeyListener(new KeyAdapter() {

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
        });
        AniPlayerWindow.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                BrowseWindow.setLocation(0, 0);
                BrowseWindow.setSize(AniPlayerWindow.getSize());
            }
        });
    }
