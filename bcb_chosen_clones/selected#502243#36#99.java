    public static void write(Date date, KeyPair keys, String pass, OutputStream out) throws Exception {
        long time = date.getTime() / 1000l;
        java.security.PublicKey pk = keys.getPublic();
        java.security.PrivateKey sk = keys.getPrivate();
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        OutputStream b = bo;
        ChecksumOutputStream cs;
        b.write(4);
        b.write((byte) (time >> 24) & 0xFF);
        b.write((byte) (time >> 16) & 0xFF);
        b.write((byte) (time >> 8) & 0xFF);
        b.write((byte) time & 0xFF);
        if (pk.getAlgorithm().equals("RSA")) {
            b.write(Algo.RSA_S);
            MPI.write(((java.security.interfaces.RSAPublicKey) pk).getModulus(), b);
            MPI.write(((java.security.interfaces.RSAPublicKey) pk).getPublicExponent(), b);
        } else if (pk.getAlgorithm().equals("DSA")) {
            b.write(Algo.DSA_S);
            MPI.write(((java.security.interfaces.DSAPublicKey) pk).getParams().getP(), b);
            MPI.write(((java.security.interfaces.DSAPublicKey) pk).getParams().getQ(), b);
            MPI.write(((java.security.interfaces.DSAPublicKey) pk).getParams().getG(), b);
            MPI.write(((java.security.interfaces.DSAPublicKey) pk).getY(), b);
        } else throw new NoSuchAlgorithmException("Invalid Public Key Algorithm");
        if (pass == null) {
            b.write(0);
            cs = new Checksum16OutputStream();
        } else {
            SecureRandom rand = new SecureRandom();
            S2K s2k;
            Cipher c;
            byte[] salt = new byte[8];
            byte[] iv = new byte[8];
            rand.nextBytes(salt);
            rand.nextBytes(iv);
            b.write(0xFE);
            b.write(Algo.DESEDE);
            b.write(0x01);
            b.write(Algo.SHA1);
            b.write(salt);
            s2k = new SaltedDESedeS2K(salt, "SHA1");
            c = Cipher.getInstance("DESede/CFB/NoPadding");
            c.init(Cipher.ENCRYPT_MODE, s2k.getKey(pass), new IvParameterSpec(iv));
            b.write(iv);
            b = new CipherOutputStream(bo, c);
            cs = new SHA1OutputStream();
        }
        if (sk.getAlgorithm().equals("RSA")) {
            MPI.write(((RSAPrivateCrtKey) sk).getPrivateExponent(), b);
            MPI.write(((RSAPrivateCrtKey) sk).getPrimeQ(), b);
            MPI.write(((RSAPrivateCrtKey) sk).getPrimeP(), b);
            MPI.write(((RSAPrivateCrtKey) sk).getCrtCoefficient(), b);
            MPI.write(((RSAPrivateCrtKey) sk).getPrivateExponent(), cs);
            MPI.write(((RSAPrivateCrtKey) sk).getPrimeQ(), cs);
            MPI.write(((RSAPrivateCrtKey) sk).getPrimeP(), cs);
            MPI.write(((RSAPrivateCrtKey) sk).getCrtCoefficient(), cs);
        } else if (sk.getAlgorithm().equals("DSA")) {
            MPI.write(((java.security.interfaces.DSAPrivateKey) sk).getX(), cs);
            MPI.write(((java.security.interfaces.DSAPrivateKey) sk).getX(), b);
        } else throw new NoSuchAlgorithmException("Invalid Private Key Algorithm");
        b.write(cs.getChecksum());
        b.close();
        PacketHeader.write(false, (byte) Packet.SECKEY, bo.size(), out);
        out.write(bo.toByteArray());
    }
