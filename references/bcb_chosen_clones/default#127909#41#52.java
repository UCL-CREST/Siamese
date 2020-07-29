            public void actionPerformed(ActionEvent event) {
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new File("."));
                chooser.setDialogTitle("Dizin Seciniz");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);
                if (chooser.showOpenDialog(selectionAna) == JFileChooser.APPROVE_OPTION) {
                    INDEXDIR = chooser.getSelectedFile().getAbsolutePath();
                    if (MainFrame.DEBUG) System.out.println(this.getClass().getName() + ": Dizin agaci icin indexDir degistiriliyor, yeni indexDir = " + INDEXDIR);
                    tree.setroot(INDEXDIR);
                }
            }
