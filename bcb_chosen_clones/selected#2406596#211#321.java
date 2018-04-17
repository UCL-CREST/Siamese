    private static void generateTIFF(Connection con, String category, String area_code, String topic_code, String timeseries, String diff_timeseries, Calendar time, String area_label, String raster_label, String image_label, String note, Rectangle2D bounds, Rectangle2D raster_bounds, String source_filename, String diff_filename, String legend_filename, String output_filename, int output_maximum_size) throws SQLException, IOException {
        Debug.println("ImageCropper.generateTIFF begin");
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
            String tiff_filename = "";
            try {
                tiff_filename = formatPath(category, timeseries, output_filename);
                new File(new_filename).mkdirs();
                Debug.println("tiff_filename: " + tiff_filename);
                BufferedImage bi = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_BYTE_INDEXED);
                bi.createGraphics().drawImage(image, 0, 0, null);
                File f = new File(tiff_filename);
                FileOutputStream out = new FileOutputStream(f);
                TIFFEncodeParam param = new TIFFEncodeParam();
                param.setCompression(TIFFEncodeParam.COMPRESSION_PACKBITS);
                TIFFImageEncoder encoder = (TIFFImageEncoder) TIFFCodec.createImageEncoder("tiff", out, param);
                encoder.encode(bi);
                out.close();
            } catch (IOException e) {
                Debug.println("ImageCropper.generateTIFF TIFFCodec e: " + e.getMessage());
                throw new IOException("GenerateTIFF.IOException: " + e);
            }
            PreparedStatement pstmt = null;
            try {
                String query = "select Proj_ID, AccessType_Code from project " + "where Proj_Code= '" + area_code.trim() + "'";
                Statement stmt = null;
                ResultSet rs = null;
                int proj_id = -1;
                int access_code = -1;
                stmt = con.createStatement();
                rs = stmt.executeQuery(query);
                if (rs.next()) {
                    proj_id = rs.getInt(1);
                    access_code = rs.getInt(2);
                }
                rs.close();
                stmt.close();
                String delete_raster = "delete from rasterlayer where " + "Raster_Name='" + tiff_name.trim() + "' and Group_Code='" + category.trim() + "' and Proj_ID =" + proj_id;
                Debug.println("***** delete_raster: " + delete_raster);
                pstmt = con.prepareStatement(delete_raster);
                boolean del = pstmt.execute();
                pstmt.close();
                String insert_raster = "insert into rasterlayer(Raster_Name, " + "Group_Code, Proj_ID, Raster_TimeCode, Raster_Xmin, " + "Raster_Ymin, Raster_Area_Xmin, Raster_Area_Ymin, " + "Raster_Visibility, Raster_Order, Raster_Path, " + "AccessType_Code, Raster_TimePeriod) values(?,?,?,?, " + "?,?,?,?,?,?,?,?,?)";
                pstmt = con.prepareStatement(insert_raster);
                pstmt.setString(1, tiff_name);
                pstmt.setString(2, category);
                pstmt.setInt(3, proj_id);
                pstmt.setString(4, timeseries);
                pstmt.setDouble(5, raster_bounds.getX());
                pstmt.setDouble(6, raster_bounds.getY());
                pstmt.setDouble(7, raster_bounds.getWidth());
                pstmt.setDouble(8, raster_bounds.getHeight());
                pstmt.setString(9, "false");
                int sequence = 0;
                if (tiff_name.endsWith("DP")) {
                    sequence = 1;
                } else if (tiff_name.endsWith("DY")) {
                    sequence = 2;
                } else if (tiff_name.endsWith("DA")) {
                    sequence = 3;
                }
                pstmt.setInt(10, sequence);
                pstmt.setString(11, tiff_filename);
                pstmt.setInt(12, access_code);
                if (time == null) {
                    pstmt.setNull(13, java.sql.Types.DATE);
                } else {
                    pstmt.setDate(13, new java.sql.Date(time.getTimeInMillis()));
                }
                pstmt.executeUpdate();
            } catch (SQLException e) {
                Debug.println("SQLException occurred e: " + e.getMessage());
                con.rollback();
                throw new SQLException("GenerateTIFF.SQLException: " + e);
            } finally {
                pstmt.close();
            }
        } catch (Exception e) {
            Debug.println("ImageCropper.generateTIFF e: " + e.getMessage());
        }
        Debug.println("ImageCropper.generateTIFF end");
    }
