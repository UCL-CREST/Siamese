    public static void exportArchive(String saveLocation, Site s, final boolean images) {
        String season = s.getSeason();
        String site = s.getSite();
        String location = String.format("%s/%s/%s", Program.Settings().getDataFolder(), season, site);
        java.io.File f = new java.io.File(String.format("%s/PenguinData.xml", location));
        SiteDocument xmlSite = null;
        try {
            xmlSite = org.penguinuri.penguin.SiteDocument.Factory.parse(f);
            SiteDocument.Site sd = xmlSite.getSite();
            sd.setSeason(season);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        File baseFolder = new File(location);
        File templateFolder = new File(location + "/templates");
        FilenameFilter filter = new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                boolean isDir = new File(dir.getAbsoluteFile() + "/" + name).isDirectory();
                boolean isJPG = name.toLowerCase().endsWith(".jpg");
                boolean isXML = name.toLowerCase().endsWith(".xml");
                return !isDir && (isXML || images && isJPG);
            }
        };
        String[] baseFiles = baseFolder.list(filter);
        String[] templateFiles = templateFolder.list(filter);
        int total = 0;
        try {
            total = baseFiles.length;
            total += templateFiles.length;
        } catch (Exception ex) {
        }
        total += 2;
        byte[] buf = new byte[BUFFER_LEN];
        LoadingBar lb = new LoadingBar(total);
        lb.setVisible(true);
        try {
            File zipFile = new File(saveLocation);
            if (zipFile.exists()) {
                while (zipFile.delete()) {
                }
            }
            String outFilename = saveLocation;
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));
            lb.increment("Compressing season database objects.");
            try {
                SiteSeasonDBDocument localDB = Program.getLocalSiteDatabase();
                SiteSeasonDBDocument.SiteSeasonDB.Sites.Site siteToCopy = null;
                Sites localSites = localDB.getSiteSeasonDB().getSites();
                for (int i = 0; i < localSites.sizeOfSiteArray(); i++) {
                    if (localSites.getSiteArray(i).getName() == null) continue;
                    SiteSeasonDBDocument.SiteSeasonDB.Sites.Site tempSite = localSites.getSiteArray(i);
                    if (tempSite.getName().toLowerCase().equals(site.toLowerCase())) {
                        siteToCopy = tempSite;
                        break;
                    }
                }
                if (siteToCopy != null) {
                    File exportDBFile = new File("./temp/exportdb.xml");
                    SiteSeasonDBDocument exportDB = SiteSeasonDBDocument.Factory.newInstance();
                    SiteSeasonDBDocument.SiteSeasonDB.Sites.Site exportSite = exportDB.addNewSiteSeasonDB().addNewSites().addNewSite();
                    exportSite.setLat(siteToCopy.getLat());
                    exportSite.setLong(siteToCopy.getLong());
                    exportSite.setName(siteToCopy.getName());
                    exportSite.setReferencePhoto(siteToCopy.getReferencePhoto());
                    exportSite.setStringValue(siteToCopy.getStringValue());
                    exportDB.save(exportDBFile);
                    FileInputStream in = new FileInputStream(exportDBFile);
                    out.putNextEntry(new ZipEntry("locations/exportdb.xml"));
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.closeEntry();
                    File exportImageFile = new File(String.format("%s/%s", Program.Settings().getLocationFolder(), siteToCopy.getReferencePhoto()));
                    FileInputStream inImg = new FileInputStream(exportImageFile);
                    out.putNextEntry(new ZipEntry(String.format("locations/%s", siteToCopy.getReferencePhoto())));
                    int lenImg;
                    while ((lenImg = inImg.read(buf)) > 0) {
                        out.write(buf, 0, lenImg);
                    }
                    inImg.close();
                    out.closeEntry();
                }
            } catch (Exception ex) {
                Debug.print(ex);
            }
            for (int i = 0; i < baseFiles.length; i++) {
                String file = String.format("%s/%s", location, baseFiles[i]);
                FileInputStream in = new FileInputStream(file);
                out.putNextEntry(new ZipEntry(baseFiles[i]));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                lb.increment("Compressing: " + baseFiles[i]);
                out.closeEntry();
                in.close();
            }
            for (int i = 0; templateFiles != null && i < templateFiles.length; i++) {
                String file = String.format("%s/templates/%s", location, templateFiles[i]);
                FileInputStream in = new FileInputStream(file);
                out.putNextEntry(new ZipEntry(String.format("templates/%s", templateFiles[i])));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                lb.increment("Compressing XML files.");
                out.closeEntry();
            }
            lb.increment("Finalising");
            out.close();
        } catch (IOException e) {
            Debug.print(e);
        }
    }
