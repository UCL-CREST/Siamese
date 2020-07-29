    public void compactar(String arqSaida) {
        int i, cont;
        byte[] dados = new byte[TAMANHO_BUFFER];
        File f = null;
        BufferedInputStream origem = null;
        FileInputStream streamDeEntrada = null;
        FileOutputStream destino = null;
        ZipOutputStream saida = null;
        ZipEntry entry = null;
        String arquivos[];
        try {
            destino = new FileOutputStream(arqSaida, false);
            System.out.println("Aki 3");
            f = new File(".");
            System.out.println(f.getAbsolutePath());
            arquivos = f.list();
            for (i = 0; i < arquivos.length; i++) {
                File arquivo = new File(arquivos[i]);
                if (arquivo.isFile() && !(arquivo.getName()).equals(arqSaida)) {
                    System.out.println("Entrou IF");
                    System.out.println("Compactando: " + arquivos[i]);
                    System.out.println("TTTT");
                    saida = new ZipOutputStream(new BufferedOutputStream(destino));
                    System.out.println("ZZZZ");
                    streamDeEntrada = new FileInputStream(arquivo);
                    origem = new BufferedInputStream(streamDeEntrada, TAMANHO_BUFFER);
                    entry = new ZipEntry(arquivos[i]);
                    saida.putNextEntry(entry);
                    while ((cont = origem.read(dados, 0, TAMANHO_BUFFER)) != -1) {
                        try {
                            saida.write(dados, 0, cont);
                        } catch (IOException ioe) {
                            System.out.println("Erro de entrada e saida no zip!");
                        }
                    }
                    origem.close();
                }
            }
            saida.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
