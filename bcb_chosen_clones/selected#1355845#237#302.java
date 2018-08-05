    private ArrayList<SynopsisObject> parseClinicalSynopsis(String cs_text) {
        ArrayList<SynopsisObject> synopsis_list = new ArrayList<SynopsisObject>();
        String domain_pattern = new String(".*?:\n");
        String subdomain_pattern = new String("\\[.*?];");
        Pattern p;
        Matcher m;
        int start = 0;
        int prev_end = 0;
        ArrayList<String> domain_list = new ArrayList<String>();
        ArrayList<TextBlock> domain_position = new ArrayList<TextBlock>();
        TextBlock domain_entry_length = new TextBlock();
        ArrayList<String> subdomain_list = new ArrayList<String>();
        ArrayList<TextBlock> subdomain_position = new ArrayList<TextBlock>();
        TextBlock subdomain_entry_length = new TextBlock();
        p = Pattern.compile(domain_pattern);
        m = p.matcher(cs_text);
        if (m.find()) {
            start = m.end();
            domain_list.add(cleanDomain(m.group()));
            while (m.find()) {
                prev_end = m.start();
                domain_entry_length = new TextBlock(start, prev_end);
                domain_position.add(domain_entry_length);
                start = m.end();
                domain_list.add(cleanDomain(m.group()));
            }
            prev_end = cs_text.length();
            domain_entry_length = new TextBlock(start, prev_end);
            domain_position.add(domain_entry_length);
        }
        p = Pattern.compile(subdomain_pattern);
        for (int z = 0; z <= domain_position.size() - 1; z++) {
            TextBlock tb = domain_position.get(z);
            String domain_text = cs_text.substring(tb.getStart(), tb.getEnd());
            subdomain_list = new ArrayList<String>();
            subdomain_position = new ArrayList<TextBlock>();
            subdomain_entry_length = new TextBlock();
            m = p.matcher(domain_text);
            if (m.find()) {
                start = m.end();
                subdomain_list.add(cleanSubDomain(m.group()));
                while (m.find()) {
                    prev_end = m.start();
                    subdomain_entry_length = new TextBlock(start, prev_end);
                    subdomain_position.add(subdomain_entry_length);
                    start = m.end();
                    subdomain_list.add(cleanSubDomain(m.group()));
                }
                prev_end = domain_text.length();
                subdomain_entry_length = new TextBlock(start, prev_end);
                subdomain_position.add(subdomain_entry_length);
            } else {
                start = 0;
                prev_end = domain_text.length();
                subdomain_entry_length = new TextBlock(start, prev_end);
                subdomain_position.add(subdomain_entry_length);
                subdomain_list.add("none");
            }
            for (int x = 0; x <= subdomain_position.size() - 1; x++) {
                TextBlock stb = subdomain_position.get(x);
                String features[] = domain_text.substring(stb.getStart(), stb.getEnd()).replaceAll(LINE_TERMINATOR, "").trim().split(";");
                for (String feature : features) synopsis_list.add(new SynopsisObject(domain_list.get(z), subdomain_list.get(x), feature.trim()));
            }
        }
        return synopsis_list;
    }
