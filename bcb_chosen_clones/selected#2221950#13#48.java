    public static void browse(String url) throws IOException, SecurityException, NoSuchMethodException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, InterruptedException {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.browse(new java.net.URI(url));
                    return;
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                } catch (java.net.URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Windows")) {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
        } else if (osName.startsWith("Mac OS")) {
            Class<?> fileMgr = Class.forName("com.apple.eio.FileManager");
            java.lang.reflect.Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[] { String.class });
            openURL.invoke(null, new Object[] { url });
        } else {
            java.util.Map<String, String> env = System.getenv();
            if (env.get("BROWSER") != null) {
                Runtime.getRuntime().exec(env.get("BROWSER") + " " + url);
                return;
            }
            String[] browsers = { "firefox", "iceweasel", "chrome", "opera", "konqueror", "epiphany", "mozilla", "netscape" };
            String browser = null;
            for (int count = 0; count < browsers.length && browser == null; count++) if (Runtime.getRuntime().exec(new String[] { "which", browsers[count] }).waitFor() == 0) {
                browser = browsers[count];
                break;
            }
            if (browser == null) throw new RuntimeException("couldn't find any browser..."); else Runtime.getRuntime().exec(new String[] { browser, url });
        }
    }
