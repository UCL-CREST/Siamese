    private void jButtonSaveProofActionPerformed(ActionEvent evt) {
        fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setAcceptAllFileFilterUsed(false);
        int returnVal = fc.showSaveDialog(Operating.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String pathToSave = fc.getSelectedFile().getAbsolutePath();
            logger.info("Saving: ");
            try {
                BufferedInputStream origin = null;
                FileOutputStream dest;
                logger.info(pathToSave.substring(pathToSave.length() - 4));
                if (pathToSave.substring(pathToSave.length() - 4).equals(".zip")) dest = new FileOutputStream(pathToSave); else dest = new FileOutputStream(pathToSave + "/Proof.zip");
                CheckedOutputStream checksum = new CheckedOutputStream(dest, new Adler32());
                ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(checksum));
                out.setComment("Proof contains:\n" + " piGreek.xml - the xml file where the Prof of membership/non membership is stored\n");
                byte data[] = new byte[BUFFER];
                String files[] = { TEMP_PATH + "/piGreek.xml" };
                for (int i = 0; i < files.length; i++) {
                    logger.info("Adding: " + files[i]);
                    FileInputStream fi = new FileInputStream(files[i]);
                    origin = new BufferedInputStream(fi, BUFFER);
                    String nameEntry = files[i].substring(files[i].indexOf("/") + 1, files[i].length());
                    ZipEntry entry = new ZipEntry(nameEntry);
                    out.putNextEntry(entry);
                    int count;
                    while ((count = origin.read(data, 0, BUFFER)) != -1) {
                        out.write(data, 0, count);
                    }
                    origin.close();
                }
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            jLabelSaveResult.setText("File Saved");
        } else {
            logger.info("Save command cancelled by user.");
        }
    }
