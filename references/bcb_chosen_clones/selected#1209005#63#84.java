    public void authenticate(final ConnectionHandler ch, final AuthenticationCriteria ac) throws NamingException {
        byte[] hash = new byte[DIGEST_SIZE];
        try {
            final MessageDigest md = MessageDigest.getInstance(this.passwordScheme);
            md.update(((String) ac.getCredential()).getBytes());
            hash = md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new NamingException(e.getMessage());
        }
        ch.connect(this.config.getBindDn(), this.config.getBindCredential());
        NamingEnumeration<SearchResult> en = null;
        try {
            en = ch.getLdapContext().search(ac.getDn(), "userPassword={0}", new Object[] { String.format("{%s}%s", this.passwordScheme, LdapUtil.base64Encode(hash)).getBytes() }, LdapConfig.getCompareSearchControls());
            if (!en.hasMore()) {
                throw new AuthenticationException("Compare authentication failed.");
            }
        } finally {
            if (en != null) {
                en.close();
            }
        }
    }
