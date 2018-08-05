    private void addDir(File dirObj, ZipOutputStream out) throws IOException {
        File[] files = dirObj.listFiles();
        byte[] tmpBuf = new byte[1024];
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                addDir(files[i], out);
                continue;
            }
            String dir = dirObj.toString();
            dir = dir.substring(dir.indexOf(File.separator) + 1);
            FileInputStream in = new FileInputStream(dirObj + File.separator + files[i].getName());
            out.putNextEntry(new ZipEntry(dir + File.separator + files[i].getName()));
            int len;
            while ((len = in.read(tmpBuf)) > 0) {
                out.write(tmpBuf, 0, len);
            }
            out.closeEntry();
            in.close();
        }
    }
