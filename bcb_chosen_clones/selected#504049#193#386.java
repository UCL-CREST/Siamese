    public byte[] evaluateResponse(byte[] responseBytes) throws SaslException {
        if (firstEvaluation) {
            firstEvaluation = false;
            StringBuilder challenge = new StringBuilder(100);
            Iterator iter = configurationManager.getRealms().values().iterator();
            Realm aRealm;
            while (iter.hasNext()) {
                aRealm = (Realm) iter.next();
                if (aRealm.getFullRealmName().equals("null")) continue;
                challenge.append("realm=\"" + aRealm.getFullRealmName() + "\"");
                challenge.append(",");
            }
            String nonceUUID = UUID.randomUUID().toString();
            String nonce = null;
            try {
                nonce = new String(Base64.encodeBase64(MD5Digest(String.valueOf(System.nanoTime() + ":" + nonceUUID))), "US-ASCII");
            } catch (UnsupportedEncodingException uee) {
                throw new SaslException(uee.getMessage(), uee);
            } catch (GeneralSecurityException uee) {
                throw new SaslException(uee.getMessage(), uee);
            }
            nonces.put(nonce, new ArrayList());
            nonces.get(nonce).add(Integer.valueOf(1));
            challenge.append("nonce=\"" + nonce + "\"");
            challenge.append(",");
            challenge.append("qop=\"" + configurationManager.getSaslQOP() + "\"");
            challenge.append(",");
            challenge.append("charset=\"utf-8\"");
            challenge.append(",");
            challenge.append("algorithm=\"md5-sess\"");
            if (configurationManager.getSaslQOP().indexOf("auth-conf") != -1) {
                challenge.append(",");
                challenge.append("cipher-opts=\"" + configurationManager.getDigestMD5Ciphers() + "\"");
            }
            try {
                return Base64.encodeBase64(challenge.toString().getBytes("US-ASCII"));
            } catch (UnsupportedEncodingException uee) {
                throw new SaslException(uee.getMessage(), uee);
            }
        } else {
            String nonce = null;
            if (!Base64.isArrayByteBase64(responseBytes)) {
                throw new SaslException("Can not decode Base64 Content", new MalformedBase64ContentException());
            }
            responseBytes = Base64.decodeBase64(responseBytes);
            List<byte[]> splittedBytes = splitByteArray(responseBytes, (byte) 0x3d);
            int tokenCountMinus1 = splittedBytes.size() - 1, lastCommaPos;
            Map rawDirectives = new HashMap();
            String key = null;
            Map<String, String> directives;
            try {
                key = new String(splittedBytes.get(0), "US-ASCII");
                for (int i = 1; i < tokenCountMinus1; i++) {
                    key = responseTokenProcessor(splittedBytes, rawDirectives, key, i, tokenCountMinus1);
                }
                responseTokenProcessor(splittedBytes, rawDirectives, key, tokenCountMinus1, tokenCountMinus1);
                if (rawDirectives.containsKey("charset")) {
                    String value = new String((byte[]) rawDirectives.get("charset"), "US-ASCII").toLowerCase(locale);
                    if (value.equals("utf-8")) {
                        encoding = "UTF-8";
                    }
                }
                if (encoding.equals("ISO-8859-1")) {
                    decodeAllAs8859(rawDirectives);
                } else {
                    decodeMixed(rawDirectives);
                }
                directives = rawDirectives;
            } catch (UnsupportedEncodingException uee) {
                throw new SaslException(uee.getMessage());
            }
            if (!directives.containsKey("username") || !directives.containsKey("nonce") || !directives.containsKey("nc") || !directives.containsKey("cnonce") || !directives.containsKey("response")) {
                throw new SaslException("Digest-Response lacks at least one neccesery key-value pair");
            }
            if (directives.get("username").indexOf('@') != -1) {
                throw new SaslException("digest-response username field must not include domain name", new AuthenticationException());
            }
            if (!directives.containsKey("qop")) {
                directives.put("qop", QOP_AUTH);
            }
            if (!directives.containsKey("realm") || ((String) directives.get("realm")).equals("")) {
                directives.put("realm", "null");
            }
            nonce = (String) directives.get("nonce");
            if (!nonces.containsKey(nonce)) {
                throw new SaslException("Illegal nonce value");
            }
            List<Integer> nonceListInMap = nonces.get(nonce);
            int nc = Integer.parseInt((String) directives.get("nc"), 16);
            if (nonceListInMap.get(nonceListInMap.size() - 1).equals(Integer.valueOf(nc))) {
                nonceListInMap.add(Integer.valueOf(++nc));
            } else {
                throw new SaslException("Illegal nc value");
            }
            nonceListInMap = null;
            if (directives.get("qop").equals(QOP_AUTH_INT)) integrity = true; else if (directives.get("qop").equals(QOP_AUTH_CONF)) privacy = true;
            if (privacy) {
                if (!directives.containsKey("cipher")) {
                    throw new SaslException("Message confidentially required but cipher entry is missing");
                }
                sessionCipher = directives.get("cipher").toLowerCase(locale);
                if ("3des,des,rc4-40,rc4,rc4-56".indexOf(sessionCipher) == -1) {
                    throw new SaslException("Unsupported cipher for message confidentiality");
                }
            }
            String realm = directives.get("realm").toLowerCase(Locale.getDefault());
            String username = directives.get("username").toLowerCase(locale);
            if (username.indexOf('@') == -1) {
                if (!directives.get("realm").equals("null")) {
                    username += directives.get("realm").substring(directives.get("realm").indexOf('@'));
                } else if (directives.get("authzid").indexOf('@') != -1) {
                    username += directives.get("authzid").substring(directives.get("authzid").indexOf('@'));
                }
            }
            DomainWithPassword domainWithPassword = configurationManager.getRealmPassword(realm, username);
            if (domainWithPassword == null || domainWithPassword.getPassword() == null) {
                log.warn("The supplied username and/or realm do(es) not match a registered entry");
                return null;
            }
            if (realm.equals("null") && username.indexOf('@') == -1) {
                username += "@" + domainWithPassword.getDomain();
            }
            byte[] HA1 = toByteArray(domainWithPassword.getPassword());
            for (int i = domainWithPassword.getPassword().length - 1; i >= 0; i--) {
                domainWithPassword.getPassword()[i] = 0xff;
            }
            domainWithPassword = null;
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("MD5");
            } catch (GeneralSecurityException gse) {
                throw new SaslException(gse.getMessage());
            }
            md.update(HA1);
            md.update(":".getBytes());
            md.update((directives.get("nonce")).getBytes());
            md.update(":".getBytes());
            md.update((directives.get("cnonce")).getBytes());
            if (directives.containsKey("authzid")) {
                md.update(":".getBytes());
                md.update((directives.get("authzid")).getBytes());
            }
            MD5DigestSessionKey = HA1 = md.digest();
            String MD5DigestSessionKeyToHex = toHex(HA1, HA1.length);
            md.update("AUTHENTICATE".getBytes());
            md.update(":".getBytes());
            md.update((directives.get("digest-uri")).getBytes());
            if (!directives.get("qop").equals(QOP_AUTH)) {
                md.update(":".getBytes());
                md.update("00000000000000000000000000000000".getBytes());
            }
            byte[] HA2 = md.digest();
            String HA2HEX = toHex(HA2, HA2.length);
            md.update(MD5DigestSessionKeyToHex.getBytes());
            md.update(":".getBytes());
            md.update((directives.get("nonce")).getBytes());
            md.update(":".getBytes());
            md.update((directives.get("nc")).getBytes());
            md.update(":".getBytes());
            md.update((directives.get("cnonce")).getBytes());
            md.update(":".getBytes());
            md.update((directives.get("qop")).getBytes());
            md.update(":".getBytes());
            md.update(HA2HEX.getBytes());
            byte[] responseHash = md.digest();
            String HexResponseHash = toHex(responseHash, responseHash.length);
            if (HexResponseHash.equals(directives.get("response"))) {
                md.update(":".getBytes());
                md.update((directives.get("digest-uri")).getBytes());
                if (!directives.get("qop").equals(QOP_AUTH)) {
                    md.update(":".getBytes());
                    md.update("00000000000000000000000000000000".getBytes());
                }
                HA2 = md.digest();
                HA2HEX = toHex(HA2, HA2.length);
                md.update(MD5DigestSessionKeyToHex.getBytes());
                md.update(":".getBytes());
                md.update((directives.get("nonce")).getBytes());
                md.update(":".getBytes());
                md.update((directives.get("nc")).getBytes());
                md.update(":".getBytes());
                md.update((directives.get("cnonce")).getBytes());
                md.update(":".getBytes());
                md.update((directives.get("qop")).getBytes());
                md.update(":".getBytes());
                md.update(HA2HEX.getBytes());
                responseHash = md.digest();
                return finalizeAuthentication.finalize(responseHash, username);
            } else {
                log.warn("Improper credentials");
                return null;
            }
        }
    }
