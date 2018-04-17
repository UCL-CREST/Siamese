    private final void savePrefs(File sp_dir, File f) {
        try {
            if (f.exists()) f.delete();
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(f));
            for (int i = 0; i < prefFileNames.length; i++) {
                InputStream is = null;
                try {
                    is = new FileInputStream(new File(sp_dir, prefFileNames[i]));
                } catch (Exception e) {
                }
                if (is != null) {
                    zos.putNextEntry(new ZipEntry(prefFileNames[i]));
                    Utils.copyBytes(is, zos);
                    is.close();
                    zos.closeEntry();
                }
            }
            zos.close();
            showMessage(getString(R.string.prefs_saved));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
