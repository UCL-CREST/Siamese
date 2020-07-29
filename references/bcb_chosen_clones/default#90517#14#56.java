    public static void main(String args[]) {
        try {
            String dateFrom = args[0];
            String dateTo = args[1];
            String depotFrom = args[2];
            String depotTo = args[3];
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/bata071112";
            Connection con = DriverManager.getConnection(url, "jboss", "jboss");
            String query = "SELECT SUBSTRING(brh.code, 1, 5) AS code, rows.* FROM acc_branch_index AS brh INNER JOIN (SELECT lpad(idx.pkid, 13, '0') as pkid, idx.cust_svc_center_id, TO_CHAR(idx.time_complete, 'dd/MM/YYYYHH24:MI:SS') AS time_complete, SUBSTRING(itm.item_code, 1, 7) AS article_code, SUBSTRING(itm.item_code, 9, 3) AS item_size, itm.name, trim(trailing '.00' from itm.total_quantity) AS total_quantity, lpad(trim(trailing '.00' from (itm.unit_amount*100)), 7, '0') AS unit_price, itm.remarks FROM  cust_sales_return_item AS itm INNER JOIN cust_sales_return_index AS idx ON (itm.sales_return_id = idx.pkid) WHERE ";
            query += "idx.time_complete BETWEEN '" + dateFrom + "' || ' 00:00:00' ";
            query += "AND '" + dateTo + "' || ' 23:59:59') AS ";
            query += "rows ON (rows.cust_svc_center_id = brh.pkid) WHERE ";
            query += "substring(brh.code, 1, 5) >= '" + depotFrom + "' ";
            query += "AND substring(brh.code, 1, 5) <= '" + depotTo + "' ";
            query += "ORDER BY brh.code,  rows.pkid, rows.article_code, rows.item_size;";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            FileOutputStream out;
            PrintStream p;
            out = new FileOutputStream("RM60.txt");
            p = new PrintStream(out);
            while (rs.next()) {
                String code = "RM60";
                String batch = rs.getString("code");
                String article = rs.getString("article_code");
                String size = rs.getString("item_size");
                String returnValue = rs.getString("unit_price");
                String salesReturnNo = rs.getString("pkid");
                String salesReturnDateTime = rs.getString("time_complete");
                Integer salesReturnQty = new Integer(rs.getString("total_quantity"));
                String row = code + batch + batch + article + "  " + size + returnValue + "                 " + salesReturnNo + salesReturnDateTime;
                int intSalesReturnQty = salesReturnQty.intValue();
                for (int cnt = 0; cnt < intSalesReturnQty; cnt++) {
                    p.println(row);
                }
            }
            stmt.close();
            p.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
