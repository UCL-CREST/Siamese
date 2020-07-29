    public void run() {
        Shell Shell = new Shell();
        String Cmd[] = { "firefox", Link };
        String LaunchRes = Shell.sendShellCommand(Cmd);
        if (LaunchRes.contains("CritERROR!!!")) {
            String MCmd[] = { "open", Link };
            String MLaunchRes = Shell.sendShellCommand(MCmd);
            if (MLaunchRes.contains("CritERROR!!!")) {
                String WCmd[] = { "explorer", Link };
                String WLaunchRes = Shell.sendShellCommand(WCmd);
                if (WLaunchRes.contains("CritERROR!!!")) {
                    if (Desktop.isDesktopSupported()) {
                        Desktop desktop;
                        desktop = Desktop.getDesktop();
                        URI uri = null;
                        try {
                            uri = new URI(Link);
                            desktop.browse(uri);
                        } catch (IOException ioe) {
                        } catch (URISyntaxException use) {
                        }
                    }
                }
            }
        }
    }
