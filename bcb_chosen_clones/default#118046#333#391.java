    private void loadProjectMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(this);
        String path = "c:\\";
        String fileName = "TestSave";
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            path = fc.getSelectedFile().getParent();
            fileName = fc.getSelectedFile().getName();
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
        } else {
            return;
        }
        try {
            int BUFFER = 2048;
            String inFileName = path + fileName + ".zip";
            String destinationDirectory = path;
            File sourceZipFile = new File(inFileName);
            File unzipDestinationDirectory = new File(destinationDirectory);
            ZipFile zipFile = new ZipFile(sourceZipFile, ZipFile.OPEN_READ);
            Enumeration zipFileEntries = zipFile.entries();
            while (zipFileEntries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
                String currentEntry = entry.getName();
                File destFile = new File(unzipDestinationDirectory, currentEntry);
                File destinationParent = destFile.getParentFile();
                destinationParent.mkdirs();
                if (!entry.isDirectory()) {
                    BufferedInputStream is = new BufferedInputStream(zipFile.getInputStream(entry));
                    int currentByte;
                    byte data[] = new byte[BUFFER];
                    FileOutputStream fos = new FileOutputStream(destFile);
                    BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);
                    while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, currentByte);
                    }
                    dest.flush();
                    dest.close();
                    is.close();
                }
            }
            zipFile.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        try {
            File originalFile = new File(path + fileName + "-original.jpg");
            File mapFile = new File(path + fileName + "-Map.jpg");
            File markedFile = new File(path + fileName + "-Marked.jpg");
            if (im == null) im = new ImageMatrix(1, 1, 1);
            im.initializeFromImage(ImageIO.read(originalFile));
            colorMapImage = ImageIO.read(mapFile);
            originalLabel.setIcon(new ImageIcon(ImageIO.read(markedFile)));
            originalFile.delete();
            mapFile.delete();
            markedFile.delete();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
