    public BugZillaAssistant(ProgressThread thread, final Throwable exception, final XmlRpcClient client) throws XmlRpcException {
        super("send_bugreport", true);
        rpcClient = client;
        thread.getProgressListener().setCompleted(35);
        if (thread.isCancelled()) {
            return;
        }
        Object[] compVals, severityVals, platformVals, osVals;
        Map<String, String> valQueryMap = new HashMap<String, String>();
        valQueryMap.put("field", "component");
        valQueryMap.put("product_id", "2");
        Map resultMap = (Map) rpcClient.execute("Bug.legal_values", new Object[] { valQueryMap });
        compVals = (Object[]) resultMap.get("values");
        thread.getProgressListener().setCompleted(50);
        if (thread.isCancelled()) {
            return;
        }
        valQueryMap = new HashMap<String, String>();
        valQueryMap.put("field", "severity");
        valQueryMap.put("product_id", "2");
        resultMap = (Map) rpcClient.execute("Bug.legal_values", new Object[] { valQueryMap });
        severityVals = (Object[]) resultMap.get("values");
        thread.getProgressListener().setCompleted(65);
        if (thread.isCancelled()) {
            return;
        }
        valQueryMap = new HashMap<String, String>();
        valQueryMap.put("field", "platform");
        valQueryMap.put("product_id", "2");
        resultMap = (Map) rpcClient.execute("Bug.legal_values", new Object[] { valQueryMap });
        platformVals = (Object[]) resultMap.get("values");
        thread.getProgressListener().setCompleted(80);
        if (thread.isCancelled()) {
            return;
        }
        valQueryMap = new HashMap<String, String>();
        valQueryMap.put("field", "op_sys");
        valQueryMap.put("product_id", "2");
        resultMap = (Map) rpcClient.execute("Bug.legal_values", new Object[] { valQueryMap });
        osVals = (Object[]) resultMap.get("values");
        thread.getProgressListener().setCompleted(95);
        if (thread.isCancelled()) {
            return;
        }
        Collection<AbstractButton> buttons = new LinkedList<AbstractButton>();
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        final JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(GAP, 0, 0, GAP);
        JLabel loginLabel = new JLabel(I18N.getMessage(I18N.getGUIBundle(), getKey() + ".login_e_mail.label"));
        loginPanel.add(loginLabel, gbc);
        final JTextField loginName = new JTextField(15);
        loginName.setToolTipText(I18N.getMessage(I18N.getGUIBundle(), getKey() + ".login_e_mail.tip"));
        gbc.gridx = 1;
        gbc.weightx = 1;
        loginPanel.add(loginName, gbc);
        JLabel passwordLabel = new JLabel(I18N.getMessage(I18N.getGUIBundle(), getKey() + ".login_password.label"));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        loginPanel.add(passwordLabel, gbc);
        final JPasswordField loginPassword = new JPasswordField(15);
        loginPassword.setToolTipText(I18N.getMessage(I18N.getGUIBundle(), getKey() + ".login_password.tip"));
        gbc.gridx = 1;
        gbc.weightx = 1;
        loginPanel.add(loginPassword, gbc);
        final JCheckBox useAnonymousLogin = new JCheckBox();
        useAnonymousLogin.setText(I18N.getMessage(I18N.getGUIBundle(), getKey() + ".login_as_anonymous.label"));
        useAnonymousLogin.setToolTipText(I18N.getMessage(I18N.getGUIBundle(), getKey() + ".login_as_anonymous.tip"));
        useAnonymousLogin.setSelected(false);
        useAnonymousLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        useAnonymousLogin.setMinimumSize(useAnonymousLogin.getPreferredSize());
        useAnonymousLogin.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    loginName.setEditable(false);
                    loginPassword.setEditable(false);
                } else {
                    loginName.setEditable(true);
                    loginPassword.setEditable(true);
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        loginPanel.add(useAnonymousLogin, gbc);
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.gridy = 3;
        gbc.insets = new Insets(GAP * 2, 0, GAP, 0);
        loginPanel.add(new JSeparator(), gbc);
        panel.add(loginPanel);
        final JPanel detailPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(GAP, 0, 0, GAP);
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.anchor = GridBagConstraints.WEST;
        c.weighty = 0;
        detailPanel.add(new JLabel(I18N.getMessage(I18N.getGUIBundle(), getKey() + ".component.label") + ":"), c);
        c.gridx = 1;
        c.gridy = 0;
        final JComboBox compBox = new JComboBox(compVals);
        compBox.setToolTipText(I18N.getMessage(I18N.getGUIBundle(), getKey() + ".component.tip"));
        compBox.setSelectedItem("Vega: Processes, data flow  and meta data");
        detailPanel.add(compBox, c);
        c.gridx = 2;
        c.gridy = 0;
        detailPanel.add(new JLabel(I18N.getMessage(I18N.getGUIBundle(), getKey() + ".severity.label") + ":"), c);
        c.gridx = 3;
        c.gridy = 0;
        final JComboBox severityBox = new JComboBox(severityVals);
        severityBox.setToolTipText(I18N.getMessage(I18N.getGUIBundle(), getKey() + ".severity.tip"));
        severityBox.setSelectedItem("normal");
        detailPanel.add(severityBox, c);
        c.gridx = 0;
        c.gridy = 1;
        detailPanel.add(new JLabel(I18N.getMessage(I18N.getGUIBundle(), getKey() + ".platform.label") + ":"), c);
        c.gridx = 1;
        c.gridy = 1;
        final JComboBox platformBox = new JComboBox(platformVals);
        platformBox.setToolTipText(I18N.getMessage(I18N.getGUIBundle(), getKey() + ".platform.tip"));
        detailPanel.add(platformBox, c);
        c.gridx = 2;
        c.gridy = 1;
        detailPanel.add(new JLabel(I18N.getMessage(I18N.getGUIBundle(), getKey() + ".os.label") + ":"), c);
        c.gridx = 3;
        c.gridy = 1;
        final JComboBox osBox = new JComboBox(osVals);
        osBox.setToolTipText(I18N.getMessage(I18N.getGUIBundle(), getKey() + ".os.tip"));
        String os = System.getProperty("os.name");
        if (os.toLowerCase(Locale.ENGLISH).contains("windows")) {
            osBox.setSelectedItem("Windows");
            platformBox.setSelectedItem("PC");
        } else if (os.toLowerCase(Locale.ENGLISH).contains("linux")) {
            osBox.setSelectedItem("Linux");
            platformBox.setSelectedItem("PC");
        } else if (os.toLowerCase(Locale.ENGLISH).contains("mac")) {
            osBox.setSelectedItem("Mac OS");
            platformBox.setSelectedItem("Macintosh");
        } else {
            osBox.setSelectedItem("Other");
            platformBox.setSelectedItem("Other");
        }
        detailPanel.add(osBox, c);
        c.gridx = 4;
        c.gridy = 0;
        c.weightx = 1;
        detailPanel.add(new JLabel(), c);
        c.gridy = 1;
        detailPanel.add(new JLabel(), c);
        c.gridwidth = 5;
        c.gridx = 0;
        c.gridy = 2;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(GAP * 2, 0, GAP, 0);
        detailPanel.add(new JSeparator(), c);
        panel.add(detailPanel);
        final JPanel mailPanel = new JPanel(new GridBagLayout());
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(GAP, 0, 0, GAP);
        c.weighty = 0;
        c.weightx = 0;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        final JCheckBox addProcessCheckBox = new JCheckBox();
        addProcessCheckBox.setText(I18N.getMessage(I18N.getGUIBundle(), getKey() + ".add_process_xml.label"));
        addProcessCheckBox.setToolTipText(I18N.getMessage(I18N.getGUIBundle(), getKey() + ".add_process_xml.tip"));
        addProcessCheckBox.setSelected(true);
        addProcessCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        addProcessCheckBox.setMinimumSize(addProcessCheckBox.getPreferredSize());
        mailPanel.add(addProcessCheckBox, c);
        c.gridx = 1;
        final JCheckBox addSysPropsCheckBox = new JCheckBox();
        addSysPropsCheckBox.setText(I18N.getMessage(I18N.getGUIBundle(), getKey() + ".add_system_props.label"));
        addSysPropsCheckBox.setToolTipText(I18N.getMessage(I18N.getGUIBundle(), getKey() + ".add_system_props.tip"));
        addSysPropsCheckBox.setSelected(true);
        addSysPropsCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        addSysPropsCheckBox.setMinimumSize(addSysPropsCheckBox.getPreferredSize());
        mailPanel.add(addSysPropsCheckBox, c);
        c.gridx = 2;
        c.weightx = 0.75;
        mailPanel.add(new JLabel(), c);
        c.gridwidth = 3;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        mailPanel.add(new JLabel(I18N.getMessage(I18N.getGUIBundle(), getKey() + ".summary.label") + ":"), c);
        final JTextField summaryField = new JTextField(15);
        summaryField.setToolTipText(I18N.getMessage(I18N.getGUIBundle(), getKey() + ".summary.tip"));
        c.gridy = 2;
        mailPanel.add(summaryField, c);
        c.gridy = 3;
        mailPanel.add(new JLabel(I18N.getMessage(I18N.getGUIBundle(), getKey() + ".description.label") + ":"), c);
        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);
        descriptionField.addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
                if (descriptionField.getText().equals(descriptionText)) {
                    descriptionField.setText("");
                    descriptionField.removeFocusListener(this);
                }
            }
        });
        descriptionField.setToolTipText(I18N.getMessage(I18N.getGUIBundle(), getKey() + ".description.tip"));
        JScrollPane descriptionPane = new ExtendedJScrollPane(descriptionField);
        descriptionPane.setBorder(createBorder());
        descriptionPane.setPreferredSize(new Dimension(400, 400));
        c.gridy = 4;
        c.weighty = 1;
        mailPanel.add(descriptionPane, c);
        c.insets = new Insets(GAP, 0, 0, 0);
        c.gridx = 3;
        c.gridy = 0;
        c.gridheight = 5;
        c.weightx = 0.25;
        c.weighty = 1;
        attachments.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane attachmentPane = new JScrollPane(attachments);
        attachmentPane.setBorder(createBorder());
        attachmentPane.setPreferredSize(new Dimension(150, 400));
        mailPanel.add(attachmentPane, c);
        panel.add(mailPanel);
        buttons.add(new JButton(new ResourceAction("send_bugreport.add_file") {

            private static final long serialVersionUID = 5152169309271935854L;

            @Override
            public void actionPerformed(ActionEvent e) {
                File file = SwingTools.chooseFile(null, null, true, null, null);
                if (file != null) {
                    ((DefaultListModel) attachments.getModel()).addElement(file);
                }
            }
        }));
        buttons.add(new JButton(new ResourceAction("send_bugreport.remove_file") {

            private static final long serialVersionUID = 5353693430346577972L;

            public void actionPerformed(ActionEvent e) {
                if (attachments.getSelectedIndex() >= 0) {
                    ((DefaultListModel) attachments.getModel()).remove(attachments.getSelectedIndex());
                }
            }
        }));
        JButton infoButton = new JButton(new ResourceAction("send_bugreport.info") {

            private static final long serialVersionUID = 2135052418891516027L;

            @Override
            public void actionPerformed(ActionEvent e) {
                BugReportViewerDialog dialog = new BugReportViewerDialog();
                dialog.setInfoText(BugReport.createCompleteBugDescription(descriptionField.getText().trim(), exception, addProcessCheckBox.isSelected(), addSysPropsCheckBox.isSelected()));
                dialog.setVisible(true);
            }
        });
        buttons.add(infoButton);
        submitButton = new JButton(new ResourceAction("send_bugreport.submit") {

            private static final long serialVersionUID = -4559762951458936715L;

            @Override
            public void actionPerformed(ActionEvent e) {
                email = loginName.getText().trim();
                pawo = loginPassword.getPassword();
                String summary = summaryField.getText().trim();
                String description = descriptionField.getText().trim();
                final String version = RapidMiner.getShortVersion();
                if (!useAnonymousLogin.isSelected()) {
                    if (email.length() <= 0) {
                        SwingTools.showVerySimpleErrorMessage("enter_email");
                        return;
                    }
                    if (!email.matches("(.+?)@(.+?)[.](.+?)")) {
                        SwingTools.showVerySimpleErrorMessage("enter_correct_email");
                        return;
                    }
                    boolean noPW = true;
                    for (char c : pawo) {
                        if (c != ' ') {
                            noPW = false;
                            break;
                        }
                    }
                    if (noPW) {
                        SwingTools.showVerySimpleErrorMessage("enter_password");
                        return;
                    }
                } else {
                    email = "bugs@rapid-i.com";
                    pawo = new char[] { '!', 'z', '4', '8', '#', 'H', 'c', '2', '$', '%', 'm', ')', '9', '+', '*', '*' };
                }
                if (summary.length() <= 0) {
                    SwingTools.showVerySimpleErrorMessage("enter_summary");
                    return;
                }
                String[] splitResult = summary.trim().split("\\s");
                if (splitResult.length < 2) {
                    SwingTools.showVerySimpleErrorMessage("enter_descriptive_summary");
                    return;
                }
                if (description.length() <= 0 || description.equals(descriptionText)) {
                    SwingTools.showVerySimpleErrorMessage("enter_description");
                    return;
                }
                try {
                    if (Desktop.isDesktopSupported()) {
                        Desktop desktop = Desktop.getDesktop();
                        if (desktop.isSupported(Desktop.Action.BROWSE)) {
                            String bugzillaSearchString = "http://bugs.rapid-i.com/buglist.cgi?field0-0-0=attach_data.thedata&type0-0-1=allwordssubstr&field0-0-1=longdesc&query_format=advanced&bug_status=NEW&bug_status=ASSIGNED&bug_status=REOPENED&value0-0-1=" + exception.getMessage() + "&type0-0-0=allwordssubstr&value0-0-0=" + exception.getMessage();
                            URL bugzillaURL = new URL(bugzillaSearchString);
                            URI bugzillaURI = new URI(bugzillaURL.getProtocol(), bugzillaURL.getHost(), bugzillaURL.getPath(), bugzillaURL.getQuery(), null);
                            desktop.browse(bugzillaURI);
                            int returnVal = SwingTools.showConfirmDialog("send_bugreport.check_browser_for_duplicates", ConfirmDialog.YES_NO_OPTION);
                            if (returnVal == ConfirmDialog.NO_OPTION) {
                                return;
                            }
                        }
                    }
                } catch (URISyntaxException e1) {
                } catch (IOException e1) {
                }
                submitButton.setEnabled(false);
                new ProgressThread("send_report_to_bugzilla", false) {

                    @Override
                    public void run() {
                        try {
                            getProgressListener().setTotal(100);
                            ListModel model = attachments.getModel();
                            File[] attachments = new File[model.getSize()];
                            for (int i = 0; i < attachments.length; i++) {
                                attachments[i] = (File) model.getElementAt(i);
                            }
                            getProgressListener().setCompleted(20);
                            XmlRpcClient client = XmlRpcHandler.login(XmlRpcHandler.BUGZILLA_URL, email, pawo);
                            getProgressListener().setCompleted(40);
                            BugReport.createBugZillaReport(client, exception, summaryField.getText().trim(), descriptionField.getText().trim(), String.valueOf(compBox.getSelectedItem()), version, String.valueOf(severityBox.getSelectedItem()), String.valueOf(platformBox.getSelectedItem()), String.valueOf(osBox.getSelectedItem()), RapidMinerGUI.getMainFrame().getProcess(), RapidMinerGUI.getMainFrame().getMessageViewer().getLogMessage(), attachments, addProcessCheckBox.isSelected(), addSysPropsCheckBox.isSelected());
                            getProgressListener().setCompleted(100);
                            SwingTools.showMessageDialog("bugreport_successful");
                            dispose();
                        } catch (XmlRpcException e1) {
                            SwingTools.showVerySimpleErrorMessage("bugreport_xmlrpc_error", e1.getLocalizedMessage());
                        } catch (Exception e2) {
                            LogService.getRoot().warning(e2.getLocalizedMessage());
                            SwingTools.showVerySimpleErrorMessage("bugreport_creation_failed");
                        } finally {
                            getProgressListener().complete();
                            for (int i = 0; i < pawo.length; i++) {
                                pawo[i] = 0;
                            }
                            submitButton.setEnabled(true);
                        }
                    }
                }.start();
            }
        });
        buttons.add(submitButton);
        buttons.add(makeCancelButton());
        layoutDefault(panel, LARGE, buttons);
    }
