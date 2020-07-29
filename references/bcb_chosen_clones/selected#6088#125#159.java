    public void addContents(ZipOutputStream pZipFile, File pDirectory, String pDirName) throws IOException {
        File[] files = pDirectory.listFiles();
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            String name = pDirName.length() == 0 ? f.getName() : pDirName + "/" + f.getName();
            if (f.isDirectory()) {
                addContents(pZipFile, f, name);
            } else if (f.isFile()) {
                FileInputStream istream = new FileInputStream(f);
                try {
                    ZipEntry zipEntry = new ZipEntry(name);
                    pZipFile.putNextEntry(zipEntry);
                    byte[] buffer = new byte[1024];
                    for (; ; ) {
                        int res = istream.read(buffer);
                        if (res == -1) {
                            break;
                        } else if (res > 0) {
                            pZipFile.write(buffer, 0, res);
                        }
                    }
                    pZipFile.closeEntry();
                    istream.close();
                    istream = null;
                } finally {
                    if (istream != null) {
                        try {
                            istream.close();
                        } catch (Throwable ignore) {
                        }
                    }
                }
            }
        }
    }
