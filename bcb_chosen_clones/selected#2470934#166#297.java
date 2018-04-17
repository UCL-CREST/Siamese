    public boolean saveProject(File file, Set<String> types, Set<String> images, Set<String> trajectories, boolean databasesIncluded, boolean onlyLinks) throws IOException, SQLException {
        int index = file.getName().lastIndexOf(".");
        String name = file.getName().substring(0, index);
        DecimalFormat format = new DecimalFormat("####");
        format.setMinimumIntegerDigits(4);
        int count = 0;
        File main = new File(name);
        if (main.exists()) {
            throw new IOException(main.getAbsolutePath());
        }
        main.mkdir();
        File version = new File(main, "version");
        version.createNewFile();
        PrintWriter writer = new PrintWriter(version);
        writer.write(Videso3D.VERSION);
        writer.flush();
        writer.close();
        File xmlDir = new File(main.getAbsolutePath() + "/xml");
        xmlDir.mkdir();
        if (types != null && !types.isEmpty()) {
            File databases = new File(main.getAbsolutePath() + "/databases");
            databases.mkdir();
            for (String t : types) {
                Type type = DatabaseManager.stringToType(t);
                if (type != null) {
                    if (onlyLinks) {
                        if (databasesIncluded) {
                            String currentName = DatabaseManager.getCurrentName(type);
                            File baseCopy = new File(databases, currentName + "." + type);
                            baseCopy.createNewFile();
                            FileChannel source = new FileInputStream(new File(currentName)).getChannel();
                            FileChannel destination = new FileOutputStream(baseCopy).getChannel();
                            destination.transferFrom(source, 0, source.size());
                            source.close();
                            destination.close();
                            List<String[]> clefs = new ArrayList<String[]>();
                            Statement st = DatabaseManager.getCurrent(Type.Databases);
                            ResultSet rs = st.executeQuery("select * from clefs where type='" + currentName + "'");
                            while (rs.next()) {
                                clefs.add(new String[] { rs.getString("name"), rs.getString("value") });
                            }
                            st.close();
                            if (!clefs.isEmpty()) {
                                File clefsFile = new File(databases, currentName + "_clefs");
                                clefsFile.createNewFile();
                                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(clefsFile));
                                oos.writeObject(clefs);
                                oos.close();
                            }
                            File filesDir = new File(currentName + "_files");
                            if (filesDir.exists() && filesDir.isDirectory()) {
                                File baseFiles = new File(databases, currentName + "_files");
                                baseFiles.mkdirs();
                                for (File f : filesDir.listFiles()) {
                                    File copy = new File(baseFiles, f.getName());
                                    copy.createNewFile();
                                    source = new FileInputStream(f).getChannel();
                                    destination = new FileOutputStream(copy).getChannel();
                                    destination.transferFrom(source, 0, source.size());
                                    source.close();
                                    destination.close();
                                }
                            }
                        }
                        File selectedObjects = new File(databases, type.toString());
                        selectedObjects.createNewFile();
                        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(selectedObjects));
                        oos.writeObject(objects.get(type));
                        oos.close();
                    } else {
                        for (Restorable r : DatasManager.getController(type).getSelectedObjects()) {
                            this.saveObjectInXml(r, new File(xmlDir, r.getClass().getName() + "-" + type + "-" + format.format(count++) + ".xml"));
                        }
                    }
                }
            }
        }
        File imagesDir = new File(main.getAbsolutePath() + "/images");
        imagesDir.mkdir();
        for (EditableSurfaceImage si : this.getImages()) {
            if (images.contains(si.getName())) {
                int idx = si.getName().lastIndexOf(".");
                String newName = si.getName();
                if (idx != -1) {
                    newName = si.getName().substring(0, idx);
                }
                File img = new File(imagesDir, newName + ".gtif");
                ImageUtils.writeImageToFile(si.getSector(), (BufferedImage) si.getImageSource(), img);
            }
        }
        File trajectoDir = new File(main, "trajectory");
        trajectoDir.mkdirs();
        for (Layer l : wwd.getModel().getLayers()) {
            if (l instanceof GEOTracksLayer && trajectories.contains(l.getName())) {
                GEOWriter geoWriter = new GEOWriter(trajectoDir.getAbsolutePath() + "/" + l.getName(), true);
                for (VidesoTrack track : ((GEOTracksLayer) l).getModel().getVisibleTracks()) {
                    geoWriter.writeTrack((GEOTrack) track);
                }
                geoWriter.close();
            }
        }
        if (types != null && types.contains("Autres objets affichés.")) {
            for (Layer l : wwd.getModel().getLayers()) {
                if (l.getName().equals(AIRSPACE_LAYER_NAME)) {
                    for (Airspace r : ((AirspaceLayer) l).getAirspaces()) {
                        this.saveObjectInXml((Restorable) r, new File(xmlDir, r.getClass().getName() + "-" + format.format(count++) + ".xml"));
                    }
                } else if (l.getName().equals(RENDERABLE_LAYER_NAME)) {
                    for (Renderable r : ((RenderableLayer) l).getRenderables()) {
                        if (r instanceof Restorable) {
                            this.saveObjectInXml((Restorable) r, new File(xmlDir, r.getClass().getName() + "-" + format.format(count++) + ".xml"));
                        }
                    }
                } else if (l.getName().equals(BALISES2D_LAYER_NAME)) {
                    for (Balise2D b : ((Balise2DLayer) l).getVisibleBalises()) {
                        this.saveObjectInXml(b, new File(xmlDir, b.getClass().getName() + "-" + format.format(count++) + ".xml"));
                    }
                } else if (l.getName().equals(BALISES3D_LAYER_NAME)) {
                    for (Balise3D b : ((Balise3DLayer) l).getVisibleBalises()) {
                        this.saveObjectInXml(b, new File(xmlDir, b.getClass().getName() + "-" + format.format(count++) + ".xml"));
                    }
                }
            }
        }
        for (Airspace a : PolygonEditorsManager.getLayer().getAirspaces()) {
            if (a.isVisible()) this.saveObjectInXml(a, new File(xmlDir, a.getClass().getName() + "-" + format.format(count++) + ".xml"));
        }
        this.saveObjectInXml(this.wwd.getView(), new File(main, "globe.xml"));
        FileManager.createZipFile(file, main);
        FileManager.deleteFile(main);
        return true;
    }
