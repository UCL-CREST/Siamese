    private void implantInDirectory(String dir, Vector entries) throws IOException {
        File f = new File(dir + File.separatorChar + "shoehorn.tmp");
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(f));
        for (int i = 0; i < entries.size(); i++) {
            zos.putNextEntry(new ZipEntry(entries.elementAt(i).toString()));
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(entries.elementAt(i).toString());
            byte[] buf = new byte[1024];
            while (true) {
                int read = is.read(buf, 0, buf.length);
                if (read == -1) break;
                zos.write(buf, 0, read);
            }
            is.close();
        }
        zos.close();
        f.renameTo(new File(dir + File.separatorChar + "launcher.jar"));
        log("Succeeded in implanting in " + dir);
    }
