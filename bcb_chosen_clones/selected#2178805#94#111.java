    private static void zip(File d) throws FileNotFoundException, IOException {
        String[] entries = d.list();
        byte[] buffer = new byte[4096];
        int bytesRead;
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(new File(d.getParent() + File.separator + "dist.zip")));
        for (int i = 0; i < entries.length; i++) {
            File f = new File(d, entries[i]);
            if (f.isDirectory()) continue;
            FileInputStream in = new FileInputStream(f);
            int skipl = d.getCanonicalPath().length();
            ZipEntry entry = new ZipEntry(f.getPath().substring(skipl));
            out.putNextEntry(entry);
            while ((bytesRead = in.read(buffer)) != -1) out.write(buffer, 0, bytesRead);
            in.close();
        }
        out.close();
        FileUtils.moveFile(new File(d.getParent() + File.separator + "dist.zip"), new File(d + File.separator + "dist.zip"));
    }
