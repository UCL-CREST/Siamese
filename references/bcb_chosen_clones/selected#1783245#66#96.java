    private void checkForNewVersion() {
        try {
            org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(net.xan.taskstack.TaskStackApp.class).getContext().getResourceMap(NewTaskDialog.class);
            String versionUrl = resourceMap.getString("Application.versionFileUrl");
            long startTime = System.currentTimeMillis();
            System.out.println("Retrieving version file from\n" + versionUrl);
            URL url = new URL(versionUrl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            while ((str = in.readLine()) != null) {
                if (str.startsWith("LastVersion")) {
                    String remoteVersion = str.substring(str.indexOf("=") + 1);
                    String localVersion = resourceMap.getString("Application.version");
                    System.out.println("Version file found");
                    System.out.println("Local version: " + localVersion);
                    System.out.println("Remote version: " + remoteVersion);
                    if (remoteVersion.compareTo(localVersion) > 0) {
                        askDownloadNewVersion(remoteVersion, localVersion);
                    }
                    break;
                }
            }
            long endTime = System.currentTimeMillis();
            System.out.println("Elapsed time " + (endTime - startTime) + "ms");
            in.close();
        } catch (MalformedURLException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
