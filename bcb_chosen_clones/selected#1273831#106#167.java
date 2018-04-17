    private void produceOutputInFile(final Map<String, Collection<File>> classToJarsMap, Collection<File> conflictingJars) throws IOException {
        Log.log("produceOutputInFile");
        File tempFile = File.createTempFile("cpn", ".txt");
        Log.log("Temp file " + tempFile.getAbsolutePath());
        tempFile.deleteOnExit();
        BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
        prepareOutput(bw, classToJarsMap, conflictingJars);
        bw.flush();
        bw.close();
        bw = null;
        classToJarsMap.clear();
        conflictingJars.clear();
        long size = tempFile.length();
        Log.log("File size " + size);
        if (size > THRESHOLD_BYTES) {
            long sizeM = size / (1024 * 1024);
            final String OPTION_OPEN = "Open with default application";
            final String OPTION_DONT_OPEN = "Don't open";
            final String OPTION_SAVE = "Save...";
            JOptionPane jop = new JOptionPane("Report file is huge, about " + sizeM + "M", JOptionPane.WARNING_MESSAGE, 0, null, new String[] { OPTION_OPEN, OPTION_DONT_OPEN, OPTION_SAVE }, OPTION_OPEN);
            JDialog dlg = jop.createDialog(sd, "Warning");
            dlg.setVisible(true);
            Object selectedValue = jop.getValue();
            if (selectedValue != null) {
                if (OPTION_OPEN.equals(selectedValue)) {
                    if (Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().open(tempFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (OPTION_SAVE.equals(selectedValue)) {
                    PersistentFileChooser fc = PersistentFileChooser.getFC(PersistentFileChooser.PREFIX_CONFLICT_FINDER_REPORT);
                    fc.setDialogType(JFileChooser.SAVE_DIALOG);
                    fc.setDialogTitle("Save report");
                    fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                    fc.setMultiSelectionEnabled(false);
                    int returnVal = fc.showOpenDialog(sd);
                    fc.save();
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File targetFile = fc.getSelectedFile();
                        tempFile.renameTo(targetFile);
                    } else {
                        tempFile.delete();
                    }
                }
            }
        } else {
            StringBuilder sbld = new StringBuilder((int) size);
            BufferedReader br = new BufferedReader(new FileReader(tempFile));
            String line = null;
            while ((line = br.readLine()) != null) {
                sbld.append(line + "\n");
            }
            br.close();
            br = null;
            sb = sbld.toString();
            sbld = null;
            tempFile.delete();
        }
    }
