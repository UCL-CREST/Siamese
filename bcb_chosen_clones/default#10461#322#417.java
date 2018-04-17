    protected void UploadFile() {
        JFileChooser chooserSave = new JFileChooser();
        int returnVal = chooserSave.showOpenDialog(this);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File localFile = chooserSave.getSelectedFile();
        if (!(localFile.exists() && localFile.canRead())) {
            JOptionPane.showMessageDialog(this, "The file does not exist or cannot read!", "XylFTP upload error", JOptionPane.ERROR_MESSAGE);
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
                    throw new XylFTPException("put", "Server refuses");
                }
                if (FTPConnection.GetStatus() == 0) {
                    throw new XylFTPException("put", "Server has closed the control connection");
                }
                HostIP = ParseIP(echo);
                HostPort = ParsePort(echo);
                FTPConnection.OpenDataConnection(HostIP, HostPort);
                FTPConnection.SetStatus(4);
                FTPConnection.SendCommand("STOR " + localFile.getName() + "\r\n");
                ShowCommand("STOR " + localFile.getName() + "\r\n");
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
                    throw new XylFTPException("put", "Server refuses");
                }
                if (FTPConnection.GetStatus() == 0) {
                    throw new XylFTPException("put", "Server has closed the control connection");
                }
                FTPConnection.SetStatus(4);
                FTPConnection.SendCommand("STOR " + localFile.getName() + "\r\n");
                ShowCommand("STOR " + localFile.getName() + "\r\n");
                FTPConnection.OpenDataConnection();
            }
            echo = FTPConnection.GetEcho();
            if (!IsValidEcho(echo)) throw new XylFTPException("put", FTPConnection.GetStatus(), "Invaild echo.");
            echoMeaning = ProcessEcho(echo);
            if (echoMeaning == 1) {
                FTPConnection.SetLocalFile(localFile.getCanonicalPath());
                if (FTPConnection.IsPassive()) {
                    FTPConnection.SendFilePassive();
                } else {
                    FTPConnection.SendFileActive();
                }
                echo = FTPConnection.GetEcho();
                echoMeaning = ProcessEcho(echo);
            } else {
                throw new XylFTPException("put", "server refuses data connection");
            }
            ListRemoteFiles();
        } catch (XylFTPException ex) {
            JOptionPane.showMessageDialog(this, "There is an error, the command is: " + ex.GetCommand() + " , the error message is: " + ex.GetMessage() + " please disconnect and connect again!", "XylFTP upload error", JOptionPane.ERROR_MESSAGE);
            if (FTPConnection.GetStatus() == 0) {
                try {
                    FTPConnection.CloseConnection();
                } catch (Exception e) {
                }
                ClearTable();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "There is an error" + " , the error message is: " + ex.getMessage() + " please disconnect and connect again!", "XylFTP upload error", JOptionPane.ERROR_MESSAGE);
            if (FTPConnection.GetStatus() == 0) {
                try {
                    FTPConnection.CloseConnection();
                } catch (Exception e) {
                }
                ClearTable();
            }
        }
        if (FTPConnection.GetStatus() == 4) {
            FTPConnection.SetStatus(2);
        }
        try {
            FTPConnection.CloseDataConnection();
        } catch (Exception ex) {
        }
    }
