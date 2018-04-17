        public void actionPerformed(ActionEvent ev) {
            Object s = ev.getSource();
            if (s == optBox) {
                if (optBox.isSelected()) {
                    filepath.setEditable(true);
                    butFile.setEnabled(true);
                } else {
                    filepath.setEditable(false);
                    butFile.setEnabled(false);
                }
            } else if (s == butFile) {
                JFileChooser file = new JFileChooser();
                int r;
                if (isOpenFileDialog) r = file.showOpenDialog(this); else r = file.showSaveDialog(this);
                if (r == JFileChooser.APPROVE_OPTION) filepath.setText(file.getSelectedFile().getPath());
            }
        }
