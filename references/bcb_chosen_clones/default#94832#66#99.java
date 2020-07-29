    private void registerControllers() {
        this.exportCsvButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setSelectedFile(new File("biasviz-output.csv"));
                int retval = fc.showSaveDialog(SaveUI.this);
                if (retval == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    try {
                        if (file.exists()) {
                            int choice = JOptionPane.showConfirmDialog(SaveUI.this, "The file \"" + file.getName() + "\" already exists in that location.\n" + "Do you want to replace it with the one you are saving?", "Replace Existing File?", JOptionPane.YES_NO_OPTION);
                            if (choice != JOptionPane.YES_OPTION) {
                                return;
                            }
                        }
                        file.createNewFile();
                        BufferedWriter out = new BufferedWriter(new FileWriter(file));
                        out.write(model.getCSV());
                        out.close();
                    } catch (IOException io) {
                        System.err.println("Error writing file " + io.getMessage());
                    }
                }
            }
        });
        this.doneButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) (SaveUI.this.cards.getLayout());
                cl.previous(SaveUI.this.cards);
            }
        });
    }
