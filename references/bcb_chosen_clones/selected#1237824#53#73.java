    public static void writeEntry(File file, File input) throws PersistenceException {
        try {
            File temporaryFile = File.createTempFile("pmMDA_zargo", ARGOUML_EXT);
            temporaryFile.deleteOnExit();
            ZipOutputStream output = new ZipOutputStream(new FileOutputStream(temporaryFile));
            FileInputStream inputStream = new FileInputStream(input);
            ZipEntry entry = new ZipEntry(file.getName().substring(0, file.getName().indexOf(ARGOUML_EXT)) + XMI_EXT);
            output.putNextEntry(new ZipEntry(entry));
            IOUtils.copy(inputStream, output);
            output.closeEntry();
            inputStream.close();
            entry = new ZipEntry(file.getName().substring(0, file.getName().indexOf(ARGOUML_EXT)) + ".argo");
            output.putNextEntry(new ZipEntry(entry));
            output.write(ArgoWriter.getArgoContent(file.getName().substring(0, file.getName().indexOf(ARGOUML_EXT)) + XMI_EXT).getBytes());
            output.closeEntry();
            output.close();
            temporaryFile.renameTo(file);
        } catch (IOException ioe) {
            throw new PersistenceException(ioe);
        }
    }
