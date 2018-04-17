    private boolean writeEntry(File f, JarOutputStream out, String RelativePath) {
        String en = "";
        File[] dContent;
        int i;
        String fPath;
        byte[] buffer = new byte[BUFFERSIZE];
        int bytes_read;
        try {
            if (f.isDirectory() == false) {
                BufferedInputStream in = new BufferedInputStream(new FileInputStream(f), BUFFERSIZE);
                fPath = f.getPath().substring(f.getPath().lastIndexOf(FILESEPARATOR));
                en = RelativePath + fPath;
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
                    writeEntry(dContent[a], out, RelativePath);
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
