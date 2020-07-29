    public static void main(String args[]) {
        Connection con;
        if (args.length != 2) {
            System.out.println("Usage: Shutdown <host> <password>");
            System.exit(0);
        }
        try {
            con = new Connection(args[0]);
            con.tStart();
            Message message = new Message(MessageTypes.SHUTDOWN_SERVER);
            java.security.MessageDigest hash = java.security.MessageDigest.getInstance("SHA-1");
            hash.update(args[1].getBytes("UTF-8"));
            message.put("pwhash", hash.digest());
            con.send(message);
            con.join();
        } catch (java.io.UnsupportedEncodingException e) {
            System.err.println("Password character encoding not supported.");
        } catch (java.io.IOException e) {
            System.out.println(e.toString());
        } catch (java.security.NoSuchAlgorithmException e) {
            System.out.println("Password hash algorithm SHA-1 not supported by runtime.");
        } catch (InterruptedException e) {
        }
        System.exit(0);
    }
