    private static void generateGIF(Connection con, String category, String area_code, String topic_code, String timeseries, String diff_timeseries, Calendar time, String area_label, String raster_label, String image_label, String note, Rectangle2D bounds, Rectangle2D raster_bounds, String source_filename, String diff_filename, String legend_filename, String output_filename, int output_maximum_size) throws SQLException, IOException {
        System.out.println("ImageCropper.generateGIF begin");
        MapContext map_context = new MapContext("test", new Configuration());
        try {
            Map map = new Map(map_context, area_label, new Configuration());
            map.setCoordSys(ProjectionCategories.default_coordinate_system);
            map.setPatternOutline(new XPatternOutline(new XPatternPaint(Color.white)));
            String type = null;
            RasterLayer rlayer = getRasterLayer(map, raster_label, getLinuxPathEquivalent(source_filename), getLinuxPathEquivalent(diff_filename), type, getLinuxPathEquivalent(legend_filename));
            map.addLayer(rlayer, true);
            map.setBounds2DImage(bounds, true);
            Dimension image_dim = null;
            image_dim = new Dimension((int) rlayer.raster.getDeviceBounds().getWidth() + 1, (int) rlayer.raster.getDeviceBounds().getHeight() + 1);
            if (output_maximum_size > 0) {
                double width_factor = image_dim.getWidth() / output_maximum_size;
                double height_factor = image_dim.getHeight() / output_maximum_size;
                double factor = Math.max(width_factor, height_factor);
                if (factor > 1.0) {
                    image_dim.setSize(image_dim.getWidth() / factor, image_dim.getHeight() / factor);
                }
            }
            map.setImageDimension(image_dim);
            map.scale();
            image_dim = new Dimension((int) map.getBounds2DImage().getWidth(), (int) map.getBounds2DImage().getHeight());
            Image image = null;
            Graphics gr = null;
            image = ImageCreator.getImage(image_dim);
            gr = image.getGraphics();
            try {
                map.paint(gr);
            } catch (Exception e) {
                Debug.println("map.paint error: " + e.getMessage());
            }
            String gif_filename = "";
            try {
                gif_filename = formatPath(category, timeseries, output_filename);
                new File(new_filename).mkdirs();
                new GifEncoder(image, new FileOutputStream(gif_filename)).encode();
            } catch (IOException e) {
                Debug.println("ImageCropper.generateGIF e: " + e.getMessage());
                throw new IOException("GenerateGIF.IOException: " + e);
            }
            PreparedStatement pstmt = null;
            try {
                String delete_raster = "delete raster_layer where " + "label='" + gif_name.trim() + "' and category='" + category.trim() + "' and area_code=' " + area_code.trim() + "'";
                pstmt = con.prepareStatement(delete_raster);
                boolean del = pstmt.execute();
                pstmt.close();
                String insert_raster = "insert into RASTER_LAYER " + "values(RASTER_LAYER_ID.nextval, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "SYSDATE, ?)";
                pstmt = con.prepareStatement(insert_raster);
                pstmt.setString(1, gif_name);
                pstmt.setString(2, category);
                pstmt.setString(3, area_code);
                pstmt.setString(4, topic_code);
                if (time == null) {
                    pstmt.setNull(5, java.sql.Types.DATE);
                } else {
                    pstmt.setDate(5, new java.sql.Date(time.getTimeInMillis()));
                }
                pstmt.setString(6, timeseries);
                pstmt.setString(7, gif_filename);
                pstmt.setNull(8, java.sql.Types.INTEGER);
                pstmt.setNull(9, java.sql.Types.INTEGER);
                pstmt.setDouble(10, raster_bounds.getX());
                pstmt.setDouble(11, raster_bounds.getY());
                pstmt.setDouble(12, raster_bounds.getWidth());
                pstmt.setDouble(13, raster_bounds.getHeight());
                pstmt.setString(14, note);
                int sequence = 0;
                if (gif_name.endsWith("DP")) {
                    sequence = 1;
                } else if (gif_name.endsWith("DY")) {
                    sequence = 2;
                } else if (gif_name.endsWith("DA")) {
                    sequence = 3;
                }
                pstmt.setInt(15, sequence);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                Debug.println("SQLException occurred e: " + e.getMessage());
                con.rollback();
                throw new SQLException("GenerateGIF.SQLException: " + e);
            } finally {
                pstmt.close();
            }
        } catch (Exception e) {
            Debug.println("ImageCropper.generateGIF e: " + e.getMessage());
        }
        System.out.println("ImageCropper.generateGIF end");
    }
