    private void addAllUploadedFiles(ZipOutputStream zipout, int progressStart, int progressLength) throws IOException, FileNotFoundException {
        File uploadPath = Utilities.uploadPath(virtualWiki, "");
        String[] files = uploadPath.list();
        int bytesRead = 0;
        byte byteArray[] = new byte[4096];
        for (int i = 0; i < files.length; i++) {
            progress = Math.min(progressStart + (int) ((double) i * (double) progressLength / (double) files.length), 99);
            logger.debug("Adding uploaded file " + files[i]);
            ZipEntry entry = new ZipEntry(files[i]);
            try {
                FileInputStream in = new FileInputStream(Utilities.uploadPath(virtualWiki, files[i]));
                zipout.putNextEntry(entry);
                while (in.available() > 0) {
                    bytesRead = in.read(byteArray, 0, Math.min(4096, in.available()));
                    zipout.write(byteArray, 0, bytesRead);
                }
                zipout.closeEntry();
                zipout.flush();
            } catch (FileNotFoundException e) {
                logger.warn("Could not open file!", e);
            } catch (IOException e) {
                logger.warn("IOException!", e);
                try {
                    zipout.closeEntry();
                    zipout.flush();
                } catch (IOException e1) {
                    ;
                }
            }
        }
    }
