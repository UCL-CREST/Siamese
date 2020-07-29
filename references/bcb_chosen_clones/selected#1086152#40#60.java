    public static void retriveRemote(ISource source, Node[] nodes, String outDirName, boolean isBinary) throws Exception {
        FTPClient client = new FTPClient();
        client.connect(source.getSourceDetail().getHost());
        client.login(source.getSourceDetail().getUser(), source.getSourceDetail().getPassword());
        if (isBinary) client.setFileType(FTPClient.BINARY_FILE_TYPE);
        FileOutputStream out = null;
        for (Node node : nodes) {
            if (!node.isLeaf()) {
                Node[] childern = source.getChildern(node);
                File dir = new File(outDirName + File.separator + node.getAlias());
                dir.mkdir();
                retriveRemote(source, childern, outDirName + File.separator + node.getAlias(), isBinary);
            } else {
                out = new FileOutputStream(outDirName + File.separator + node.getAlias());
                client.retrieveFile(node.getAbsolutePath(), out);
                out.flush();
                out.close();
            }
        }
        client.disconnect();
    }
