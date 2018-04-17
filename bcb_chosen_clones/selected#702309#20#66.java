    public static String crypt(String strPassword, String strSalt) {
        try {
            StringTokenizer st = new StringTokenizer(strSalt, "$");
            st.nextToken();
            byte[] abyPassword = strPassword.getBytes();
            byte[] abySalt = st.nextToken().getBytes();
            MessageDigest _md = MessageDigest.getInstance("MD5");
            _md.update(abyPassword);
            _md.update(MAGIC.getBytes());
            _md.update(abySalt);
            MessageDigest md2 = MessageDigest.getInstance("MD5");
            md2.update(abyPassword);
            md2.update(abySalt);
            md2.update(abyPassword);
            byte[] abyFinal = md2.digest();
            for (int n = abyPassword.length; n > 0; n -= 16) {
                _md.update(abyFinal, 0, n > 16 ? 16 : n);
            }
            abyFinal = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
            for (int j = 0, i = abyPassword.length; i != 0; i >>>= 1) {
                if ((i & 1) == 1) _md.update(abyFinal, j, 1); else _md.update(abyPassword, j, 1);
            }
            StringBuffer sbPasswd = new StringBuffer();
            sbPasswd.append(MAGIC);
            sbPasswd.append(new String(abySalt));
            sbPasswd.append('$');
            abyFinal = _md.digest();
            for (int n = 0; n < 1000; n++) {
                MessageDigest md3 = MessageDigest.getInstance("MD5");
                if ((n & 1) != 0) md3.update(abyPassword); else md3.update(abyFinal);
                if ((n % 3) != 0) md3.update(abySalt);
                if ((n % 7) != 0) md3.update(abyPassword);
                if ((n & 1) != 0) md3.update(abyFinal); else md3.update(abyPassword);
                abyFinal = md3.digest();
            }
            int[] anFinal = new int[] { (abyFinal[0] & 0x7f) | (abyFinal[0] & 0x80), (abyFinal[1] & 0x7f) | (abyFinal[1] & 0x80), (abyFinal[2] & 0x7f) | (abyFinal[2] & 0x80), (abyFinal[3] & 0x7f) | (abyFinal[3] & 0x80), (abyFinal[4] & 0x7f) | (abyFinal[4] & 0x80), (abyFinal[5] & 0x7f) | (abyFinal[5] & 0x80), (abyFinal[6] & 0x7f) | (abyFinal[6] & 0x80), (abyFinal[7] & 0x7f) | (abyFinal[7] & 0x80), (abyFinal[8] & 0x7f) | (abyFinal[8] & 0x80), (abyFinal[9] & 0x7f) | (abyFinal[9] & 0x80), (abyFinal[10] & 0x7f) | (abyFinal[10] & 0x80), (abyFinal[11] & 0x7f) | (abyFinal[11] & 0x80), (abyFinal[12] & 0x7f) | (abyFinal[12] & 0x80), (abyFinal[13] & 0x7f) | (abyFinal[13] & 0x80), (abyFinal[14] & 0x7f) | (abyFinal[14] & 0x80), (abyFinal[15] & 0x7f) | (abyFinal[15] & 0x80) };
            to64(sbPasswd, anFinal[0] << 16 | anFinal[6] << 8 | anFinal[12], 4);
            to64(sbPasswd, anFinal[1] << 16 | anFinal[7] << 8 | anFinal[13], 4);
            to64(sbPasswd, anFinal[2] << 16 | anFinal[8] << 8 | anFinal[14], 4);
            to64(sbPasswd, anFinal[3] << 16 | anFinal[9] << 8 | anFinal[15], 4);
            to64(sbPasswd, anFinal[4] << 16 | anFinal[10] << 8 | anFinal[5], 4);
            to64(sbPasswd, anFinal[11], 2);
            return sbPasswd.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
