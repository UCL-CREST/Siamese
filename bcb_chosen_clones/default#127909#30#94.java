    public MainFrame() {
        final CheckTree tree = new CheckTree();
        final JPanel selectionAna = new JPanel();
        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("Dosya");
        file.setMnemonic(KeyEvent.VK_D);
        JMenuItem yolBelirle = new JMenuItem("Kaynak Dizin");
        yolBelirle.setMnemonic(KeyEvent.VK_K);
        yolBelirle.setToolTipText("Icerik Kontrolu icin Klasor secimi");
        yolBelirle.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new File("."));
                chooser.setDialogTitle("Dizin Seciniz");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);
                if (chooser.showOpenDialog(selectionAna) == JFileChooser.APPROVE_OPTION) {
                    INDEXDIR = chooser.getSelectedFile().getAbsolutePath();
                    if (MainFrame.DEBUG) System.out.println(this.getClass().getName() + ": Dizin agaci icin indexDir degistiriliyor, yeni indexDir = " + INDEXDIR);
                    tree.setroot(INDEXDIR);
                }
            }
        });
        file.add(yolBelirle);
        JMenuItem closeWindow = new JMenuItem("Cikis");
        closeWindow.setMnemonic(KeyEvent.VK_C);
        closeWindow.setToolTipText("Programdan Cik");
        closeWindow.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });
        file.add(closeWindow);
        menuBar.add(file);
        JMenu search = new JMenu("Arama");
        file.setMnemonic(KeyEvent.VK_S);
        JMenuItem searchElement = new JMenuItem("Arama");
        JMenuItem searchMedia = new JMenuItem("Medya ara");
        searchMedia.setToolTipText("Medya nin icerigini getir");
        searchMedia.addActionListener(new AramaDugmeDinleme(MEDIASEARCH));
        searchElement.setToolTipText("Icerik Arama");
        searchElement.addActionListener(new AramaDugmeDinleme(FILESEARCH));
        search.add(searchElement);
        search.add(searchMedia);
        menuBar.add(search);
        setJMenuBar(menuBar);
        JPanel treePanel = new JPanel();
        treePanel.setLayout(new BorderLayout());
        JButton saveDatabase = new JButton("Veritabanina Kaydet");
        selectionAna.setLayout(new BorderLayout());
        SelectionPanel selectionPanel = new SelectionPanel();
        JScrollPane selectionPane = new JScrollPane(selectionPanel);
        selectionAna.add(selectionPane);
        selectionAna.add(saveDatabase, BorderLayout.SOUTH);
        saveDatabase.addActionListener(new DatabaseSaveListener(selectionPanel));
        JScrollPane sp = new JScrollPane(tree);
        treePanel.add(sp);
        getContentPane().add(treePanel, BorderLayout.WEST);
        JButton listeGetir = new JButton("Liste Getir");
        listeGetir.addActionListener(new ListeCekmeDugmeDinleme(tree, selectionPanel));
        treePanel.add(listeGetir, BorderLayout.SOUTH);
        getContentPane().add(selectionAna, BorderLayout.CENTER);
    }
