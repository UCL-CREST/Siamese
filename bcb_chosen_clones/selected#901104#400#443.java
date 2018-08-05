    private boolean writeEntry(File f, JarOutputStream out, int depth) {
        String en = "";
        File[] dContent;
        int i;
        String fPath;
        byte[] buffer = new byte[BUFFERSIZE];
        int bytes_read;
        try {
            if (f.isDirectory() == false) {
                BufferedInputStream in = new BufferedInputStream(new FileInputStream(f), BUFFERSIZE);
                i = f.getPath().length();
                fPath = f.getPath();
                for (int a = 0; a <= depth; a++) {
                    i = fPath.lastIndexOf(FILESEPARATOR, i) - 1;
                }
                en = fPath.substring(i + 2, fPath.length());
                out.putNextEntry(new ZipEntry(en));
                while ((bytes_read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytes_read);
                    if (aborted) {
                        in.close();
                        out.closeEntry();
                        return false;
                    }
                    writtenBytes += bytes_read;
                }
                in.close();
                out.closeEntry();
                return true;
            } else {
                dContent = f.listFiles();
                for (int a = 0; a < dContent.length; a++) {
                    writeEntry(dContent[a], out, depth + 1);
                    if (aborted) {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("[writeEntry(), JarWriter] ERROR\n" + e);
            return false;
        }
        return true;
    }
