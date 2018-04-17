    public static void fileToZIP(File fileName, String zipFileName) {
        try {
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFileName)));
            byte[] data = new byte[1000];
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(fileName));
            int count;
            out.putNextEntry(new ZipEntry(fileName.getName()));
            while ((count = in.read(data, 0, 1000)) != -1) {
                out.write(data, 0, count);
            }
            in.close();
            out.flush();
            out.close();
            System.out.println("Your file is zipped");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
