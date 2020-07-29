    public static void makeAZip(File[] files, File zipFile, FileName fn) throws Exception {
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
        byte data[] = new byte[BUFFER];
        for (int i = 0; i < files.length; i++) {
            if (files[i] != null) {
                String newName = fn.getNewName(i, files[i].getName());
                ZipEntry entry = new ZipEntry(newName);
                out.putNextEntry(entry);
                System.out.println("Adding: " + files[i] + " as " + newName);
                FileInputStream fi = new FileInputStream(files[i]);
                BufferedInputStream in = new BufferedInputStream(fi, BUFFER);
                int count;
                while ((count = in.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                out.closeEntry();
                in.close();
            }
        }
        out.close();
    }
