	public static String getMyMacAddress2() throws SocketException, UnknownHostException {
		InetAddress ip = InetAddress.getByName("192.168.0.12");
		System.out.println(ip.getHostAddress());
		NetworkInterface network = NetworkInterface.getByInetAddress(ip);
		byte[] mac = network.getHardwareAddress();
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < mac.length; i++) {
			sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));		
		}
		String macStr = sb.toString();
		return macStr;
	}
