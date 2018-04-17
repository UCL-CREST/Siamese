    private String modContentForIds(String con) {
        Pattern p = Pattern.compile("GOTO [a-zA-Z]{1,}+");
        Matcher m = p.matcher(con);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            System.out.println(start + ", " + end);
            String sub = con.substring(start, end);
            String[] splitter = sub.split(" ");
            if (splitter.length >= 2) {
                String idSt = splitter[1];
                for (Method meth : methods) {
                    if (meth.name.equals(idSt)) {
                        idSt = meth.id + "";
                        break;
                    }
                }
                String sub2 = splitter[0] + " " + idSt;
                con = con.replaceAll(sub, sub2);
            }
        }
        return con;
    }
