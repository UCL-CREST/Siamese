    public GLMStorageFrame() {
        super("Specify Your GLM - Untitled");
        this.lastselectedsourcerow = -1;
        try {
            Class transferAgentClass = this.getStorageTransferAgentClass();
            if (transferAgentClass == null) {
                throw new RuntimeException("Transfer agent class can not be null.");
            }
            Class[] parameterTypes = new Class[] { RepositoryStorage.class };
            Constructor constr = transferAgentClass.getConstructor(parameterTypes);
            Object[] actualValues = new Object[] { this };
            this.transferAgent = (GLMStorageTransferAgent) constr.newInstance(actualValues);
        } catch (Exception err) {
            throw new RuntimeException("Unable to instantiate transfer agent.", err);
        }
        this.waitIndicator = new JLabel("X");
        this.waitIndicator.setHorizontalAlignment(JLabel.CENTER);
        this.waitIndicator.setPreferredSize(new Dimension(25, 25));
        this.waitIndicator.setMaximumSize(new Dimension(25, 25));
        this.waitIndicator.setMinimumSize(new Dimension(25, 25));
        this.NoCallbackChangeMode = false;
        this.setSize(new Dimension(1100, 650));
        this.setLayout(new GridLayout(1, 1));
        this.Config = new GLMStorageConfig();
        this.Config.initialize();
        this.connectionHandler = new RepositoryConnectionHandler(this.Config);
        this.Connection = (StatisticalModelStorageConnectivity) this.connectionHandler.getConnection("default");
        this.Prefs = new StatisticalModelToolsPrefs();
        this.Prefs.initialize();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formatted_date = formatter.format(new Date());
        this.createdOnText = new JTextField(formatted_date);
        this.createdByText = new JTextField(this.Prefs.getConfigValue("createdby"));
        this.reposListeners = new Vector();
        this.iconServer = new IconServer();
        this.iconServer.setConfigFile(this.Prefs.getConfigValue("default", "iconmapfile"));
        this.findGLM = new GLMStorageFindNameDialog(Config, iconServer);
        this.findGLM.setSearchClass(GLMStorage.class);
        this.nicknameText = new IconifiedDomainNameTextField(findGLM, this.iconServer);
        int stdButtonHeight = this.nicknameText.getButtonHeight();
        int setBoxHeight = this.nicknameText.getHeight();
        this.enabledRadio = new JRadioButton("Enabled:");
        this.enabledRadio.setSelected(true);
        this.enabledRadio.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                if (!enabledRadio.isSelected()) {
                    int t = JOptionPane.showConfirmDialog(null, "Note, disabling a storage deprecates it and schedules it for deletion.  Disable this storage?", "Deprecate storage?", JOptionPane.YES_NO_CANCEL_OPTION);
                    if (t != JOptionPane.YES_OPTION) {
                        enabledRadio.setEnabled(false);
                        enabledRadio.setSelected(true);
                        enabledRadio.setEnabled(true);
                    }
                }
            }
        });
        this.editPrefsButton = new JButton("Preferences...");
        this.editPrefsButton.setPreferredSize(new Dimension(4 * stdButtonHeight, stdButtonHeight));
        this.editPrefsButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                prefsEditor.setVisible(true);
            }
        });
        this.commentTextArea = new JTextArea(2, 16);
        this.commentTextArea.setToolTipText("A detailed (possibly formatted) description including guidance to future developers of this set.");
        this.commentTextArea.setText(" ");
        this.findGLM.addOkListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                String selectedSet = findGLM.getSelectedName();
                if (selectedSet != null) {
                    GLMStorage set = (GLMStorage) Connection.getStorage(selectedSet);
                    GLMStorageFrame.this.transferStorage(set);
                } else {
                }
            }
        });
        URL url = this.getClass().getResource("images/file_write_icon.png");
        ImageIcon icon = new ImageIcon(url);
        Image im = icon.getImage();
        icon.setImage(im.getScaledInstance(25, -1, Image.SCALE_SMOOTH));
        this.nicknameText.setPreferredSize(new Dimension(200, 25));
        this.nicknameText.setText(this.Prefs.getConfigValue("default", "domainname") + ".");
        this.nicknameText.setNameTextToolTipText("Right click to search the database.");
        String[] configs = new String[] { "com.mockturtlesolutions.snifflib.flatfiletools.database.FlatFileToolsConfig" };
        this.dataSetGroup = new DefaultMultiConfigNameSelectorGroup(configs);
        this.dataConfigSelector = (JComboBox) this.dataSetGroup.getConfigSelector();
        this.dataReposView = (JButton) this.dataSetGroup.getRepositorySelector();
        this.dataSetText = (IconifiedDomainNameTextField) this.dataSetGroup.getNicknameSelector();
        this.dataConfigSelector.setPreferredSize(new Dimension(100, stdButtonHeight));
        url = this.getClass().getResource("images/file_write_icon.png");
        icon = new ImageIcon(url);
        im = icon.getImage();
        icon.setImage(im.getScaledInstance(25, -1, Image.SCALE_SMOOTH));
        this.uploadButton = new JButton(icon);
        this.uploadButton.setPreferredSize(new Dimension(60, stdButtonHeight));
        this.uploadButton.setToolTipText("Uploads entire set configuration to repository.");
        this.uploadButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                boolean do_transfer = false;
                try {
                    String expname = getNickname();
                    int split = expname.lastIndexOf('.');
                    String domain = "";
                    String name = "";
                    String usersdomain = Prefs.getConfigValue("default", "domainname");
                    if (split > 0) {
                        domain = expname.substring(0, split);
                        name = expname.substring(split + 1, expname.length());
                    } else {
                        name = expname;
                    }
                    name = name.trim();
                    if (name.equals("")) {
                        JOptionPane.showMessageDialog(null, "Cowardly refusing to upload with an empty buffer name...");
                        return;
                    }
                    if (!domain.equals(usersdomain)) {
                        int s = JOptionPane.showConfirmDialog(null, "If you are not the original author, you may wish to switch the current domain name " + domain + " to \nyour domain name " + usersdomain + ".  Would you like to do this?\n (If you'll be using this domain often, you may want to set it in your preferences.)", "Potential WWW name-space clash!", JOptionPane.YES_NO_CANCEL_OPTION);
                        if (s == JOptionPane.YES_OPTION) {
                            setNickname(usersdomain + "." + name);
                            do_transfer = executeTransfer();
                        }
                        if (s == JOptionPane.NO_OPTION) {
                            do_transfer = executeTransfer();
                        }
                    } else {
                        do_transfer = executeTransfer();
                    }
                    setTitle("Specify your GLM - " + expname);
                } catch (Exception err) {
                    throw new RuntimeException("Problem uploading storage.", err);
                }
                setEditable(true);
            }
        });
        this.repositoryView = new JButton("default");
        this.repositoryView.setPreferredSize(new Dimension(3 * stdButtonHeight, stdButtonHeight));
        this.repositoryView.addActionListener(new ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                setRepository(repositoryView.getText());
                repositoryEditor.setVisible(true);
            }
        });
        this.prefsEditor = new PrefsConfigFrame(this.Prefs);
        this.prefsEditor.setVisible(false);
        this.prefsEditor.addCloseListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                prefsEditor.setVisible(false);
            }
        });
        this.prefsEditor.addSelectListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                prefsEditor.setVisible(false);
            }
        });
        this.repositoryEditor = new ReposConfigFrame(this.Config);
        this.repositoryEditor.addSelectListener(new SelectListener());
        this.repositoryEditor.addCloseListener(new CloseListener());
        this.mappingTableModel = new MappingTableModel();
        this.variableMappingsTable = new JTable(this.mappingTableModel);
        TableColumn col = this.variableMappingsTable.getColumnModel().getColumn(1);
        col.setCellEditor(new MappingTableCellEditor());
        Vector varnames = new Vector();
        varnames.add("Height");
        varnames.add("Weight");
        varnames.add("Length");
        varnames.add("Lattitude");
        Collections.sort(varnames);
        this.variableGrabber = new JList(new VariableListModel());
        this.modelLeftHandSide = new JTextField("");
        this.modelRightHandSide = new JTextField("");
        this.modelClassTerms = new JTable(new ClassTermsTableModel());
        this.analysisByTerms = new JTextField("");
        this.randomTerms = new JTable(new RandomTermsTableModel());
        this.addLeftHandSideButton = new JButton("->");
        this.addRightHandSideButton = new JButton("->");
        this.addModelClassTermButton = new JButton("->");
        this.addAnalysisByTermButton = new JButton("->");
        this.addRandomTermButton = new JButton("->");
        this.removeRandomTermButton = new JButton("Remove");
        this.removeClassTermButton = new JButton("Remove");
        this.addNewModelClassTermButton = new JButton("New");
        this.addNewRandomTermButton = new JButton("New");
        this.removeParameterButton = new JButton("Remove");
        this.removeParameterButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                TreePath[] paths = parameterTree.getSelectionPaths();
                for (int i = 0; i < paths.length; i++) {
                    TreePath p = paths[i];
                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) p.getLastPathComponent();
                    if (selectedNode != null) {
                        DomainNameNode uo = (DomainNameNode) selectedNode.getUserObject();
                        String paramName = uo.getDomain();
                        parameterTree.removeDomainNameNode(paramName);
                    }
                }
            }
        });
        this.removeRandomTermButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                int row = randomTerms.getSelectedRow();
                RandomTermsTableModel model = (RandomTermsTableModel) randomTerms.getModel();
                if (row >= 0) {
                    model.removeRow(row);
                }
            }
        });
        this.removeClassTermButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                int row = modelClassTerms.getSelectedRow();
                ClassTermsTableModel model = (ClassTermsTableModel) modelClassTerms.getModel();
                if (row >= 0) {
                    model.removeRow(row);
                }
            }
        });
        this.addLeftHandSideButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                Object[] vars = variableGrabber.getSelectedValues();
                if (vars.length <= 0) {
                    return;
                }
                String currText = modelLeftHandSide.getText();
                currText = currText.trim();
                if (currText.length() == 0) {
                    currText = (String) vars[0];
                } else {
                    currText = currText + vars[0];
                }
                for (int j = 1; j < vars.length; j++) {
                    currText = currText + vars[j];
                }
                modelLeftHandSide.setText(currText);
            }
        });
        this.addRightHandSideButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                Object[] vars = variableGrabber.getSelectedValues();
                if (vars.length <= 0) {
                    return;
                }
                String currText = modelRightHandSide.getText();
                currText = currText.trim();
                if (currText.length() == 0) {
                    currText = (String) vars[0];
                } else {
                    currText = currText + vars[0];
                }
                for (int j = 1; j < vars.length; j++) {
                    currText = currText + vars[j];
                }
                modelRightHandSide.setText(currText);
            }
        });
        this.addNewModelClassTermButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                Object[] vars = variableGrabber.getSelectedValues();
                if (vars.length <= 0) {
                    return;
                }
                int row = -1;
                String currText = "";
                ClassTermsTableModel model = (ClassTermsTableModel) modelClassTerms.getModel();
                if (row < 0) {
                    model.addRow(currText);
                    row = model.getRowCount() - 1;
                }
                currText = (String) model.getValueAt(row, 0);
                currText = currText.trim();
                if (currText.length() == 0) {
                    currText = (String) vars[0];
                } else {
                    currText = currText + vars[0];
                }
                for (int j = 1; j < vars.length; j++) {
                    currText = currText + vars[j];
                }
                model.setValueAt(currText, row, 0);
            }
        });
        this.addModelClassTermButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                Object[] vars = variableGrabber.getSelectedValues();
                if (vars.length <= 0) {
                    return;
                }
                int row = modelClassTerms.getSelectedRow();
                String currText = "";
                ClassTermsTableModel model = (ClassTermsTableModel) modelClassTerms.getModel();
                if (row < 0) {
                    model.addRow(currText);
                    row = model.getRowCount() - 1;
                }
                currText = (String) model.getValueAt(row, 0);
                currText = currText.trim();
                if (currText.length() == 0) {
                    currText = (String) vars[0];
                } else {
                    currText = currText + vars[0];
                }
                for (int j = 1; j < vars.length; j++) {
                    currText = currText + vars[j];
                }
                model.setValueAt(currText, row, 0);
            }
        });
        this.addAnalysisByTermButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                Object[] vars = variableGrabber.getSelectedValues();
                if (vars.length <= 0) {
                    return;
                }
                String currText = analysisByTerms.getText();
                currText = currText.trim();
                if (currText.length() == 0) {
                    currText = (String) vars[0];
                } else {
                    currText = currText + vars[0];
                }
                for (int j = 1; j < vars.length; j++) {
                    currText = currText + vars[j];
                }
                analysisByTerms.setText(currText);
            }
        });
        this.addNewRandomTermButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                Object[] vars = variableGrabber.getSelectedValues();
                if (vars.length <= 0) {
                    return;
                }
                int row = -1;
                String currText = "";
                RandomTermsTableModel model = (RandomTermsTableModel) randomTerms.getModel();
                if (row < 0) {
                    model.addRow(currText);
                    row = model.getRowCount() - 1;
                }
                currText = (String) model.getValueAt(row, 0);
                currText = currText.trim();
                if (currText.length() == 0) {
                    currText = (String) vars[0];
                } else {
                    currText = currText + vars[0];
                }
                for (int j = 1; j < vars.length; j++) {
                    currText = currText + vars[j];
                }
                model.setValueAt(currText, row, 0);
            }
        });
        this.addRandomTermButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                Object[] vars = variableGrabber.getSelectedValues();
                if (vars.length <= 0) {
                    return;
                }
                int row = randomTerms.getSelectedRow();
                String currText = "";
                RandomTermsTableModel model = (RandomTermsTableModel) randomTerms.getModel();
                if (row < 0) {
                    model.addRow(currText);
                    row = model.getRowCount() - 1;
                }
                currText = (String) model.getValueAt(row, 0);
                currText = currText.trim();
                if (currText.length() == 0) {
                    currText = (String) vars[0];
                } else {
                    currText = currText + vars[0];
                }
                for (int j = 1; j < vars.length; j++) {
                    currText = currText + vars[j];
                }
                model.setValueAt(currText, row, 0);
            }
        });
        JPanel setBox = new JPanel();
        setBox.setBackground(Color.gray);
        SpringLayout layout = new SpringLayout();
        setBox.setLayout(layout);
        setBox.add(this.editPrefsButton);
        Box jointBox1 = Box.createHorizontalBox();
        JLabel label = new JLabel("Created On:");
        jointBox1.add(label);
        JScrollPane js1 = new JScrollPane(this.createdOnText);
        js1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        js1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        js1.setPreferredSize(new Dimension(100, 50));
        js1.setMaximumSize(new Dimension(100, 50));
        js1.setMinimumSize(new Dimension(100, 50));
        jointBox1.add(js1);
        setBox.add(jointBox1);
        Box jointBox2 = Box.createHorizontalBox();
        label = new JLabel("Created By:");
        jointBox2.add(label);
        JScrollPane js2 = new JScrollPane(this.createdByText);
        js2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        js2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        js2.setPreferredSize(new Dimension(100, 50));
        js2.setMaximumSize(new Dimension(100, 50));
        js2.setMinimumSize(new Dimension(100, 50));
        jointBox2.add(js2);
        setBox.add(jointBox2);
        setBox.add(this.uploadButton);
        setBox.add(this.repositoryView);
        setBox.add(this.nicknameText);
        setBox.add(this.enabledRadio);
        JPanel rightBorder = new JPanel();
        setBox.add(rightBorder);
        layout.putConstraint(SpringLayout.EAST, rightBorder, 0, SpringLayout.EAST, setBox);
        layout.putConstraint(SpringLayout.WEST, this.editPrefsButton, 0, SpringLayout.WEST, setBox);
        layout.putConstraint(SpringLayout.NORTH, this.editPrefsButton, 0, SpringLayout.NORTH, setBox);
        layout.putConstraint(SpringLayout.EAST, this.editPrefsButton, 0, SpringLayout.WEST, jointBox1);
        layout.putConstraint(SpringLayout.EAST, jointBox1, 0, SpringLayout.WEST, jointBox2);
        layout.putConstraint(SpringLayout.EAST, jointBox2, 0, SpringLayout.WEST, this.uploadButton);
        layout.putConstraint(SpringLayout.EAST, this.uploadButton, 0, SpringLayout.WEST, this.repositoryView);
        layout.putConstraint(SpringLayout.EAST, this.repositoryView, 0, SpringLayout.WEST, this.nicknameText);
        layout.putConstraint(SpringLayout.EAST, this.nicknameText, 0, SpringLayout.WEST, this.enabledRadio);
        layout.putConstraint(SpringLayout.EAST, this.enabledRadio, 0, SpringLayout.WEST, rightBorder);
        layout.putConstraint(SpringLayout.SOUTH, this.enabledRadio, 0, SpringLayout.SOUTH, this.nicknameText);
        layout.putConstraint(SpringLayout.NORTH, this.enabledRadio, 0, SpringLayout.NORTH, this.nicknameText);
        layout.putConstraint(SpringLayout.SOUTH, setBox, 0, SpringLayout.SOUTH, this.nicknameText);
        Box SETBOX = Box.createHorizontalBox();
        SETBOX.add(setBox);
        Box declareBox = Box.createHorizontalBox();
        this.declareModelVariableButton = new JButton("New Variable:");
        this.declareModelVariableButton.setPreferredSize(new Dimension(150, 50));
        this.declareModelVariableButton.setMaximumSize(new Dimension(150, 50));
        this.declareModelVariableButton.setMinimumSize(new Dimension(150, 50));
        this.declareModelVariableText = new JTextField("");
        this.declareModelVariableButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                String var = declareModelVariableText.getText();
                VariableListModel mod = (VariableListModel) (variableGrabber.getModel());
                mod.add(var);
            }
        });
        this.declareModelParameterButton = new JButton("New Parameter:");
        this.declareModelParameterButton.setPreferredSize(new Dimension(150, 50));
        this.declareModelParameterButton.setMaximumSize(new Dimension(150, 50));
        this.declareModelParameterButton.setMinimumSize(new Dimension(150, 50));
        this.declareModelParameterText = new JTextField("");
        this.declareModelParameterButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                String var = declareModelParameterText.getText();
                NamedParameterNode newnode = new NamedParameterNode(var, new DblMatrix(1.0));
                parameterTree.insertDomainNameNode(newnode);
            }
        });
        JScrollPane jsp3 = new JScrollPane(this.declareModelVariableText);
        jsp3.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jsp3.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        jsp3.setPreferredSize(new Dimension(100, 50));
        jsp3.setMaximumSize(new Dimension(100, 50));
        jsp3.setMinimumSize(new Dimension(100, 50));
        declareBox.add(this.declareModelVariableButton);
        declareBox.add(jsp3);
        Box declareParamBox = Box.createHorizontalBox();
        JScrollPane jsp10 = new JScrollPane(this.declareModelParameterText);
        jsp10.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jsp10.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        jsp10.setPreferredSize(new Dimension(100, 50));
        jsp10.setMaximumSize(new Dimension(100, 50));
        jsp10.setMinimumSize(new Dimension(100, 50));
        declareParamBox.add(this.declareModelParameterButton);
        declareParamBox.add(jsp10);
        declareParamBox.add(this.removeParameterButton);
        Box mainbox = Box.createVerticalBox();
        Box varMapBox = Box.createHorizontalBox();
        JScrollPane jsp = new JScrollPane(this.variableMappingsTable);
        jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jsp.setPreferredSize(new Dimension(200, 100));
        jsp.setMaximumSize(new Dimension(200, 100));
        jsp.setMinimumSize(new Dimension(200, 100));
        Box varbox = Box.createVerticalBox();
        varbox.add(new JLabel("Variables:"));
        JScrollPane jsp2 = new JScrollPane(this.variableGrabber);
        jsp2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jsp2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jsp2.setPreferredSize(new Dimension(300, 100));
        jsp2.setMaximumSize(new Dimension(300, 100));
        jsp2.setMinimumSize(new Dimension(300, 100));
        varbox.add(jsp2);
        JButton mapButton = new JButton("Map");
        mapButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                MappingTableModel model = (MappingTableModel) variableMappingsTable.getModel();
                VariableListModel mod = (VariableListModel) (variableGrabber.getModel());
                String var = (String) variableGrabber.getSelectedValue();
                if (var != null) {
                    model.addRow(var);
                }
                System.out.println("Just added a row to model");
            }
        });
        JButton removeVariableButton = new JButton("Remove Variable");
        removeVariableButton.setPreferredSize(new Dimension(150, 50));
        removeVariableButton.setMaximumSize(new Dimension(150, 50));
        removeVariableButton.setMinimumSize(new Dimension(150, 50));
        removeVariableButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                VariableListModel mod = (VariableListModel) (variableGrabber.getModel());
                String var = (String) variableGrabber.getSelectedValue();
                if (var != null) {
                    mod.remove(var);
                    MappingTableModel model = (MappingTableModel) variableMappingsTable.getModel();
                    model.removeMappingsFor(var);
                }
            }
        });
        mapButton.setPreferredSize(new Dimension(100, 50));
        mapButton.setMaximumSize(new Dimension(100, 50));
        mapButton.setMinimumSize(new Dimension(100, 50));
        Box variableControlBox = Box.createHorizontalBox();
        variableControlBox.add(declareBox);
        variableControlBox.add(removeVariableButton);
        varbox.add(variableControlBox);
        varMapBox.add(varbox);
        this.removeVariableMappingButton = new JButton("Remove");
        this.removeVariableMappingButton.setPreferredSize(new Dimension(100, 50));
        this.removeVariableMappingButton.setMaximumSize(new Dimension(100, 50));
        this.removeVariableMappingButton.setMinimumSize(new Dimension(100, 50));
        this.removeVariableMappingButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                MappingTableModel model = (MappingTableModel) variableMappingsTable.getModel();
                int selectedrow = variableMappingsTable.getSelectedRow();
                if (selectedrow >= 0) {
                    model.removeRow(selectedrow);
                }
                System.out.println("Just removed a row from model");
            }
        });
        Box mapControl = Box.createHorizontalBox();
        Box tableBox = Box.createVerticalBox();
        tableBox.add(new JLabel("Variable Mappings:"));
        tableBox.add(jsp);
        mapControl.add(mapButton);
        mapControl.add(this.removeVariableMappingButton);
        tableBox.add(mapControl);
        varMapBox.add(tableBox);
        varMapBox.add(Box.createHorizontalGlue());
        Box glmbox = Box.createHorizontalBox();
        glmbox.add(new JLabel("Model: "));
        Box addbox = Box.createVerticalBox();
        JScrollPane jsp5 = new JScrollPane(this.modelLeftHandSide);
        jsp5.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jsp5.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        jsp5.setPreferredSize(new Dimension(100, 50));
        jsp5.setMaximumSize(new Dimension(100, 50));
        jsp5.setMinimumSize(new Dimension(100, 50));
        addbox.add(jsp5);
        addbox.add(this.addLeftHandSideButton);
        glmbox.add(addbox);
        url = this.getClass().getResource("images/equals_icon.png");
        icon = new ImageIcon(url);
        im = icon.getImage();
        icon.setImage(im.getScaledInstance(25, -1, Image.SCALE_SMOOTH));
        JLabel eqLabel = new JLabel(icon);
        eqLabel.setPreferredSize(new Dimension(50, 50));
        eqLabel.setMaximumSize(new Dimension(50, 50));
        eqLabel.setMinimumSize(new Dimension(50, 50));
        glmbox.add(eqLabel);
        addbox = Box.createVerticalBox();
        JScrollPane jsp6 = new JScrollPane(this.modelRightHandSide);
        jsp6.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jsp6.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        jsp6.setPreferredSize(new Dimension(300, 50));
        jsp6.setMaximumSize(new Dimension(300, 50));
        jsp6.setMinimumSize(new Dimension(300, 50));
        addbox.add(jsp6);
        addbox.add(this.addRightHandSideButton);
        glmbox.add(addbox);
        Box factorsbox = Box.createHorizontalBox();
        addbox = Box.createVerticalBox();
        addbox.add(new JLabel("Analysis By:"));
        JScrollPane jsp7 = new JScrollPane(this.analysisByTerms);
        jsp7.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jsp7.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        jsp7.setPreferredSize(new Dimension(200, 50));
        jsp7.setMaximumSize(new Dimension(200, 50));
        jsp7.setMinimumSize(new Dimension(200, 50));
        addbox.add(jsp7);
        addbox.add(this.addAnalysisByTermButton);
        factorsbox.add(addbox);
        addbox = Box.createVerticalBox();
        addbox.add(new JLabel("Class Terms:"));
        JScrollPane jsp8 = new JScrollPane(this.modelClassTerms);
        jsp8.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jsp8.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jsp8.setPreferredSize(new Dimension(200, 200));
        jsp8.setMaximumSize(new Dimension(200, 200));
        jsp8.setMinimumSize(new Dimension(200, 200));
        addbox.add(jsp8);
        Box ccontBox = Box.createHorizontalBox();
        ccontBox.add(this.addModelClassTermButton);
        ccontBox.add(this.addNewModelClassTermButton);
        ccontBox.add(this.removeClassTermButton);
        addbox.add(ccontBox);
        factorsbox.add(addbox);
        addbox = Box.createVerticalBox();
        addbox.add(new JLabel("Random Terms:"));
        JScrollPane jsp9 = new JScrollPane(this.randomTerms);
        jsp9.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jsp9.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jsp9.setPreferredSize(new Dimension(200, 200));
        jsp9.setMaximumSize(new Dimension(200, 200));
        jsp9.setMinimumSize(new Dimension(200, 200));
        addbox.add(jsp9);
        Box ccontBox2 = Box.createHorizontalBox();
        ccontBox2.add(this.addRandomTermButton);
        ccontBox2.add(this.addNewRandomTermButton);
        ccontBox2.add(this.removeRandomTermButton);
        addbox.add(ccontBox2);
        factorsbox.add(addbox);
        Box databox = Box.createHorizontalBox();
        databox.add(new JLabel("Data Set:"));
        this.dataConfigSelector.setEditable(true);
        this.dataConfigSelector.setToolTipText((String) this.dataConfigSelector.getSelectedItem());
        this.editDataSetButton = new JButton("Edit");
        this.editDataSetButton.setToolTipText("Edit the data storage.");
        this.editDataSetButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                Component edframe = dataSetGroup.getGraphicalEditor();
                if (edframe == null) {
                    return;
                } else {
                    if (edframe instanceof Frame) {
                        edframe.setVisible(true);
                    } else if (edframe instanceof Container) {
                        JFrame out = new JFrame();
                        out.add(edframe);
                        edframe.setVisible(true);
                        out.setVisible(true);
                    } else {
                        JFrame out = new JFrame();
                        out.setLayout(new GridLayout(1, 1));
                        JPanel panel = new JPanel();
                        panel.add(edframe);
                        edframe.setVisible(true);
                        out.add(panel);
                        out.setVisible(true);
                    }
                }
            }
        });
        databox.add(this.dataConfigSelector);
        databox.add(this.dataReposView);
        databox.add(this.dataSetText);
        databox.add(this.editDataSetButton);
        mainbox.add(databox);
        mainbox.add(varMapBox);
        mainbox.add(Box.createVerticalStrut(5));
        mainbox.add(Box.createVerticalGlue());
        mainbox.add(Box.createHorizontalGlue());
        mainbox.add(glmbox);
        mainbox.add(factorsbox);
        DomainNameTree tree = new DomainNameTree();
        if (tree.getTree() == null) {
            System.out.println("The gotten tree is null!" + tree.getTree());
        }
        this.parameterTree = new HierarchyTree(tree.getTree());
        ToolTipManager.sharedInstance().registerComponent(this.parameterTree);
        Box paramBox = Box.createVerticalBox();
        JScrollPane jsp11 = new JScrollPane(this.parameterTree);
        jsp11.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jsp11.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jsp11.setPreferredSize(new Dimension(200, 300));
        jsp11.setMaximumSize(new Dimension(200, 300));
        jsp11.setMinimumSize(new Dimension(200, 300));
        paramBox.add(new JLabel("Model Parameters"));
        paramBox.add(jsp11);
        paramBox.add(declareParamBox);
        Box OUTBOX = Box.createHorizontalBox();
        OUTBOX.add(mainbox);
        OUTBOX.add(paramBox);
        Box TOPBOX = Box.createVerticalBox();
        TOPBOX.add(SETBOX);
        Box commentBox = Box.createVerticalBox();
        commentBox.add(new JLabel("Comment:"));
        commentBox.add(new JScrollPane(this.commentTextArea));
        TOPBOX.add(commentBox);
        TOPBOX.add(OUTBOX);
        this.add(TOPBOX);
        String last_repos = Prefs.getConfigValue("default", "lastrepository").trim();
        if (this.Config.hasRepository(last_repos)) {
            this.setRepository(last_repos);
        }
    }
