    public boolean downloadFTP(String ipFTP, String loginFTP, String senhaFTP, String diretorioFTP, String diretorioAndroid, String arquivoFTP) throws SocketException, IOException {
        boolean retorno = false;
        FileOutputStream arqReceber = null;
        try {
            ftp.connect(ipFTP);
            Log.i("DownloadFTP", "Connected: " + ipFTP);
            ftp.login(loginFTP, senhaFTP);
            Log.i("DownloadFTP", "Logged on");
            ftp.enterLocalPassiveMode();
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            arqReceber = new FileOutputStream(file.toString());
            ftp.retrieveFile("/tablet_ftp/Novo/socialAlimenta.xml", arqReceber);
            retorno = true;
            ftp.disconnect();
            Log.i("DownloadFTP", "retorno:" + retorno);
        } catch (Exception e) {
            ftp.disconnect();
            Log.e("DownloadFTP", "Erro:" + e.getMessage());
        } finally {
            Log.e("DownloadFTP", "Finally");
        }
        return retorno;
    }
