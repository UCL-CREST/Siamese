    public static void addFolderToArchive(String prefix, File folder, ZipOutputStream out) throws Exception {
        BufferedInputStream origin = null;
        String files[] = folder.list();
        byte data[] = new byte[BUFFER];
        for (int i = 0; i < files.length; i++) {
            System.out.println("Adding: " + files[i]);
            System.out.println("With prefix: " + prefix + files[i]);
            File fileToAdd = new File(folder, files[i]);
            if (fileToAdd.isFile()) {
                FileInputStream fi = new FileInputStream(fileToAdd);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(prefix + files[i]);
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            } else if (fileToAdd.isDirectory()) {
                ZipEntry entry = new ZipEntry(files[i] + "/");
                out.putNextEntry(entry);
                addFolderToArchive(prefix + files[i] + "/", fileToAdd, out);
            }
        }
    }
