            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int rval = chooser.showOpenDialog(((JComponent) e.getSource()).getTopLevelAncestor());
                if (rval == JFileChooser.APPROVE_OPTION) {
                    rootModel.addElement(chooser.getSelectedFile().getAbsolutePath());
                    refreshFileListPreview();
                }
            }
