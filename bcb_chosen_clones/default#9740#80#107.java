    private static int getGridID(double lat, double lon) {
        BigDecimal big_lat = new BigDecimal(lat);
        BigDecimal big_lon = new BigDecimal(lon);
        String zLat = big_lat.setScale(6, 6).toString();
        String zLon = big_lon.setScale(6, 6).toString();
        int grid_fallback = -999;
        try {
            String url = "jdbc:postgresql://155.206.19.246/ODM-Gamma";
            Class.forName("org.postgresql.Driver");
            Connection db = DriverManager.getConnection(url, "postgis", "");
            Statement st = db.createStatement();
            ResultSet rs = st.executeQuery("SELECT gid from mdm.grid WHERE point_geom=GeomFromText('POINT(" + zLon + " " + zLat + ")',4326)");
            while (rs.next()) {
                int gridid = rs.getInt(1);
                rs.close();
                st.close();
                db.close();
                return gridid;
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Caught ClassNotFoundException: " + e.getMessage());
            return grid_fallback;
        } catch (SQLException e) {
            System.out.println("Caught SQLException: " + e.getMessage());
            return grid_fallback;
        }
        return grid_fallback;
    }
