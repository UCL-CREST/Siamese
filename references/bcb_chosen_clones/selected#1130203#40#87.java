    public void run() {
        List<String> remoteVersions = new LinkedList<String>();
        for (String s : VERSION_URLS) {
            URL url = null;
            try {
                url = new URL(s);
            } catch (MalformedURLException e) {
                LogService.getGlobal().log("Cannot create update target url: " + e.getMessage(), LogService.ERROR);
            }
            if (url != null) {
                BufferedReader in = null;
                try {
                    in = new BufferedReader(new InputStreamReader(url.openStream()));
                    String remoteVersion = in.readLine();
                    if ((remoteVersion != null) && (remoteVersion.length() > 0) && (Character.isDigit(remoteVersion.charAt(0)))) {
                        remoteVersions.add(remoteVersion);
                    }
                } catch (IOException e) {
                    LogService.getGlobal().log("Not able to check for updates. Maybe no internet connection.", LogService.WARNING);
                } finally {
                    try {
                        if (in != null) in.close();
                    } catch (IOException e) {
                        throw new Error(e);
                    }
                }
            }
        }
        if (remoteVersions.size() > 0) {
            RapidMinerGUI.saveLastUpdateCheckDate();
        }
        Iterator<String> i = remoteVersions.iterator();
        VersionNumber newestVersion = getVersionNumber(Version.getLongVersion());
        while (i.hasNext()) {
            String remoteVersionString = i.next();
            if (remoteVersionString != null) {
                VersionNumber remoteVersion = getVersionNumber(remoteVersionString);
                if (isNewer(remoteVersion, newestVersion)) {
                    newestVersion = remoteVersion;
                }
            }
        }
        if ((newestVersion != null) && (isNewer(newestVersion, getVersionNumber(Version.getLongVersion())))) {
            JOptionPane.showMessageDialog(mainFrame, "New version of the RapidMiner Community Edition is available:" + Tools.getLineSeparator() + Tools.getLineSeparator() + "          RapidMiner " + newestVersion + Tools.getLineSeparator() + Tools.getLineSeparator() + "Please download it from:" + Tools.getLineSeparator() + "          http://www.rapidminer.com", "New RapidMiner version", JOptionPane.INFORMATION_MESSAGE);
        } else if (showFailureDialog) {
            JOptionPane.showMessageDialog(mainFrame, "No newer versions of the RapidMiner Community Edition available!", "RapidMiner CE is up to date", JOptionPane.INFORMATION_MESSAGE);
        }
    }
