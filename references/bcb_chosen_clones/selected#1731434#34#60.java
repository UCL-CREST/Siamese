    public boolean checkLogin(String pMail, String pMdp) {
        boolean vLoginOk = false;
        if (pMail == null || pMdp == null) {
            throw new IllegalArgumentException("Login and password are mandatory. Null values are forbidden.");
        }
        try {
            Criteria crit = ((Session) this.entityManager.getDelegate()).createCriteria(Client.class);
            crit.add(Restrictions.ilike("email", pMail));
            MessageDigest vMd5Instance;
            try {
                vMd5Instance = MessageDigest.getInstance("MD5");
                vMd5Instance.reset();
                vMd5Instance.update(pMdp.getBytes());
                byte[] vDigest = vMd5Instance.digest();
                BigInteger vBigInt = new BigInteger(1, vDigest);
                String vHashPassword = vBigInt.toString(16);
                crit.add(Restrictions.eq("mdp", vHashPassword));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            Client pClient = (Client) crit.uniqueResult();
            vLoginOk = (pClient != null);
        } catch (DataAccessException e) {
            mLogger.error("Exception - DataAccessException occurs : {} on complete checkLogin( {}, {} )", new Object[] { e.getMessage(), pMail, pMdp });
        }
        return vLoginOk;
    }
