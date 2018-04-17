	public FTPClient sample1c(String server, int port, String username, String password) throws SocketException, IOException {
		FTPClient ftpClient = new FTPClient();
		ftpClient.setDefaultPort(port);
		ftpClient.connect(server);
		ftpClient.login(username, password);
		return ftpClient;
	}
