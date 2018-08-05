    public boolean clonarFichero(String rutaFicheroOrigen, String rutaFicheroDestino) {
        System.out.println("");
        System.out.println("*********** DENTRO DE 'clonarFichero' ***********");
        boolean estado = false;
        try {
            FileInputStream entrada = new FileInputStream(rutaFicheroOrigen);
            FileOutputStream salida = new FileOutputStream(rutaFicheroDestino);
            FileChannel canalOrigen = entrada.getChannel();
            FileChannel canalDestino = salida.getChannel();
            canalOrigen.transferTo(0, canalOrigen.size(), canalDestino);
            entrada.close();
            salida.close();
            estado = true;
        } catch (IOException e) {
            System.out.println("No se encontro el archivo");
            e.printStackTrace();
            estado = false;
        }
        return estado;
    }
