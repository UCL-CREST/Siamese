        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            if (button.equals(recordB)) {
                record = recordB.getText().startsWith("Record");
                if (record) {
                    track = sequence.createTrack();
                    startTime = System.currentTimeMillis();
                    createShortEvent(PROGRAM, cc.col * 8 + cc.row);
                    recordB.setText("Stop");
                    playB.setEnabled(false);
                    saveB.setEnabled(false);
                } else {
                    String name = null;
                    if (instruments != null) {
                        name = instruments[cc.col * 8 + cc.row].getName();
                    } else {
                        name = Integer.toString(cc.col * 8 + cc.row);
                    }
                    tracks.add(new TrackData(cc.num + 1, name, track));
                    table.tableChanged(new TableModelEvent(dataModel));
                    recordB.setText("Record");
                    playB.setEnabled(true);
                    saveB.setEnabled(true);
                }
            } else if (button.equals(playB)) {
                if (playB.getText().startsWith("Play")) {
                    try {
                        sequencer.open();
                        sequencer.setSequence(sequence);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    sequencer.start();
                    playB.setText("Stop");
                    recordB.setEnabled(false);
                } else {
                    sequencer.stop();
                    playB.setText("Play");
                    recordB.setEnabled(true);
                }
            } else if (button.equals(saveB)) {
                try {
                    File file = new File(System.getProperty("user.dir"));
                    JFileChooser fc = new JFileChooser(file);
                    fc.setFileFilter(new javax.swing.filechooser.FileFilter() {

                        public boolean accept(File f) {
                            if (f.isDirectory()) {
                                return true;
                            }
                            return false;
                        }

                        public String getDescription() {
                            return "Save as .mid file.";
                        }
                    });
                    if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                        saveMidiFile(fc.getSelectedFile());
                    }
                } catch (SecurityException ex) {
                    JavaSound.showInfoDialog();
                    ex.printStackTrace();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
