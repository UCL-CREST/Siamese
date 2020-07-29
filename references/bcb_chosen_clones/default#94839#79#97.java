    public static void storeGrid(float lat, float lon, int landmask) {
        BigDecimal big_lat = new BigDecimal(lat);
        BigDecimal big_lon = new BigDecimal(lon);
        String zLat = big_lat.setScale(6, 6).toString();
        String zLon = big_lon.setScale(6, 6).toString();
        try {
            String url = "jdbc:postgresql://155.206.19.246/ODM-Gamma";
            Class.forName("org.postgresql.Driver");
            Connection db = DriverManager.getConnection(url, "postgis", "");
            Statement st = db.createStatement();
            st.executeUpdate("INSERT INTO mdm.grid (point_geom, landmask) VALUES (GeomFromText('POINT(" + zLon + " " + zLat + ")',4326)," + landmask + ")");
            st.close();
            db.close();
        } catch (ClassNotFoundException e) {
            System.out.println("Caught ClassNotFoundException: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Caught SQLException: " + e.getMessage());
        }
    }
