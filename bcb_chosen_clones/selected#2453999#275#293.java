    private String generate(String value) throws Exception {
        String resStr = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(value.getBytes("utf-8"), 0, value.length());
            byte[] result = md.digest();
            resStr = FTGenerate.convertToHex(result);
            md.reset();
        } catch (NoSuchAlgorithmException nae) {
            this.getLog().severe("Hash no funcionando");
            nae.printStackTrace();
            throw new Exception("Hash no funcionando");
        } catch (UnsupportedEncodingException ee) {
            this.getLog().severe("Encoding no funcionando");
            ee.printStackTrace();
            throw new Exception("Encoding no funcionando");
        }
        return resStr;
    }
