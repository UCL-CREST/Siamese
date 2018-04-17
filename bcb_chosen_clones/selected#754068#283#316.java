    protected File extractArchive(File archive) {
        byte buf[] = new byte[BUF_SIZE];
        ZipInputStream zis = null;
        File archiveDirectory = null;
        FileOutputStream fos;
        ZipEntry entry;
        File curEntry;
        int n;
        try {
            zis = new ZipInputStream(new FileInputStream(archive));
            while ((entry = zis.getNextEntry()) != null) {
                curEntry = new File(workingDir, entry.getName());
                if (entry.isDirectory()) {
                    getLogger().info("skip directory: " + entry.getName());
                    if (archiveDirectory == null) archiveDirectory = curEntry;
                    continue;
                }
                getLogger().info("zip-entry (file): " + entry.getName() + " ==> real path: " + curEntry.getAbsolutePath());
                if (!curEntry.getParentFile().exists()) curEntry.getParentFile().mkdirs();
                fos = new FileOutputStream(curEntry);
                while ((n = zis.read(buf, 0, buf.length)) > -1) fos.write(buf, 0, n);
                fos.close();
                zis.closeEntry();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                if (zis != null) zis.close();
            } catch (Throwable t) {
            }
        }
        return archiveDirectory;
    }
