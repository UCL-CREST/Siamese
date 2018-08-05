    private void addClient(ZipOutputStream zos, File clientBinary) throws IOException, ClientBinaryNotFoundException {
        String[] files = new String[] { "AUTHORS", clientBinary.getName(), "LICENSE", "README" };
        for (String filename : files) {
            File f = new File(clientBinary.getParentFile() + System.getProperty("file.separator") + filename);
            if (!f.exists() || f.isDirectory()) {
                continue;
            }
            InputStream in = new FileInputStream(f);
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
