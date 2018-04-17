    private void addFilesToExistingZip(File zipFile, String[] dirs, File[] files) throws IOException {
        File tempFile = new File(zipFile.getAbsoluteFile() + ".temp");
        if (tempFile.exists()) tempFile.delete();
        boolean renameOk = zipFile.renameTo(tempFile);
        if (!renameOk) {
            throw new RuntimeException("Could not rename the file " + zipFile.getAbsolutePath() + " to " + tempFile.getAbsolutePath());
        }
        ZipInputStream zin = new ZipInputStream(new FileInputStream(tempFile));
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));
        ZipEntry entry = zin.getNextEntry();
        byte[] buf = new byte[1024];
        while (entry != null) {
            String entryName = entry.getName();
            boolean inFiles = false;
            for (int i = 0; i < files.length; i++) {
                String fileName = dirs[i] + "/" + files[i].getName();
                if (fileName.equals(entryName)) {
                    inFiles = true;
                    break;
                }
            }
            if (!inFiles) {
                out.putNextEntry(new ZipEntry(entryName));
                int len;
                while ((len = zin.read(buf)) > 0) out.write(buf, 0, len);
            }
            entry = zin.getNextEntry();
        }
        zin.close();
        for (int i = 0; i < files.length; i++) {
            InputStream in = new FileInputStream(files[i]);
            out.putNextEntry(new ZipEntry(dirs[i] + "/" + files[i].getName()));
            int len;
            while ((len = in.read(buf)) > 0) out.write(buf, 0, len);
            out.closeEntry();
            in.close();
        }
        out.close();
        tempFile.delete();
    }
