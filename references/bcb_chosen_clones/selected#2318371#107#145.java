    public void executaAlteracoes() {
        Album album = Album.getAlbum();
        Photo[] fotos = album.getFotos();
        Photo f;
        int ultimoFotoID = -1;
        int albumID = album.getAlbumID();
        sucesso = true;
        PainelWebFotos.setCursorWait(true);
        albumID = recordAlbumData(album, albumID);
        sucesso = recordFotoData(fotos, ultimoFotoID, albumID);
        String caminhoAlbum = Util.getFolder("albunsRoot").getPath() + File.separator + albumID;
        File diretorioAlbum = new File(caminhoAlbum);
        if (!diretorioAlbum.isDirectory()) {
            if (!diretorioAlbum.mkdir()) {
                Util.log("[AcaoAlterarAlbum.executaAlteracoes.7]/ERRO: diretorio " + caminhoAlbum + " n�o pode ser criado. abortando");
                return;
            }
        }
        for (int i = 0; i < fotos.length; i++) {
            f = fotos[i];
            if (f.getCaminhoArquivo().length() > 0) {
                try {
                    FileChannel canalOrigem = new FileInputStream(f.getCaminhoArquivo()).getChannel();
                    FileChannel canalDestino = new FileOutputStream(caminhoAlbum + File.separator + f.getFotoID() + ".jpg").getChannel();
                    canalDestino.transferFrom(canalOrigem, 0, canalOrigem.size());
                    canalOrigem = null;
                    canalDestino = null;
                } catch (Exception e) {
                    Util.log("[AcaoAlterarAlbum.executaAlteracoes.8]/ERRO: " + e);
                    sucesso = false;
                }
            }
        }
        prepareThumbsAndFTP(fotos, albumID, caminhoAlbum);
        prepareExtraFiles(album, caminhoAlbum);
        fireChangesToGUI(fotos);
        dispatchAlbum();
        PainelWebFotos.setCursorWait(false);
    }
