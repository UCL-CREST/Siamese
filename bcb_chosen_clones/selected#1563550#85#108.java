    private static void zipDir(final File zipDir, final ZipOutputStream zos, final String path) throws IOException {
        final String[] dirList = zipDir.list();
        final byte[] readBuffer = new byte[chunk];
        int bytesIn = 0;
        for (int i = 0; i < dirList.length; i++) {
            final File file = new File(zipDir, dirList[i]);
            final String filePath = ((path != null && path.length() > 0) ? (path + File.separator) : "") + file.getName();
            if (file.isDirectory()) {
                zipDir(file, zos, filePath);
                continue;
            }
            final FileInputStream fis = new FileInputStream(file);
            try {
                final ZipEntry anEntry = new ZipEntry(filePath);
                zos.putNextEntry(anEntry);
                while ((bytesIn = fis.read(readBuffer)) != -1) {
                    zos.write(readBuffer, 0, bytesIn);
                }
                zos.flush();
            } finally {
                fis.close();
            }
        }
    }
