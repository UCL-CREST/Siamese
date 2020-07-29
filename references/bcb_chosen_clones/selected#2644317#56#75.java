    public void outputZipPackage(String[] fileNames, byte[][] contents, OutputStream outputStream) {
        if (fileNames == null || contents == null || fileNames.length != contents.length) {
            return;
        }
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(outputStream));
        for (int i = 0; i < fileNames.length; i++) {
            ZipEntry zipEntry = new ZipEntry(fileNames[i]);
            try {
                out.putNextEntry(zipEntry);
                out.write(contents[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
