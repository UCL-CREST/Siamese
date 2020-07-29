    public static void compactar(String arqEntrada, String arqSaida) {
        int i, cont;
        byte[] dados = new byte[TAMANHO_BUFFER];
        File f = null;
        BufferedInputStream origem = null;
        FileInputStream streamDeEntrada = null;
        BufferedOutputStream buffer = null;
        FileOutputStream destino = null;
        ZipOutputStream saida = null;
        ZipEntry entry = null;
        try {
            destino = new FileOutputStream(arqSaida);
            buffer = new BufferedOutputStream(destino);
            saida = new ZipOutputStream(buffer);
            File arquivo = new File(arqEntrada);
            if (arquivo.isFile() && !(arquivo.getName()).equals(arqSaida)) {
                System.out.println("Compactando: " + arquivo.getName());
                streamDeEntrada = new FileInputStream(arquivo);
                origem = new BufferedInputStream(streamDeEntrada, TAMANHO_BUFFER);
                entry = new ZipEntry(arquivo.getName());
                saida.putNextEntry(entry);
                while ((cont = origem.read(dados, 0, TAMANHO_BUFFER)) != -1) {
                    saida.write(dados, 0, cont);
                }
                origem.close();
            }
            saida.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
