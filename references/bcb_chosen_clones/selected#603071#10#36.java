    public static void main(String argv[]) {
        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream("./archive/mail.zip");
            CheckedOutputStream checksum = new CheckedOutputStream(dest, new Adler32());
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(checksum));
            byte data[] = new byte[BUFFER];
            File f = new File("./corrig�s");
            String files[] = f.list();
            for (int i = 0; i < files.length; i++) {
                System.out.println("Adding: " + files[i]);
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
            System.out.println("checksum: " + checksum.getChecksum().getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
