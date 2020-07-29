        public boolean check(String credentials) throws IOException {
            if (credentials == null) return true;
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.reset();
                md.update(method.getBytes("ISO-8859-1"));
                md.update((byte) ':');
                md.update(uri.getBytes("ISO-8859-1"));
                byte[] ha2 = md.digest();
                md.update(credentials.getBytes("ISO-8859-1"));
                md.update((byte) ':');
                md.update(nonce.getBytes("ISO-8859-1"));
                md.update((byte) ':');
                md.update(MessageDigester.byteArrayToHex(ha2).getBytes("ISO-8859-1"));
                byte[] digest = md.digest();
                return (MessageDigester.byteArrayToHex(digest).equalsIgnoreCase(response));
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("MD5 not supported");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Encoding not supported");
            }
        }
