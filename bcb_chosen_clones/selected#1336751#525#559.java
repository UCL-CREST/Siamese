    private void addAllUploadedFiles(Environment en, ZipOutputStream zipout, int progressStart, int progressLength) throws IOException, FileNotFoundException {
        File uploadPath = en.uploadPath(virtualWiki, "");
        String[] files = uploadPath.list();
        int bytesRead = 0;
        byte byteArray[] = new byte[4096];
        for (int i = 0; i < files.length; i++) {
            String fileName = files[i];
            File file = en.uploadPath(virtualWiki, fileName);
            if (file.isDirectory()) {
                continue;
            }
            progress = Math.min(progressStart + (int) ((double) i * (double) progressLength / files.length), 99);
            logger.fine("Adding uploaded file " + fileName);
            ZipEntry entry = new ZipEntry(safename(fileName));
            try {
                FileInputStream in = new FileInputStream(file);
                zipout.putNextEntry(entry);
                while (in.available() > 0) {
                    bytesRead = in.read(byteArray, 0, Math.min(4096, in.available()));
                    zipout.write(byteArray, 0, bytesRead);
                }
                zipout.closeEntry();
                zipout.flush();
            } catch (FileNotFoundException e) {
                logger.log(Level.WARNING, "Could not open file!", e);
            } catch (IOException e) {
                logger.log(Level.WARNING, "IOException!", e);
                try {
                    zipout.closeEntry();
                    zipout.flush();
                } catch (IOException e1) {
                }
            }
        }
    }
