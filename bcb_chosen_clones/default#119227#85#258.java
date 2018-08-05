    public ErrorHandler(Exception ex) {
        final ErrorHandler stream = this;
        if (errorFrame == null) {
            fc2 = new JFileChooser(new File(System.getProperty("user.dir")));
            StackTraceElement[] elements = ex.getStackTrace();
            for (int i = 0; i < elements.length; i++) {
                stackTrace.append(elements[i]).append("\n");
            }
            GridBagConstraints c = new GridBagConstraints();
            errorConsole = new JPanel(new GridBagLayout());
            errorConsole.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            clicker.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            buttons.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            errorFrame = new JFrame() {

                protected void processWindowEvent(WindowEvent e) {
                    super.processWindowEvent(e);
                    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
                        this.setVisible(false);
                        resetAll();
                    }
                }
            };
            errorFrame.addWindowFocusListener(new WindowFocusListener() {

                public void windowGainedFocus(WindowEvent e) {
                }

                public void windowLostFocus(WindowEvent e) {
                    if (errorFrame != null && modal) errorFrame.toFront();
                }
            });
            errorFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            outputArea = new JTextArea(15, 40);
            JMenuBar menuBar = new JMenuBar();
            menu.setEnabled(false);
            errorFrame.setTitle("Fehlerausgabe");
            if (stackTrace.length() > 0) {
                outputArea.setText(stackTrace.toString());
            } else {
                outputArea.setText(ex.toString());
            }
            JMenuItem item1 = new JMenuItem("Alles markieren");
            JMenuItem item2 = new JMenuItem("Speichern unter");
            JMenuItem item3 = new JMenuItem("Schließen");
            JMenuItem item4 = new JMenuItem("Alles kopieren");
            menu.add(item1);
            menu.add(item4);
            menu.add(item2);
            menu.addSeparator();
            menu.add(item3);
            menuBar.add(menu);
            item1.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    outputArea.setSelectionStart(0);
                    outputArea.setSelectionEnd(outputArea.getText().length());
                }
            });
            item2.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    if (fc2.showSaveDialog(errorConsole) != JFileChooser.CANCEL_OPTION) {
                        File file = fc2.getSelectedFile();
                        if (file != null) {
                            try {
                                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                                writer.write(outputArea.getText());
                                writer.close();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            });
            item3.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    errorFrame.dispose();
                    resetAll();
                }
            });
            item4.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    outputArea.setSelectionStart(0);
                    outputArea.setSelectionEnd(outputArea.getText().length());
                    StringSelection ss = new StringSelection(outputArea.getSelectedText());
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
                }
            });
            errorFrame.setJMenuBar(menuBar);
            click.addMouseListener(new MouseAdapter() {

                public void mouseClicked(MouseEvent e) {
                    if (status == false) {
                        errorFrame.getContentPane().add(errorConsole, java.awt.BorderLayout.SOUTH);
                        buttons.add(cancel, BorderLayout.WEST);
                        clicker.remove(cancel);
                        ((JButton) e.getSource()).setText("<<< Details ausblenden");
                        menu.setEnabled(true);
                        status = true;
                    } else {
                        errorFrame.getContentPane().remove(errorConsole);
                        buttons.remove(cancel);
                        clicker.add(cancel, BorderLayout.WEST);
                        ((JButton) e.getSource()).setText("Details anzeigen >>>");
                        menu.setEnabled(false);
                        status = false;
                    }
                    errorFrame.pack();
                    Dimension size = errorFrame.getSize();
                    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
                    errorFrame.setLocation(screen.width / 2 - size.width / 2, screen.height / 2 - size.height / 2);
                }
            });
            send.addMouseListener(new MouseAdapter() {

                public void mouseClicked(MouseEvent e) {
                    submitBug(name.getText(), mail.getText(), infoArea.getText(), stackTrace);
                }
            });
            info.addMouseListener(new MouseAdapter() {

                public void mouseClicked(MouseEvent e) {
                    modal = false;
                    showInfoWindow(e);
                }
            });
            clicker.add(click, java.awt.BorderLayout.EAST);
            errorFrame.getContentPane().setLayout(new java.awt.BorderLayout());
            JTextArea error = new JTextArea(5, 40);
            error.setEditable(false);
            error.setBackground(new Color(204, 204, 204));
            error.setFont(new Font("Times", Font.BOLD, 12));
            error.setText(ex.getLocalizedMessage());
            JScrollPane errorpane = new JScrollPane(error);
            errorpane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black), BorderFactory.createEmptyBorder(5, 5, 5, 5))));
            errorFrame.getContentPane().add(errorpane, BorderLayout.NORTH);
            errorFrame.getContentPane().add(clicker, java.awt.BorderLayout.CENTER);
            infoArea = new JTextArea("", 5, 30);
            JScrollPane infoPane = new JScrollPane(infoArea);
            outputArea.setEditable(false);
            JScrollPane pane = new JScrollPane(outputArea);
            c.gridx = 0;
            c.gridy = 0;
            c.fill = GridBagConstraints.BOTH;
            errorConsole.add(pane, c);
            JPanel textpanel = new JPanel(new GridLayout(3, 2));
            textpanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            textpanel.add(new JLabel("Dein Name: "));
            textpanel.add(name);
            textpanel.add(new JLabel("Deine Emailadresse: "));
            textpanel.add(mail);
            textpanel.add(new JLabel("Beschreibung: "));
            textpanel.add(new JLabel(""));
            c.gridy = 1;
            errorConsole.add(textpanel, c);
            c.gridy = 2;
            errorConsole.add(infoPane, c);
            info.setIcon(IconUtils.getGeneralIcon("About", 16));
            buttons.add(send, BorderLayout.EAST);
            buttons.add(info, BorderLayout.CENTER);
            clicker.add(cancel, BorderLayout.WEST);
            c.gridy = 3;
            errorConsole.add(buttons, c);
            errorFrame.pack();
            Dimension size = errorFrame.getSize();
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            errorFrame.setLocation(screen.width / 2 - size.width / 2, screen.height / 2 - size.height / 2);
            errorFrame.show();
        }
    }
