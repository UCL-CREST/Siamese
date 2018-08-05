    public static void copia(File nombreFuente, File nombreDestino) throws IOException {
        FileInputStream fis = new FileInputStream(nombreFuente);
        FileOutputStream fos = new FileOutputStream(nombreDestino);
        FileChannel canalFuente = fis.getChannel();
        FileChannel canalDestino = fos.getChannel();
        canalFuente.transferTo(0, canalFuente.size(), canalDestino);
        fis.close();
        fos.close();
    }
