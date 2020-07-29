    public static final String crypt(String password, String salt) throws NoSuchAlgorithmException {
        String magic = "$1$";
        byte finalState[];
        MessageDigest ctx, ctx1;
        long l;
        if (salt.startsWith(magic)) {
            salt = salt.substring(magic.length());
        }
        if (salt.indexOf('$') != -1) {
            salt = salt.substring(0, salt.indexOf('$'));
        }
        if (salt.length() > 8) {
            salt = salt.substring(0, 8);
        }
        ctx = MessageDigest.getInstance("MD5");
        ctx.update(password.getBytes());
        ctx.update(magic.getBytes());
        ctx.update(salt.getBytes());
        ctx1 = MessageDigest.getInstance("MD5");
        ctx1.update(password.getBytes());
        ctx1.update(salt.getBytes());
        ctx1.update(password.getBytes());
        finalState = ctx1.digest();
        for (int pl = password.length(); pl > 0; pl -= 16) {
            for (int i = 0; i < (pl > 16 ? 16 : pl); i++) ctx.update(finalState[i]);
        }
        clearbits(finalState);
        for (int i = password.length(); i != 0; i >>>= 1) {
            if ((i & 1) != 0) {
                ctx.update(finalState[0]);
            } else {
                ctx.update(password.getBytes()[0]);
            }
        }
        finalState = ctx.digest();
        for (int i = 0; i < 1000; i++) {
            ctx1 = MessageDigest.getInstance("MD5");
            if ((i & 1) != 0) {
                ctx1.update(password.getBytes());
            } else {
                for (int c = 0; c < 16; c++) ctx1.update(finalState[c]);
            }
            if ((i % 3) != 0) {
                ctx1.update(salt.getBytes());
            }
            if ((i % 7) != 0) {
                ctx1.update(password.getBytes());
            }
            if ((i & 1) != 0) {
                for (int c = 0; c < 16; c++) ctx1.update(finalState[c]);
            } else {
                ctx1.update(password.getBytes());
            }
            finalState = ctx1.digest();
        }
        StringBuffer result = new StringBuffer();
        result.append(magic);
        result.append(salt);
        result.append("$");
        l = (bytes2u(finalState[0]) << 16) | (bytes2u(finalState[6]) << 8) | bytes2u(finalState[12]);
        result.append(to64(l, 4));
        l = (bytes2u(finalState[1]) << 16) | (bytes2u(finalState[7]) << 8) | bytes2u(finalState[13]);
        result.append(to64(l, 4));
        l = (bytes2u(finalState[2]) << 16) | (bytes2u(finalState[8]) << 8) | bytes2u(finalState[14]);
        result.append(to64(l, 4));
        l = (bytes2u(finalState[3]) << 16) | (bytes2u(finalState[9]) << 8) | bytes2u(finalState[15]);
        result.append(to64(l, 4));
        l = (bytes2u(finalState[4]) << 16) | (bytes2u(finalState[10]) << 8) | bytes2u(finalState[5]);
        result.append(to64(l, 4));
        l = bytes2u(finalState[11]);
        result.append(to64(l, 2));
        clearbits(finalState);
        return result.toString();
    }
