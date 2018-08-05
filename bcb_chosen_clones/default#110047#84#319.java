    public void init(String[] args) throws Exception {
        prop = new Properties();
        prop.load(new FileInputStream("PanFmpGui.properties"));
        if (args.length == 1) {
            searchService = new SearchService(args[0]);
        } else if (args.length > 1) {
            System.out.println("Please specify path to panFMP config file, e.g.");
            System.out.println("java -jar PanFmpGui ./config.xml");
        }
        this.setSize(800, 600);
        this.setTitle("panFMP GUI - alpha");
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((dim.width - getSize().width) / 2, (dim.height - getSize().height) / 2);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JMenuBar menuBar = new JMenuBar();
        JMenu menuSystem = new JMenu("System");
        JMenuItem loadLocal = new JMenuItem();
        loadLocal.setText("Load config file");
        loadLocal.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser("/");
                fileChooser.addChoosableFileFilter(new XmlFilter());
                fileChooser.setAcceptAllFileFilterUsed(false);
                int returnVal = fileChooser.showOpenDialog(PanFmpGui.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File pathToConfigFile = fileChooser.getSelectedFile();
                    try {
                        searchService = new SearchService(pathToConfigFile.toString());
                        Config conf = new Config(pathToConfigFile.toString(), ConfigMode.SEARCH);
                        Map<String, Config.Config_Field> fields = conf.fields;
                        fieldsCombo.removeAllItems();
                        Iterator<Map.Entry<String, Config.Config_Field>> itFields = fields.entrySet().iterator();
                        while (itFields.hasNext()) {
                            Map.Entry itPair = (Map.Entry) itFields.next();
                            Config_Field fc = (Config_Field) itPair.getValue();
                            if (fc.datatype.equals(DataType.STRING) || fc.datatype.equals(DataType.TOKENIZEDTEXT)) fieldsCombo.addItem(fc.name);
                        }
                        Collection<IndexConfig> indexList = conf.indices.values();
                        virtIndexCombo.removeAllItems();
                        for (IndexConfig iconf : indexList) {
                            if (iconf instanceof VirtualIndexConfig) {
                                VirtualIndexConfig viconf = (VirtualIndexConfig) iconf;
                                virtIndexCombo.addItem(viconf.id);
                            }
                        }
                        PanFmpGui.this.status.setText("Config file successfully loaded");
                    } catch (Exception ex) {
                    }
                    fieldsCombo.setEnabled(true);
                    virtIndexCombo.setEnabled(true);
                    listContentButton.setEnabled(true);
                }
            }
        });
        JMenuItem options = new JMenuItem();
        options.setText("Options");
        options.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                PanFmpGuiOptions pfgo = new PanFmpGuiOptions(PanFmpGui.this.prop);
                pfgo.init();
                pfgo.setVisible(true);
            }
        });
        JMenuItem close = new JMenuItem();
        close.setText("Close");
        close.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                PanFmpGui.this.setVisible(false);
                PanFmpGui.this.dispose();
            }
        });
        menuSystem.add(loadLocal);
        menuSystem.add(options);
        menuSystem.addSeparator();
        menuSystem.add(close);
        JMenu menuOperations = new JMenu("Operations");
        JMenuItem startHarvesting = new JMenuItem();
        startHarvesting.setText("Start Harvesting");
        startHarvesting.setEnabled(false);
        JMenuItem startRebuilding = new JMenuItem();
        startRebuilding.setText("Rebuild Index");
        startRebuilding.setEnabled(false);
        menuOperations.add(startHarvesting);
        menuOperations.add(startRebuilding);
        menuBar.add(menuSystem);
        menuBar.add(menuOperations);
        this.setJMenuBar(menuBar);
        JPanel listTermOptionsPanel = new JPanel();
        {
            TableLayout listTermOptionsPanelLayout = new TableLayout(new double[][] { { 10, 125, 150, 10, TableLayout.PREFERRED, 10 }, { 5, 25, 25 } });
            listTermOptionsPanel.setLayout(listTermOptionsPanelLayout);
            JLabel fieldsLabel = new JLabel("Available Fields");
            fieldsCombo = new JComboBox();
            if (searchService == null) {
                fieldsCombo.setEnabled(false);
            } else {
                fieldsCombo.setEnabled(true);
            }
            JLabel virtIndexLabel = new JLabel("Virtual Index");
            virtIndexCombo = new JComboBox();
            if (searchService == null) {
                virtIndexCombo.setEnabled(false);
            } else {
                virtIndexCombo.setEnabled(true);
            }
            listContentButton = new JButton("list content");
            listContentButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    SearchHandler searchHandler = new SearchHandler(PanFmpGui.this.searchService, PanFmpGui.this.virtIndexCombo.getSelectedItem().toString(), PanFmpGui.this.fieldsCombo.getSelectedItem().toString(), Integer.parseInt(prop.getProperty("numberOfResults")));
                    try {
                        String[] searchArray = searchHandler.listTerms();
                        PanFmpGui.this.status.setText(searchArray.length + " entries found");
                        StringBuffer resultBuffer = new StringBuffer();
                        for (String result : searchArray) {
                            resultBuffer.append(result + "\n");
                        }
                        PanFmpGui.this.textArea.setText(resultBuffer.toString());
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
            });
            if (searchService == null) {
                listContentButton.setEnabled(false);
            } else {
                listContentButton.setEnabled(true);
            }
            listTermOptionsPanel.add(fieldsLabel, "1, 1");
            listTermOptionsPanel.add(fieldsCombo, "2, 1");
            listTermOptionsPanel.add(virtIndexLabel, "1, 2");
            listTermOptionsPanel.add(virtIndexCombo, "2, 2");
            listTermOptionsPanel.add(listContentButton, "4, 1");
        }
        gbc = new GridBagConstraints();
        textArea = new JTextArea(20, 66);
        JScrollPane listTermsScrollPane = new JScrollPane(textArea);
        textArea.setEditable(false);
        JPanel listTerms = new JPanel();
        TableLayout listTermsLayout = new TableLayout(new double[][] { { 10, TableLayout.FILL, 10 }, { 10, 80, TableLayout.FILL, 10 } });
        listTermsLayout.setHGap(5);
        listTermsLayout.setVGap(5);
        listTerms.setLayout(listTermsLayout);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        listTerms.add(listTermOptionsPanel, "1, 1");
        gbc.ipady = 20;
        gbc.gridx = 0;
        gbc.gridy = 1;
        listTerms.add(listTermsScrollPane, "1, 2");
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("List Terms", null, listTerms, "Lists all index entries of a field");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        tabbedPane.setEnabledAt(0, true);
        JPanel searchIndex = new JPanel();
        {
            searchIndex.setLayout(new GridLayout());
            JPanel freeSearch = new JPanel();
            {
                freeSearch.setLayout(new GridBagLayout());
                freeSearchInputTextArea = new JTextArea(5, 40);
                JScrollPane scrollPane = new JScrollPane(freeSearchInputTextArea);
                freeSearchInputTextArea.setEditable(true);
                gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 0;
                freeSearch.add(scrollPane, gbc);
                freeSearchButton = new JButton("start search");
                freeSearchButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        SearchHandler searchHandler = new SearchHandler(PanFmpGui.this.searchService, "dataportal-c3grid", PanFmpGui.this.fieldsCombo.getSelectedItem().toString(), PanFmpGui.this.freeSearchInputTextArea.getText(), Integer.parseInt(prop.get("numberOfResults").toString()));
                        try {
                            SearchResponse searchResponse = searchHandler.freeSearch();
                            SearchResponseItem[] searchResponseItems = searchResponse.getResults();
                            StringBuffer buffer = new StringBuffer("");
                            buffer.append(searchResponse.getTotalCount() + "\n");
                            for (SearchResponseItem sResItem : searchResponseItems) {
                                buffer.append(sResItem.getIdentifier() + "\n");
                            }
                            PanFmpGui.this.freeSearchOutputTextArea.setText(buffer.toString());
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }
                    }
                });
                gbc = new GridBagConstraints();
                gbc.gridx = 1;
                gbc.gridy = 0;
                gbc.gridwidth = GridBagConstraints.REMAINDER;
                freeSearch.add(freeSearchButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                freeSearchOutputTextArea = new JTextArea(20, 60);
                scrollPane = new JScrollPane(freeSearchOutputTextArea);
                freeSearchOutputTextArea.setEditable(false);
                gbc = new GridBagConstraints();
                gbc.ipady = 20;
                gbc.gridx = 0;
                gbc.gridy = 1;
                gbc.gridwidth = GridBagConstraints.REMAINDER;
                freeSearch.add(scrollPane, gbc);
            }
            JTabbedPane searchIndexTabbedPane = new JTabbedPane();
            searchIndexTabbedPane.addTab("Field Search", new JLabel());
            searchIndexTabbedPane.setEnabledAt(0, true);
            searchIndexTabbedPane.addTab("Free Search", freeSearch);
            searchIndexTabbedPane.setEnabledAt(1, true);
            gbc = new GridBagConstraints();
            gbc.anchor = GridBagConstraints.FIRST_LINE_START;
            searchIndex.add(searchIndexTabbedPane, gbc);
        }
        tabbedPane.addTab("Search in Index", null, searchIndex, "Does twice as much nothing");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        tabbedPane.setEnabledAt(1, true);
        JPanel panel3 = new JPanel();
        tabbedPane.addTab("Edit Config File", null, panel3, "Still does nothing");
        {
            jPanel1 = new JPanel();
            panel3.add(jPanel1);
            TableLayout jPanel1Layout = new TableLayout(new double[][] { { TableLayout.FILL, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL }, { 126.0, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL } });
            jPanel1Layout.setHGap(5);
            jPanel1Layout.setVGap(5);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1.setPreferredSize(new java.awt.Dimension(447, 141));
            jPanel1.setSize(PanFmpGui.this.getWidth() - 20, 141);
        }
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
        tabbedPane.setEnabledAt(2, true);
        this.add(tabbedPane);
        this.add(new JSeparator(), BorderLayout.SOUTH);
        this.status = new JLabel("Status");
        this.add(this.status, BorderLayout.SOUTH);
    }
