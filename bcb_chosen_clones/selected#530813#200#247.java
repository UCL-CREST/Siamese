        protected void execute(Context context) throws java.lang.Exception {
            Connection c = null;
            Statement s = null;
            Integer check = context.getValueAsInteger("Total");
            System.err.println("In BuyWidget.execute()");
            try {
                c = context.getConnection();
                c.setAutoCommit(false);
                s = c.createStatement();
                int total = computeCheckoutTotal(context, s);
                if (check == null) {
                    throw new Exception("Shouldn't: No total?");
                }
                if (check.intValue() != total) {
                    throw new Exception("Shouldn't: Basket changed? " + "total was " + total + "; checksum was " + check);
                }
                StringBuffer q = new StringBuffer("select BIDSTATE.Bid, BIDSTATE.Amount, " + "BIDSTATE.QShipping, BIDSTATE.QInsure " + "from BID, BIDSTATE " + "where  BIDSTATE.Bid = BID.Bid " + "and ( BIDSTATE.BidStatus = 0 " + "or BIDSTATE.BidStatus = 15) " + "and BID.Customer = ");
                q.append(context.get("customer"));
                q.append(" and bidstate.bidstate =  " + "( select max( bidstate.bidstate) " + "from bidstate " + "where bid = bid.bid) ");
                System.err.println(q.toString());
                Contexts rows = new RSContexts(s.executeQuery(q.toString()));
                Enumeration e = rows.elements();
                while (e.hasMoreElements()) {
                    Context row = (Context) e.nextElement();
                    row.merge((Map) context);
                    row.put("Username", context.get(ConnectionPool.DBUSERMAGICTOKEN));
                    row.put("BidStatus", BidStatus.OFFER);
                    s.executeUpdate(bidStateInsert(row));
                    s.execute(bidPrivateInsert(context, row));
                }
                c.commit();
            } catch (Exception any) {
                c.rollback();
                throw new DataStoreException("Your card will not be debited: " + any.getMessage());
            } finally {
                try {
                    if (s != null) {
                        s.close();
                    }
                    if (c != null) {
                        context.releaseConnection(c);
                    }
                } catch (SQLException sex) {
                } catch (DataStoreException dse) {
                }
            }
            context.put(REDIRECTMAGICTOKEN, "account");
        }
