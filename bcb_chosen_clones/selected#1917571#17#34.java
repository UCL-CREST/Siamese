    public static void main(String[] args) {
        String in = "/Users/laemmel/svn/shared-svn/projects/120multiDestPeds/floor_plan/boundaries.shp";
        String out = "/Users/laemmel/svn/shared-svn/projects/120multiDestPeds/floor_plan/boundaries_closed.shp";
        ShapeFileReader reader = new ShapeFileReader();
        reader.readFileAndInitialize(in);
        QuadTree<Coordinate> quad = new QuadTree<Coordinate>(reader.getBounds().getMinX(), reader.getBounds().getMinY(), reader.getBounds().getMaxX(), reader.getBounds().getMaxY());
        for (Feature ft : reader.getFeatureSet()) {
            MultiLineString ml = (MultiLineString) ft.getDefaultGeometry();
            for (int i = 0; i < ml.getNumGeometries(); i++) {
                LineString ls = (LineString) ml.getGeometryN(i);
                Point start = ls.getStartPoint();
                checkIt(start, quad);
                Point end = ls.getEndPoint();
                checkIt(end, quad);
            }
        }
        ShapeFileWriter.writeGeometries(reader.getFeatureSet(), out);
    }
