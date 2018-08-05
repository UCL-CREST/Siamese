    protected void assignListeners() {
        groupsList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent event) {
                refreshInfo();
            }
        });
        saveButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                JFileChooser fileDialog = new JFileChooser(".");
                fileDialog.setFileFilter(ReaderData.mkExtensionFileFilter(".grp", "Group Files"));
                int outcome = fileDialog.showSaveDialog((Frame) null);
                if (outcome == JFileChooser.APPROVE_OPTION) {
                    assert (fileDialog.getCurrentDirectory() != null);
                    assert (fileDialog.getSelectedFile() != null);
                    String fileName = fileDialog.getCurrentDirectory().toString() + File.separator + fileDialog.getSelectedFile().getName();
                    try {
                        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
                        ReaderWriterGroup.write(out, writer);
                        System.err.println("Wrote groups informations to output '" + fileName + "'.");
                        out.close();
                    } catch (IOException e) {
                        System.err.println("error while writing (GroupManager.saveClt):");
                        e.printStackTrace();
                    }
                } else if (outcome == JFileChooser.CANCEL_OPTION) {
                }
            }
        });
        loadButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                JFileChooser fileDialog = new JFileChooser(".");
                fileDialog.setFileFilter(ReaderData.mkExtensionFileFilter(".grp", "Group Files"));
                int outcome = fileDialog.showOpenDialog((Frame) null);
                if (outcome == JFileChooser.APPROVE_OPTION) {
                    assert (fileDialog.getCurrentDirectory() != null);
                    assert (fileDialog.getSelectedFile() != null);
                    String fileName = fileDialog.getCurrentDirectory().toString() + File.separator + fileDialog.getSelectedFile().getName();
                    BufferedReader fileReader = null;
                    try {
                        fileReader = new BufferedReader(new FileReader(fileName));
                        ReaderWriterGroup.read(fileReader, writer);
                        fileReader.close();
                    } catch (Exception e) {
                        System.err.println("Exception while reading from file '" + fileName + "'.");
                        System.err.println(e);
                    }
                } else if (outcome == JFileChooser.CANCEL_OPTION) {
                }
            }
        });
        ItemListener propItemListener = new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent event) {
                int[] indices = groupsList.getSelectedIndices();
                for (int index : indices) {
                    Group group = getGroupFromListIndex(index);
                    if (group != null) {
                        if (event.getSource() instanceof JComboBox) {
                            JComboBox eventSource = (JComboBox) event.getSource();
                            if (eventSource == colorComboBox) {
                                Color color = colorComboBox.getSelectedColor();
                                assert (color != null);
                                group.setColor(color);
                                shapeComboBox.setColor(color);
                            } else if (eventSource == shapeComboBox) {
                                Shape shape = shapeComboBox.getSelectedShape();
                                assert (shape != null);
                                group.setShape(shape);
                            }
                        } else if (event.getSource() instanceof JCheckBox) {
                            JCheckBox eventSource = (JCheckBox) event.getSource();
                            if (eventSource == showGroupCheckBox) {
                                group.visible = showGroupCheckBox.isSelected();
                            } else if (eventSource == showGraphicInfoCheckBox) {
                                group.info = showGraphicInfoCheckBox.isSelected();
                            }
                        }
                    }
                }
                graph.notifyAboutGroupsChange(null);
            }
        };
        colorComboBox.addItemListener(propItemListener);
        shapeComboBox.addItemListener(propItemListener);
        showGroupCheckBox.addItemListener(propItemListener);
        showGraphicInfoCheckBox.addItemListener(propItemListener);
        showGroupfreeNodesCheckBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent event) {
                graph.getGroup(0).visible = showGroupfreeNodesCheckBox.isSelected();
                graph.notifyAboutGroupsChange(null);
            }
        });
        ActionListener propActionListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                JButton botton = (JButton) event.getSource();
                Group group = getGroupFromListIndex(groupsList.getSelectedIndex());
                if (group != null) {
                    for (GraphVertex graphVertex : group) {
                        if (botton == showLabelsButton) {
                            graphVertex.setShowName(NameVisibility.Priority.GROUPS, true);
                        } else if (botton == hideLabelsButton) {
                            graphVertex.setShowName(NameVisibility.Priority.GROUPS, false);
                        }
                    }
                    graph.notifyAboutGroupsChange(null);
                }
            }
        };
        showLabelsButton.addActionListener(propActionListener);
        hideLabelsButton.addActionListener(propActionListener);
        newButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                String newGroupName = JOptionPane.showInputDialog(null, "Enter a name", "Name of the new group", JOptionPane.QUESTION_MESSAGE);
                if (newGroupName != null) {
                    if (graph.getGroup(newGroupName) == null) {
                        graph.addGroup(new Group(newGroupName, graph));
                    }
                }
            }
        });
        editButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                Group group = getGroupFromListIndex(groupsList.getSelectedIndex());
                if (group != null) {
                    DialogEditGroup dialog = new DialogEditGroup(graph, group);
                    dialog.setModal(true);
                    dialog.setVisible(true);
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                int index = groupsList.getSelectedIndex();
                if (index > 0 && index < graph.getNumberOfGroups() - 1) {
                    graph.removeGroup(index);
                }
            }
        });
        upButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                int index = groupsList.getSelectedIndex();
                if (index < graph.getNumberOfGroups() - 1) {
                    graph.moveGroupUp(index);
                    groupsList.setSelectedIndex(index - 1);
                }
            }
        });
        downButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                int index = groupsList.getSelectedIndex();
                if (index < graph.getNumberOfGroups() - 1) {
                    graph.moveGroupDown(index);
                    groupsList.setSelectedIndex(index + 1);
                }
            }
        });
    }
