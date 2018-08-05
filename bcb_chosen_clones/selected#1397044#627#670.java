    public static List<String> extract(String zipFilePath, String destDirPath) throws IOException {
        List<String> list = null;
        ZipFile zip = new ZipFile(zipFilePath);
        try {
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                File destFile = new File(destDirPath, entry.getName());
                if (entry.isDirectory()) {
                    destFile.mkdirs();
                } else {
                    InputStream in = zip.getInputStream(entry);
                    OutputStream out = new FileOutputStream(destFile);
                    try {
                        IOUtils.copy(in, out);
                    } finally {
                        IOUtils.closeQuietly(in);
                        IOUtils.closeQuietly(out);
                        try {
                            out.close();
                        } catch (IOException ioe) {
                            ioe.getMessage();
                        }
                        try {
                            in.close();
                        } catch (IOException ioe) {
                            ioe.getMessage();
                        }
                    }
                }
                if (list == null) {
                    list = new ArrayList<String>();
                }
                list.add(destFile.getAbsolutePath());
            }
            return list;
        } finally {
            try {
                zip.close();
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }
