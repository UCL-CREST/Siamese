    public static void main(String[] args) {
        LinkedList bezirke = new LinkedList();
        LinkedList ubezirke = new LinkedList();
        String unterbezirke[][] = new String[23][2];
        Client client = new Client();
        String body = client.getURLasBuffer("http://www.luise-berlin.de/Strassen/_Navi/n_strall.htm");
        Pattern p = Pattern.compile("<a name=\".*?</td></tr>");
        Matcher m = p.matcher(body);
        Pattern p1 = Pattern.compile("<A HREF=\"N_bez[0-9]*.htm\" target=\"Drei\">.*?;</A>");
        while (m.find()) {
            bezirke.add(body.substring(m.start() + 14, m.end() - 10));
        }
        bezirke.remove(0);
        bezirke.add(0, "Charlottenburg-Wilmersdorf");
        bezirke.remove(10);
        bezirke.add(10, "Tempelhof-Sch�neberg");
        Matcher m1 = p1.matcher(body);
        while (m1.find()) {
            ubezirke.add(body.substring(m1.start() + 36, m1.end() - 10));
        }
        int size_ubez = ubezirke.size();
        for (int i = 0; i < size_ubez; i++) {
            Pattern p2 = Pattern.compile("<A HREF=\"N_bez[0-9]*.htm\" target=\"Drei\">" + ubezirke.get(i) + ".*?;</A>");
            Matcher m2 = p2.matcher(body);
            while (m2.find()) {
                String nr = body.substring(m2.start() + 14, m2.end());
                String nr2 = nr.substring(0, 2);
                unterbezirke[i][0] = (String) ubezirke.get(i);
                unterbezirke[i][1] = nr2;
            }
        }
        for (int i = 0; i < 23; i++) {
        }
        Client client2 = new Client();
        Client client3 = new Client();
        Pattern p3 = Pattern.compile("ALT=\"aktuell\".*?</TD></TR>");
        Pattern p4 = Pattern.compile("<A HREF=\"n_.*?\" TARGET=\"NaviList\"><IMG SRC=\"kg_pu");
        Pattern p5 = Pattern.compile("<TITLE>.*?</TITLE>");
        String prefix = "http://www.luise-berlin.de/Strassen/_Navi/n_b";
        String postfix = ".htm";
        int z = 0;
        Collection bezirke_list = null;
        Connection cn;
        try {
            cn = new DBObject().get_connection();
            if (cn == null) {
                log.info("Fehler aufgetreten");
            }
            bezirke_list = BezirkeDB.findAll(cn);
            cn.close();
        } catch (SQLException e) {
            if (log.isInfoEnabled()) log.info("Hier ist ein Fehler in der Datenbank aufgetreten: " + e.toString());
        }
        for (int i = 0; i < 23; i++) {
            String streets2 = client3.getURLasBuffer(prefix + unterbezirke[i][1] + "_" + z + postfix);
            Matcher m5 = p5.matcher(streets2);
            BezirkeDTO aktueller_bezirk = null;
            if (m5.find()) {
                Iterator it = bezirke_list.iterator();
                while (it.hasNext()) {
                    BezirkeDTO erg = (BezirkeDTO) it.next();
                    if (erg.getUnterbezirk().equals(streets2.substring(m5.start() + 18, m5.end() - 8))) {
                        aktueller_bezirk = erg;
                    }
                }
            }
            do {
                String streets = client2.getURLasBuffer(prefix + unterbezirke[i][1] + "_" + z + postfix);
                Matcher m3 = p3.matcher(streets);
                while (m3.find()) {
                    try {
                        cn = new DBObject().get_connection();
                        if (cn == null) {
                            log.info("Fehler aufgetreten");
                        }
                        StrassenDB.insert(cn, new StrassenDTO(0, streets.substring(m3.start() + 66, m3.end() - 10), aktueller_bezirk, 0, 0));
                        cn.close();
                    } catch (SQLException e) {
                        if (log.isInfoEnabled()) log.info("Hier ist ein Fehler in der Datenbank aufgetreten: " + e.toString());
                    }
                }
                Matcher m4 = p4.matcher(streets);
                if (m4.find()) {
                    z++;
                } else {
                    z = 0;
                }
            } while (z != 0);
            System.out.println();
        }
    }
