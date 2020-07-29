    public static void createZipArchive(File archiveFile, File[] tobeZippedFiles) throws Exception {
        try {
            byte buffer[] = new byte[BUFFER_SIZE];
            FileOutputStream stream = new FileOutputStream(archiveFile);
            ZipOutputStream out = new ZipOutputStream(stream);
            for (int i = 0; i < tobeZippedFiles.length; i++) {
                if (tobeZippedFiles[i] == null || !tobeZippedFiles[i].exists() || tobeZippedFiles[i].isDirectory()) continue;
                ZipEntry zipAdd = new ZipEntry(tobeZippedFiles[i].getName());
                zipAdd.setTime(tobeZippedFiles[i].lastModified());
                out.putNextEntry(zipAdd);
                FileInputStream in = new FileInputStream(tobeZippedFiles[i]);
                while (true) {
                    int nRead = in.read(buffer, 0, buffer.length);
                    if (nRead <= 0) break;
                    out.write(buffer, 0, nRead);
                }
                in.close();
            }
            out.flush();
            out.close();
            stream.close();
        } catch (Exception e) {
            throw e;
        }
    }
