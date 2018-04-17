    public static boolean zip(String[] filename, String outname) {
        boolean test = true;
        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(outname);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            byte data[] = new byte[BUFFER];
            for (int i = 0; i < filename.length; i++) {
                File file = new File(filename[i]);
                FileInputStream fi = new FileInputStream(file);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(file.getName());
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }
            out.close();
        } catch (Exception e) {
            test = false;
            e.printStackTrace();
        }
        return test;
    }
