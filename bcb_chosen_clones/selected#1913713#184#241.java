    public boolean uploadFTP(String ipFTP, String loginFTP, String senhaFTP, String diretorioFTP, String diretorioAndroid, String arquivoFTP) {
        try {
            dialogHandler.sendEmptyMessage(0);
            File file = new File(diretorioAndroid);
            File file2 = new File(diretorioAndroid + arquivoFTP);
            Log.v("uploadFTP", "Atribuidas as vari�veis");
            String status = "";
            if (file.isDirectory()) {
                Log.v("uploadFTP", "� diret�rio");
                if (file.list().length > 0) {
                    Log.v("uploadFTP", "file.list().length > 0");
                    ftp.connect(ipFTP);
                    ftp.login(loginFTP, senhaFTP);
                    ftp.enterLocalPassiveMode();
                    ftp.setFileTransferMode(FTPClient.ASCII_FILE_TYPE);
                    ftp.setFileType(FTPClient.ASCII_FILE_TYPE);
                    ftp.changeWorkingDirectory(diretorioFTP);
                    FileInputStream arqEnviar = new FileInputStream(diretorioAndroid + arquivoFTP);
                    Log.v("uploadFTP", "FileInputStream declarado");
                    if (ftp.storeFile(arquivoFTP, arqEnviar)) {
                        Log.v("uploadFTP", "ftp.storeFile(arquivoFTP, arqEnviar)");
                        status = ftp.getStatus().toString();
                        Log.v("uploadFTP", "getStatus(): " + status);
                        if (file2.delete()) {
                            Log.i("uploadFTP", "Arquivo " + arquivoFTP + " exclu�do com sucesso");
                            retorno = true;
                        } else {
                            Log.e("uploadFTP", "Erro ao excluir o arquivo " + arquivoFTP);
                            retorno = false;
                        }
                    } else {
                        Log.e("uploadFTP", "ERRO: arquivo " + arquivoFTP + "n�o foi enviado!");
                        retorno = false;
                    }
                } else {
                    Log.e("uploadFTP", "N�o existe o arquivo " + arquivoFTP + "neste diret�rio!");
                    retorno = false;
                }
            } else {
                Log.e("uploadFTP", "N�o � diret�rio");
                retorno = false;
            }
            if (ftp.isConnected()) {
                Log.v("uploadFTP", "isConnected ");
                ftp.abort();
                status = ftp.getStatus().toString();
                Log.v("uploadFTP", "quit " + retorno);
            }
            return retorno;
        } catch (IOException e) {
            Log.e("uploadFTP", "ERRO FTP: " + e);
            retorno = false;
            return retorno;
        } finally {
            handler.sendEmptyMessage(0);
            Log.v("uploadFTP", "finally executado");
        }
    }
