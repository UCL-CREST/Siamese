    public int updatewuliao(Addwuliao aw) {
        int flag = 0;
        Connection conn = null;
        PreparedStatement pm = null;
        try {
            conn = Pool.getConnection();
            conn.setAutoCommit(false);
            pm = conn.prepareStatement("update addwuliao set inname=?,innum=?,inprice=?,productsdetail=?where pid=?");
            pm.setString(1, aw.getInname());
            pm.setInt(2, aw.getInnum());
            pm.setDouble(3, aw.getInprice());
            pm.setString(4, aw.getProductsdetail());
            pm.setString(5, aw.getPid());
            flag = pm.executeUpdate();
            conn.commit();
            Pool.close(pm);
            Pool.close(conn);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (Exception ep) {
                ep.printStackTrace();
            }
            Pool.close(pm);
            Pool.close(conn);
        } finally {
            Pool.close(pm);
            Pool.close(conn);
        }
        return flag;
    }
