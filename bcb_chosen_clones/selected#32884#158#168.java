    public static String getUserToken(String userName) {
        if (userName != null && userName.trim().length() > 0) try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update((userName + seed).getBytes("ISO-8859-1"));
            return BaseController.bytesToHex(md.digest());
        } catch (NullPointerException npe) {
        } catch (NoSuchAlgorithmException e) {
        } catch (UnsupportedEncodingException e) {
        }
        return null;
    }
