    Object onSuccess() {
        this.mErrorExist = true;
        this.mErrorMdp = true;
        if (this.mNewMail.equals(mClient.getEmail()) || !this.mNewMail.equals(mClient.getEmail()) && !mClientManager.exists(this.mNewMail)) {
            this.mErrorExist = false;
            if (mNewMdp != null && mNewMdpConfirm != null) {
                if (this.mNewMdp.equals(this.mNewMdpConfirm)) {
                    this.mErrorMdp = false;
                    MessageDigest sha1Instance;
                    try {
                        sha1Instance = MessageDigest.getInstance("SHA1");
                        sha1Instance.reset();
                        sha1Instance.update(this.mNewMdp.getBytes());
                        byte[] digest = sha1Instance.digest();
                        BigInteger bigInt = new BigInteger(1, digest);
                        String vHashPassword = bigInt.toString(16);
                        mClient.setMdp(vHashPassword);
                    } catch (NoSuchAlgorithmException e) {
                        mLogger.error(e.getMessage(), e);
                    }
                }
            } else {
                this.mErrorMdp = false;
            }
            if (!this.mErrorMdp) {
                mClient.setAdresse(this.mNewAdresse);
                mClient.setEmail(this.mNewMail);
                mClient.setNom(this.mNewNom);
                mClient.setPrenom((this.mNewPrenom != null ? this.mNewPrenom : ""));
                Client vClient = new Client(mClient);
                mClientManager.save(vClient);
                mComponentResources.discardPersistentFieldChanges();
                return "Client/List";
            }
        }
        return errorZone.getBody();
    }
