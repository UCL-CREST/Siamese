	public FTPClient sample1b(String server, String username, String password) throws SocketException, IOException {
		FTPClient ftpClient = new FTPClient();
		ftpClient.connect(server);
		ftpClient.login(username, password);
		return ftpClient;
	}
