    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String pathInfo = httpServletRequest.getPathInfo();
        log.info("PathInfo: " + pathInfo);
        if (pathInfo == null || pathInfo.equals("") || pathInfo.equals("/")) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        String fileName = pathInfo.charAt(0) == '/' ? pathInfo.substring(1) : pathInfo;
        log.info("FileName: " + fileName);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getDataSource().getConnection();
            ps = con.prepareStatement("select file, size from files where name=?");
            ps.setString(1, fileName);
            rs = ps.executeQuery();
            if (rs.next()) {
                httpServletResponse.setContentType(getServletContext().getMimeType(fileName));
                httpServletResponse.setContentLength(rs.getInt("size"));
                OutputStream os = httpServletResponse.getOutputStream();
                org.apache.commons.io.IOUtils.copy(rs.getBinaryStream("file"), os);
                os.flush();
            } else {
                httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        } finally {
            if (rs != null) try {
                rs.close();
            } catch (SQLException e) {
            }
            if (ps != null) try {
                ps.close();
            } catch (SQLException e) {
            }
            if (con != null) try {
                con.close();
            } catch (SQLException e) {
            }
        }
    }
