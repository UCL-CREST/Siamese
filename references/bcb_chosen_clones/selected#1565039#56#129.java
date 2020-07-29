    private void getLines(PackageManager pm) throws PackageManagerException {
        final Pattern p = Pattern.compile("\\s*deb\\s+(ftp://|http://)(\\S+)\\s+((\\S+\\s*)*)(./){0,1}");
        Matcher m;
        if (updateUrlAndFile == null) updateUrlAndFile = new ArrayList<UrlAndFile>();
        BufferedReader f;
        String protocol;
        String host;
        String shares;
        String adress;
        try {
            f = new BufferedReader(new FileReader(sourcesList));
            while ((protocol = f.readLine()) != null) {
                m = p.matcher(protocol);
                if (m.matches()) {
                    protocol = m.group(1);
                    host = m.group(2);
                    if (m.group(3).trim().equalsIgnoreCase("./")) shares = ""; else shares = m.group(3).trim();
                    if (shares == null) adress = protocol + host; else {
                        shares = shares.replace(" ", "/");
                        if (!host.endsWith("/") && !shares.startsWith("/")) host = host + "/";
                        adress = host + shares;
                        while (adress.contains("//")) adress = adress.replace("//", "/");
                        adress = protocol + adress;
                    }
                    if (!adress.endsWith("/")) adress = adress + "/";
                    String changelogdir = adress;
                    changelogdir = changelogdir.substring(changelogdir.indexOf("//") + 2);
                    if (changelogdir.endsWith("/")) changelogdir = changelogdir.substring(0, changelogdir.lastIndexOf("/"));
                    changelogdir = changelogdir.replace('/', '_');
                    changelogdir = changelogdir.replaceAll("\\.", "_");
                    changelogdir = changelogdir.replaceAll("-", "_");
                    changelogdir = changelogdir.replaceAll(":", "_COLON_");
                    adress = adress + "Packages.gz";
                    final String serverFileLocation = adress.replaceAll(":", "_COLON_");
                    final NameFileLocation nfl = new NameFileLocation();
                    try {
                        final GZIPInputStream in = new GZIPInputStream(new ConnectToServer(pm).getInputStream(adress));
                        final String rename = new File(nfl.rename(serverFileLocation, listsDir)).getCanonicalPath();
                        final FileOutputStream out = new FileOutputStream(rename);
                        final byte[] buf = new byte[4096];
                        int len;
                        while ((len = in.read(buf)) > 0) out.write(buf, 0, len);
                        out.close();
                        in.close();
                        final File file = new File(rename);
                        final UrlAndFile uaf = new UrlAndFile(protocol + host, file, changelogdir);
                        updateUrlAndFile.add(uaf);
                    } catch (final Exception e) {
                        final String message = "URL: " + adress + " caused exception";
                        if (null != pm) {
                            logger.warn(message, e);
                            pm.addWarning(message + "\n" + e.toString());
                        } else logger.warn(message, e);
                        e.printStackTrace();
                    }
                }
            }
            f.close();
        } catch (final FileNotFoundException e) {
            final String message = PreferenceStoreHolder.getPreferenceStoreByName("Screen").getPreferenceAsString("sourcesList.corrupt", "Entry not found sourcesList.corrupt");
            if (null != pm) {
                logger.warn(message, e);
                pm.addWarning(message + "\n" + e.toString());
            } else logger.warn(message, e);
            e.printStackTrace();
        } catch (final IOException e) {
            final String message = PreferenceStoreHolder.getPreferenceStoreByName("Screen").getPreferenceAsString("SearchForServerFile.getLines.IOException", "Entry not found SearchForServerFile.getLines.IOException");
            if (null != pm) {
                logger.warn(message, e);
                pm.addWarning(message + "\n" + e.toString());
            } else logger.warn(message, e);
            e.printStackTrace();
        }
    }
