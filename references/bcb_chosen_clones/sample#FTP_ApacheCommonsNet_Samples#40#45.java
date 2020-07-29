	public FTPClient sample2b(String server, String username, String password) throws SocketException, IOException {
		FTPSClient ftpClient = new FTPSClient();
		ftpClient.connect(server);
		ftpClient.login(username, password);
		return ftpClient;
	}
