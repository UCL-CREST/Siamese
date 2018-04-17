    private static void extract(final ZipFile zf, final ZipEntry zipEntry, final String desDir, final int... startDirLevel) throws IOException {
        File desf = new File(desDir);
        if (!desf.exists()) {
            desf.mkdirs();
        }
        int start = 1;
        if (null != startDirLevel && startDirLevel.length > 0) {
            start = startDirLevel[0];
            if (start < 1) {
                start = 1;
            }
        }
        String startDir = "";
        String zeName = zipEntry.getName();
        String folder = zeName;
        boolean isDir = zipEntry.isDirectory();
        if (null != folder) {
            String[] folders = folder.split("\\/");
            if (null != folders && folders.length > 0) {
                int len = folders.length;
                if (start == 1) {
                    startDir = zeName;
                } else {
                    if (start > len) {
                    } else {
                        for (int i = start - 1; i < len; i++) {
                            startDir += "/" + folders[i];
                        }
                        if (null != startDir) {
                            startDir = startDir.substring(1);
                        }
                    }
                }
            }
        }
        startDir = StringUtils.trim(startDir);
        if (StringUtils.isNotEmpty(startDir)) {
            StringBuilder desFileName = new StringBuilder(desDir);
            if (!desDir.endsWith("/") && !startDir.startsWith("/")) {
                desFileName.append("/");
            }
            desFileName.append(startDir);
            File destFile = new File(desFileName.toString());
            if (isDir) {
                if (!destFile.exists()) {
                    destFile.mkdirs();
                }
            } else {
                File parentDir = new File(destFile.getParentFile().getPath());
                if (!parentDir.exists()) {
                    parentDir.mkdirs();
                }
                InputStream is = zf.getInputStream(zipEntry);
                OutputStream os = new FileOutputStream(destFile);
                IOUtils.copy(is, os);
                if (null != is) {
                    is.close();
                }
                if (null != os) {
                    os.close();
                }
            }
        }
    }
