    private void addAllImages(Environment en, ZipOutputStream zipout, int progressStart, int progressLength) throws IOException {
        String[] files = new File(imageDir).list();
        int bytesRead = 0;
        byte byteArray[] = new byte[4096];
        FileInputStream in = null;
        for (int i = 0; i < files.length; i++) {
            progress = Math.min(progressStart + (int) ((double) i * (double) progressLength / files.length), 99);
            File fileToHandle = new File(imageDir, files[i]);
            if (fileToHandle.isFile() && fileToHandle.canRead()) {
                try {
                    logger.fine("Adding image file " + files[i]);
                    ZipEntry entry = new ZipEntry("images/" + files[i]);
                    zipout.putNextEntry(entry);
                    in = new FileInputStream(fileToHandle);
                    while (in.available() > 0) {
                        bytesRead = in.read(byteArray, 0, Math.min(4096, in.available()));
                        zipout.write(byteArray, 0, bytesRead);
                    }
                } catch (FileNotFoundException e) {
                } catch (IOException e) {
                } finally {
                    try {
                        zipout.closeEntry();
                    } catch (IOException e1) {
                    }
                    try {
                        zipout.flush();
                    } catch (IOException e1) {
                    }
                    try {
                        if (in != null) {
                            in.close();
                            in = null;
                        }
                    } catch (IOException e1) {
                    }
                }
            }
        }
    }
