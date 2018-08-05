    private static void zip(File temp, File zip) {
        try {
            zip.delete();
            zip.createNewFile();
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zip));
            FileInputStream fis = new FileInputStream(temp);
            byte[] data = new byte[fis.available()];
            fis.read(data);
            ZipEntry entry = new ZipEntry("bdd.sql");
            zos.putNextEntry(entry);
            zos.write(data);
            zos.closeEntry();
            fis.close();
            zos.close();
        } catch (IOException e) {
            JOptionPane.showConfirmDialog(editeur, "Impossible de créer le fichier.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
