    private boolean extract(File archive, File dir, IProgressMonitor monitor) {
        monitor.subTask("Extract : " + archive.getName());
        if (!dir.exists()) {
            dir.mkdirs();
        } else {
            File[] files = dir.listFiles();
            for (int j = 0; j < files.length; j++) {
                files[j].delete();
            }
        }
        ZipInputStream zis = null;
        try {
            zis = new ZipInputStream(new FileInputStream(archive));
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                File indexFile = new File(dir, entry.getName());
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(indexFile);
                    IOUtils.copy(zis, fos);
                } finally {
                    IOUtils.closeQuietly(fos);
                }
            }
            return true;
        } catch (Exception ex) {
            M4EclipsePlugin.log(new Status(IStatus.ERROR, M4EclipsePlugin.PLUGIN_ID, -1, "For index '" + dir.getName() + "' Unable to initialize indexes", ex));
        } finally {
            IOUtils.closeQuietly(zis);
        }
        return false;
    }
