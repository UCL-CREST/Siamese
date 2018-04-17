    protected void initFrame() {
        JPanel contents = new JPanel(new BorderLayout());
        triadDisplay = new TriadDisplay();
        triadDisplay.addItemListener(new mySelector());
        contents.add(triadDisplay, BorderLayout.CENTER);
        contents.add(new JLabel("Which one is most different?"), BorderLayout.NORTH);
        statusWindow = new JLabel();
        contents.add(statusWindow, BorderLayout.SOUTH);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(contents, BorderLayout.CENTER);
        JMenuBar menuBar = new JMenuBar();
        JMenu menu;
        JMenuItem menuItem;
        ActionListener listener;
        menu = new JMenu("File");
        menuItem = new JMenuItem("Open");
        listener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String directory = (lastDirectory != null) ? lastDirectory : "data";
                JFileChooser fileChooser = new JFileChooser(directory);
                int returnVal = fileChooser.showOpenDialog(statusWindow.getTopLevelAncestor());
                switch(returnVal) {
                    case JFileChooser.APPROVE_OPTION:
                        openFile(fileChooser.getSelectedFile());
                        break;
                    case JFileChooser.CANCEL_OPTION:
                        message("Open cancelled", 10);
                        break;
                    default:
                        System.err.println("Bogosity from JFileChooser");
                        break;
                }
            }
        };
        menuItem.addActionListener(listener);
        menu.add(menuItem);
        menuItem = new JMenuItem("Quit");
        listener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        };
        menuItem.addActionListener(listener);
        menu.add(menuItem);
        menuBar.add(menu);
        menu = new JMenu("Test");
        menuItem = new JMenuItem("Start Test");
        listener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                startTest();
            }
        };
        menuItem.addActionListener(listener);
        menu.add(menuItem);
        menuItem = new JMenuItem("Stop Test");
        listener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                stopTest();
            }
        };
        menuItem.addActionListener(listener);
        menu.add(menuItem);
        menuItem.setEnabled(false);
        stopMenuItem = menuItem;
        menuBar.add(menu);
        menu = new JMenu("Debug");
        menuItem = new JMenuItem("Timings");
        listener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Gesture[] gestures = triadDisplay.getGestures();
                for (int i = 0; i < 3; i++) {
                    System.out.println("Gesture " + i + ":");
                    gestures[i].printTiming(System.out);
                }
            }
        };
        menuItem.addActionListener(listener);
        menu.add(menuItem);
        menuBar.add(menu);
        getRootPane().setJMenuBar(menuBar);
        HystericResizer hr = new HystericResizer();
        getRootPane().addComponentListener(hr);
    }
