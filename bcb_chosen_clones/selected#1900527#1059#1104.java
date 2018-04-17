    private void jButtonSaveActionPerformed(ActionEvent evt) {
        fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setAcceptAllFileFilterUsed(false);
        int returnVal = fc.showSaveDialog(Operating.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String pathToSave = fc.getSelectedFile().getAbsolutePath();
            logger.info("Saving: ");
            comm.saveTreeToXML(TEMP_PATH + "/SK.xml", "UTF-8");
            ((RootMerkleNode) comm.getTree().root().element()).saveToXML(TEMP_PATH + "/PK.xml", "UTF-8");
            try {
                FileOutputStream dest;
                logger.info(pathToSave.substring(pathToSave.length() - 4));
                if (pathToSave.substring(pathToSave.length() - 4).equals(".zip")) dest = new FileOutputStream(pathToSave); else dest = new FileOutputStream(pathToSave + "/ZKS.zip");
                BufferedInputStream origin = null;
                CheckedOutputStream checksum = new CheckedOutputStream(dest, new Adler32());
                ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(checksum));
                out.setComment("ZKS contains:\n" + " SK.xml - the xml file that contains the ZKS Secret Key (contains all the stored elements)\n" + " PK.xml - the xml file that contains the ZKS Public Key");
                byte data[] = new byte[BUFFER];
                String files[] = { TEMP_PATH + "/SK.xml", TEMP_PATH + "/PK.xml" };
                for (int i = 0; i < files.length; i++) {
                    logger.info("Adding: " + files[i]);
                    FileInputStream fi = new FileInputStream(files[i]);
                    origin = new BufferedInputStream(fi, BUFFER);
                    String tmp[] = files[i].split("/");
                    String nameEntry = tmp[tmp.length - 1];
                    ZipEntry entry = new ZipEntry(nameEntry);
                    out.putNextEntry(entry);
                    int count;
                    while ((count = origin.read(data, 0, BUFFER)) != -1) {
                        out.write(data, 0, count);
                    }
                    origin.close();
                }
                out.close();
                jLabelGenerateStatus.setText("Files saved");
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Attenzione! Selezionare un percorso valido!", "ERRORE", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            logger.info("Save command cancelled by user.");
        }
    }
