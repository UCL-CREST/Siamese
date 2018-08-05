	public FTPClient sample2c(String server, int port, String username, String password) throws SocketException, IOException {
		FTPSClient ftpClient = new FTPSClient();
		ftpClient.setDefaultPort(port);
		ftpClient.connect(server);
		ftpClient.login(username, password);
		return ftpClient;
	}
