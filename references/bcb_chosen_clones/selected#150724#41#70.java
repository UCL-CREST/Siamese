    public boolean doZipDir(String dirName, String type, boolean child, String outfile) {
        try {
            String fen = "@!#";
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(outfile);
            CheckedOutputStream checksum = new CheckedOutputStream(dest, new Adler32());
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(checksum));
            byte data[] = new byte[BUFFER];
            String file = FileUtil.getFileList(dirName, fen, type, child);
            if (StringUtil.isNULL(file)) return false;
            String files[] = StringUtil.split(file, fen);
            if (files == null || files.length < 1) return false;
            for (int i = 0; i < files.length; i++) {
                FileInputStream fi = new FileInputStream(files[i]);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(files[i]);
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
