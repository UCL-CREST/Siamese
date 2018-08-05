	public static KeyPair generateKeys1() throws NoSuchAlgorithmException, IOException {
		//Minimum Example
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		KeyPair keyPair = keyGen.generateKeyPair();
		return keyPair;
	}
