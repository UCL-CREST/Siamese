    private static MyCookieData parseCookie(Cookie cookie) throws CookieException {
        String value = cookie.getValue();
        System.out.println("original cookie: " + value);
        value = value.replace("%3A", ":");
        value = value.replace("%40", "@");
        System.out.println("cookie after replacement: " + value);
        String[] parts = value.split(":");
        if (parts.length < 4) throw new CookieException("only " + parts.length + " parts in the cookie! " + value);
        String email = parts[0];
        String nickname = parts[1];
        boolean admin = Boolean.getBoolean(parts[2].toLowerCase());
        String hsh = parts[3];
        boolean valid_cookie = true;
        String cookie_secret = System.getProperty("COOKIE_SECRET");
        if (cookie_secret == "") throw new CookieException("cookie secret is not set");
        if (email.equals("")) {
            System.out.println("email is empty!");
            nickname = "";
            admin = false;
        } else {
            try {
                MessageDigest sha = MessageDigest.getInstance("SHA");
                sha.update((email + nickname + admin + cookie_secret).getBytes());
                StringBuilder builder = new StringBuilder();
                for (byte b : sha.digest()) {
                    byte tmphigh = (byte) (b >> 4);
                    tmphigh = (byte) (tmphigh & 0xf);
                    builder.append(hextab.charAt(tmphigh));
                    byte tmplow = (byte) (b & 0xf);
                    builder.append(hextab.charAt(tmplow));
                }
                System.out.println();
                String vhsh = builder.toString();
                if (!vhsh.equals(hsh)) {
                    System.out.println("hash not same!");
                    System.out.println("hash passed in: " + hsh);
                    System.out.println("hash generated: " + vhsh);
                    valid_cookie = false;
                } else System.out.println("cookie match!");
            } catch (NoSuchAlgorithmException ex) {
            }
        }
        return new MyCookieData(email, admin, nickname, valid_cookie);
    }
