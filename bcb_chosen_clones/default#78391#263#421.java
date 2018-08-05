    private void initGUI() {
        try {
            FormLayout thisLayout = new FormLayout("5dlu, right:60dlu, 5dlu, 60dlu, 5dlu, right:60dlu, 5dlu, max(p;60dlu), 5dlu, max(p;60dlu), 5dlu, 60dlu", "max(p;5dlu), max(p;5dlu), 5dlu, max(p;5dlu), 5dlu, max(p;5dlu), 5dlu, max(p;15dlu), 5dlu, max(p;15dlu), 5dlu, max(p;15dlu), 5dlu, max(p;15dlu)");
            this.setLayout(thisLayout);
            {
                lblTaskDate = new JLabel();
                this.add(lblTaskDate, new CellConstraints("2, 2, 1, 1, default, default"));
                lblTaskDate.setText("Task Date");
            }
            {
                lblTaskStart = new JLabel();
                this.add(lblTaskStart, new CellConstraints("2, 4, 1, 1, default, default"));
                lblTaskStart.setText("Task Start Date");
            }
            {
                lblTaskStop = new JLabel();
                this.add(lblTaskStop, new CellConstraints("2, 6, 1, 1, default, default"));
                lblTaskStop.setText("Task End Date");
            }
            {
                txtTaskDate = new JTextField();
                this.add(txtTaskDate, new CellConstraints("4, 2, 1, 1, default, default"));
                txtTaskDate.setText("task date");
                txtTaskDate.setToolTipText("Date of creation   ie.   1886-08-16");
            }
            {
                txtTaskStart = new JTextField();
                this.add(txtTaskStart, new CellConstraints("4, 4, 1, 1, default, default"));
                txtTaskStart.setText("task start");
                txtTaskStart.setToolTipText("Date assignment should be issued   ie.   1886-08-16");
            }
            {
                txtTaskEnd = new JTextField();
                this.add(txtTaskEnd, new CellConstraints("4, 6, 1, 1, default, default"));
                txtTaskEnd.setText("task end");
                txtTaskEnd.setToolTipText("Last date of assignment availablity   ie.   1886-08-16");
            }
            {
                lblTaskType = new JLabel();
                this.add(lblTaskType, new CellConstraints("6, 2, 1, 1, default, default"));
                lblTaskType.setText("Task Type");
            }
            {
                lblTaskDesc = new JLabel();
                this.add(lblTaskDesc, new CellConstraints("6, 4, 1, 1, default, default"));
                lblTaskDesc.setText("Task Description");
            }
            {
                ComboBoxModel cboTaskTypeModel = new DefaultComboBoxModel(new String[] { "Handout", "Homework", "Quiz", "Test" });
                cboTaskType = new JComboBox();
                this.add(cboTaskType, new CellConstraints("8, 2, 1, 1, default, default"));
                cboTaskType.setModel(cboTaskTypeModel);
                cboTaskType.setEditable(false);
            }
            {
                txtTaskDescr = new JTextField();
                this.add(txtTaskDescr, new CellConstraints("8, 4, 3, 1, default, default"));
                txtTaskDescr.setText("task description");
                txtTaskDescr.setToolTipText("Short description of task");
            }
            {
                lblFilePath = new JLabel();
                this.add(lblFilePath, new CellConstraints("2, 10, 1, 1, default, default"));
                lblFilePath.setText("File Path");
            }
            {
                txtFilePath = new JTextField();
                this.add(txtFilePath, new CellConstraints("4, 10, 5, 1, default, default"));
                txtFilePath.setText("file path");
                txtFilePath.setEditable(false);
                txtFilePath.setFocusable(false);
                txtFilePath.setToolTipText("Relative file path   ie.   .\\Fauntleroy\\HW1.doc");
            }
            {
                lblFileDescr = new JLabel();
                this.add(lblFileDescr, new CellConstraints("2, 12, 1, 1, default, default"));
                lblFileDescr.setText("File Description");
            }
            {
                txtFileDescr = new JTextField();
                this.add(txtFileDescr, new CellConstraints("4, 12, 3, 1, default, default"));
                txtFileDescr.setText("file descripton");
                txtFileDescr.setToolTipText("Short description of file");
            }
            {
                btnBrowse = new JButton();
                this.add(btnBrowse, new CellConstraints("10, 10, 1, 1, default, default"));
                btnBrowse.setText("Browse");
                btnBrowse.setToolTipText("Browse for the file to be associated");
                btnBrowse.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        System.out.println("File Browse button pressed.");
                        NewFileChooser = new JFileChooser(mediator.getCurDir());
                        int returnVal = NewFileChooser.showOpenDialog(AssignmentEdit.this);
                        if (returnVal == JFileChooser.APPROVE_OPTION) {
                            String file = mediator.molestPath(NewFileChooser.getSelectedFile().toString());
                            txtFilePath.setText(mediator.molestPath(file));
                            System.out.println(mediator.molestPath(file.replaceFirst(mediator.getCurDir(), "").trim()));
                        } else {
                            System.out.print("Open command cancelled by user.\n");
                        }
                    }
                });
            }
            {
                btnSaveApply = new JButton();
                this.add(btnSaveApply, new CellConstraints("10, 14, 1, 1, default, default"));
                if (edit == true) {
                    btnSaveApply.setText("Save");
                } else {
                    btnSaveApply.setText("Apply");
                }
                btnSaveApply.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        System.out.println("SaveApply button pressed.");
                        if (errorCheckTextBoxes() == true) {
                            if (edit == true) {
                                saveFile(saveTask(null));
                            } else {
                                saveFile(saveTask(thisTask));
                            }
                            mediator.pullTab(mediator.AssignmentEditTab);
                            mediator.showLastTab();
                            mediator.ClassroomOptionsTab.refreshTaskTable();
                            mediator.deactivate(mediator.AssignmentEditTab);
                            mediator.ClassroomOptionsTab.tableVisibility(false, true);
                        }
                    }
                });
            }
            {
                btnCancel = new JButton();
                this.add(btnCancel, new CellConstraints("12, 14, 1, 1, default, default"));
                btnCancel.setText("Cancel");
                btnCancel.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        System.out.println("Cancel button pressed.");
                        mediator.ClassroomOptionsTab.refreshTaskTable();
                        mediator.showLastTab();
                        mediator.pullTab(mediator.AssignmentEditTab);
                        mediator.deactivate(mediator.AssignmentEditTab);
                    }
                });
            }
            {
                chkAvailable = new JCheckBox();
                this.add(chkAvailable, new CellConstraints("4, 8, 3, 1, default, default"));
                chkAvailable.setText("Make this assignment available");
                chkAvailable.setVisible(true);
            }
            setSize(800, 600);
            this.setPreferredSize(new java.awt.Dimension(600, 300));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
