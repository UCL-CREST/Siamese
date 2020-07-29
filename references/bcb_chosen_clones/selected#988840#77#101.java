    public void pkgResources(Set<String> res, String fileName) throws IOException {
        File f = new File(fileName);
        if (!f.exists()) {
            f.createNewFile();
        }
        byte[] buf = new byte[1024];
        JarOutputStream out = new JarOutputStream(new FileOutputStream(fileName));
        for (String s : res) {
            s = s.replace('.', '/') + ".class";
            if (debugEnabled) {
                System.out.println("adding: " + s);
            }
            InputStream in = this.getClass().getClassLoader().getResourceAsStream(s);
            if (in == null) continue;
            out.putNextEntry(new ZipEntry(s));
            int w = in.read(buf);
            while (w >= 0) {
                out.write(buf, 0, w);
                w = in.read(buf);
            }
            in.close();
        }
        out.finish();
        out.close();
    }
