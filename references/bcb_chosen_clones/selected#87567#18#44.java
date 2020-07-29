    public static String createPseudoUUID() {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(new UID().toString().getBytes());
            try {
                String localHost = InetAddress.getLocalHost().toString();
                messageDigest.update(localHost.getBytes());
            } catch (UnknownHostException e) {
                throw new OXFException(e);
            }
            byte[] digestBytes = messageDigest.digest();
            StringBuffer sb = new StringBuffer();
            sb.append(toHexString(NumberUtils.readIntBigEndian(digestBytes, 0)));
            sb.append('-');
            sb.append(toHexString(NumberUtils.readShortBigEndian(digestBytes, 4)));
            sb.append('-');
            sb.append(toHexString(NumberUtils.readShortBigEndian(digestBytes, 6)));
            sb.append('-');
            sb.append(toHexString(NumberUtils.readShortBigEndian(digestBytes, 8)));
            sb.append('-');
            sb.append(toHexString(NumberUtils.readShortBigEndian(digestBytes, 10)));
            sb.append(toHexString(NumberUtils.readIntBigEndian(digestBytes, 12)));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new OXFException(e);
        }
    }
