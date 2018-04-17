    protected void DownloadFile() {
        int selectedRow = jTableRemoteFiles.getSelectedRow();
        String strFileToDown, strRemotePathAndName;
        if (FTPConnection.GetStatus() != 2 || RemoteFileList == null) {
            JOptionPane.showMessageDialog(this, "You have not connected to server!", "XylFTP download error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (selectedRow == -1 || selectedRow >= RemoteFileList.length) {
            JOptionPane.showMessageDialog(this, "You should select a file first!", "XylFTP download error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (RemoteFileList[selectedRow].IsDirectory) {
            JOptionPane.showMessageDialog(this, "You can't download a folder!", "XylFTP download error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        strFileToDown = RemoteFileList[selectedRow].Name;
        JFileChooser chooserSave = new JFileChooser();
        chooserSave.setSelectedFile(new File(strFileToDown));
        int returnVal = chooserSave.showSaveDialog(this);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File localFile = chooserSave.getSelectedFile();
        if (localFile.exists()) {
            int iChoice = JOptionPane.showConfirmDialog(this, "The file already exists, do you want to overwrite it?", "XylFTP download Message", JOptionPane.YES_NO_OPTION);
            if (iChoice == 1) {
                return;
            }
        }
        if (localFile.exists() && !localFile.canWrite()) {
            JOptionPane.showMessageDialog(this, "This file cannot write!", "XylFTP download error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String echo;
        int echoMeaning;
        String HostIP;
        int HostPort;
        try {
            if (FTPConnection.IsPassive()) {
                FTPConnection.SendCommand("PASV\r\n");
                ShowCommand("PASV\r\n");
                echo = FTPConnection.GetEcho();
                echoMeaning = ProcessEcho(echo);
                if (echoMeaning == 5) {
                    throw new XylFTPException("get", "Server refuses");
                }
                if (FTPConnection.GetStatus() == 0) {
                    throw new XylFTPException("get", "Server has closed the control connection");
                }
                HostIP = ParseIP(echo);
                HostPort = ParsePort(echo);
                FTPConnection.OpenDataConnection(HostIP, HostPort);
                FTPConnection.SetStatus(3);
                FTPConnection.SendCommand("RETR " + strFileToDown + "\r\n");
                ShowCommand("RETR " + strFileToDown + "\r\n");
            } else {
                String selfIp, selfPort;
                selfIp = FTPConnection.GetSelfIP();
                selfPort = FTPConnection.GetSelfPort();
                FTPConnection.ListenForDataConnection();
                FTPConnection.SendCommand("PORT " + selfIp + selfPort + "\r\n");
                ShowCommand("PORT " + selfIp + selfPort + "\r\n");
                echo = FTPConnection.GetEcho();
                echoMeaning = ProcessEcho(echo);
                if (echoMeaning == 5) {
                    throw new XylFTPException("get", "Server refuses");
                }
                if (FTPConnection.GetStatus() == 0) {
                    throw new XylFTPException("get", "Server has closed the control connection");
                }
                FTPConnection.SetStatus(3);
                FTPConnection.SendCommand("RETR " + strFileToDown + "\r\n");
                ShowCommand("RETR " + strFileToDown + "\r\n");
                FTPConnection.OpenDataConnection();
            }
            echo = FTPConnection.GetEcho();
            if (!IsValidEcho(echo)) throw new XylFTPException("ls", FTPConnection.GetStatus(), "Invaild echo.");
            echoMeaning = ProcessEcho(echo);
            if (echoMeaning == 1) {
                FTPConnection.SetLocalFile(localFile.getCanonicalPath());
                if (FTPConnection.IsPassive()) {
                    FTPConnection.GetFilePassive();
                } else {
                    FTPConnection.GetFileActive();
                }
                echo = FTPConnection.GetEcho();
                echoMeaning = ProcessEcho(echo);
            } else {
                throw new XylFTPException("put", "server refuses data connection");
            }
        } catch (XylFTPException ex) {
            JOptionPane.showMessageDialog(this, "There is an error, the command is: " + ex.GetCommand() + " , the error message is: " + ex.GetMessage() + " please disconnect and connect again!", "XylFTP download error", JOptionPane.ERROR_MESSAGE);
            if (FTPConnection.GetStatus() == 0) {
                try {
                    FTPConnection.CloseConnection();
                } catch (Exception e) {
                }
                ClearTable();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "There is an error" + " , the error message is: " + ex.getMessage() + " please disconnect and connect again!", "XylFTP download error", JOptionPane.ERROR_MESSAGE);
            if (FTPConnection.GetStatus() == 0) {
                try {
                    FTPConnection.CloseConnection();
                } catch (Exception e) {
                }
                ClearTable();
            }
        }
        if (FTPConnection.GetStatus() == 3) {
            FTPConnection.SetStatus(2);
        }
        try {
            FTPConnection.CloseDataConnection();
        } catch (Exception ex) {
        }
    }
