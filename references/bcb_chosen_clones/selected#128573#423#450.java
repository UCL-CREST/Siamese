    public static long packJar(File inDir, File outJar) throws IOException {
        FileOutputStream fos = new FileOutputStream(outJar);
        JarOutputStream jos = new JarOutputStream(fos);
        long count = 0;
        Set<File> files = enumFiles(inDir);
        for (File f : files) {
            File fRel = makeRelativeTo(f, inDir);
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(f);
                jos.putNextEntry(new ZipEntry(fRel.getPath().replace(File.separatorChar, '/')));
                byte[] buffer = new byte[1024];
                int bytesRead;
                while (-1 != (bytesRead = fis.read(buffer))) {
                    jos.write(buffer, 0, bytesRead);
                }
                ++count;
            } finally {
                if (fis != null) {
                    fis.close();
                    fis = null;
                }
            }
        }
        jos.close();
        fos.close();
        return count;
    }
