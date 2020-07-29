    public void execute() throws BuildException {
        System.out.println("reading " + dir);
        File target_dir = new File(dir);
        String[] children = target_dir.list();
        if (children == null) {
        } else {
            for (int i = 0; i < children.length; i++) {
                if (children[i].endsWith(".java")) java_files.add(children[i]);
            }
        }
        for (int i = 0; i < java_files.size(); i++) {
            StringBuffer the_file = new StringBuffer();
            String file_name = dir + "/" + java_files.get(i);
            System.out.print("Searching " + file_name + "....");
            int substitutions = 0;
            try {
                FileReader file_reader = new FileReader(file_name);
                BufferedReader buf_reader = new BufferedReader(file_reader);
                int line_number = 0;
                do {
                    String line = buf_reader.readLine();
                    line_number++;
                    if (line == null) break;
                    boolean line_ends_with_plus = line.endsWith("+");
                    StringTokenizer tokens = new StringTokenizer(line, "(");
                    String sys_out_print = null;
                    try {
                        sys_out_print = tokens.nextToken();
                    } catch (NoSuchElementException nse) {
                    }
                    if (sys_out_print == null) {
                    } else if (sys_out_print.trim().equals(target_string)) {
                        String after_paren = "";
                        try {
                            after_paren = tokens.nextToken();
                        } catch (NoSuchElementException nse) {
                        }
                        StringTokenizer tokens2 = new StringTokenizer(after_paren, "+");
                        String immed_after_paren = tokens2.nextToken().replace('"', ' ').trim();
                        String spatt = ".*[.]java[,][\\s][\\d]+[:]";
                        Pattern ppatt = Pattern.compile(spatt);
                        Matcher pmatch = ppatt.matcher(immed_after_paren);
                        if (pmatch.matches()) {
                            immed_after_paren.replace(':', ' ').trim();
                            StringBuffer new_line = new StringBuffer(sys_out_print + "(\"" + java_files.get(i) + ", " + line_number + ": \" +");
                            while (tokens2.hasMoreTokens()) new_line.append(tokens2.nextToken() + "+");
                            String new_line2 = new_line.toString().substring(0, new_line.length() - 1);
                            StringBuffer new_line3 = new StringBuffer(new_line2 + "(");
                            while (tokens.hasMoreTokens()) new_line3.append(tokens.nextToken() + "(");
                            String new_line4 = new_line3.toString().substring(0, new_line3.length() - 1);
                            if ((line_ends_with_plus) && !new_line4.endsWith("+")) new_line4 = new_line4 + "+";
                            line = new_line4;
                            substitutions++;
                        } else {
                            String patternStr = target_string + "\\(";
                            String replacementStr = target_string + "(\"" + java_files.get(i) + ", " + line_number + ": \" + ";
                            Pattern pattern = Pattern.compile(patternStr);
                            Matcher matcher = pattern.matcher(line);
                            String new_line = matcher.replaceAll(replacementStr);
                            line = new_line;
                            substitutions++;
                        }
                    }
                    the_file.append(line + "\n");
                } while (true);
                buf_reader.close();
            } catch (IOException e) {
                System.out.println("IO exception: " + e);
            }
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter(file_name));
                out.write(the_file.toString());
                out.close();
            } catch (IOException e) {
                System.out.println("Write IOE");
            }
            System.out.println(" " + substitutions + " substitutions.");
        }
    }
