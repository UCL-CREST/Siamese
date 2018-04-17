    public void descargarArchivo() {
        try {
            FileInputStream fis = new FileInputStream(resultados.elementAt(materialSelccionado).getRuta());
            FileOutputStream fos = new FileOutputStream(rutaDestinoDescarga);
            FileChannel inChannel = fis.getChannel();
            FileChannel outChannel = fos.getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
            fis.close();
            fos.close();
        } catch (IOException ioe) {
            System.err.println("Error al Generar Copia del Material\n" + ioe);
        }
    }
