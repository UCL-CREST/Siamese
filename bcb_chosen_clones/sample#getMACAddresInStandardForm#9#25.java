	public static String getMyMacAddress1() throws SocketException, UnknownHostException {
		InetAddress ip = InetAddress.getByName("192.168.0.12");
		System.out.println(ip.getHostAddress());
		NetworkInterface network = NetworkInterface.getByInetAddress(ip);
		byte[] mac = network.getHardwareAddress();
		String macStr = "";
		for(int i = 0; i < mac.length; i++) {
			int a = mac [i] & 0xFF;
	        if (a > 15) macStr += Integer.toHexString (a);
	        else macStr += "0" + Integer.toHexString (a);

	        if (i < (mac.length - 1)) {
	            macStr += "-";
	        }
		}
		return macStr;
	}
