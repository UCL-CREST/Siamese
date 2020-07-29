    private void saveDataTree(String file) {
        try {
            theDataTreePanel.prepareForSaving();
            File theFile = new File(file);
            if (file.indexOf(".zip") == -1) {
                theFile = new File(file + ".zip");
            }
            FileOutputStream fos = new FileOutputStream(theFile);
            java.util.zip.ZipOutputStream zos = new ZipOutputStream(fos);
            ZipEntry thisEntry = new ZipEntry("DATA");
            zos.putNextEntry(thisEntry);
            ObjectOutputStream oos = new ObjectOutputStream(zos);
            oos.writeObject(theDataTreePanel);
            oos.flush();
            zos.closeEntry();
            fos.close();
            theDataTreePanel.setParentFrame(this);
            sendMessage("Data saved to " + theFile.getAbsolutePath());
        } catch (IOException ee) {
            sendErrorMessage("Data could not be saved: " + ee);
        }
    }
