    public char check(String password) {
        if (captchaRandom.equals("null")) {
            return 's';
        }
        if (captchaRandom.equals("used")) {
            return 'm';
        }
        String encryptionBase = secret + captchaRandom;
        if (!alphabet.equals(ALPHABET_DEFAULT) || letters != LETTERS_DEFAULT) {
            encryptionBase += ":" + alphabet + ":" + letters;
        }
        MessageDigest md5;
        byte[] digest = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(encryptionBase.getBytes());
            digest = md5.digest();
        } catch (NoSuchAlgorithmException e) {
        }
        String correctPassword = "";
        int index;
        for (int i = 0; i < letters; i++) {
            index = (digest[i] + 256) % 256 % alphabet.length();
            correctPassword += alphabet.substring(index, index + 1);
        }
        if (!password.equals(correctPassword)) {
            return 'w';
        } else {
            captchaRandom = "used";
            return 't';
        }
    }
