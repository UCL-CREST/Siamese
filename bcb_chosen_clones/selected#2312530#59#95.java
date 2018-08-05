    static boolean writeProperties(Map<String, String> customProps, File destination) throws IOException {
        synchronized (PropertiesIO.class) {
            L.info(Msg.msg("PropertiesIO.writeProperties.start"));
            File tempFile = null;
            BufferedInputStream existingCfgInStream = null;
            FileInputStream in = null;
            FileOutputStream out = null;
            PrintStream ps = null;
            FileChannel fromChannel = null, toChannel = null;
            String line = null;
            try {
                existingCfgInStream = new BufferedInputStream(destination.exists() ? new FileInputStream(destination) : defaultPropertiesStream());
                tempFile = File.createTempFile("properties-", ".tmp", null);
                ps = new PrintStream(tempFile);
                while ((line = Utils.readLine(existingCfgInStream)) != null) {
                    String lineReady2write = setupLine(line, customProps);
                    ps.println(lineReady2write);
                }
                destination.getParentFile().mkdirs();
                in = new FileInputStream(tempFile);
                out = new FileOutputStream(destination, false);
                fromChannel = in.getChannel();
                toChannel = out.getChannel();
                fromChannel.transferTo(0, fromChannel.size(), toChannel);
                L.info(Msg.msg("PropertiesIO.writeProperties.done").replace("#file#", destination.getAbsolutePath()));
                return true;
            } finally {
                if (existingCfgInStream != null) existingCfgInStream.close();
                if (ps != null) ps.close();
                if (fromChannel != null) fromChannel.close();
                if (toChannel != null) toChannel.close();
                if (out != null) out.close();
                if (in != null) in.close();
                if (tempFile != null && tempFile.exists()) tempFile.delete();
            }
        }
    }
