    private static void createNonCompoundData(String dir, String type) {
        try {
            Set s = new HashSet();
            File nouns = new File(dir + "index." + type);
            FileInputStream fis = new FileInputStream(nouns);
            InputStreamReader reader = new InputStreamReader(fis);
            StringBuffer sb = new StringBuffer();
            int chr = reader.read();
            while (chr >= 0) {
                if (chr == '\n' || chr == '\r') {
                    String line = sb.toString();
                    if (line.length() > 0) {
                        if (line.charAt(0) != ' ') {
                            String[] spaceSplit = PerlHelp.split(line);
                            if (spaceSplit[0].indexOf('_') < 0) {
                                s.add(spaceSplit[0]);
                            }
                        }
                    }
                    sb.setLength(0);
                } else {
                    sb.append((char) chr);
                }
                chr = reader.read();
            }
            System.out.println(type + " size=" + s.size());
            File output = new File(dir + "nonCompound." + type + "s.gz");
            FileOutputStream fos = new FileOutputStream(output);
            GZIPOutputStream gzos = new GZIPOutputStream(new BufferedOutputStream(fos));
            PrintWriter writer = new PrintWriter(gzos);
            writer.println("# This file was extracted from WordNet data, the following copyright notice");
            writer.println("# from WordNet is attached.");
            writer.println("#");
            writer.println("#  This software and database is being provided to you, the LICENSEE, by  ");
            writer.println("#  Princeton University under the following license.  By obtaining, using  ");
            writer.println("#  and/or copying this software and database, you agree that you have  ");
            writer.println("#  read, understood, and will comply with these terms and conditions.:  ");
            writer.println("#  ");
            writer.println("#  Permission to use, copy, modify and distribute this software and  ");
            writer.println("#  database and its documentation for any purpose and without fee or  ");
            writer.println("#  royalty is hereby granted, provided that you agree to comply with  ");
            writer.println("#  the following copyright notice and statements, including the disclaimer,  ");
            writer.println("#  and that the same appear on ALL copies of the software, database and  ");
            writer.println("#  documentation, including modifications that you make for internal  ");
            writer.println("#  use or for distribution.  ");
            writer.println("#  ");
            writer.println("#  WordNet 1.7 Copyright 2001 by Princeton University.  All rights reserved. ");
            writer.println("#  ");
            writer.println("#  THIS SOFTWARE AND DATABASE IS PROVIDED \"AS IS\" AND PRINCETON  ");
            writer.println("#  UNIVERSITY MAKES NO REPRESENTATIONS OR WARRANTIES, EXPRESS OR  ");
            writer.println("#  IMPLIED.  BY WAY OF EXAMPLE, BUT NOT LIMITATION, PRINCETON  ");
            writer.println("#  UNIVERSITY MAKES NO REPRESENTATIONS OR WARRANTIES OF MERCHANT-  ");
            writer.println("#  ABILITY OR FITNESS FOR ANY PARTICULAR PURPOSE OR THAT THE USE  ");
            writer.println("#  OF THE LICENSED SOFTWARE, DATABASE OR DOCUMENTATION WILL NOT  ");
            writer.println("#  INFRINGE ANY THIRD PARTY PATENTS, COPYRIGHTS, TRADEMARKS OR ");
            writer.println("#  OTHER RIGHTS. ");
            writer.println("#  ");
            writer.println("#  The name of Princeton University or Princeton may not be used in");
            writer.println("#  advertising or publicity pertaining to distribution of the software");
            writer.println("#  and/or database.  Title to copyright in this software, database and");
            writer.println("#  any associated documentation shall at all times remain with");
            writer.println("#  Princeton University and LICENSEE agrees to preserve same.  ");
            for (Iterator i = s.iterator(); i.hasNext(); ) {
                String mwe = (String) i.next();
                writer.println(mwe);
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
