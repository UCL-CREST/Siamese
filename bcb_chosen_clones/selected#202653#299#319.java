    private boolean LoadTicket() {
        boolean result = true;
        try {
            System.out.println("LoadTicket ticketid=" + ticketid);
            if (action != null) {
                tick = (RubyObject) actioninvoke.invokeMethod(action, "loadTicket", tcl.getTicket());
                classobj = (RubyObject) actioninvoke.invokeMethod(action, "loadScript");
                CRC32 check = new CRC32();
                check.update(tick.inspect().toString().getBytes());
                initalcrc = check.getValue();
            } else {
                System.out.println("LoadTicket error: ruby objects not loaded");
                result = false;
            }
        } catch (Exception ex) {
            System.out.println("LoadTicket error: " + ex.getMessage());
            result = false;
        }
        System.out.println("LoadTicket done");
        return result;
    }
