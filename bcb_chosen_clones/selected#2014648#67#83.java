    public String genPass() {
        String salto = "Z1mX502qLt2JTcW9MTDTGBBw8VBQQmY2";
        String clave = (int) (Math.random() * 10) + "" + (int) (Math.random() * 10) + "" + (int) (Math.random() * 10) + "" + (int) (Math.random() * 10) + "" + (int) (Math.random() * 10) + "" + (int) (Math.random() * 10) + "" + (int) (Math.random() * 10);
        password = clave;
        String claveConSalto = clave + salto;
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
            m.update(claveConSalto.getBytes("utf-8"), 0, claveConSalto.length());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String claveCifrada = new BigInteger(1, m.digest()).toString(16);
        return claveCifrada + ":" + salto;
    }
