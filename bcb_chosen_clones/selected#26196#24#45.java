    public static File createBar(File file, String newbarfilename) {
        try {
            if (newbarfilename.toLowerCase().endsWith(".xpdl")) newbarfilename = newbarfilename.toLowerCase().replace(".xpdl", "");
            File barcreato = new File(newbarfilename + ".bar");
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(barcreato)));
            byte[] data = new byte[1000];
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(file.getCanonicalPath()));
            int count;
            out.putNextEntry(new ZipEntry(file.getName()));
            while ((count = in.read(data, 0, 1000)) != -1) {
                out.write(data, 0, count);
            }
            in.close();
            out.flush();
            out.close();
            System.out.println("BAR File successfully created!");
            return barcreato;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
