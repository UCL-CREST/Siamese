	public FTPClient sample3b(String ftpserver, String proxyserver, int proxyport, String username, String password) throws SocketException, IOException {
		FTPHTTPClient ftpClient = new FTPHTTPClient(proxyserver, proxyport);
		ftpClient.connect(ftpserver);
		ftpClient.login(username, password);
		return ftpClient;
	}
