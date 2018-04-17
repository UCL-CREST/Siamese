    public void createZip(List<String> listado, String zip_filename) {
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(zip_filename);
            zos = new ZipOutputStream(fos);
            zos.setLevel(9);
            Logger.getLogger("sirf.actions").debug(listado);
            Set<String> listadoSet = new HashSet<String>(listado);
            for (String pathname : listadoSet) {
                Logger.getLogger("sirf.actions").debug(pathname);
                FileInputStream fis = new FileInputStream(pathname);
                byte[] content_file = new byte[1024];
                ZipEntry ze = this.getEntry(pathname, getDirSubSistema(pathname));
                zos.putNextEntry(ze);
                while (fis.read(content_file, 0, 1024) != -1) {
                    zos.write(content_file);
                }
                zos.closeEntry();
                fis.close();
            }
            zos.flush();
            zos.close();
        } catch (FileNotFoundException e) {
            throw new ComprimirException("Fichero no encontrado: " + e.getMessage());
        } catch (IOException e) {
            throw new ComprimirException("Error de entrada/salida. El fichero puede estar en uso.");
        }
    }
