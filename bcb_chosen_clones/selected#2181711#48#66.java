    private void addFileToZip(File file, EntryConfigurationListener callback) throws IOException {
        String abs = file.getAbsolutePath();
        abs = abs.substring(startFromLen + 1);
        ZipEntry entry = new ZipEntry(abs);
        if (callback != null) callback.configure(entry);
        output.putNextEntry(entry);
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis, 2048);
            try {
                int readIn;
                while ((readIn = bis.read(buff)) > 0) output.write(buff, 0, readIn);
            } finally {
                bis.close();
            }
        } finally {
            output.closeEntry();
        }
    }
