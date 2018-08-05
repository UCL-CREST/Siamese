    public static void openURL(String url) throws Exception {
        if (osName.startsWith("Mac OS")) {
            Class<?> fileMgr = Class.forName("com.apple.eio.FileManager");
            Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[] { String.class });
            openURL.invoke(null, new Object[] { url });
        } else if (osName.startsWith("Windows")) {
            rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
        } else {
            boolean found = false;
            for (String browser : browsers) {
                if (!found) {
                    found = rt.exec(new String[] { "which", browser }).waitFor() == 0;
                    if (found) rt.exec(new String[] { browser, url });
                }
            }
            if (!found) throw new Exception("Could not launch any web browser");
        }
    }
