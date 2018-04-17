    public boolean SaveTicket() {
        boolean result = true;
        System.out.println("SaveTicket for " + TicketID());
        try {
            CRC32 check = new CRC32();
            check.update(tick.inspect().toString().getBytes());
            System.out.println("SaveTicket (" + tcl.getTicketid().toString() + ") " + tcl.getType() + "," + String.valueOf(initalcrc) + " crc=" + String.valueOf(check.getValue()));
            if (initalcrc != check.getValue()) {
                System.out.println("SaveTicket yaml start");
                String b64 = (String) tul.CallTicketCode(actioninvoke, null, "generateTicketFromObjects", tick, classobj);
                System.out.println("SaveTicket yaml end");
                tcl.setTicket(b64);
            }
        } catch (Exception ex) {
            System.out.println("SaveTicket error: " + ex.getMessage());
            ex.printStackTrace();
            result = false;
        }
        System.out.println("SaveTicket done");
        return result;
    }
