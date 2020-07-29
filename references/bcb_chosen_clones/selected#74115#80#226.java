    public static HashMap<Integer, GridClass> buildFromGrid(String filePath) throws IOException {
        File wktDir = new File(filePath);
        wktDir.mkdirs();
        int[] wktMap = null;
        ArrayList<Integer> maxValues = new ArrayList<Integer>();
        ArrayList<String> labels = new ArrayList<String>();
        HashMap<Integer, GridClass> classes = new HashMap<Integer, GridClass>();
        Properties p = new Properties();
        p.load(new FileReader(filePath + ".txt"));
        ArrayList<Integer> keys = new ArrayList<Integer>();
        for (String key : p.stringPropertyNames()) {
            try {
                int k = Integer.parseInt(key);
                keys.add(k);
            } catch (NumberFormatException e) {
                System.out.println("Excluding shape key '" + key + "'");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        java.util.Collections.sort(keys);
        for (int j = 0; j < keys.size(); j++) {
            int k = keys.get(j);
            String key = String.valueOf(k);
            try {
                String name = p.getProperty(key);
                GridClass gc = new GridClass();
                gc.setName(name);
                gc.setId(k);
                System.out.println("getting wkt for " + filePath + " > " + key);
                File zipFile = new File(filePath + File.separator + key + ".wkt.zip");
                ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
                zos.putNextEntry(new ZipEntry(key + ".wkt"));
                Map wktIndexed = Envelope.getGridSingleLayerEnvelopeAsWktIndexed(filePath + "," + key + "," + key, wktMap);
                zos.write(((String) wktIndexed.get("wkt")).getBytes());
                zos.close();
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath + File.separator + key + ".wkt"));
                bos.write(((String) wktIndexed.get("wkt")).getBytes());
                bos.close();
                System.out.println("wkt written to file");
                gc.setArea_km(SpatialUtil.calculateArea((String) wktIndexed.get("wkt")) / 1000.0 / 1000.0);
                wktMap = (int[]) wktIndexed.get("map");
                gc.setMinShapeIdx(Grid2Shape.getArrayMin(wktMap));
                gc.setMaxShapeIdx(Grid2Shape.getArrayMax(wktMap));
                maxValues.add(gc.getMaxShapeIdx());
                labels.add(name.replace("\"", "'"));
                FileWriter fw = new FileWriter(filePath + File.separator + key + ".wkt.index");
                fw.append((String) wktIndexed.get("index"));
                fw.close();
                RandomAccessFile raf = new RandomAccessFile(filePath + File.separator + key + ".wkt.index.dat", "rw");
                String[] index = ((String) wktIndexed.get("index")).split("\n");
                int len = ((String) wktIndexed.get("wkt")).length();
                WKTReader r = new WKTReader();
                for (int i = 0; i < index.length; i++) {
                    if (index[i].length() > 1) {
                        String[] cells = index[i].split(",");
                        raf.writeInt(Integer.parseInt(cells[0]));
                        int polygonStart = Integer.parseInt(cells[1]);
                        raf.writeInt(polygonStart);
                        int polygonEnd = len;
                        if (i + 1 < index.length) {
                            polygonEnd = Integer.parseInt(index[i + 1].split(",")[1]) - 1;
                        }
                        String polygonWkt = ((String) wktIndexed.get("wkt")).substring(polygonStart, polygonEnd);
                        Geometry g = r.read("POLYGON" + polygonWkt);
                        raf.writeFloat((float) g.getEnvelopeInternal().getMinX());
                        raf.writeFloat((float) g.getEnvelopeInternal().getMinY());
                        raf.writeFloat((float) g.getEnvelopeInternal().getMaxX());
                        raf.writeFloat((float) g.getEnvelopeInternal().getMaxY());
                        raf.writeFloat((float) (SpatialUtil.calculateArea(polygonWkt) / 1000.0 / 1000.0));
                    }
                }
                raf.close();
                wktIndexed = null;
                System.out.println("getting multipolygon for " + filePath + " > " + key);
                MultiPolygon mp = Envelope.getGridEnvelopeAsMultiPolygon(filePath + "," + key + "," + key);
                gc.setBbox(mp.getEnvelope().toText().replace(" (", "(").replace(", ", ","));
                classes.put(k, gc);
                try {
                    zos = new ZipOutputStream(new FileOutputStream(filePath + File.separator + key + ".kml.zip"));
                    zos.putNextEntry(new ZipEntry(key + ".kml"));
                    Encoder encoder = new Encoder(new KMLConfiguration());
                    encoder.setIndenting(true);
                    encoder.encode(mp, KML.Geometry, zos);
                    zos.close();
                    System.out.println("kml written to file");
                    zos = new ZipOutputStream(new FileOutputStream(filePath + File.separator + key + ".geojson.zip"));
                    zos.putNextEntry(new ZipEntry(key + ".geojson"));
                    FeatureJSON fjson = new FeatureJSON();
                    final SimpleFeatureType TYPE = DataUtilities.createType("class", "the_geom:MultiPolygon,id:Integer,name:String");
                    SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);
                    featureBuilder.add(mp);
                    featureBuilder.add(k);
                    featureBuilder.add(name);
                    SimpleFeature sf = featureBuilder.buildFeature(null);
                    fjson.writeFeature(sf, zos);
                    zos.close();
                    System.out.println("geojson written to file");
                    File newFile = new File(filePath + File.separator + key + ".shp");
                    ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
                    Map<String, Serializable> params = new HashMap<String, Serializable>();
                    params.put("url", newFile.toURI().toURL());
                    params.put("create spatial index", Boolean.FALSE);
                    ShapefileDataStore newDataStore = (ShapefileDataStore) dataStoreFactory.createNewDataStore(params);
                    newDataStore.createSchema(TYPE);
                    newDataStore.forceSchemaCRS(DefaultGeographicCRS.WGS84);
                    Transaction transaction = new DefaultTransaction("create");
                    String typeName = newDataStore.getTypeNames()[0];
                    SimpleFeatureSource featureSource = newDataStore.getFeatureSource(typeName);
                    SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;
                    featureStore.setTransaction(transaction);
                    SimpleFeatureCollection collection = FeatureCollections.newCollection();
                    collection.add(sf);
                    featureStore.addFeatures(collection);
                    transaction.commit();
                    transaction.close();
                    zos = new ZipOutputStream(new FileOutputStream(filePath + File.separator + key + ".shp.zip"));
                    String[] exts = { ".dbf", ".shp", ".shx", ".prj" };
                    for (String ext : exts) {
                        zos.putNextEntry(new ZipEntry(key + ext));
                        FileInputStream fis = new FileInputStream(filePath + File.separator + key + ext);
                        byte[] buffer = new byte[1024];
                        int size;
                        while ((size = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, size);
                        }
                        fis.close();
                        new File(filePath + File.separator + key + ext).delete();
                    }
                    zos.close();
                    System.out.println("shape file written to zip");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Grid g = new Grid(filePath);
        g.writeGrid(filePath + File.separator + "polygons", wktMap, g.xmin, g.ymin, g.xmax, g.ymax, g.xres, g.yres, g.nrows, g.ncols);
        copyHeaderAsInt(filePath + ".grd", File.separator + "polygons.grd");
        exportSLD(filePath + File.separator + "polygons.sld", new File(filePath + ".txt").getName(), maxValues, labels);
        writeProjectionFile(filePath + File.separator + "polygons.prj");
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(filePath + ".classes.json"), classes);
        return classes;
    }
