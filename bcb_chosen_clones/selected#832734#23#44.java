    public void unzip(String zipFileName, String outputDirectory) throws Exception {
        ZipInputStream in = new ZipInputStream(new FileInputStream(zipFileName));
        ZipEntry z;
        while ((z = in.getNextEntry()) != null) {
            System.out.println("unziping " + z.getName());
            if (z.isDirectory()) {
                String name = z.getName();
                name = name.substring(0, name.length() - 1);
                File f = new File(outputDirectory + File.separator + name);
                f.mkdir();
                System.out.println("mkdir " + outputDirectory + File.separator + name);
            } else {
                File f = new File(outputDirectory + File.separator + z.getName());
                f.createNewFile();
                FileOutputStream out = new FileOutputStream(f);
                int b;
                while ((b = in.read()) != -1) out.write(b);
                out.close();
            }
        }
        in.close();
    }
