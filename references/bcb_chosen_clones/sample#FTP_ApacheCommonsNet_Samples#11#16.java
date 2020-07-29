	public FTPClient sample1a(String server, int port, String username, String password) throws SocketException, IOException {
		FTPClient ftpClient = new FTPClient();
		ftpClient.connect(server, port);
		ftpClient.login(username, password);
		return ftpClient;
	}
