    private void buildMenu() {
        JMenuBar menuBar;
        JMenu file;
        JMenuItem newItem, open, save, close, quit;
        menuBar = new JMenuBar();
        file = new JMenu("File...");
        file.setMnemonic(KeyEvent.VK_F);
        menuBar.add(file);
        newItem = new JMenuItem("New", KeyEvent.VK_N);
        newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.META_MASK));
        newItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new Training();
            }
        });
        open = new JMenuItem("Open", KeyEvent.VK_O);
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.META_MASK));
        open.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                try {
                    chooser.setCurrentDirectory(new File(new File(".").getCanonicalPath()));
                } catch (IOException ioe) {
                }
                int returnVal = chooser.showOpenDialog(frame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    openFile(chooser.getSelectedFile());
                }
            }
        });
        save = new JMenuItem("Save", KeyEvent.VK_S);
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.META_MASK));
        save.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                saveClassifier();
            }
        });
        close = new JMenuItem("Close", KeyEvent.VK_W);
        close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.META_MASK));
        close.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                recognitionInfoFrame.setVisible(false);
            }
        });
        quit = new JMenuItem("Quit", KeyEvent.VK_Q);
        quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.META_MASK));
        quit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        file.add(newItem);
        file.add(open);
        file.add(save);
        file.addSeparator();
        file.add(close);
        file.add(quit);
        menuBar.add(file);
        frame.setJMenuBar(menuBar);
    }
