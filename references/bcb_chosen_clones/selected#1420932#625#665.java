    public boolean comprimirFicheros_x_algoritmo(String[] archivoZipear, String rutaArchivoComprimido, String nombreAlgoritmo) {
        System.out.println("**** DENTRO DE 'comprimirFicheros_x_algoritmo' ****");
        boolean estado = false;
        try {
            Checksum algoritmoCompresor = null;
            if (nombreAlgoritmo.equalsIgnoreCase("Adler32")) {
                algoritmoCompresor = new Adler32();
            } else if (nombreAlgoritmo.equalsIgnoreCase("CRC32")) {
                algoritmoCompresor = new CRC32();
            }
            FileInputStream entradaFile = null;
            BufferedInputStream entradaBuffer = null;
            FileOutputStream salidaFile = new FileOutputStream(rutaArchivoComprimido);
            CheckedOutputStream comprobacion = new CheckedOutputStream(salidaFile, algoritmoCompresor);
            ZipOutputStream ZIP = new ZipOutputStream(new BufferedOutputStream(comprobacion));
            System.out.println("Algoritmo Compresor: " + nombreAlgoritmo);
            byte[] datos = new byte[TAMANO_BUFFER];
            for (int i = 0; i < archivoZipear.length; i++) {
                String nombreArchivo = archivoZipear[i];
                System.out.println("Archivos: " + nombreArchivo);
                entradaFile = new FileInputStream(nombreArchivo);
                entradaBuffer = new BufferedInputStream(entradaFile, TAMANO_BUFFER);
                ZipEntry entradaZip = new ZipEntry(nombreArchivo);
                ZIP.putNextEntry(entradaZip);
                int bytes;
                while ((bytes = entradaBuffer.read(datos, 0, TAMANO_BUFFER)) != -1) {
                    ZIP.write(datos, 0, bytes);
                    estado = true;
                }
                entradaBuffer.close();
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
