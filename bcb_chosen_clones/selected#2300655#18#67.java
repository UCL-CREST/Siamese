    @SuppressWarnings("unchecked")
    public static void patch(String patch, String file) throws Exception {
        LinkedList<String> entries = new LinkedList<String>();
        ZipFile patch_files = new ZipFile(patch);
        Enumeration patch_entries = patch_files.entries();
        ZipFile original_files = new ZipFile(file);
        Enumeration original_entries = original_files.entries();
        FileOutputStream t = new FileOutputStream(file + ".new");
        CheckedOutputStream csum = new CheckedOutputStream(t, new Adler32());
        BufferedOutputStream bos = new BufferedOutputStream(csum);
        ZipOutputStream zipper = new ZipOutputStream(bos);
        while (patch_entries.hasMoreElements()) {
            ZipEntry ze = (ZipEntry) patch_entries.nextElement();
            entries.add(ze.getName());
            zipper.putNextEntry(ze);
            InputStream is = patch_files.getInputStream(ze);
            int i = 0;
            while (true) {
                i = is.read();
                if (i == -1) break;
                zipper.write(i);
            }
            zipper.closeEntry();
        }
        try {
            while (original_entries.hasMoreElements()) {
                ZipEntry ze = (ZipEntry) original_entries.nextElement();
                if (!entries.contains(ze.getName())) {
                    zipper.putNextEntry(ze);
                    InputStream is = original_files.getInputStream(ze);
                    int i = 0;
                    while (true) {
                        i = is.read();
                        if (i == -1) break;
                        zipper.write(i);
                    }
                    zipper.closeEntry();
                }
            }
            zipper.close();
            String temp = file + ".backup";
            File fpatch = new File(file + ".new");
            File ffile = new File(file);
            System.out.println(ffile.renameTo(new File(temp)));
            System.out.println(fpatch.renameTo(new File(file)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        zipper.close();
    }
