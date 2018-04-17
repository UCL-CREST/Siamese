    private void zipDir(File dir, String jarEntry, JarOutputStream jos) throws IOException {
        String[] dirList = dir.list();
        for (int i = 0; i < dirList.length; i++) {
            File f = new File(dir, dirList[i]);
            if (f.isDirectory()) {
                zipDir(f, jarEntry + dirList[i] + File.separatorChar, jos);
                continue;
            }
            FileInputStream fis = new FileInputStream(f);
            ZipEntry anEntry = new ZipEntry(jarEntry + dirList[i]);
            jos.putNextEntry(anEntry);
            byte[] readBuffer = new byte[2156];
            int bytesIn = 0;
            while ((bytesIn = fis.read(readBuffer)) != -1) {
                jos.write(readBuffer, 0, bytesIn);
            }
            fis.close();
        }
    }
