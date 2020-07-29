    protected void zipDirectory(File dir, File zipfile) throws IOException, IllegalArgumentException {
        if (!dir.isDirectory()) throw new IllegalArgumentException("Compress: not a directory:  " + dir);
        String[] entries = dir.list();
        byte[] buffer = new byte[4096];
        int bytes_read;
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));
        for (int i = 0; i < entries.length; i++) {
            File f = new File(dir, entries[i]);
            if (f.isDirectory()) continue;
            FileInputStream in = new FileInputStream(f);
            ZipEntry entry = new ZipEntry(f.getPath());
            out.putNextEntry(entry);
            while ((bytes_read = in.read(buffer)) != -1) out.write(buffer, 0, bytes_read);
            in.close();
        }
        out.close();
    }
