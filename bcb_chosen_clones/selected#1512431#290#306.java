    private void addExistingFileToZip(ZipOutputStream out, String entryName, String filePath) {
        if (filePath != null) {
            byte[] buf = new byte[1024];
            try {
                BufferedReader br = new BufferedReader(new FileReader(filePath));
                out.putNextEntry(new ZipEntry(entryName));
                int data;
                while ((data = br.read()) != -1) {
                    out.write(data);
                }
                out.closeEntry();
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
