    private void zipSingleDirectory(File dir, JarOutputStream jaros, File topLevelDir) throws IOException, IllegalArgumentException {
        byte[] buffer;
        int bytesRead;
        File[] entries = dir.listFiles();
        CRC32 crc = new CRC32();
        for (File f : entries) {
            if (f.isDirectory()) {
                if (!f.toString().equals(topLevelDir.toString())) {
                    zipSingleDirectory(f, jaros, topLevelDir);
                }
                continue;
            }
            buffer = new byte[(int) f.length()];
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
            bytesRead = bis.read(buffer);
            bis.close();
            if (bytesRead != f.length()) {
                throw new IOException("%s: failed to read all " + f.length() + " bytes.");
            }
            crc.reset();
            crc.update(buffer, 0, bytesRead);
            String jarEntryName = f.getPath().substring(topLevelDir.toString().length() + 1);
            jarEntryName = jarEntryName.replace('\\', '/');
            JarEntry entry = new JarEntry(jarEntryName);
            entry.setSize(f.length());
            entry.setCrc(crc.getValue());
            entry.setTime(f.lastModified());
            jaros.putNextEntry(entry);
            jaros.write(buffer, 0, bytesRead);
        }
    }
