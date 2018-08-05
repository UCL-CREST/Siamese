    @org.junit.Test
    public void checkGetSalesOrders() throws IOException, SQLException, Exception {
        Long salesPersonId = new Long(1);
        Long productId = new Long(1);
        Long regionId = new Long(1);
        String query = "SELECT c.salesOrderId, b.productId, d.regionname ,b.productname, b.cost ,c.orderQty , c.orderDate FROM salesperson a, product b , salesorder c , region d WHERE b.productId = c.productId and a.salesPersonId = c.salesPersonId  and a.regionId = d.regionId " + "and b.productId =" + productId + " and a.regionId =" + regionId + " ORDER BY c.salesOrderId";
        resultSet = statement.executeQuery(query);
        List<TestSalesOrderService> testSalesOrder = populateTestSalesOrderObject(resultSet);
        StringBuilder queryString = new StringBuilder();
        queryString.append("/salesPersonId/" + salesPersonId);
        queryString.append("/productId/" + productId);
        queryString.append("/regionId/" + regionId);
        String urlForSalesData = "http://localhost:8080/SalesOrderService/SalesOrder/SalesOrderData";
        urlForSalesData = urlForSalesData + queryString;
        String responseString = null;
        responseString = getScenarioData(urlForSalesData);
        List<TestSalesOrderService> actualSalesOrderList = getSalesOrderInfo(responseString);
        for (int i = 0; i < testSalesOrder.size(); i++) {
            TestSalesOrderService s1 = testSalesOrder.get(i);
            TestSalesOrderService s2 = actualSalesOrderList.get(i);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date strDate1 = dateFormat.parse(s2.getOrderDate());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = df.format(strDate1);
            assertEquals(s1.getProductName(), s2.getProductName());
            assertEquals(s1.getOrderDate(), strDate);
            assertEquals(s1.getOrderQuantity(), s2.getOrderQuantity());
            assertEquals(s1.getRegionName(), s2.getRegionName());
            assertEquals(s1.getProductID(), s2.getProductID());
            assertEquals(s1.getSalesOrderID(), s2.getSalesOrderID());
            assertEquals(s1.getCost(), s2.getCost());
        }
    }
