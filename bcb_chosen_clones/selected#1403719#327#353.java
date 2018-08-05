    public String generateMappackMD5(File mapPackFile) throws IOException, NoSuchAlgorithmException {
        ZipFile zip = new ZipFile(mapPackFile);
        try {
            Enumeration<? extends ZipEntry> entries = zip.entries();
            MessageDigest md5Total = MessageDigest.getInstance("MD5");
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.isDirectory()) continue;
                String name = entry.getName();
                if (name.toUpperCase().startsWith("META-INF")) continue;
                md5.reset();
                InputStream in = zip.getInputStream(entry);
                byte[] data = Utilities.getInputBytes(in);
                in.close();
                byte[] digest = md5.digest(data);
                log.trace("Hashsum " + Hex.encodeHexString(digest) + " includes \"" + name + "\"");
                md5Total.update(digest);
                md5Total.update(name.getBytes());
            }
            String md5sum = Hex.encodeHexString(md5Total.digest());
            log.trace("md5sum of " + mapPackFile.getName() + ": " + md5sum);
            return md5sum;
        } finally {
            zip.close();
        }
    }
