    private void getCriptoFile(Key senha, String arquivoEntrada, String arquivoSaida, String algoritmo, Boolean descriptografa) {
        Cipher cifra;
        try {
            cifra = Cipher.getInstance(algoritmo);
            FileInputStream fis2 = null;
            byte[] buffer2 = new byte[1];
            int bytesLidos = -1;
            fis2 = new FileInputStream(arquivoEntrada);
            bytesLidos = -1;
            int i = 0;
            while ((bytesLidos = fis2.read(buffer2)) != -1) {
                i++;
            }
            fis2.close();
            fis2 = new FileInputStream(arquivoEntrada);
            byte[] conteudoArquivo = new byte[i];
            fis2.read(conteudoArquivo);
            if (descriptografa) {
                cifra.init(Cipher.DECRYPT_MODE, senha);
            } else {
                cifra.init(Cipher.ENCRYPT_MODE, senha);
            }
            FileOutputStream fos = new FileOutputStream(arquivoSaida);
            fos.write(cifra.doFinal(conteudoArquivo));
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
