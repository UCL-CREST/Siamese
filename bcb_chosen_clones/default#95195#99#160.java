    public static void cargaGrafo(String file) {
        try {
            BufferedReader ent = new BufferedReader(new FileReader(file));
            String s = ent.readLine();
            StringTokenizer st = new StringTokenizer(s, "/");
            String nodos[] = new String[st.countTokens()];
            int k = 0;
            while (st.hasMoreElements()) {
                String sub = (st.nextToken());
                new Persona(sub);
                nodos[k] = sub;
                k++;
            }
            s = ent.readLine();
            StringTokenizer st2 = new StringTokenizer(s, "/");
            k = 0;
            while (st2.hasMoreTokens()) {
                String conexiones = st2.nextToken();
                StringTokenizer personas = new StringTokenizer(conexiones, ",");
                while (personas.hasMoreTokens()) {
                    String persona = personas.nextToken();
                    Persona p = Persona.getPersona(nodos[k]);
                    Persona p2 = Persona.getPersona(persona);
                    if (p != null && p2 != null) {
                        p.agregaAmigo(p2);
                    }
                }
                k++;
            }
            s = ent.readLine();
            StringTokenizer st3 = new StringTokenizer(s, "/");
            int i = 0;
            while (st3.hasMoreElements()) {
                String c2 = st3.nextToken();
                if (!(c2.equals("@"))) {
                    Persona x = encuentraElemento(c2);
                    Persona.personas.get(i).agregaEnemigo(x);
                }
                i++;
            }
            s = ent.readLine();
            int cont = 0;
            StringTokenizer st4 = new StringTokenizer(s, "/");
            while (st4.hasMoreElements()) {
                StringTokenizer sub4 = new StringTokenizer(st4.nextToken(), ",");
                String a = "";
                while (sub4.hasMoreElements()) {
                    String z = sub4.nextToken();
                    if (!(z.equals("0.0"))) {
                        a += z + "|";
                    }
                }
                StringTokenizer ultimo = new StringTokenizer(a, "|");
                for (int ult = 1; ult <= 3; ult++) {
                    Persona.personas.get(cont).setAttribute(ultimo.nextToken(), Float.parseFloat(ultimo.nextToken()));
                }
                cont++;
            }
            ent.close();
        } catch (Exception e) {
        }
    }
