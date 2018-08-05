    public boolean downloadFile(String webdir, String fileName, String localdir) {
        boolean result = false;
        checkDownLoadDirectory(localdir);
        FTPClient ftp = new FTPClient();
        try {
            ftp.connect(url);
            ftp.login(username, password);
            if (!"".equals(webdir.trim())) ftp.changeDirectory(webdir);
            ftp.download(fileName, new File(localdir + "//" + fileName));
            result = true;
            ftp.disconnect(true);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FTPIllegalReplyException e) {
            e.printStackTrace();
        } catch (FTPException e) {
            e.printStackTrace();
        } catch (FTPDataTransferException e) {
            e.printStackTrace();
        } catch (FTPAbortedException e) {
            e.printStackTrace();
        }
        return result;
    }
