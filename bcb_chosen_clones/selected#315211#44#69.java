    public static IDesignTimeProcess<?> createProcess(Class<?> iProcessClass, WebUser webUser) throws Exception {
        if (!EmailConfig.isUserEmail()) {
            return null;
        }
        if (EmailConfig.isInternalEmail()) {
            return ProcessFactory.createProcess(iProcessClass);
        } else {
            String cn = iProcessClass.getName() + "ImapBean";
            Class<?> imapClass = Class.forName(cn);
            try {
                Constructor<?> constructor = imapClass.getConstructor(ProtocolFactory.class);
                if (constructor != null) {
                    ConnectionProfile profile = EmailConfig.getConnectionProfile();
                    EmailUser user = webUser.getEmailUser();
                    ConnectionMetaHandler handler = webUser.getConnectionMetaHandler();
                    AuthProfile auth = new AuthProfile();
                    auth.setUserName(user.getAccount());
                    auth.setPassword(user.getPassword());
                    ProtocolFactory factory = new ProtocolFactory(profile, auth, handler);
                    return (IDesignTimeProcess<?>) constructor.newInstance(factory);
                }
            } catch (Exception e) {
            }
            return (IDesignTimeProcess<?>) imapClass.newInstance();
        }
    }
