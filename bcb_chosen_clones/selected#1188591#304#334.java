    private void doLogin(String password) throws LoginFailedException, IncorrectPasswordException {
        final long mgr = Constants.MANAGER;
        Data data, response;
        try {
            response = sendAndWait(new Request(mgr)).get(0);
            MessageDigest md;
            try {
                md = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("MD5 hash not supported.");
            }
            byte[] challenge = response.getBytes();
            md.update(challenge);
            md.update(password.getBytes(Data.STRING_ENCODING));
            data = Data.valueOf(md.digest());
            try {
                response = sendAndWait(new Request(mgr).add(0, data)).get(0);
            } catch (ExecutionException ex) {
                throw new IncorrectPasswordException();
            }
            setLoginMessage(response.getString());
            response = sendAndWait(new Request(mgr).add(0, getLoginData())).get(0);
            setID(response.getWord());
        } catch (InterruptedException ex) {
            throw new LoginFailedException(ex);
        } catch (ExecutionException ex) {
            throw new LoginFailedException(ex);
        } catch (IOException ex) {
            throw new LoginFailedException(ex);
        }
    }
