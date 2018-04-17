    Object onSuccess() {
        this.mErrorExist = true;
        this.mErrorMdp = true;
        if (!mClientManager.exists(this.mNewMail)) {
            this.mErrorExist = false;
            if (mNewMdp.equals(mNewMdpConfirm)) {
                this.mErrorMdp = false;
                MessageDigest sha1Instance;
                try {
                    sha1Instance = MessageDigest.getInstance("SHA1");
                    sha1Instance.reset();
                    sha1Instance.update(this.mNewMdp.getBytes());
                    byte[] digest = sha1Instance.digest();
                    BigInteger bigInt = new BigInteger(1, digest);
                    String vHashPassword = bigInt.toString(16);
                    Client vClient = new Client(this.mNewNom, (this.mNewPrenom != null ? this.mNewPrenom : ""), this.mNewMail, vHashPassword, this.mNewAdresse, 1);
                    mClientManager.save(vClient);
                    mComponentResources.discardPersistentFieldChanges();
                    return "Client/List";
                } catch (NoSuchAlgorithmException e) {
                    mLogger.error(e.getMessage(), e);
                }
            }
        }
        return errorZone.getBody();
    }
