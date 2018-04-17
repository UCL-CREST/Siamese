    private String generateUniqueIdMD5(Run run, HttpServletRequest request, String groupIdString) {
        String portalUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String uniqueportalUrl = portalUrl + "run:" + run.getId().toString() + "group:" + groupIdString;
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        m.update(uniqueportalUrl.getBytes(), 0, uniqueportalUrl.length());
        String uniqueIdMD5 = new BigInteger(1, m.digest()).toString(16);
        return uniqueIdMD5;
    }
