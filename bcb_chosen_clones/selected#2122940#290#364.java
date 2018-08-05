    public static String extractIconPath(String siteURL) throws IOException {
        siteURL = siteURL.trim();
        if (!siteURL.startsWith("http://")) {
            siteURL = "http://" + siteURL;
        }
        URL url = new URL(siteURL);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String iconURL = null;
        String iconPath = null;
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            if (inputLine.contains("type=\"image/x-icon\"") || inputLine.toLowerCase().contains("rel=\"shortcut icon\"")) {
                String tmp = new String(inputLine);
                String[] smallLines = inputLine.replace(">", ">\n").split("\n");
                for (String smallLine : smallLines) {
                    if (smallLine.contains("type=\"image/x-icon\"") || smallLine.toLowerCase().contains("rel=\"shortcut icon\"")) {
                        tmp = smallLine;
                        break;
                    }
                }
                iconURL = tmp.replaceAll("^.*href=\"", "");
                iconURL = iconURL.replaceAll("\".*", "");
                tmp = null;
                String originalSiteURL = new String(siteURL);
                siteURL = getHome(siteURL);
                if (iconURL.charAt(0) == '/') {
                    if (siteURL.charAt(siteURL.length() - 1) == '/') {
                        iconURL = siteURL + iconURL.substring(1);
                    } else {
                        iconURL = siteURL + iconURL;
                    }
                } else if (!iconURL.startsWith("http://")) {
                    if (siteURL.charAt(siteURL.length() - 1) == '/') {
                        iconURL = siteURL + iconURL;
                    } else {
                        iconURL = siteURL + "/" + iconURL;
                    }
                }
                siteURL = originalSiteURL;
                break;
            }
            if (inputLine.contains("</head>".toLowerCase())) {
                break;
            }
        }
        in.close();
        siteURL = getHome(siteURL);
        if (iconURL == null || "".equals(iconURL.trim())) {
            iconURL = "favicon.ico";
            if (siteURL.charAt(siteURL.length() - 1) == '/') {
                iconURL = siteURL + iconURL;
            } else {
                iconURL = siteURL + "/" + iconURL;
            }
        }
        try {
            String iconFileName = siteURL;
            if (iconFileName.startsWith("http://")) {
                iconFileName = iconFileName.substring(7);
            }
            iconFileName = iconFileName.replaceAll("\\W", " ").trim().replace(" ", "_").concat(".ico");
            iconPath = JReader.getConfig().getShortcutIconsDir() + File.separator + iconFileName;
            InputStream inIcon = new URL(iconURL).openStream();
            OutputStream outIcon = new FileOutputStream(iconPath);
            byte[] buf = new byte[1024];
            int len;
            while ((len = inIcon.read(buf)) > 0) {
                outIcon.write(buf, 0, len);
            }
            inIcon.close();
            outIcon.close();
        } catch (Exception e) {
        }
        return iconPath;
    }
