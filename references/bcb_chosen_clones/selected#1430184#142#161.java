    protected void archiveAndDeleteContents(String pathRoot, File root, FilenameFilter filter, ZipOutputStream zos) throws IOException {
        byte[] buffer = new byte[4096];
        for (File file : root.listFiles(filter)) if (file.isDirectory()) {
            archiveAndDeleteContents(pathRoot, file, filter, zos);
            file.delete();
        } else {
            String name = file.getAbsolutePath();
            name = name.substring(pathRoot.length());
            ZipEntry entry = new ZipEntry(name);
            if (name.toLowerCase().endsWith(".zip")) entry.setMethod(ZipEntry.STORED); else entry.setMethod(ZipEntry.DEFLATED);
            entry.setTime(file.lastModified());
            zos.putNextEntry(entry);
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file), 4096);
            int len = 0;
            while ((len = bis.read(buffer)) != -1) zos.write(buffer, 0, len);
            bis.close();
            zos.closeEntry();
            file.delete();
        }
    }
