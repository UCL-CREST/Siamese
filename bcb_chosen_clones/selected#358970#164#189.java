    public synchronized String getSerialNumber() {
        if (serialNum != null) return serialNum;
        final StringBuffer buf = new StringBuffer();
        Iterator it = classpath.iterator();
        while (it.hasNext()) {
            ClassPathEntry entry = (ClassPathEntry) it.next();
            buf.append(entry.getResourceURL().toString());
            buf.append(":");
        }
        serialNum = (String) AccessController.doPrivileged(new PrivilegedAction() {

            public Object run() {
                try {
                    MessageDigest digest = MessageDigest.getInstance("SHA");
                    digest.update(buf.toString().getBytes());
                    byte[] data = digest.digest();
                    serialNum = new BASE64Encoder().encode(data);
                    return serialNum;
                } catch (NoSuchAlgorithmException exp) {
                    BootSecurityManager.securityLogger.log(Level.SEVERE, exp.getMessage(), exp);
                    return buf.toString();
                }
            }
        });
        return serialNum;
    }
