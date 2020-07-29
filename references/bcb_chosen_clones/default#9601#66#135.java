    public ArrayList<Character> loadFile(BufferedReader file) throws IOException {
        String line;
        ArrayList<Character> characters = new ArrayList<Character>(1000);
        line = file.readLine();
        while ((line = file.readLine()) != null) {
            Character currentCharacter = new Character();
            if (line.length() == 0) continue;
            if (line.charAt(0) == '#') continue;
            int pipe;
            String unicode = line.substring(0, line.indexOf(' '));
            line = line.substring(line.indexOf(" ") + 1);
            if (line.indexOf(" ") < 0) {
                continue;
            }
            line = line.substring(line.indexOf(" "));
            pipe = line.indexOf('|');
            if (pipe == -1) {
                continue;
            }
            currentCharacter.codepoint = Integer.parseInt(unicode, 16);
            line = line.substring(pipe + 1);
            String tokline, argline;
            int tokindex = line.indexOf('|');
            if (tokindex != -1) {
                tokline = line.substring(0, tokindex);
                argline = line.substring(tokindex + 1);
            } else {
                argline = null;
                tokline = line;
            }
            StringTokenizer st = new StringTokenizer(tokline);
            WhileLoop: while (st.hasMoreTokens()) {
                String tok = st.nextToken();
                for (int i = 0; i < tok.length(); i++) {
                    switch(tok.charAt(i)) {
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            currentCharacter.addStroke(tok.charAt(i) - '0');
                            break;
                        case 'b':
                            currentCharacter.addStroke(62);
                            break;
                        case 'c':
                            currentCharacter.addStroke(26);
                            break;
                        case 'x':
                            currentCharacter.addStroke(21);
                            break;
                        case 'y':
                            currentCharacter.addStroke(23);
                            break;
                        case '|':
                            break WhileLoop;
                        default:
                            System.out.println("unknown symbol in kanji database: " + tok.charAt(i));
                            System.out.println(line);
                            continue;
                    }
                }
            }
            characters.add(currentCharacter);
        }
        return characters;
    }
