    protected void genUI() {
        executeButton.setMnemonic(KeyEvent.VK_R);
        saveButton.setMnemonic(KeyEvent.VK_C);
        startExecutionButton.setMnemonic(KeyEvent.VK_N);
        tcDocsPane.setEditable(false);
        tcDocsPane.setContentType("text/html");
        TextAreaAppender.addTextArea(tcLogsPane);
        tcDocsPane.setEditorKit(new HTMLEditorKit());
        tcDocsPane.addHyperlinkListener(new HyperlinkListener() {

            public void hyperlinkUpdate(HyperlinkEvent e) {
            }
        });
        ExecuteButtonAction buttonListener = new ExecuteButtonAction(this);
        executeButton.addActionListener(buttonListener);
        executeButton.setIcon(ResourceManager.getInstance().getImageIcon("icons/running_32"));
        executeButton.setToolTipText("Run Test(s)");
        startExecutionButton.setIcon(ResourceManager.getInstance().getImageIcon("icons/start"));
        startExecutionButton.setToolTipText("Continue test case execution (F8)");
        startExecutionButton.setVisible(false);
        startExecutionButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                continueDebug();
            }
        });
        stepOverExecutionButton.setIcon(ResourceManager.getInstance().getImageIcon("icons/stepover"));
        stepOverExecutionButton.setToolTipText("Step over the script execution (F6)");
        stepOverExecutionButton.setVisible(false);
        stepOverExecutionButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                continueStep();
            }
        });
        stepIntoExecutionButton.setIcon(ResourceManager.getInstance().getImageIcon("icons/stepinto"));
        stepIntoExecutionButton.setToolTipText("Step into the script execution (F5)");
        stepIntoExecutionButton.setVisible(false);
        stepIntoExecutionButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                continueStepInto();
            }
        });
        stopExecutionButton.setIcon(ResourceManager.getInstance().getImageIcon("icons/stop"));
        stopExecutionButton.setToolTipText("Stop execution");
        stopExecutionButton.setVisible(false);
        stopExecutionButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                stopExecution();
            }
        });
        debugButton.setIcon(ResourceManager.getInstance().getImageIcon("icons/debug"));
        debugButton.setToolTipText("Debug Test(s)");
        debugButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                {
                    runTestSuite(true, 1, false);
                }
            }
        });
        saveButton.setIcon(ResourceManager.getInstance().getImageIcon("icons/save_32"));
        saveButton.setToolTipText("Save and check script(s) syntax");
        saveButton.setName("save button");
        saveButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                checkScriptsSyntax();
            }
        });
        resultsButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                boolean showTestSuiteReport = resultsPane.getCurrentRunName().equals("Run1");
                String resDir = TestEngineConfiguration.getInstance().getString("reporting.generated_report_path");
                String baseDir = System.getProperty("user.dir");
                String filename = baseDir + File.separator + resDir + File.separator + (showTestSuiteReport ? "index.html" : "campaign.html");
                File resultsFile = new File(filename);
                try {
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().open(resultsFile);
                    } else {
                        logger.error("Feature not supported by this platform");
                    }
                } catch (IOException ex) {
                    logger.error("Could not open " + filename);
                }
            }
        });
        resultsButton.setToolTipText("View the HTML Test Run Summary Results");
        resultsButton.setName("test run results button");
        JPanel northP = new JPanel(new SpringLayout());
        northP.add(executeButton);
        northP.add(saveButton);
        northP.add(debugButton);
        northP.add(resultsButton);
        northP.add(new CommonShortcutsPanel());
        northP.add(startExecutionButton);
        northP.add(stepOverExecutionButton);
        northP.add(stepIntoExecutionButton);
        northP.add(stopExecutionButton);
        SpringUtilities.makeCompactGrid(northP, 1, 9, 6, 6, 6, 6);
        add(northP, BorderLayout.NORTH);
        tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
        editorTabbedPane = new JTabbedPane(JTabbedPane.TOP);
        editorTabbedPane.addMouseListener(new TabMouseListener());
        editorTabbedPane.setFocusable(false);
        sourcePanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        getTcSourcePane().add(editorTabbedPane);
        sourcePanel.setTopComponent(getTcSourcePane());
        sourcePanel.setFocusable(false);
        sourcePanel.setDividerSize(4);
        debugPanel = new DebugVariablePanel();
        debugPanel.setPreferredSize(new Dimension(100, 150));
        sourcePanel.setResizeWeight(0.9);
        sourcePanel.setBottomComponent(debugPanel);
        debugPanel.setVisible(false);
        tabbedPane.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (tabbedPane.getSelectedIndex() == SOURCE_INDEX) {
                    Component tab = editorTabbedPane.getSelectedComponent();
                    if (tab != null && tab instanceof JScrollPane) {
                        JScrollPane scrollPane = (JScrollPane) tab;
                        tab = scrollPane.getViewport().getView();
                        if (tab != null) {
                            tab.requestFocusInWindow();
                        }
                    }
                }
                if (!isDocTabSelected()) {
                    return;
                }
                NonWrappingTextPane tsPane = getTcSourceTextPane();
                if (tsPane != null) {
                    File tsFile = new File(tsPane.getFileName());
                    PythonTestScript pScript = new PythonTestScript(tsFile, getTestSuiteDirectory());
                    if (pScript.isDocSynchronized()) {
                        return;
                    }
                    parent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    pScript.generateDoc();
                    HTMLDocumentLoader loader = new HTMLDocumentLoader();
                    HTMLDocument doc;
                    try {
                        File tcDoc = pScript.getTestcaseDoc();
                        if (tcDoc != null) {
                            doc = loader.loadDocument(tcDoc.toURI().toURL());
                            setTestCaseInfo(doc);
                        } else {
                            setTestCaseInfo(null);
                        }
                    } catch (MalformedURLException e1) {
                        logger.error(e1);
                        setTestCaseInfo(null);
                    } catch (IOException e1) {
                        logger.error(e1);
                        setTestCaseInfo(null);
                    }
                    parent.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });
        tabbedPane.add(DOC, new JScrollPane(tcDocsPane));
        tabbedPane.add(SOURCE, sourcePanel);
        tabbedPane.add(RESULTS, resultsPane);
        tabbedPane.add(LOGS, tcLogsPane);
        add(tabbedPane, BorderLayout.CENTER);
    }
