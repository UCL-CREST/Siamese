    public static String sendScripts(Session session) {
        Channel channel = null;
        String tempDirectory = "";
        Logger.getLogger(RsyncHelper.class.getName()).log(Level.INFO, "Start sendScripts.");
        try {
            {
                channel = session.openChannel("exec");
                final String command = "mktemp -d /tmp/scipionXXXXXXXX";
                ((ChannelExec) channel).setCommand(command);
                InputStream in = channel.getInputStream();
                channel.connect();
                String[] result = inputStreamToString(in, channel);
                tempDirectory = result[1];
                tempDirectory = tempDirectory.replaceAll("\n", "");
                Logger.getLogger(RsyncHelper.class.getName()).log(Level.INFO, "status:" + result[0] + "-command:" + command + "-result:" + tempDirectory);
                IOUtils.closeQuietly(in);
                channel.disconnect();
            }
            {
                channel = session.openChannel("exec");
                final String command = "chmod 700 " + tempDirectory;
                ((ChannelExec) channel).setCommand(command);
                InputStream in = channel.getInputStream();
                channel.connect();
                String[] result = inputStreamToString(in, channel);
                Logger.getLogger(RsyncHelper.class.getName()).log(Level.INFO, "status:" + result[0] + "-command:" + command + "-result:" + result[1]);
                IOUtils.closeQuietly(in);
                channel.disconnect();
            }
            {
                InputStream rsyncHelperContentInput = Thread.currentThread().getContextClassLoader().getResourceAsStream("scripts/" + RSYNC_HELPER_SCRIPT);
                channel = session.openChannel("exec");
                final String command = "cat > " + tempDirectory + "/" + RSYNC_HELPER_SCRIPT;
                ((ChannelExec) channel).setCommand(command);
                OutputStream out = channel.getOutputStream();
                channel.connect();
                IOUtils.copy(rsyncHelperContentInput, out);
                IOUtils.closeQuietly(out);
                channel.disconnect();
            }
            {
                channel = session.openChannel("exec");
                final String command = "chmod 700 " + tempDirectory + "/" + RSYNC_HELPER_SCRIPT;
                ((ChannelExec) channel).setCommand(command);
                InputStream in = channel.getInputStream();
                channel.connect();
                String[] result = inputStreamToString(in, channel);
                Logger.getLogger(RsyncHelper.class.getName()).log(Level.INFO, "status:" + result[0] + "-command:" + command + "-result:" + result[1]);
                IOUtils.closeQuietly(in);
                channel.disconnect();
            }
            {
                InputStream askPassContentInput = Thread.currentThread().getContextClassLoader().getResourceAsStream("scripts/" + RSYNC_ASKPASS_SCRIPT);
                channel = session.openChannel("exec");
                final String command = "cat > " + tempDirectory + "/" + RSYNC_ASKPASS_SCRIPT;
                ((ChannelExec) channel).setCommand(command);
                OutputStream out = channel.getOutputStream();
                channel.connect();
                IOUtils.copy(askPassContentInput, out);
                IOUtils.closeQuietly(out);
                channel.disconnect();
            }
            {
                channel = session.openChannel("exec");
                final String command = "chmod 700 " + tempDirectory + "/" + RSYNC_ASKPASS_SCRIPT;
                ((ChannelExec) channel).setCommand(command);
                InputStream in = channel.getInputStream();
                channel.connect();
                String[] result = inputStreamToString(in, channel);
                Logger.getLogger(RsyncHelper.class.getName()).log(Level.INFO, "status:" + result[0] + "-command:" + command + "-result:" + result[1]);
                IOUtils.closeQuietly(in);
                channel.disconnect();
            }
        } catch (IOException ex) {
            Logger.getLogger(RsyncHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSchException ex) {
            Logger.getLogger(RsyncHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        Logger.getLogger(RsyncHelper.class.getName()).log(Level.INFO, "End sendScripts.");
        return tempDirectory;
    }
