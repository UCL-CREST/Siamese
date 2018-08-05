    public static void save(MainWindow mw, String filename) {
        if (filename != null) {
            try {
                BufferedInputStream origin = null;
                FileOutputStream dest;
                if (filename.endsWith(".cecco")) dest = new FileOutputStream(filename); else dest = new FileOutputStream(filename + ".cecco");
                System.out.println("Saving : " + filename);
                ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
                byte data[] = new byte[BUFFER];
                File[] files = new File[3];
                files[0] = FileContener.getOnto();
                files[1] = FileContener.getCurrentContr();
                files[2] = FileContener.getCurrentCacog();
                for (int i = 0; i < files.length; i++) {
                    System.out.println("Adding: " + files[i]);
                    FileInputStream fi = new FileInputStream(files[i]);
                    origin = new BufferedInputStream(fi, BUFFER);
                    ZipEntry entry = new ZipEntry(files[i].getName());
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
            }
        }
    }
