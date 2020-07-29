	public FTPClient sample2a(String server, int port, String username, String password) throws SocketException, IOException {
		FTPSClient ftpClient = new FTPSClient();
		ftpClient.connect(server, port);
		ftpClient.login(username, password);
		return ftpClient;
	}
