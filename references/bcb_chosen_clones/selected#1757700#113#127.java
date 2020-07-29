    private void writeFile(ZipOutputStream out, File file, String path) throws IOException {
        ZipEntry entry = new ZipEntry(path);
        entry.setTime(file.lastModified());
        entry.setSize(file.length());
        out.putNextEntry(entry);
        if (cvsRoot != null && path.endsWith("/CVS/Root")) {
            out.write(cvsRoot.getBytes());
        } else {
            InputStream in = new FileInputStream(file);
            int bytesRead;
            while ((bytesRead = in.read(BUF)) != -1) out.write(BUF, 0, bytesRead);
            in.close();
        }
        out.closeEntry();
    }
