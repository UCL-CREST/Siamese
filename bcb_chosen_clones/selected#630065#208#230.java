    private static void outToZipStream(File argDirectory, ZipOutputStream argZipOut, String argPath) throws IOException {
        File file[] = argDirectory.listFiles();
        if (file == null) return;
        for (int i = 0; i < file.length; i++) {
            if (file[i].isDirectory()) {
                String t = argPath + file[i].getName() + "/";
                ZipEntry zipEntry = new ZipEntry(t);
                argZipOut.putNextEntry(zipEntry);
                outToZipStream(file[i], argZipOut, t);
            } else {
                ZipEntry zipEntry = new ZipEntry(argPath + file[i].getName());
                argZipOut.putNextEntry(zipEntry);
                FileInputStream fileInputStream = new FileInputStream(file[i]);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream, BUFFER);
                byte data[] = new byte[BUFFER];
                int count;
                while ((count = bufferedInputStream.read(data, 0, BUFFER)) != -1) {
                    argZipOut.write(data, 0, count);
                }
                bufferedInputStream.close();
            }
        }
    }
