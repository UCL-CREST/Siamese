    public boolean clonarFichero(FileInputStream rutaFicheroOrigen, String rutaFicheroDestino) {
        System.out.println("");
        boolean estado = false;
        try {
            FileOutputStream salida = new FileOutputStream(rutaFicheroDestino);
            FileChannel canalOrigen = rutaFicheroOrigen.getChannel();
            FileChannel canalDestino = salida.getChannel();
            canalOrigen.transferTo(0, canalOrigen.size(), canalDestino);
            rutaFicheroOrigen.close();
            salida.close();
            estado = true;
        } catch (IOException e) {
            System.out.println("No se encontro el archivo");
            e.printStackTrace();
            estado = false;
        }
        return estado;
    }
