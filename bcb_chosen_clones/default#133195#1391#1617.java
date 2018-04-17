    public void actionPerformed(ActionEvent e) {
        this.TADebug.append("\n=======================================" + "\nactionPerformed(ActionEvent e)\n" + "Action event: '" + e.getActionCommand() + "'");
        if (e.getActionCommand().equalsIgnoreCase("gcclick")) {
            UpdatePanel(CBGCode.getSelectedItem(), false);
            JBAdd.setEnabled(true);
            resetColor();
        } else if (e.getActionCommand().equalsIgnoreCase("mcclick")) {
            UpdatePanel(CBMCode.getSelectedItem(), false);
            JBAdd.setEnabled(true);
            resetColor();
        } else if (e.getActionCommand().equalsIgnoreCase("exit")) {
            if (JOptionPane.showConfirmDialog(this, java.util.ResourceBundle.getBundle("MainFrameBundle").getString("ARE YOU SURE YOU WANT TO QUIT?"), java.util.ResourceBundle.getBundle("MainFrameBundle").getString("QUIT"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        } else if (e.getActionCommand().equalsIgnoreCase("add")) {
            if (checkValues()) {
                tabelle.newEnteredCode(new EnteredCode(tmpProgram, TFX.getText(), TFZ.getText(), TFF.getText(), TFH.getText(), TFComment.getText()));
                tabelle.fireTableDataChanged();
                table.changeSelection(this.previousPosition + 1, 0, false, false);
                clearPanel();
            }
        } else if (e.getActionCommand().equalsIgnoreCase("insert")) {
            this.previousPosition = this.position;
            if (checkValues()) {
                try {
                    tabelle.insertEnteredCode(new EnteredCode(tmpProgram, TFX.getText(), TFZ.getText(), TFF.getText(), TFH.getText(), TFComment.getText()), this.position);
                    tabelle.fireTableRowsInserted(position, position);
                    table.changeSelection(1, 0, false, false);
                    clearPanel();
                } catch (ArrayIndexOutOfBoundsException ex) {
                    JOptionPane.showMessageDialog(this, java.util.ResourceBundle.getBundle("MainFrameBundle").getString("NO LINE IN PROGRAM OR NOT SELECTED!"));
                    this.TADebug.append("\nTried to insert at a non-existing line");
                }
            }
        } else if (e.getActionCommand().equalsIgnoreCase("update")) {
            if (checkValues()) {
                try {
                    tabelle.updateEnteredCode(new EnteredCode(tmpProgram, TFX.getText(), TFZ.getText(), TFF.getText(), TFH.getText(), TFComment.getText()), this.position);
                    tabelle.fireTableRowsUpdated(position, position);
                    clearPanel();
                } catch (ArrayIndexOutOfBoundsException ex) {
                    JOptionPane.showMessageDialog(this, java.util.ResourceBundle.getBundle("MainFrameBundle").getString("NO LINE IN PROGRAM OR NOT SELECTED!"));
                    this.TADebug.append("\nTried to update a non-existing line");
                }
            }
        } else if (e.getActionCommand().equalsIgnoreCase("delete")) {
            try {
                tabelle.deleteEnteredCode(position);
                this.TADebug.append("\nLine deleted");
            } catch (ArrayIndexOutOfBoundsException ex) {
                JOptionPane.showMessageDialog(this, java.util.ResourceBundle.getBundle("MainFrameBundle").getString("NO LINE IN PROGRAM OR NOT SELECTED!"));
                this.TADebug.append("\nTried to delete a non-existing line");
            }
            tabelle.fireTableDataChanged();
            if (this.previousPosition > 0) {
                table.changeSelection(this.previousPosition - 1, 0, false, false);
            }
            clearPanel();
        } else if (e.getActionCommand().equalsIgnoreCase("open")) {
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                currentFile = fc.getSelectedFile();
                importProgramFile(currentFile);
                this.TADebug.append("\nLoading file " + currentFile);
                tabelle.fireTableDataChanged();
                this.setTitle(this.programVersion + " - " + currentFile);
                this.TADebug.append("\nProgram title updated to " + this.getTitle());
                this.MISave.setEnabled(true);
                this.JBDelete.setEnabled(true);
            } else {
                this.TADebug.append("\nAborted");
            }
        } else if (e.getActionCommand().equalsIgnoreCase("saveas")) {
            boolean goOn = true;
            do {
                int returnVal = fc.showSaveDialog(this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    currentFile = fc.getSelectedFile();
                    if (!currentFile.getName().toLowerCase().endsWith(".cnc")) {
                        currentFile = new File(currentFile.getAbsolutePath() + ".cnc");
                    }
                    this.TADebug.append("\nTry to save program as file " + currentFile);
                    if (currentFile.exists()) {
                        this.TADebug.append("\n" + currentFile + " already exists!");
                        if (JOptionPane.showConfirmDialog(this, java.util.ResourceBundle.getBundle("MainFrameBundle").getString("OVERWRITE EXISTING FILE ") + currentFile + "?", java.util.ResourceBundle.getBundle("MainFrameBundle").getString("SELECTED FILE ALREADY EXISTS!"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            goOn = false;
                        } else {
                            goOn = true;
                        }
                    } else {
                        goOn = false;
                    }
                    if (!goOn) {
                        try {
                            exportProgramAsFile(export(true), currentFile);
                            this.TADebug.append("\nSuccessfully saved " + currentFile);
                            this.setTitle(this.programVersion + " - " + currentFile);
                            this.MISave.setEnabled(true);
                        } catch (IOException ex) {
                            this.TADebug.append("\nIOException while writing " + currentFile);
                            JOptionPane.showMessageDialog(this, java.util.ResourceBundle.getBundle("MainFrameBundle").getString("ERROR WHILE WRITING TO ") + currentFile, ioError, JOptionPane.ERROR_MESSAGE);
                        }
                        goOn = false;
                    }
                } else {
                    this.TADebug.append("\nAborted by user");
                    goOn = false;
                }
            } while (goOn);
        } else if (e.getActionCommand().equalsIgnoreCase("new")) {
            tabelle.enteredCode.clear();
            tabelle.fireTableDataChanged();
            clearPanel();
            this.TFProgName.setText("title of program");
            this.MISave.setEnabled(false);
            this.setTitle(this.programVersion);
        } else if (e.getActionCommand().equalsIgnoreCase("metric")) {
            c.setUseMetric(true);
            UpdatePanel(tmpProgram, false);
        } else if (e.getActionCommand().equalsIgnoreCase("imperial")) {
            c.setUseMetric(false);
            UpdatePanel(tmpProgram, false);
        } else if (e.getActionCommand().equalsIgnoreCase("usedebug")) {
            c.setUseDebugOutput(this.CBDebug.isSelected());
            if (c.getUseDebugOutput()) {
                TPMain.addTab(java.util.ResourceBundle.getBundle("MainFrameBundle").getString("DEBUGGING OUTPUT"), JPDebug);
                this.pack();
            } else {
                TPMain.remove(JPDebug);
                this.pack();
            }
        } else if (e.getActionCommand().equalsIgnoreCase("defaultsettings")) {
            c = new Config();
            this.JCBBaudRate.setSelectedIndex(1);
            this.JCBSerialInterface.setSelectedIndex(0);
            this.UpdateConfigTab();
        } else if (e.getActionCommand().equalsIgnoreCase("defaultlanguage")) {
            c.setUseDefaultLanguage(this.CBLanguage.isSelected());
            this.JCBLanguage.setEnabled(!this.CBLanguage.isSelected());
        } else if (e.getActionCommand().equalsIgnoreCase("confsave")) {
            c.setCBComPortPosition(this.JCBSerialInterface.getSelectedIndex());
            c.setComPort(this.JCBSerialInterface.getItemAt(c.getCBComPortPosition()).toString());
            c.setBaudRate(Integer.parseInt(this.JCBBaudRate.getSelectedItem().toString()));
            c.setCBBaudRatePosition(this.JCBBaudRate.getSelectedIndex());
            c.setIp(this.TFIPAddress.getText());
            c.setUseDebugOutput(this.CBDebug.isSelected());
            c.setParity(this.JCBParity.getSelectedIndex());
            c.setStopBit(this.JCBStopBit.getSelectedIndex());
            c.setDataBit(this.JCBDataBits.getSelectedIndex());
            c.setFlowControl(this.JCBFlowControl.getSelectedIndex());
            try {
                c.setPort(Integer.parseInt(this.TFIPPort.getText()));
            } catch (InputMismatchException ex) {
                c.setPort(10001);
                this.TADebug.append("\nNo valid tcp port entered, set to 10001");
            }
            UpdateConfigTab();
            String errMsg = java.util.ResourceBundle.getBundle("MainFrameBundle").getString("ERROR WHILE WRITING THE CONFIG FILE: ") + configFile;
            try {
                FileOutputStream fos = new FileOutputStream(configFile);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(c);
                oos.close();
                this.TADebug.append("\nSuccessfully saved the configuration in:" + "\n" + configFile);
                this.TADebug.append("\n" + this.c);
                JOptionPane.showMessageDialog(this, java.util.ResourceBundle.getBundle("MainFrameBundle").getString("SUCCESSFULLY SAVED THE CONFIG FILE TO ") + "\n" + configFile);
            } catch (FileNotFoundException ex) {
                this.TADebug.append("\nFileNotFoundException while saving" + " the config file " + configFile);
                JOptionPane.showMessageDialog(this, errMsg, fileNotFoundError, JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                this.TADebug.append("\nIOException while saving" + " the config file " + configFile);
                JOptionPane.showMessageDialog(this, errMsg, ioError, JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                this.TADebug.append("\nUnexpected error while saving" + " the config file " + configFile);
                JOptionPane.showMessageDialog(this, errMsg, unexpectedError, JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getActionCommand().equalsIgnoreCase("configabort")) {
            this.JCBSerialInterface.setSelectedIndex(c.getCBComPortPosition());
            this.JCBBaudRate.setSelectedIndex(c.getCBBaudRatePosition());
            this.JCBParity.setSelectedIndex(c.getParity());
            this.JCBStopBit.setSelectedIndex(c.getStopBitPosition());
            this.JCBDataBits.setSelectedIndex(c.getDataBitPosition());
            this.JCBFlowControl.setSelectedIndex(c.getFlowControlPosition());
            this.TFIPAddress.setText(c.getIp());
            this.TFIPPort.setText("" + c.getPort());
            this.CBDebug.setSelected(c.getUseDebugOutput());
            UpdateConfigTab();
        } else if (e.getActionCommand().equalsIgnoreCase("about")) {
            JOptionPane.showMessageDialog(this, aboutText, java.util.ResourceBundle.getBundle("MainFrameBundle").getString("ABOUT"), JOptionPane.INFORMATION_MESSAGE);
        } else if (e.getActionCommand().equalsIgnoreCase("receive")) {
            if (this.BGTrans.getSelection().getActionCommand().equalsIgnoreCase("useeth")) {
                rn = new ReceiveNetwork(this, this.c);
                rxtx = new Thread(rn);
                rxtx.start();
                this.threadId = 1;
            } else if (this.BGTrans.getSelection().getActionCommand().equalsIgnoreCase("useserial")) {
                rs = new ReceiveSerial(this, this.c);
                rxtx = new Thread(rs);
                rxtx.start();
                this.threadId = 2;
            } else {
                JOptionPane.showMessageDialog(this, java.util.ResourceBundle.getBundle("MainFrameBundle").getString("PLEASE CHOOSE WETHER TO USE SERIAL OR ETHERNET!"));
            }
        } else if (e.getActionCommand().equalsIgnoreCase("transmit")) {
            if (this.BGTrans.getSelection().getActionCommand().equalsIgnoreCase("useeth")) {
                sn = new SendNetwork(export(false), this.c, this);
                rxtx = new Thread(sn);
                rxtx.start();
                this.threadId = 3;
            } else if (this.BGTrans.getSelection().getActionCommand().equalsIgnoreCase("useserial")) {
                ss = new SendSerial(export(true), this.c, this);
                rxtx = new Thread(ss);
                rxtx.start();
                this.threadId = 4;
            } else {
                JOptionPane.showMessageDialog(this, java.util.ResourceBundle.getBundle("MainFrameBundle").getString("PLEASE CHOOSE WETHER TO USE SERIAL OR ETHERNET!"));
            }
        } else if (e.getActionCommand().equalsIgnoreCase("setxport")) {
            c.setUseXPort(this.CBXport.isSelected());
        } else if (e.getActionCommand().equalsIgnoreCase("rxtxabort")) {
            if ((this.threadId < 5) && (this.threadId > 0)) {
                rxtxAbort();
            }
        } else {
            this.TADebug.append("\nUnknown ActionCommand!");
        }
    }
