    public static HashCash mintCash(String resource, Map<String, List<String>> extensions, Calendar date, int value, int version) throws NoSuchAlgorithmException {
        if (version < 0 || version > 1) throw new IllegalArgumentException("Only supported versions are 0 and 1");
        if (value < 0 || value > hashLength) throw new IllegalArgumentException("Value must be between 0 and " + hashLength);
        if (resource.contains(":")) throw new IllegalArgumentException("Resource must not contain ':'");
        HashCash result = new HashCash();
        MessageDigest md = MessageDigest.getInstance("SHA1");
        result.myResource = resource;
        result.myExtensions = (null == extensions ? new HashMap<String, List<String>>() : extensions);
        result.myDate = date;
        result.myVersion = version;
        String prefix;
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString);
        switch(version) {
            case 0:
                prefix = version + ":" + dateFormat.format(date.getTime()) + ":" + resource + ":" + serializeExtensions(extensions) + ":";
                result.myToken = generateCash(prefix, value, md);
                md.reset();
                md.update(result.myToken.getBytes());
                result.myValue = numberOfLeadingZeros(md.digest());
                break;
            case 1:
                result.myValue = value;
                prefix = version + ":" + value + ":" + dateFormat.format(date.getTime()) + ":" + resource + ":" + serializeExtensions(extensions) + ":";
                result.myToken = generateCash(prefix, value, md);
                break;
            default:
                throw new IllegalArgumentException("Only supported versions are 0 and 1");
        }
        return result;
    }
