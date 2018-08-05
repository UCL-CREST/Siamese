    public static void addFilesToExistingZip(final File zipFile, final List<PatchEntry> entries, final List<String> delete) throws IOException {
        File tempFile = File.createTempFile(zipFile.getName(), null);
        tempFile.delete();
        boolean renameOk = zipFile.renameTo(tempFile);
        if (!renameOk) {
            throw new RuntimeException("could not rename the file " + zipFile.getAbsolutePath() + " to " + tempFile.getAbsolutePath());
        }
        byte[] buf = new byte[1024];
        ZipInputStream zin = new ZipInputStream(new FileInputStream(tempFile));
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));
        ZipEntry entry = zin.getNextEntry();
        int unchangedCount = 0;
        int deletedCount = 0;
        while (entry != null) {
            String name = entry.getName();
            boolean notInFiles = true;
            for (PatchEntry e : entries) {
                if (e.name.equals(name)) {
                    notInFiles = false;
                    break;
                }
            }
            if (notInFiles && delete.contains(name)) {
                deletedCount++;
                log(String.format("Deleting %s (%s of %s)", name, deletedCount, delete.size()));
                notInFiles = false;
            }
            if (notInFiles) {
                unchangedCount++;
                gui.setProgress(String.format("Copying unchanged files (%s so far)...", unchangedCount));
                out.putNextEntry(new ZipEntry(name));
                int len;
                while ((len = zin.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
            entry = zin.getNextEntry();
        }
        zin.close();
        int i = 0;
        for (PatchEntry e : entries) {
            log(String.format("Patching %s (%s of %s)", e.name, i + 1, entries.size()), i, entries.size());
            InputStream in = e.inStream;
            out.putNextEntry(new ZipEntry(e.name));
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.closeEntry();
            in.close();
            i++;
        }
        out.close();
        tempFile.delete();
    }
