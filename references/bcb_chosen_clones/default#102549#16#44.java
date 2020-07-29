    public void getZipFiles(String filename) {
        try {
            String destinationname = "c:\\mods\\peu\\";
            byte[] buf = new byte[1024];
            ZipInputStream zipinputstream = null;
            ZipEntry zipentry;
            zipinputstream = new ZipInputStream(new FileInputStream(filename));
            zipentry = zipinputstream.getNextEntry();
            while (zipentry != null) {
                String entryName = zipentry.getName();
                System.out.println("entryname " + entryName);
                int n;
                FileOutputStream fileoutputstream;
                File newFile = new File(entryName);
                String directory = newFile.getParent();
                if (directory == null) {
                    if (newFile.isDirectory()) break;
                }
                fileoutputstream = new FileOutputStream(destinationname + entryName);
                while ((n = zipinputstream.read(buf, 0, 1024)) > -1) fileoutputstream.write(buf, 0, n);
                fileoutputstream.close();
                zipinputstream.closeEntry();
                zipentry = zipinputstream.getNextEntry();
            }
            zipinputstream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
