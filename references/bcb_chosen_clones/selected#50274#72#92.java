    public ClientDTO changePassword(String pMail, String pMdp) {
        Client vClientBean = null;
        ClientDTO vClientDTO = null;
        vClientBean = mClientDao.getClient(pMail);
        if (vClientBean != null) {
            MessageDigest vMd5Instance;
            try {
                vMd5Instance = MessageDigest.getInstance("MD5");
                vMd5Instance.reset();
                vMd5Instance.update(pMdp.getBytes());
                byte[] vDigest = vMd5Instance.digest();
                BigInteger vBigInt = new BigInteger(1, vDigest);
                String vHashPassword = vBigInt.toString(16);
                vClientBean.setMdp(vHashPassword);
                vClientDTO = BeanToDTO.getInstance().createClientDTO(vClientBean);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return vClientDTO;
    }
