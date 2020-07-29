    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/vnd.google-earth.kml+xml");
        PrintWriter out = response.getWriter();
        try {
            double lat = 0;
            double lng = 0;
            double dir = 0;
            String err = "";
            String sqlReq = " select " + "             srvtime, dvctime, orient1, orient2, orient3, X(\"position\"),Y(\"position\"),ext " + " from timeline line order by srvtime desc limit 1; ";
            try {
                Class.forName("org.postgresql.Driver");
                String url = "jdbc:postgresql://127.0.0.1:5432/gisdb";
                String username = "postgres";
                String password = "postgres";
                Connection con = DriverManager.getConnection(url, username, password);
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sqlReq);
                while (rs.next()) {
                    for (int i = 1; i < 9; i++) {
                        err += rs.getString(i) + "|";
                    }
                    try {
                        lat = Double.valueOf(rs.getString(7));
                        lng = Double.valueOf(rs.getString(6));
                    } catch (Exception er) {
                    }
                }
                st.close();
                con.close();
            } catch (Exception e) {
                err = e.toString();
            }
            double p1_x = lng;
            double p1_y = lat + 0.002;
            double p2_x = lng + 0.001;
            double p2_y = lat - 0.001;
            double p3_x = lng - 0.001;
            double p3_y = lat - 0.001;
            double p1_x1 = lng;
            double p1_y1 = lat + 0.002 / 2;
            double p2_x1 = lng + 0.001 / 2;
            double p2_y1 = lat - 0.001 / 2;
            double p3_x1 = lng - 0.001 / 2;
            double p3_y1 = lat - 0.001 / 2;
            String plm = "    <Style id=\"yellowLineGreenPoly\">" + "      <LineStyle>" + "        <color>7f00ffff</color>" + "        <width>14</width>" + "      </LineStyle>" + "      <PolyStyle>" + "        <color>7f00ff00</color>" + "      </PolyStyle>" + "    </Style> " + "<Placemark> " + "<name>The Pentagon" + err + "</name> " + "<styleUrl>#yellowLineGreenPoly</styleUrl> " + " <LineString>" + "        <extrude>1</extrude>" + "        <tessellate>1</tessellate>" + "      <altitudeMode>relativeToGround</altitudeMode>" + "          <coordinates>" + "            " + p1_x + "," + p1_y + ",10 " + "            " + p2_x + "," + p2_y + ",10 " + "            " + p3_x + "," + p3_y + ",10 " + "            " + p1_x + "," + p1_y + ",10 " + "          </coordinates>" + " </LineString> " + "  </Placemark>";
            String kml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n" + "<Document>" + plm + "</Document>" + "</kml>";
            out.println(kml);
        } finally {
            out.close();
        }
    }
