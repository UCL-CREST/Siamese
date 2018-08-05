    protected void channelConnected() throws IOException {
        MessageDigest md = null;
        String digest = "";
        try {
            String userid = nateon.getUserId();
            if (userid.endsWith("@nate.com")) userid = userid.substring(0, userid.lastIndexOf('@'));
            md = MessageDigest.getInstance("MD5");
            md.update(nateon.getPassword().getBytes());
            md.update(userid.getBytes());
            byte[] bData = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : bData) {
                int v = (int) b;
                v = v < 0 ? v + 0x100 : v;
                String s = Integer.toHexString(v);
                if (s.length() == 1) sb.append('0');
                sb.append(s);
            }
            digest = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        NateOnMessage out = new NateOnMessage("LSIN");
        out.add(nateon.getUserId()).add(digest).add("MD5").add("3.615");
        out.setCallback("processAuth");
        writeMessage(out);
    }
