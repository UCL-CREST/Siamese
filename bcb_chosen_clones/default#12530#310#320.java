            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                try {
                    chooser.setCurrentDirectory(new File(new File(".").getCanonicalPath()));
                } catch (IOException ioe) {
                }
                int returnVal = chooser.showOpenDialog(frame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    openFile(chooser.getSelectedFile());
                }
            }
