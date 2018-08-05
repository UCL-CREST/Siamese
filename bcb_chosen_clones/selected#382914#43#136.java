    public boolean authenticate(String userName, String loginPassword) {
        if (!systemConfigManager.getBool("ldap", "authEnable")) {
            return false;
        }
        String ldapName = userName;
        AkteraUser user = userDAO.findUserByName(userName);
        if (user != null && StringTools.isNotTrimEmpty(user.getLdapName())) {
            ldapName = user.getLdapName();
        }
        String server = systemConfigManager.getString("ldap", "authHost");
        if (StringTools.isTrimEmpty(server)) {
            return false;
        }
        int port = NumberTools.toInt(systemConfigManager.get("ldap", "authPort"), 389);
        String type = StringTools.trim(systemConfigManager.getString("ldap", "authType"));
        String baseDn = StringTools.trim(systemConfigManager.getString("ldap", "authBaseDn"));
        String userDn = StringTools.trim(systemConfigManager.getString("ldap", "authUserDn"));
        String password = StringTools.trim(systemConfigManager.getString("ldap", "authPassword"));
        String query = StringTools.trim(systemConfigManager.getString("ldap", "authQuery"));
        String bindDn = StringTools.trim(systemConfigManager.getString("ldap", "authBindDn"));
        String passwordAttributeName = StringTools.trim(systemConfigManager.getString("ldap", "authPasswordAttributeName"));
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userName", userName);
        params.put("ldapName", ldapName);
        params.put("loginName", StringTools.isTrimEmpty(ldapName) ? userName : ldapName);
        query = StringTools.replaceTemplate(query, params);
        bindDn = StringTools.replaceTemplate(bindDn, params);
        Hashtable<String, Object> env = new Hashtable<String, Object>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://" + server + ":" + port + "/" + baseDn);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        if ("ldapAuthBind".equals(type)) {
            env.put(Context.SECURITY_PRINCIPAL, bindDn);
            env.put(Context.SECURITY_CREDENTIALS, loginPassword);
            try {
                DirContext ctx = new InitialDirContext(env);
                try {
                    ctx.close();
                } catch (Exception ignored) {
                }
                return true;
            } catch (Exception ignored) {
                return false;
            }
        }
        if (StringTools.isTrimEmpty(userDn) || StringTools.isTrimEmpty(password)) {
            return false;
        }
        env.put(Context.SECURITY_PRINCIPAL, userDn);
        env.put(Context.SECURITY_CREDENTIALS, password);
        DirContext ctx = null;
        NamingEnumeration<SearchResult> results = null;
        try {
            ctx = new InitialDirContext(env);
            SearchControls controls = new SearchControls();
            controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            results = ctx.search("", query, controls);
            if (results.hasMore()) {
                SearchResult searchResult = results.next();
                Attributes attributes = searchResult.getAttributes();
                if (attributes.get(passwordAttributeName) == null) {
                    return false;
                }
                String pass = new String((byte[]) attributes.get(passwordAttributeName).get());
                if (pass.startsWith("{SHA}") || pass.startsWith("{MD5}")) {
                    String method = pass.substring(1, pass.indexOf('}'));
                    MessageDigest digest = MessageDigest.getInstance(method);
                    digest.update(loginPassword.getBytes(), 0, loginPassword.length());
                    if (pass.equals("{" + method + "}" + Base64.encode(digest.digest()))) {
                        return true;
                    }
                } else {
                    if (pass.equals(loginPassword)) {
                        return true;
                    }
                }
            }
        } catch (Exception x) {
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (Exception e) {
                }
            }
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (Exception e) {
                }
            }
        }
        return false;
    }
