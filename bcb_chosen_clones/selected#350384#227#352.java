    private void execute() {
        Iterator<Entry<String, File>> iterator = files.entrySet().iterator();
        int t = 0;
        Pattern pattern = Pattern.compile("\\[(.+?)\\]");
        Pattern replacePattern = Pattern.compile(".*[/\\\\_@].*");
        while (iterator.hasNext()) {
            Entry<String, File> entry = iterator.next();
            currentFilename = entry.getValue().getName();
            fileMap.put(currentFilename, t);
            String label = nscripterMap.addLabel(currentFilename, null);
            BufferedReader in = null;
            BufferedWriter out = null;
            try {
                in = new BufferedReader(new InputStreamReader(new FileInputStream(entry.getValue()), "UTF-16"));
                out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(targetDir, t + ".txt")), "GBK"));
                if (t == 0) {
                    writer(out, ";mode800");
                    writer(out, "*define");
                    writer(out, "caption \"甘井子传说\"");
                    writer(out, "roff");
                    writer(out, "savenumber 10");
                    writer(out, "kidokuskip");
                    writer(out, "windowback");
                    writer(out, "numalias tempnum," + nscripterMap.getNumberVariable("temp_num").substring(1));
                    writer(out, "game");
                    writer(out, "*start");
                }
                writer(out, label);
                String line;
                num = 0;
                for (int i = 1; (line = in.readLine()) != null; i++) {
                    num++;
                    line = line.trim();
                    if (line.length() == 0) continue;
                    if (line.startsWith(";")) continue;
                    if (line.startsWith("*")) {
                        int index = line.indexOf('|');
                        if (index >= 0) line = line.substring(0, index);
                        writer(out, nscripterMap.addLabel(currentFilename, line));
                        continue;
                    }
                    if (line.startsWith("@")) {
                        String command = transform(line.substring(1));
                        if (command != null) {
                            writer(out, command);
                        }
                        continue;
                    }
                    Matcher matcher = pattern.matcher(line);
                    StringBuilder sb = new StringBuilder();
                    int index = 0;
                    while (matcher.find()) {
                        String temp = line.substring(index, matcher.start());
                        if (replacePattern.matcher(temp).matches()) {
                            for (int j = 0; j < temp.length(); j++) {
                                char c = temp.charAt(j);
                                switch(c) {
                                    case '/':
                                        sb.append('／');
                                        break;
                                    case '\\':
                                        sb.append('＼');
                                        break;
                                    case '_':
                                        sb.append('＿');
                                        break;
                                    case '@':
                                        sb.append('＠');
                                        break;
                                    default:
                                        sb.append(c);
                                        break;
                                }
                            }
                        } else {
                            sb.append(temp);
                        }
                        index = matcher.end();
                        String command = transform(matcher.group(1));
                        if (command == null) command = "";
                        sb.append(command);
                    }
                    sb.append(line.substring(index, line.length()));
                    if (sb.length() > 0) {
                        writer(out, sb.toString());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (in != null) try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (out != null) try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            t++;
        }
        if (!ifMap.isEmpty()) {
            BufferedWriter out = null;
            try {
                out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(targetDir, t++ + ".txt")), "GBK"));
                Iterator<Entry<String, String>> iter = ifMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Entry<String, String> entry = iter.next();
                    out.write(entry.getKey());
                    out.newLine();
                    out.write(entry.getValue());
                    out.newLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (out != null) try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
