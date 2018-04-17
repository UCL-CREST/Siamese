	public static void generateKeys2(int keySize, Path publicKey, Path privateKey) throws NoSuchAlgorithmException, IOException {
		//Fuller Example
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(keySize);
		KeyPair keyPair = keyGen.generateKeyPair();
		PublicKey pubkey = keyPair.getPublic();
		PrivateKey privkey = keyPair.getPrivate();
		
		Files.createDirectories(publicKey.getParent());
		Files.createFile(publicKey);
		Files.createDirectories(privateKey.getParent());
		Files.createFile(privateKey);
		
		ObjectOutputStream oout = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(publicKey.toFile())));
		oout.writeObject(pubkey);
		oout.close();
		
		oout = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(privateKey.toFile())));
		oout.writeObject(privkey);
		oout.close();
	}
