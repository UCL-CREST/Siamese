    public void addFinance(int clubid, int quarterid, String date, String desc, String loc, BigDecimal amount) throws FinanceException, SQLException {
        String budgetQuery = "SELECT used, available FROM Budget WHERE club_id=" + clubid + " and quarter_id=" + quarterid + ";";
        String financeUpdate = "INSERT INTO Finance (`club_id`, `transaction_date`, `description`, `location`, `amount`) VALUES ('" + clubid + "', '" + date + "', '" + desc + "', '" + "', '" + loc + "', '" + amount + "');";
        Budget b = new Budget();
        try {
            cn.setAutoCommit(false);
            Statement sm = cn.createStatement();
            ResultSet rs = sm.executeQuery(budgetQuery);
            if (rs.first()) {
                b.used = rs.getBigDecimal(1);
                b.available = rs.getBigDecimal(2);
            } else {
                throw new FinanceException("No budget exists for this club!!");
            }
            if (b.available.compareTo(amount.negate()) >= 0) {
                if (amount.equals(new BigDecimal(0))) ;
                {
                    b.used = b.used.subtract(amount);
                }
                b.available = b.available.add(amount);
                sm = cn.createStatement();
                sm.executeUpdate(financeUpdate);
                sm = cn.createStatement();
                sm.executeUpdate("Update Budget SET used = " + b.used + ", amount = " + b.available + " WHERE club_id=" + clubid + " and quarter_id=" + quarterid + ";");
                cn.commit();
            } else {
                throw new FinanceException("The proposed expenditure is not within the club's budget.");
            }
        } catch (SQLException e) {
            cn.rollback();
            throw e;
        } finally {
            cn.setAutoCommit(true);
        }
    }
