    @Override
    public String execute() throws Exception {
        SystemContext sc = getSystemContext();
        if (sc.getExpireTime() == -1) {
            return LOGIN;
        } else if (upload != null) {
            try {
                Enterprise e = LicenceUtils.get(upload);
                sc.setEnterpriseName(e.getEnterpriseName());
                sc.setExpireTime(e.getExpireTime());
                String webPath = ServletActionContext.getServletContext().getRealPath("/");
                File desFile = new File(webPath, LicenceUtils.LICENCE_FILE_NAME);
                FileChannel sourceChannel = new FileInputStream(upload).getChannel();
                FileChannel destinationChannel = new FileOutputStream(desFile).getChannel();
                sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
                sourceChannel.close();
                destinationChannel.close();
                return LOGIN;
            } catch (Exception e) {
            }
        }
        return "license";
    }
