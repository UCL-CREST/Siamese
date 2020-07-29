        public void actionPerformed(ActionEvent ev) {
            Object src = ev.getSource();
            if (src == butConfirm) {
                boolean computeMD5, computeSHA1;
                computeMD5 = dialMD5.activeOption();
                computeSHA1 = dialSHA1.activeOption();
                JFileChooser fc = new JFileChooser();
                fc.setMultiSelectionEnabled(false);
                if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    int i, j;
                    String inFiles[];
                    JoinParts task;
                    j = listModel.getSize();
                    inFiles = new String[j];
                    for (i = 0; i < j; ++i) inFiles[i] = (String) listModel.elementAt(i);
                    showProgress("Joining file pieces...");
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    task = new JoinParts(inFiles, fc.getSelectedFile(), computeMD5, computeSHA1);
                    task.addPropertyChangeListener(owner);
                    task.execute();
                } else {
                    JOptionPane.showMessageDialog(null, "Join files :\naction cancelled by user", "JoinSplit", JOptionPane.INFORMATION_MESSAGE);
                }
                setVisible(false);
            } else if (src == butCancel) {
                setVisible(false);
            } else if (src == butAdd) {
                JFileChooser fc = new JFileChooser();
                fc.setMultiSelectionEnabled(true);
                if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File[] extraFiles = fc.getSelectedFiles();
                    for (int i = 0; i < extraFiles.length; ++i) listModel.addElement(extraFiles[i].getPath());
                }
            } else if (src == butRemove) {
                int removeInd[] = listfiles.getSelectedIndices();
                for (int ind = removeInd.length - 1; ind >= 0; --ind) listModel.remove(removeInd[ind]);
            } else if (src == butUp) {
                Object swaptemp;
                int upInd[] = listfiles.getSelectedIndices();
                if ((upInd.length > 0) && (upInd[0] > 0)) {
                    for (int ind = 0; ind < upInd.length; ++ind) {
                        swaptemp = listModel.elementAt(upInd[ind] - 1);
                        listModel.setElementAt(listModel.elementAt(upInd[ind]), upInd[ind] - 1);
                        listModel.setElementAt(swaptemp, upInd[ind]);
                        upInd[ind]--;
                    }
                }
                listfiles.setSelectedIndices(upInd);
            } else if (src == butDown) {
                Object swaptemp;
                int downInd[] = listfiles.getSelectedIndices();
                if ((downInd.length > 0) && (downInd[downInd.length - 1] < listModel.getSize() - 1)) {
                    for (int ind = downInd.length - 1; ind >= 0; --ind) {
                        swaptemp = listModel.elementAt(downInd[ind] + 1);
                        listModel.setElementAt(listModel.elementAt(downInd[ind]), downInd[ind] + 1);
                        listModel.setElementAt(swaptemp, downInd[ind]);
                        downInd[ind]++;
                    }
                }
                listfiles.setSelectedIndices(downInd);
            }
        }
