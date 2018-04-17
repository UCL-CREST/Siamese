    protected boolean check(String username, String password, String realm, String nonce, String nc, String cnonce, String qop, String uri, String response, HttpServletRequest request) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(username.getBytes());
            md.update((byte) ':');
            md.update(realm.getBytes());
            md.update((byte) ':');
            md.update(password.getBytes());
            byte[] ha1 = md.digest();
            md.reset();
            md.update(request.getMethod().getBytes());
            md.update((byte) ':');
            md.update(uri.getBytes());
            byte[] ha2 = md.digest();
            md.update(TypeUtil.toString(ha1, 16).getBytes());
            md.update((byte) ':');
            md.update(nonce.getBytes());
            md.update((byte) ':');
            md.update(nc.getBytes());
            md.update((byte) ':');
            md.update(cnonce.getBytes());
            md.update((byte) ':');
            md.update(qop.getBytes());
            md.update((byte) ':');
            md.update(TypeUtil.toString(ha2, 16).getBytes());
            byte[] digest = md.digest();
            return response.equals(encode(digest));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
