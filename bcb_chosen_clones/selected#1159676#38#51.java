    public synchronized String encryptPassword(String password) {
        try {
            m_cipher.init(Cipher.ENCRYPT_MODE, Synergizer.getMultitasker(m_algorithm));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            CipherOutputStream cos = new CipherOutputStream(baos, m_cipher);
            ObjectOutputStream oos = new ObjectOutputStream(cos);
            oos.writeObject(password);
            oos.flush();
            oos.close();
            return Helper.buildHexStringFromBytes(baos.toByteArray());
        } catch (Exception e) {
            throw ValidationException.errorEncryptingPassword(e);
        }
    }
