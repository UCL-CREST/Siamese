    private void createJarArchive(File archiveFile, List<File> filesToBeJared, File base) throws Exception {
        FileOutputStream stream = new FileOutputStream(archiveFile);
        JarOutputStream out = new JarOutputStream(stream);
        for (File tobeJared : filesToBeJared) {
            if (tobeJared == null || !tobeJared.exists() || tobeJared.isDirectory()) continue;
            String entryName = tobeJared.getAbsolutePath().substring(base.getAbsolutePath().length() + 1).replace("\\", "/");
            JarEntry jarEntry = new JarEntry(entryName);
            jarEntry.setTime(tobeJared.lastModified());
            out.putNextEntry(jarEntry);
            FileInputStream in = new FileInputStream(tobeJared);
            IOUtils.copy(in, out);
            IOUtils.closeQuietly(in);
            out.closeEntry();
        }
        out.close();
        stream.close();
        System.out.println("Generated file: " + archiveFile);
    }
