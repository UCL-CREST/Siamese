        public void actionPerformed(ActionEvent e) {
            fc.showOpenDialog(dbimport);
            File selFile = fc.getSelectedFile();
            if (selFile != null) {
                jTFimportFile.setText(selFile.toString());
            }
        }
