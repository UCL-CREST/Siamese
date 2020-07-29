    public ImageDisplay(ImageDisplayApplet applet, GraphicsConfiguration gc) {
        this.applet = applet;
        if (applet == null) {
            frame = new JFrame(gc);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
        Action openAction = new AbstractAction("Open File", new ImageIcon(getClass().getResource("/open.gif"))) {

            public void actionPerformed(ActionEvent e) {
                if (filechooser == null) {
                    filechooser = new JFileChooser();
                    filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                }
                if (filechooser.showOpenDialog(ImageDisplay.this) == JFileChooser.APPROVE_OPTION) {
                    open(filechooser.getSelectedFile());
                }
            }
        };
        JToolBar bar = new JToolBar();
        bar.add(new ToolBarButton(openAction));
        add(bar, BorderLayout.NORTH);
        if (applet == null) {
            frame.setTitle("Image Display - Control Panel");
            frame.getContentPane().add(this, BorderLayout.CENTER);
            frame.pack();
            frame.show();
        }
    }
