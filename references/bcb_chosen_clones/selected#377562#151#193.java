    private void compressJar(LinkedList<String> filenames, String audiopath, String donorFolder, String outFilename, String ctg0, String lng0) throws IOException {
        String filename = null;
        if (outFilename.indexOf(File.separator) != -1) {
            filename = outFilename.substring(outFilename.lastIndexOf(File.separator) + 1, outFilename.length());
        } else {
            filename = outFilename;
        }
        byte[] buf = new byte[1024];
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));
        try {
            Iterator it = filenames.iterator();
            while (it.hasNext()) {
                String name = (String) it.next();
                FileInputStream in = new FileInputStream(audiopath + File.separator + name);
                out.putNextEntry(new ZipEntry("sound" + File.separator + name));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            rewriteFile(donorFolder, out, "", buf);
            byte[] bytes;
            bytes = ctg0.getBytes("Cp1251");
            out.putNextEntry(new ZipEntry("ctg0.properties"));
            for (int i = 0; i < bytes.length; i++) {
                out.write(bytes[i]);
            }
            bytes = lng0.getBytes("Cp1251");
            out.putNextEntry(new ZipEntry("lng0.properties"));
            for (int i = 0; i < bytes.length; i++) {
                out.write(bytes[i]);
            }
            out.close();
        } catch (IOException e) {
            out.putNextEntry(new ZipEntry("not null"));
            out.close();
            File f = new File(outFilename);
            f.delete();
            throw new IOException();
        }
    }
