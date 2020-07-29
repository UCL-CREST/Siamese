    private void showResultFrame() {
        resultFrame = new JFrame() {

            protected void processWindowEvent(WindowEvent e) {
                super.processWindowEvent(e);
                if (e.getID() == WindowEvent.WINDOW_CLOSING) {
                    this.setVisible(false);
                    this.dispose();
                    resultFrame = null;
                }
            }
        };
        resultFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Datei");
        resultFrame.setTitle("Importausgabe");
        JScrollPane pane = new JScrollPane(resultArea);
        resultArea.setText("");
        JMenuItem item1 = new JMenuItem("Alles markieren");
        JMenuItem item2 = new JMenuItem("Speichern unter");
        JMenuItem item3 = new JMenuItem("Schliessen");
        JMenuItem item4 = new JMenuItem("Alles kopieren");
        menu.add(item1);
        menu.add(item4);
        menu.add(item2);
        menu.addSeparator();
        menu.add(item3);
        menuBar.add(menu);
        item1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                resultArea.setSelectionStart(0);
                resultArea.setSelectionEnd(resultArea.getText().length());
            }
        });
        item2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                if (fc2.showSaveDialog(resultFrame) != JFileChooser.CANCEL_OPTION) {
                    File file = fc2.getSelectedFile();
                    if (file != null) {
                        try {
                            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                            writer.write(resultArea.getText());
                            writer.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            new ErrorHandler(ex);
                        }
                    }
                }
            }
        });
        item3.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                resultFrame.dispose();
            }
        });
        item4.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                resultArea.setSelectionStart(0);
                resultArea.setSelectionEnd(resultArea.getText().length());
                StringSelection ss = new StringSelection(resultArea.getSelectedText());
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
            }
        });
        resultFrame.setJMenuBar(menuBar);
        resultFrame.getContentPane().add(pane);
        resultFrame.pack();
        Dimension size = resultFrame.getSize();
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        resultFrame.setLocation(screen.width / 2 - size.width / 2, screen.height / 2 - size.height / 2);
        resultFrame.show();
        createOutput();
    }
