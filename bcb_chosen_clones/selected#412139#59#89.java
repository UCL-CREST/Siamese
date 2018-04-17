    public static void wzip(ZipOutputStream out, File f, String base) {
        byte data[] = new byte[BUFFER];
        BufferedInputStream origin = null;
        FileInputStream fin = null;
        String temp = null;
        int cont = 0;
        try {
            File[] fs = f.listFiles();
            for (int i = 0; i < fs.length; i++) {
                log.debug("Adding: " + fs[i]);
                temp = fs[i].getAbsolutePath();
                cont = temp.indexOf(base);
                if (fs[i].isDirectory()) {
                    temp = temp.substring(cont).replace('\\', '/') + "/";
                    out.putNextEntry(new ZipEntry(temp));
                    wzip(out, fs[i], base);
                } else {
                    fin = new FileInputStream(fs[i]);
                    origin = new BufferedInputStream(fin, BUFFER);
                    out.putNextEntry(new ZipEntry(temp.substring(cont)));
                    int count;
                    while ((count = origin.read(data, 0, BUFFER)) != -1) {
                        out.write(data, 0, count);
                    }
                    origin.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
