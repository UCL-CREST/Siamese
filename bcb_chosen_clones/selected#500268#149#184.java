    void loadSVG(String svgFileURL) {
        try {
            URL url = new URL(svgFileURL);
            URLConnection c = url.openConnection();
            c.setRequestProperty("Accept-Encoding", "gzip");
            InputStream is = c.getInputStream();
            String encoding = c.getContentEncoding();
            if ("gzip".equals(encoding) || "x-gzip".equals(encoding) || svgFileURL.toLowerCase().endsWith(".svgz")) {
                is = new GZIPInputStream(is);
            }
            is = new BufferedInputStream(is);
            Document svgDoc = AppletUtils.parse(is, false);
            if (svgDoc != null) {
                if (grMngr.mainView.isBlank() == null) {
                    grMngr.mainView.setBlank(cfgMngr.backgroundColor);
                }
                SVGReader.load(svgDoc, grMngr.mSpace, true, svgFileURL);
                grMngr.seekBoundingBox();
                grMngr.buildLogicalStructure();
                ConfigManager.defaultFont = VText.getMainFont();
                grMngr.reveal();
                if (grMngr.previousLocations.size() == 1) {
                    grMngr.previousLocations.removeElementAt(0);
                }
                if (grMngr.rView != null) {
                    grMngr.rView.getGlobalView(grMngr.mSpace.getCamera(1), 100);
                }
                grMngr.cameraMoved(null, null, 0);
            } else {
                System.err.println("An error occured while loading file " + svgFileURL);
            }
        } catch (Exception ex) {
            grMngr.reveal();
            ex.printStackTrace();
        }
    }
