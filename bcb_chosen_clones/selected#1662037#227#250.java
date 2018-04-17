    public static String novoMetodoDeCriptografarParaMD5QueNaoFoiUtilizadoAinda(String input) {
        if (input == null) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(input.getBytes("UTF-8"));
            BigInteger hash = new BigInteger(1, digest.digest());
            String output = hash.toString(16);
            if (output.length() < 32) {
                int sizeDiff = 32 - output.length();
                do {
                    output = "0" + output;
                } while (--sizeDiff > 0);
            }
            return output;
        } catch (NoSuchAlgorithmException ns) {
            LoggerFactory.getLogger(UtilAdrs.class).error(Msg.EXCEPTION_MESSAGE, UtilAdrs.class.getSimpleName(), ns);
            return input;
        } catch (UnsupportedEncodingException e) {
            LoggerFactory.getLogger(UtilAdrs.class).error(Msg.EXCEPTION_MESSAGE, UtilAdrs.class.getSimpleName(), e);
            return input;
        }
    }
