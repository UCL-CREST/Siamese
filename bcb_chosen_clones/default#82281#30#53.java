    public void zipFiles(File f) {
        try {
            if (f.isDirectory()) {
                File[] fList = f.listFiles();
                for (int i = 0; i < fList.length; i++) {
                    zipFiles(fList[i]);
                }
            } else {
                DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(f)));
                int len = dis.available();
                byte[] data = new byte[len];
                dis.readFully(data);
                dis.close();
                String filePath = f.getPath();
                String entryName = filePath.substring(sourceDir.length(), filePath.length());
                ZipEntry ze = new ZipEntry(entryName);
                zout.putNextEntry(ze);
                zout.write(data, 0, len);
                zout.closeEntry();
            }
        } catch (IOException ioe) {
            System.out.println("Exception: " + ioe + "\r\n");
        }
    }
