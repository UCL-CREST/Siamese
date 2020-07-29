    private void addRunsolver(ZipOutputStream zos) throws IOException, ClientBinaryNotFoundException {
        String[] files = new String[] { "runsolver", "runsolver_copyright.txt" };
        for (String filename : files) {
            InputStream in = new FileInputStream(new File(Util.getPath() + System.getProperty("file.separator") + "bin" + System.getProperty("file.separator") + filename));
            if (in == null) {
                throw new ClientBinaryNotFoundException();
            }
            ZipEntry entry = new ZipEntry(filename);
            zos.putNextEntry(entry);
            byte[] buf = new byte[1024];
            int data;
            while ((data = in.read(buf)) > -1) {
                zos.write(buf, 0, data);
            }
            zos.closeEntry();
            in.close();
        }
    }
