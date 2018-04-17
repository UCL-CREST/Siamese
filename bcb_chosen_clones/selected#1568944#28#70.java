    public void removeJarFiles() throws IOException {
        HashSet<GridNode> nodes = (HashSet) batchTask.returnNodeCollection();
        Iterator<GridNode> ic = nodes.iterator();
        InetAddress addLocal = InetAddress.getLocalHost();
        String hostnameLocal = addLocal.getHostName();
        while (ic.hasNext()) {
            GridNode node = ic.next();
            String address = node.getPhysicalAddress();
            InetAddress addr = InetAddress.getByName(address);
            byte[] rawAddr = addr.getAddress();
            Map<String, String> attributes = node.getAttributes();
            InetAddress hostname = InetAddress.getByAddress(rawAddr);
            if (hostname.getHostName().equals(hostnameLocal)) continue;
            String gridPath = attributes.get("GRIDGAIN_HOME");
            FTPClient ftp = new FTPClient();
            try {
                String[] usernamePass = inputNodes.get(hostname.getHostName());
                ftp.connect(hostname);
                ftp.login(usernamePass[0], usernamePass[1]);
                int reply = ftp.getReplyCode();
                if (!FTPReply.isPositiveCompletion(reply)) {
                    ftp.disconnect();
                    System.err.println("FTP server refused connection.");
                    continue;
                }
                ftp.login(usernamePass[0], usernamePass[1]);
                String directory = gridPath + "/libs/ext/";
                ftp.changeWorkingDirectory(directory);
                FTPFile[] fs = ftp.listFiles();
                for (FTPFile f : fs) {
                    if (f.isDirectory()) continue;
                    System.out.println(f.getName());
                    ftp.deleteFile(f.getName());
                }
                ftp.sendCommand("rm *");
                ftp.logout();
                ftp.disconnect();
            } catch (Exception e) {
                MessageCenter.getMessageCenter(BatchMainSetup.class).error("Problems with the FTP connection." + "A file has not been succesfully transfered", e);
                e.printStackTrace();
            }
        }
    }
