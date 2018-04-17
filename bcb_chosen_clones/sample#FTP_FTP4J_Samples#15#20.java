	public FTPClient sample1(String server, int port, String username, String password) throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
		FTPClient ftpClient = new FTPClient();
		ftpClient.connect(server, port);
		ftpClient.login(username, password);
		return ftpClient;
	}
