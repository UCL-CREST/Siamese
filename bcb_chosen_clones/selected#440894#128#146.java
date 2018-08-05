    public static void _save(PortletRequest req, PortletResponse res, PortletConfig config, ActionForm form) throws Exception {
        try {
            String filePath = getUserManagerConfigPath() + "user_manager_config.properties";
            String tmpFilePath = UtilMethods.getTemporaryDirPath() + "user_manager_config_properties.tmp";
            File from = new java.io.File(tmpFilePath);
            from.createNewFile();
            File to = new java.io.File(filePath);
            to.createNewFile();
            FileChannel srcChannel = new FileInputStream(from).getChannel();
            FileChannel dstChannel = new FileOutputStream(to).getChannel();
            dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
            srcChannel.close();
            dstChannel.close();
        } catch (NonWritableChannelException we) {
        } catch (IOException e) {
            Logger.error(UserManagerPropertiesFactory.class, "Property File save Failed " + e, e);
        }
        SessionMessages.add(req, "message", "message.usermanager.display.save");
    }
