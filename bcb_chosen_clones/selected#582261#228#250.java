    private void _save(ActionRequest req, ActionResponse res, PortletConfig config, ActionForm form) throws Exception {
        List list = (List) req.getAttribute(WebKeys.LANGUAGE_MANAGER_LIST);
        for (int i = 0; i < list.size(); i++) {
            long langId = ((Language) list.get(i)).getId();
            try {
                String filePath = getGlobalVariablesPath() + "cms_language_" + langId + ".properties";
                String tmpFilePath = getTemporyDirPath() + "cms_language_" + langId + "_properties.tmp";
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
                Logger.error(this, "Property File save Failed " + e, e);
            }
        }
        SessionMessages.add(req, "message", "message.languagemanager.save");
    }
