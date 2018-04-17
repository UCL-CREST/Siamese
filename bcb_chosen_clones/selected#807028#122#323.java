    private static Component createServerPanel(final JFrame frame, final Model model) {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        JLabel urlLabel = new JLabel("URL:");
        final JLabel urlStart = new JLabel("http://");
        final AddressSources addressSources = model.getAddressSources();
        final JComboBox urlHostComboBox = new JComboBox(createAddressListComboBoxModel(addressSources));
        urlHostComboBox.setEditable(true);
        urlHostComboBox.setSelectedItem(model.getAddress());
        urlHostComboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                final Object selected = urlHostComboBox.getSelectedItem();
                final String newValue;
                if (selected instanceof Address) {
                    Address address = (Address) selected;
                    try {
                        newValue = address.getValue();
                    } catch (FailedToGetValueOfAddress e) {
                        urlHostComboBox.setSelectedItem(model.getAddress());
                        final String message = e.getMessage();
                        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else if (selected == RefreshAddressListDummyObject.INSTANCE) {
                    newValue = model.getAddress();
                    urlHostComboBox.setSelectedItem(newValue);
                    urlHostComboBox.setModel(createAddressListComboBoxModel(addressSources));
                } else if (selected == EditAddressListDummyObject.INSTANCE) {
                    newValue = model.getAddress();
                    urlHostComboBox.setSelectedItem(newValue);
                    JDialog dialog = EditAddressListDialog.create(frame, model.getAddressSources());
                    dialog.setVisible(true);
                    urlHostComboBox.setModel(createAddressListComboBoxModel(addressSources));
                } else {
                    newValue = selected.toString();
                }
                model.setAddress(newValue);
                urlHostComboBox.setSelectedItem(newValue);
            }
        });
        final JLabel urlPortLabel = new JLabel(":?");
        final JButton visitUrlButton = new JButton("Visit URL");
        urlPortLabel.addHierarchyListener(new PortURLUpdater(urlPortLabel, model));
        visitUrlButton.setEnabled(Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Action.BROWSE));
        visitUrlButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                URI uri;
                try {
                    uri = model.getURI();
                } catch (URISyntaxException e1) {
                    final String message = String.format("URL is invalid.");
                    JOptionPane.showConfirmDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    Desktop.getDesktop().browse(uri);
                } catch (IOException e) {
                    final String message = String.format("Failed to browse %s", uri);
                    JOptionPane.showConfirmDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            ;
        });
        JButton copyUrlButton = new JButton("Copy URL");
        copyUrlButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                URL url;
                try {
                    url = model.getURL();
                } catch (MalformedURLException e) {
                    final String message = e.getMessage();
                    JOptionPane.showConfirmDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                StringSelection selection = new StringSelection(url.toString());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, selection);
            }

            ;
        });
        JButton editPortButton = new JButton("Edit Port");
        editPortButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                showEditPortDialog(frame, model);
            }
        });
        JLabel commandsLabel = new JLabel("Commands:");
        JButton startButton = StartButton.create(model);
        JButton stopButton = StopButton.create(model);
        JLabel limitsLabel = new JLabel("Limits:");
        JCheckBox deliverLimitCheckBox = new JCheckBox("Limit deliver speed to", model.isDeliverSpeedLimitEnabled());
        deliverLimitCheckBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                model.setDeliverSpeedLimitEnabled(!model.isDeliverSpeedLimitEnabled());
            }
        });
        final Integer currentDeliverLimit = Integer.valueOf(model.getDeliverSpeedLimitLimit() / BYTES_PER_KB);
        final JSpinner deliverLimitSpinner = new JSpinner(new SpinnerNumberModel(currentDeliverLimit, Integer.valueOf(0), null, Integer.valueOf(5)));
        deliverLimitSpinner.getModel().addChangeListener(new javax.swing.event.ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                final int kB = (Integer) deliverLimitSpinner.getValue();
                model.setDeliverSpeedLimit(kB * BYTES_PER_KB);
            }
        });
        final JLabel lastdeliverLimitLabel = new JLabel(" kB/s");
        JCheckBox receiveLimitCheckBox = new JCheckBox("Limit receive speed to", model.isReceiveSpeedLimitEnabled());
        receiveLimitCheckBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                model.setReceiveSpeedLimitEnabled(!model.isReceiveSpeedLimitEnabled());
            }
        });
        final Integer currentreceiveLimit = Integer.valueOf(model.getReceiveSpeedLimitLimit() / BYTES_PER_KB);
        final JSpinner receiveLimitSpinner = new JSpinner(new SpinnerNumberModel(currentreceiveLimit, Integer.valueOf(0), null, Integer.valueOf(50)));
        receiveLimitSpinner.getModel().addChangeListener(new javax.swing.event.ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                final int kB = (Integer) receiveLimitSpinner.getValue();
                model.setReceiveSpeedLimit(kB * BYTES_PER_KB);
            }
        });
        final JLabel lastReceiveLimitLabel = new JLabel(" kB/s");
        Group horizontalLeftSideGroup = layout.createParallelGroup(Alignment.TRAILING);
        horizontalLeftSideGroup.addComponent(urlLabel);
        horizontalLeftSideGroup.addComponent(commandsLabel);
        horizontalLeftSideGroup.addComponent(limitsLabel);
        SequentialGroup horizontalURLGroup = layout.createSequentialGroup();
        horizontalURLGroup.addComponent(urlStart);
        horizontalURLGroup.addComponent(urlHostComboBox, 50, 300, 300);
        horizontalURLGroup.addComponent(urlPortLabel);
        horizontalURLGroup.addPreferredGap(ComponentPlacement.RELATED);
        horizontalURLGroup.addComponent(editPortButton);
        horizontalURLGroup.addPreferredGap(ComponentPlacement.RELATED);
        horizontalURLGroup.addComponent(visitUrlButton);
        horizontalURLGroup.addPreferredGap(ComponentPlacement.RELATED);
        horizontalURLGroup.addComponent(copyUrlButton);
        SequentialGroup horizontalCommandsGroup = layout.createSequentialGroup();
        horizontalCommandsGroup.addComponent(startButton);
        horizontalCommandsGroup.addPreferredGap(ComponentPlacement.RELATED);
        horizontalCommandsGroup.addComponent(stopButton);
        SequentialGroup horizontalLimitGroup = layout.createSequentialGroup();
        horizontalLimitGroup.addComponent(deliverLimitCheckBox);
        horizontalLimitGroup.addComponent(deliverLimitSpinner, GroupLayout.DEFAULT_SIZE, 60, GroupLayout.PREFERRED_SIZE);
        horizontalLimitGroup.addComponent(lastdeliverLimitLabel);
        horizontalLimitGroup.addPreferredGap(ComponentPlacement.UNRELATED);
        horizontalLimitGroup.addComponent(receiveLimitCheckBox);
        horizontalLimitGroup.addComponent(receiveLimitSpinner, GroupLayout.DEFAULT_SIZE, 80, GroupLayout.PREFERRED_SIZE);
        horizontalLimitGroup.addComponent(lastReceiveLimitLabel);
        Group horizontalRightSideGroup = layout.createParallelGroup(Alignment.LEADING);
        horizontalRightSideGroup.addGroup(horizontalURLGroup);
        horizontalRightSideGroup.addGroup(horizontalCommandsGroup);
        horizontalRightSideGroup.addGroup(horizontalLimitGroup);
        SequentialGroup horizontalGroup = layout.createSequentialGroup();
        horizontalGroup.addGroup(horizontalLeftSideGroup);
        horizontalGroup.addPreferredGap(ComponentPlacement.RELATED);
        horizontalGroup.addGroup(horizontalRightSideGroup);
        Group verticalURLGroup = layout.createBaselineGroup(true, false);
        verticalURLGroup.addComponent(urlLabel);
        verticalURLGroup.addComponent(urlStart);
        verticalURLGroup.addComponent(urlHostComboBox);
        verticalURLGroup.addComponent(urlPortLabel);
        verticalURLGroup.addComponent(editPortButton);
        verticalURLGroup.addComponent(visitUrlButton);
        verticalURLGroup.addComponent(copyUrlButton);
        Group verticalCommandsGroup = layout.createBaselineGroup(true, false);
        verticalCommandsGroup.addComponent(commandsLabel);
        verticalCommandsGroup.addComponent(startButton);
        verticalCommandsGroup.addComponent(stopButton);
        Group verticalLimitsGroup = layout.createBaselineGroup(true, false);
        verticalLimitsGroup.addComponent(limitsLabel);
        verticalLimitsGroup.addComponent(deliverLimitCheckBox);
        verticalLimitsGroup.addComponent(deliverLimitSpinner);
        verticalLimitsGroup.addComponent(lastdeliverLimitLabel);
        verticalLimitsGroup.addComponent(receiveLimitCheckBox);
        verticalLimitsGroup.addComponent(receiveLimitSpinner);
        verticalLimitsGroup.addComponent(lastReceiveLimitLabel);
        SequentialGroup verticalGroup = layout.createSequentialGroup();
        verticalGroup.addGroup(verticalURLGroup);
        verticalGroup.addPreferredGap(ComponentPlacement.RELATED);
        verticalGroup.addGroup(verticalCommandsGroup);
        verticalGroup.addPreferredGap(ComponentPlacement.RELATED);
        verticalGroup.addGroup(verticalLimitsGroup);
        layout.setVerticalGroup(verticalGroup);
        layout.setHorizontalGroup(horizontalGroup);
        return createLabeledPanel("Server:", panel);
    }
