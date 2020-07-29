    public boolean comprimirFicheros(String[] archivoZipear, String rutaArchivoComprimido) {
        System.out.println("**** DENTRO DE 'comprimirFicheros' ****");
        boolean estado = false;
        try {
            FileInputStream entradaFile = null;
            BufferedInputStream entradaBuffer = null;
            File archivo = new File(rutaArchivoComprimido);
            FileOutputStream salidaFile = new FileOutputStream(archivo);
            ZipOutputStream ZIP = new ZipOutputStream(salidaFile);
            byte[] datos = new byte[TAMANO_BUFFER];
            for (int i = 0; i < archivoZipear.length; i++) {
                String nombreArchivo = archivoZipear[i];
                System.out.println("Archivos: " + nombreArchivo);
                entradaFile = new FileInputStream(nombreArchivo);
                entradaBuffer = new BufferedInputStream(entradaFile, TAMANO_BUFFER);
                ZipEntry entradaZip = new ZipEntry(archivoZipear[i]);
                ZIP.putNextEntry(entradaZip);
                int bytes;
                while ((bytes = entradaBuffer.read(datos)) > 0) {
                    ZIP.write(datos, 0, bytes);
                    estado = true;
                }
                ZIP.closeEntry();
                entradaFile.close();
            }
            ZIP.close();
        } catch (IOException e) {
            e.printStackTrace();
            estado = false;
        } catch (Exception e) {
            e.printStackTrace();
            estado = false;
        }
        return estado;
    }
