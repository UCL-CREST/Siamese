    public EEViewer() throws HeadlessException {
        super();
        structureTree = new JTree();
        folderList = new JList();
        folderList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        folderList.addMouseListener(this);
        folderList.setVisibleRowCount(0);
        infoPanel = new JEditorPane();
        infoPanel.setEditable(false);
        infoPanel.setContentType("text/html");
        infoPanel.addHyperlinkListener(new HyperlinkListener() {

            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == EventType.ACTIVATED && Desktop.isDesktopSupported()) {
                    try {
                        logger.info("Url: " + e.getDescription());
                        Desktop.getDesktop().browse(e.getURL().toURI());
                    } catch (Exception ex) {
                        logger.error(ex);
                    }
                }
            }
        });
        structureTree.addTreeSelectionListener(this);
        verticalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(folderList), new JScrollPane(infoPanel));
        horizontalSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(structureTree), verticalSplit);
        this.getContentPane().add(horizontalSplit);
        JToolBar toolbar = new JToolBar();
        toolbar.add(makeNavigationButton("search.gif", "SEARCH", "Suchfenster öffnen", "Suchen"));
        toolbar.addSeparator();
        toolbar.add(makeNavigationButton("help.gif", "HELP", "Hilfefenster öffnen", "Hilfe"));
        this.getContentPane().add(toolbar, BorderLayout.PAGE_START);
        Dimension screen = getToolkit().getScreenSize();
        this.setBounds(100, 100, screen.width - 200, screen.height - 200);
        horizontalSplit.setDividerLocation(0.8);
        verticalSplit.setDividerLocation(400);
        JMenuBar menubar = createMenu();
        this.setJMenuBar(menubar);
        this.addWindowListener(this);
    }
