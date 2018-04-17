    public void start(String infilesdir, String outfilesdir) {
        try {
            System.out.println("Creating EC import files ...");
            Vector ec_entries = new Vector();
            String filename_ec_classes = infilesdir + "enzclass.txt";
            String filename_ec_enzymes = infilesdir + "enzyme.dat";
            BufferedReader in_ec_classes = new BufferedReader(new FileReader(filename_ec_classes));
            boolean read = false;
            Vector lines = new Vector();
            String inputline = in_ec_classes.readLine();
            while (in_ec_classes.ready()) {
                if (inputline.startsWith("1.") && !read) read = true; else if (inputline.startsWith("------") && read) read = false;
                if (read && (inputline.length() > 0)) {
                    if (inputline.indexOf(".") == inputline.lastIndexOf(".")) {
                        String lastline = (String) lines.get(lines.size() - 1);
                        lastline = lastline + " " + inputline.trim();
                        lines.set(lines.size() - 1, lastline);
                    } else lines.add(inputline);
                }
                inputline = in_ec_classes.readLine();
            }
            in_ec_classes.close();
            Map nr2name = new TreeMap();
            for (int i = 0; i < lines.size(); i++) {
                String line = (String) lines.get(i);
                int split = 9;
                String ec_nr = line.substring(0, split);
                String ec_name = line.substring(split, line.length());
                StringTokenizer st = new StringTokenizer(ec_nr);
                String temp = "";
                while (st.hasMoreTokens()) temp = temp + st.nextToken();
                ec_nr = temp;
                ec_name = ec_name.trim();
                if (ec_name.endsWith(".")) ec_name = ec_name.substring(0, ec_name.length() - 1);
                if (getNrHyphens(ec_nr) == 3) if (ec_name.endsWith("s")) ec_name = ec_name.substring(0, ec_name.length() - 1);
                nr2name.put(ec_nr, ec_name);
                String is_a_nr = getHigherTerm(ec_nr);
                String is_a_name = (String) nr2name.get(is_a_nr);
                if (is_a_name == null) is_a_name = "";
                Vector entry = new Vector();
                entry.add(ec_nr);
                entry.add(ec_name);
                entry.add(is_a_nr);
                entry.add(is_a_name);
                entry.add(new Vector());
                entry.add("");
                ec_entries.add(entry);
            }
            BufferedReader in_ec_enzymes = new BufferedReader(new FileReader(filename_ec_enzymes));
            String ec_nr = "";
            String ec_name = "";
            Vector synonyms = new Vector();
            String description = "";
            String description_ca = "";
            String description_cf = "";
            String description_cc = "";
            inputline = in_ec_enzymes.readLine();
            while (in_ec_enzymes.ready() && (inputline.compareTo("//") != 0)) inputline = in_ec_enzymes.readLine();
            boolean synonym_end = true;
            boolean ec_name_end = true;
            Set ready = new TreeSet();
            while (in_ec_enzymes.ready()) {
                if ((inputline.compareTo("//") == 0) && (ec_nr.length() > 0)) {
                    ec_entries = createEntry(ec_nr, ec_name, synonyms, description, description_ca, description_cf, description_cc, nr2name, ready, ec_entries);
                    ec_nr = "";
                    ec_name = "";
                    synonyms = new Vector();
                    description = "";
                    description_ca = "";
                    description_cf = "";
                    description_cc = "";
                }
                if (inputline.startsWith("ID")) {
                    StringTokenizer st = new StringTokenizer(inputline);
                    st.nextToken();
                    ec_nr = st.nextToken();
                }
                if (inputline.startsWith("DE")) {
                    StringTokenizer st = new StringTokenizer(inputline);
                    st.nextToken();
                    if (ec_name_end) ec_name = st.nextToken(); else {
                        if (ec_name.endsWith("-")) ec_name = ec_name + st.nextToken(); else ec_name = ec_name + " " + st.nextToken();
                        ec_name_end = true;
                    }
                    while (st.hasMoreTokens()) ec_name = ec_name + " " + st.nextToken();
                    if (ec_name.endsWith(".")) ec_name = ec_name.substring(0, ec_name.length() - 1); else ec_name_end = false;
                }
                if (inputline.startsWith("AN")) {
                    StringTokenizer st = new StringTokenizer(inputline);
                    st.nextToken();
                    String synonym = "";
                    if (synonym_end) synonym = st.nextToken(); else {
                        int lastpos = synonyms.size() - 1;
                        String lastsyn = (String) synonyms.remove(lastpos);
                        if (lastsyn.endsWith("-")) synonym = lastsyn + st.nextToken(); else synonym = lastsyn + " " + st.nextToken();
                        synonym_end = true;
                    }
                    while (st.hasMoreTokens()) synonym = synonym + " " + st.nextToken();
                    if (synonym.endsWith(".")) synonym = synonym.substring(0, synonym.length() - 1); else synonym_end = false;
                    synonyms.add(synonym);
                }
                if (inputline.startsWith("CA")) {
                    StringTokenizer st = new StringTokenizer(inputline);
                    st.nextToken();
                    while (st.hasMoreTokens()) description_ca = description_ca + " " + st.nextToken();
                }
                if (inputline.startsWith("CF")) {
                    StringTokenizer st = new StringTokenizer(inputline);
                    st.nextToken();
                    while (st.hasMoreTokens()) description_cf = description_cf + " " + st.nextToken();
                }
                if (inputline.startsWith("CC")) {
                    StringTokenizer st = new StringTokenizer(inputline);
                    st.nextToken();
                    while (st.hasMoreTokens()) description_cc = description_cc + " " + st.nextToken();
                }
                inputline = in_ec_enzymes.readLine();
            }
            ec_entries = createEntry(ec_nr, ec_name, synonyms, description, description_ca, description_cf, description_cc, nr2name, ready, ec_entries);
            in_ec_enzymes.close();
            String outfile_concept = outfilesdir + "ec_concept.txt";
            String outfile_concept_name = outfilesdir + "ec_concept_name.txt";
            String outfile_concept_acc = outfilesdir + "ec_concept_acc.txt";
            String outfile_relation = outfilesdir + "ec_relation.txt";
            BufferedWriter out_ec_concept = new BufferedWriter(new FileWriter(outfile_concept));
            BufferedWriter out_ec_concept_name = new BufferedWriter(new FileWriter(outfile_concept_name));
            BufferedWriter out_ec_concept_acc = new BufferedWriter(new FileWriter(outfile_concept_acc));
            BufferedWriter out_ec_relation = new BufferedWriter(new FileWriter(outfile_relation));
            for (int i = 0; i < ec_entries.size(); i++) {
                Vector entry = (Vector) ec_entries.get(i);
                ec_nr = (String) entry.get(0);
                ec_name = (String) entry.get(1);
                String is_a_nr = (String) entry.get(2);
                String is_a_name = (String) entry.get(3);
                synonyms = (Vector) entry.get(4);
                description = (String) entry.get(5);
                String id = "EC:" + ec_nr;
                String pos_tag = "";
                String descript = description;
                String element_of = "EC";
                out_ec_concept.write(id + "\t" + pos_tag + "\t" + descript + "\t" + "Thing" + "\t");
                out_ec_concept.write(element_of + "\n");
                String name = ec_name;
                String name_stemmed = "";
                String name_tagged = "";
                String is_unique = "0";
                String is_preferred = "1";
                String original_name = ec_name;
                String is_not_substring = "0";
                String synonym_type = "STD";
                out_ec_concept_name.write(id + "\t" + name + "\t" + name_stemmed + "\t" + name_tagged + "\t");
                out_ec_concept_name.write(element_of + "\t" + is_unique + "\t" + is_preferred + "\t");
                out_ec_concept_name.write(original_name + "\t" + is_not_substring + "\t" + synonym_type + "\n");
                Set names = new TreeSet();
                names.add(name);
                for (int j = 0; j < synonyms.size(); j++) {
                    name = (String) synonyms.get(j);
                    if (!names.contains(name)) {
                        names.add(name);
                        is_preferred = "0";
                        original_name = name;
                        out_ec_concept_name.write(id + "\t" + name + "\t" + name_stemmed + "\t" + name_tagged + "\t");
                        out_ec_concept_name.write(element_of + "\t" + is_unique + "\t" + is_preferred + "\t");
                        out_ec_concept_name.write(original_name + "\t" + is_not_substring + "\t" + synonym_type + "\n");
                    }
                }
                String id_acc = ec_nr;
                out_ec_concept_acc.write(id + "\t" + id_acc + "\t" + element_of + "\n");
                if (is_a_nr.length() > 0) {
                    String from_id = id;
                    String to_id = "EC:" + is_a_nr;
                    String rel_type = "is_a";
                    String from_element_of = "EC";
                    String to_element_of = "EC";
                    String from_name = ec_name;
                    String to_name = is_a_name;
                    out_ec_relation.write(from_id + "\t" + to_id + "\t" + rel_type + "\t");
                    out_ec_relation.write(from_element_of + "\t" + to_element_of + "\n");
                }
            }
            out_ec_concept.close();
            out_ec_concept_name.close();
            out_ec_concept_acc.close();
            out_ec_relation.close();
            String concept_string = "copy concept ( id, pos_tag, description, of_type_fk,element_of ) ";
            String concept_name_string = "copy concept_name ( id, name, name_stemmed, name_tagged, element_of, is_unique, is_preferred, name_orig, is_not_substring, synonym_type ) ";
            String concept_acc_string = "copy concept_acc ( id, concept_accession, element_of ) ";
            String relation_string = "copy relation ( from_concept, to_concept, of_type, from_element_of, to_element_of ) ";
            String outfile_ec_script = outfilesdir + "ec.sql";
            BufferedWriter out_ec_script = new BufferedWriter(new FileWriter(outfile_ec_script));
            out_ec_script.write(concept_string + "from '" + outfilesdir + "ec_concept.txt';\n");
            out_ec_script.write(concept_acc_string + "from '" + outfilesdir + "ec_concept_acc.txt';\n");
            out_ec_script.write(concept_name_string + "from '" + outfilesdir + "ec_concept_name.txt';\n");
            out_ec_script.write(relation_string + " from '" + outfilesdir + "ec_relation.txt';\n");
            out_ec_script.close();
        } catch (Exception e) {
            System.out.println("Exception message: " + e.getMessage());
        }
    }
