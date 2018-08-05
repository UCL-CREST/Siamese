    public HashCash(String cash) throws NoSuchAlgorithmException {
        myToken = cash;
        String[] parts = cash.split(":");
        myVersion = Integer.parseInt(parts[0]);
        if (myVersion < 0 || myVersion > 1) throw new IllegalArgumentException("Only supported versions are 0 and 1");
        if ((myVersion == 0 && parts.length != 6) || (myVersion == 1 && parts.length != 7)) throw new IllegalArgumentException("Improperly formed HashCash");
        try {
            int index = 1;
            if (myVersion == 1) myValue = Integer.parseInt(parts[index++]); else myValue = 0;
            SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString);
            Calendar tempCal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            tempCal.setTime(dateFormat.parse(parts[index++]));
            myResource = parts[index++];
            myExtensions = deserializeExtensions(parts[index++]);
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(cash.getBytes());
            byte[] tempBytes = md.digest();
            int tempValue = numberOfLeadingZeros(tempBytes);
            if (myVersion == 0) myValue = tempValue; else if (myVersion == 1) myValue = (tempValue > myValue ? myValue : tempValue);
        } catch (java.text.ParseException ex) {
            throw new IllegalArgumentException("Improperly formed HashCash", ex);
        }
    }
