    private void write(File file, ZipOutputStream out, int pos, ZipFile zip) throws IOException {
        if (file.isDirectory()) {
            File[] entries = file.listFiles();
            for (File f : entries) write(f, out, pos, zip);
        } else {
            byte[] buffer = new byte[4096];
            int bytesRead;
            FileInputStream in = null;
            try {
                in = new FileInputStream(file);
                ZipEntry entry = new ZipEntry(file.getPath().substring(pos));
                out.putNextEntry(entry);
                while ((bytesRead = in.read(buffer)) != -1) out.write(buffer, 0, bytesRead);
                out.closeEntry();
            } finally {
                Util.close(in);
            }
        }
    }
