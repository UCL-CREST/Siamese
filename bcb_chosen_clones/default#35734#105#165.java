    public void init() throws ServletException {
        users = new HashMap();
        usersToCheck = new HashMap();
        practices = new HashMap();
        try {
            BufferedReader r = new BufferedReader(new FileReader(home + usersFile));
            String line;
            while ((line = r.readLine()) != null) {
                UserData dati = new UserData(r.readLine(), r.readLine(), r.readLine());
                String uid = line;
                users.put(uid, dati);
            }
            r.close();
            PrintWriter p = new PrintWriter(new FileWriter(home + usersFile), true);
            Iterator i = users.keySet().iterator();
            while (i.hasNext()) {
                String uid = (String) i.next();
                UserData dati = (UserData) users.get(uid);
                p.println(uid);
                p.println(dati.passwd);
                p.println(dati.esercitaz);
                p.println(dati.numEs);
            }
            p.close();
            r = new BufferedReader(new FileReader(home + marksFile));
            voti = new TreeMap(Stat.getComparator());
            commenti = new TreeMap(Stat.getComparator());
            while ((line = r.readLine()) != null) {
                Stat s = new Stat();
                StringTokenizer st = new StringTokenizer(line, "$");
                st.nextToken();
                s.uid = st.nextToken().trim();
                String temp = st.nextToken().trim();
                s.esercitazione = Integer.parseInt(temp.substring(1, temp.indexOf(".html")));
                s.esercizio = Integer.parseInt(st.nextToken().trim());
                int voto = Integer.parseInt(st.nextToken().trim());
                if (voto >= 0) {
                    voti.put(s, new Integer(voto));
                    String commento;
                    if (st.hasMoreTokens()) {
                        commento = st.nextToken().trim();
                        if (commento.length() > 0) {
                            Stat s2 = new Stat();
                            s2.esercitazione = s.esercitazione;
                            s2.esercizio = s.esercizio;
                            s2.uid = "";
                            ArrayList l;
                            if (commenti.containsKey(s2)) {
                                l = (ArrayList) commenti.get(s2);
                            } else l = new ArrayList();
                            l.add(commento.replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
                            commenti.put(s2, l);
                        }
                    }
                }
            }
            r.close();
        } catch (IOException e) {
            throw new ServletException("IOException " + e);
        }
    }
