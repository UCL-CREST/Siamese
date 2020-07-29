    public static void createJarFromDirectory(String jarFileName, String jarDirectory, String[] filtertedExtensions) throws IOException {
        File directory = new File(jarDirectory);
        if (!directory.exists()) {
            return;
        }
        File jar = new File(jarFileName);
        if (!jar.exists()) {
            new File(jar.getParent()).mkdirs();
        }
        JarOutputStream jarOut = new JarOutputStream(new FileOutputStream(jar), new Manifest());
        Vector files = findFiles(jarDirectory, filtertedExtensions);
        for (int i = 0; i < files.size(); i++) {
            File file = (File) files.elementAt(i);
            String relativePathToDirectory = file.getAbsolutePath().substring(directory.getAbsolutePath().length() + 1);
            String entryName = relativePathToDirectory.replace('\\', '/');
            FileInputStream inStream = new FileInputStream(file);
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            int length = 0;
            byte[] buffer = new byte[1024];
            while ((length = inStream.read(buffer)) > 0) {
                byteStream.write(buffer, 0, length);
            }
            byte[] arr = byteStream.toByteArray();
            byteStream.close();
            JarEntry meta = new JarEntry(entryName);
            jarOut.putNextEntry(meta);
            meta.setSize(arr.length);
            meta.setCompressedSize(arr.length);
            CRC32 crc = new CRC32();
            crc.update(arr);
            meta.setCrc(crc.getValue());
            meta.setMethod(ZipEntry.STORED);
            jarOut.write(arr, 0, arr.length);
            jarOut.closeEntry();
        }
        jarOut.close();
    }
