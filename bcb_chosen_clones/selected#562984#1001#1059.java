    private void Reserve() throws SQLException {
        Statement stbookings, stchartwl;
        String sp = "";
        if (useragent) sp = "agent"; else sp = "user";
        String userbooksql = "";
        String agentbooksql = "";
        String bookingid = String.valueOf(System.currentTimeMillis());
        String currentcoach;
        String currentseat;
        try {
            if (useragent) {
                agentbooksql = "update hp_administrator.agent_bookings set BOOKINGS = xmlquery('copy $new := $BOOKINGS modify do insert ";
                agentbooksql += " <detail booking_id=\"" + booking_details.getTicketno() + "\" status=\"open\" train_no=\"" + booking_details.getTrain_no() + "\" source=\"" + booking_details.getSource() + "\" dest=\"" + booking_details.getDestination() + "\" dep_date=\"" + booking_details.getDate() + "\" > ";
            } else if (!useragent) {
                userbooksql = "update hp_administrator.user_bookings set BOOKINGS = xmlquery('copy $new := $BOOKINGS modify do insert ";
                userbooksql += " <detail booking_id=\"" + booking_details.getTicketno() + "\" status=\"open\" train_no=\"" + booking_details.getTrain_no() + "\" source=\"" + booking_details.getSource() + "\" dest=\"" + booking_details.getDestination() + "\" dep_date=\"" + booking_details.getDate() + "\" > ";
            }
            for (int tickpos = 0; tickpos < booking_details.getNoOfPersons(); tickpos++) {
                currentcoach = coach.get(tickpos);
                currentseat = seatno.get(tickpos);
                if (!currentcoach.equals("WL")) {
                    String chartavailupdsql = "update hp_administrator.chart_wl_order set AVAILABLE_BOOKED = xmlquery('copy $new := $AVAILABLE_BOOKED   modify do insert ";
                    chartavailupdsql += "<seat number=\"" + currentseat + "\"><details user_id=\"" + booking_details.getUserId() + "\" usertype=\"" + sp + "\" ticket_no=\"" + booking_details.getTicketno() + "\" name=\"" + booking_details.getNameAt(tickpos) + "\" age=\"" + booking_details.getAgeAt(tickpos) + "\" sex=\"" + booking_details.getSexAt(tickpos) + "\" type=\"primary\"  /></seat>";
                    chartavailupdsql += " into $new/status/class[@name=\"" + booking_details.getTclass() + "\"]/coach[@number=\"" + currentcoach + "\"] ";
                    chartavailupdsql += " return  $new' ) where train_no like '" + booking_details.getTrain_no() + "' and date = '" + booking_details.getDate() + "' ";
                    System.out.println(chartavailupdsql);
                    stchartwl = conn.createStatement();
                    int updstat = stchartwl.executeUpdate(chartavailupdsql);
                    if (updstat > 0) System.out.println("chart_wl  availability  updated");
                } else if (currentcoach.equals("WL")) {
                    String chartwlupdsql = "update hp_administrator.chart_wl_order set WAITLISTING = xmlquery('copy $new := $WAITLISTING modify do insert ";
                    chartwlupdsql += "<details user_id=\"" + booking_details.getUserId() + "\" usertype=\"" + sp + "\" ticket_no=\"" + booking_details.getTicketno() + "\" name=\"" + booking_details.getNameAt(tickpos) + "\" age=\"" + booking_details.getAgeAt(tickpos) + "\" sex=\"" + booking_details.getSexAt(tickpos) + "\" type=\"primary\" /></seat>";
                    chartwlupdsql += " into $new/status/class[@name=\"" + booking_details.getTclass() + "\"] ";
                    chartwlupdsql += " return  $new' ) where train_no like '" + booking_details.getTrain_no() + "' and date = '" + booking_details.getDate() + "' ";
                    System.out.println(chartwlupdsql);
                    stchartwl = conn.createStatement();
                    int updstat = stchartwl.executeUpdate(chartwlupdsql);
                    if (updstat > 0) System.out.println("chart_wl  waitlisting  updated");
                }
                if (useragent) agentbooksql += "<person><coach>" + currentcoach + "</coach><seat>" + currentseat + "</seat></person>"; else userbooksql += "<person><coach>" + currentcoach + "</coach><seat>" + currentseat + "</seat></person>";
            }
            if (useragent) {
                agentbooksql += "</detail>   as first into $new/book return  $new' ) where agent_id like '" + booking_details.getUserId() + "'";
                System.out.println(agentbooksql);
                stbookings = conn.createStatement();
                int updstat = stbookings.executeUpdate(agentbooksql);
                if (updstat > 0) System.out.println("agent bookings updated");
            } else {
                userbooksql += "</detail>   as first into $new/book return  $new' ) where user_id like '" + booking_details.getUserId() + "'";
                System.out.println(userbooksql);
                stbookings = conn.createStatement();
                int updstat = stbookings.executeUpdate(userbooksql);
                if (updstat > 0) System.out.println("user bookings  updated");
            }
        } catch (SQLException e) {
            conn.rollback();
            e.printStackTrace();
        }
    }
