        public void actionPerformed(ActionEvent ev) {
            Object src = ev.getSource();
            if (src == butConfirm) {
                int i;
                long limitSize;
                boolean computeMD5, computeSHA1;
                computeMD5 = dialMD5.activeOption();
                computeSHA1 = dialSHA1.activeOption();
                try {
                    limitSize = Long.parseLong(inputPieceMaxSize.getText());
                } catch (NumberFormatException e) {
                    limitSize = -1;
                }
                if (limitSize > 0) {
                    i = sizeMultiple.getSelectedIndex();
                    while (i-- > 0) limitSize *= 1024;
                    File f = new File(filepath.getText());
                    JFileChooser fc = new JFileChooser();
                    fc.setMultiSelectionEnabled(false);
                    if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                        SplitFile task;
                        showProgress("Splitting file...");
                        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                        task = new SplitFile(f, fc.getSelectedFile().getPath(), limitSize, computeMD5, computeSHA1);
                        task.addPropertyChangeListener(owner);
                        task.execute();
                    } else {
                        JOptionPane.showMessageDialog(null, "Split file :\naction cancelled by user", "JoinSplit", JOptionPane.INFORMATION_MESSAGE);
                    }
                    setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(null, "Action aborted :\nYou must enter a strictly positive number", "JoinSplit", JOptionPane.ERROR_MESSAGE);
                }
            } else if (src == butCancel) {
                setVisible(false);
            }
        }
