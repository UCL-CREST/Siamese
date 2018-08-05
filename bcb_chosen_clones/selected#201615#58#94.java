    private void compactar(String nomeArquivo) {
        File arquivo, novoArquivo;
        FileInputStream fis;
        ZipOutputStream zos;
        byte[] dadosArquivo = new byte[2048];
        arquivo = new File(nomeArquivo);
        if (arquivo.exists()) {
            try {
                fis = new FileInputStream(arquivo);
                novoArquivo = new File(nomeArquivo + ".zip");
                if (novoArquivo.createNewFile()) {
                    novoArquivo.delete();
                    novoArquivo.createNewFile();
                }
                System.out.println(novoArquivo.getName() + "....");
                zos = new ZipOutputStream(new FileOutputStream(novoArquivo));
                zos.putNextEntry(new ZipEntry(arquivo.getName()));
                while (fis.read(dadosArquivo) > 0) {
                    zos.write(dadosArquivo);
                    zos.flush();
                }
                fis.close();
                zos.close();
                System.out.println("OK!");
            } catch (FileNotFoundException e) {
                System.out.println("ERRO!");
                System.err.println("Erro ao compactar ou ler arquivo " + nomeArquivo + ".");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("ERRO!");
                System.err.println("Erro ao escrever ou ler dados do arquivo " + nomeArquivo + ".");
                e.printStackTrace();
            }
        } else {
            System.err.println("Arquivo " + nomeArquivo + " n�o existe.");
        }
    }
