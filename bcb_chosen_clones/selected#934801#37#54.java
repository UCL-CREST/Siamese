    public void execute(BackUpInfoFileGroup fileGroup, ZipOutputStream zos, String name) {
        int bytesIn = 0;
        try {
            ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(name)));
            out.writeObject(fileGroup);
            out.close();
            FileInputStream fis = new FileInputStream(new File(name));
            ZipEntry anEntry = new ZipEntry(name);
            zos.putNextEntry(anEntry);
            while ((bytesIn = fis.read(BUFFER)) != -1) {
                zos.write(BUFFER, 0, bytesIn);
            }
            fis.close();
            new File(name).delete();
        } catch (IOException e) {
            throw new BackupException(e.getMessage());
        }
    }
