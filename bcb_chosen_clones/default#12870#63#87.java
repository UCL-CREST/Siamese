    public static void openURL(URL url) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException, InterruptedException {
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Mac OS")) {
            Class fileMgr = Class.forName("com.apple.eio.FileManager");
            Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[] { String.class });
            openURL.invoke(null, new Object[] { url.toString() });
        } else if (osName.startsWith("Windows")) {
            String cmdLine = "rundll32 url.dll,FileProtocolHandler " + url.toString();
            Process exec = Runtime.getRuntime().exec(cmdLine);
            exec.waitFor();
        } else {
            String[] browsers = { "firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape" };
            String browser = null;
            for (int count = 0; count < browsers.length && browser == null; count++) {
                if (Runtime.getRuntime().exec(new String[] { "which", browsers[count] }).waitFor() == 0) {
                    browser = browsers[count];
                }
            }
            if (browser == null) {
                throw new IllegalStateException("Could not find web browser");
            } else {
                Runtime.getRuntime().exec(new String[] { browser, url.toString() });
            }
        }
    }
