    public void salva(UploadedFile imagem, Usuario usuario) {
        File destino = new File(pastaImagens, usuario.getId() + ".imagem");
        try {
            IOUtils.copyLarge(imagem.getFile(), new FileOutputStream(destino));
        } catch (IOException e) {
            throw new RuntimeException("Erro ao copiar imagem", e);
        }
    }
