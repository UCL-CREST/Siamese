    @Override
    @RemoteMethod
    public boolean encrypt(int idAnexo) {
        try {
            Anexo anexo = anexoService.selectById(idAnexo);
            aes.init(Cipher.ENCRYPT_MODE, aeskeySpec);
            FileInputStream fis = new FileInputStream(config.baseDir + "/arquivos_upload_direto/" + anexo.getAnexoCaminho());
            CipherOutputStream cos = new CipherOutputStream(new FileOutputStream(config.baseDir + "/arquivos_upload_direto/encrypt/" + anexo.getAnexoCaminho()), aes);
            IOUtils.copy(fis, cos);
            cos.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
