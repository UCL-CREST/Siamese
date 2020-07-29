    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Clear")) {
            xstrokes.removeAllElements();
            ystrokes.removeAllElements();
            curxvec = null;
            curyvec = null;
            Rectangle r = c.getBounds();
            c.getGraphics().clearRect(0, 0, r.width, r.height);
            c.paint(c.getGraphics());
            current_character_strokes = "";
            currentStrokes.removeAllElements();
            return;
        }
        int sc;
        Vector minScores = new Vector();
        Vector minChars = new Vector();
        String curk;
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader("unistrok." + xstrokes.size()));
        } catch (Exception ew) {
            this.getToolkit().beep();
            return;
        }
        try {
            String line;
            while (true) {
                line = in.readLine();
                String goline = "";
                if (line == null) {
                    if (al == null) {
                        Enumeration e1 = minScores.elements();
                        Enumeration e2 = minChars.elements();
                        while (e1.hasMoreElements()) {
                            String kanji = (String) e2.nextElement();
                            Integer score = (Integer) e1.nextElement();
                        }
                    } else {
                        int sz;
                        sz = minChars.size();
                        char[] kanj = new char[sz];
                        int i;
                        for (i = 0; i < sz; i++) {
                            String s;
                            s = (String) minChars.elementAt(sz - i - 1);
                            if (s.charAt(0) == '0') kanj[i] = '?'; else {
                                int index;
                                index = s.indexOf(' ');
                                if (index != -1) s = s.substring(0, index);
                                try {
                                    int hexcode;
                                    hexcode = Integer.parseInt(s, 16);
                                    kanj[i] = (char) hexcode;
                                } catch (Exception ez11) {
                                    kanj[i] = '?';
                                }
                            }
                        }
                        ActionEvent ae = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, new String(kanj));
                        al.actionPerformed(ae);
                    }
                    return;
                } else {
                    if (line.length() == 0) continue;
                    if (line.charAt(0) == '#') continue;
                    int index;
                    index = line.indexOf('|');
                    if (index == -1) continue;
                    curk = line.substring(0, index);
                    line = line.substring(index + 1);
                    String tokline;
                    String argline;
                    int tokindex = line.indexOf('|');
                    if (tokindex != -1) {
                        tokline = line.substring(0, tokindex);
                        argline = line.substring(tokindex + 1);
                    } else {
                        argline = null;
                        tokline = line;
                    }
                    StringTokenizer st = new StringTokenizer(tokline);
                    if (st.countTokens() != xstrokes.size()) continue;
                    WhileLoop: while (st.hasMoreTokens()) {
                        String tok = st.nextToken();
                        int i;
                        for (i = 0; i < tok.length(); i++) {
                            switch(tok.charAt(i)) {
                                case '2':
                                case '1':
                                case '3':
                                case '4':
                                case '6':
                                case '7':
                                case '8':
                                case '9':
                                    goline = goline + tok.charAt(i);
                                    break;
                                case 'b':
                                    goline = goline + "62";
                                    break;
                                case 'c':
                                    goline = goline + "26";
                                    break;
                                case 'x':
                                    goline = goline + "21";
                                    break;
                                case 'y':
                                    goline = goline + "23";
                                    break;
                                case '|':
                                    break WhileLoop;
                                default:
                                    System.out.println("unknown symbol in kanji database");
                                    System.out.println(line);
                                    continue;
                            }
                        }
                        goline = goline + " ";
                    }
                    int ns;
                    if (minScores.size() < NUMKAN) ns = getScore(goline, 999999); else {
                        int cutoff1, cutoff2;
                        cutoff1 = ((Integer) minScores.firstElement()).intValue();
                        cutoff2 = ((Integer) minScores.lastElement()).intValue() * 2;
                        ns = getScore(goline, Math.min(cutoff1, cutoff2));
                    }
                    if (argline != null) {
                        st = new StringTokenizer(argline);
                        while (st.hasMoreTokens()) {
                            try {
                                String tok = st.nextToken();
                                int minindex;
                                minindex = tok.indexOf("-");
                                if (minindex == -1) {
                                    System.out.println("bad filter");
                                    continue;
                                }
                                String arg1, arg2;
                                arg1 = tok.substring(0, minindex);
                                arg2 = tok.substring(minindex + 1, tok.length());
                                int arg1stroke, arg2stroke;
                                arg1stroke = Integer.parseInt(arg1.substring(1));
                                boolean must = (arg2.charAt(arg2.length() - 1) == '!');
                                if (must) arg2stroke = Integer.parseInt(arg2.substring(1, arg2.length() - 1)); else arg2stroke = Integer.parseInt(arg2.substring(1));
                                Vector stroke1x, stroke1y, stroke2x, stroke2y;
                                stroke1x = (Vector) xstrokes.elementAt(arg1stroke - 1);
                                stroke1y = (Vector) ystrokes.elementAt(arg1stroke - 1);
                                stroke2x = (Vector) xstrokes.elementAt(arg2stroke - 1);
                                stroke2y = (Vector) ystrokes.elementAt(arg2stroke - 1);
                                int val1, val2;
                                switch(arg1.charAt(0)) {
                                    case 'x':
                                        val1 = ((Integer) stroke1x.firstElement()).intValue();
                                        break;
                                    case 'y':
                                        val1 = ((Integer) stroke1y.firstElement()).intValue();
                                        break;
                                    case 'i':
                                        val1 = ((Integer) stroke1x.lastElement()).intValue();
                                        break;
                                    case 'j':
                                        val1 = ((Integer) stroke1y.lastElement()).intValue();
                                        break;
                                    case 'a':
                                        val1 = (((Integer) stroke1x.firstElement()).intValue() + ((Integer) stroke1x.lastElement()).intValue()) / 2;
                                        break;
                                    case 'b':
                                        val1 = (((Integer) stroke1y.firstElement()).intValue() + ((Integer) stroke1y.lastElement()).intValue()) / 2;
                                        break;
                                    case 'l':
                                        int dx, dy;
                                        dx = ((Integer) stroke1x.lastElement()).intValue() - ((Integer) stroke1x.firstElement()).intValue();
                                        dy = ((Integer) stroke1y.lastElement()).intValue() - ((Integer) stroke1y.firstElement()).intValue();
                                        val1 = (int) (Math.sqrt((double) (dx * dx + dy * dy)));
                                        break;
                                    default:
                                        System.out.println("bad filter");
                                        continue;
                                }
                                switch(arg2.charAt(0)) {
                                    case 'x':
                                        val2 = ((Integer) stroke2x.firstElement()).intValue();
                                        break;
                                    case 'y':
                                        val2 = ((Integer) stroke2y.firstElement()).intValue();
                                        break;
                                    case 'i':
                                        val2 = ((Integer) stroke2x.lastElement()).intValue();
                                        break;
                                    case 'j':
                                        val2 = ((Integer) stroke2y.lastElement()).intValue();
                                        break;
                                    case 'a':
                                        val2 = (((Integer) stroke2x.firstElement()).intValue() + ((Integer) stroke2x.lastElement()).intValue()) / 2;
                                        break;
                                    case 'b':
                                        val2 = (((Integer) stroke2y.firstElement()).intValue() + ((Integer) stroke2y.lastElement()).intValue()) / 2;
                                        break;
                                    case 'l':
                                        int dx, dy;
                                        dx = ((Integer) stroke2x.lastElement()).intValue() - ((Integer) stroke2x.firstElement()).intValue();
                                        dy = ((Integer) stroke2y.lastElement()).intValue() - ((Integer) stroke2y.firstElement()).intValue();
                                        val2 = (int) (Math.sqrt((double) (dx * dx + dy * dy)));
                                        break;
                                    default:
                                        System.out.println("bad filter");
                                        continue;
                                }
                                ns = ns - (val1 - val2);
                                if (must && (val1 < val2)) ns += 9999999;
                            } catch (Exception ez2) {
                                System.out.println("bad filter");
                                continue;
                            }
                        }
                    }
                    int size;
                    size = minScores.size();
                    if ((size < NUMKAN) || (ns < ((Integer) minScores.firstElement()).intValue())) {
                        if (size == 0) {
                            minScores.addElement(new Integer(ns));
                            minChars.addElement(new String(curk));
                        } else {
                            if (ns <= ((Integer) minScores.lastElement()).intValue()) {
                                minScores.addElement(new Integer(ns));
                                minChars.addElement(new String(curk));
                            } else {
                                int i = 0;
                                while (((Integer) minScores.elementAt(i)).intValue() > ns) i++;
                                minScores.insertElementAt(new Integer(ns), i);
                                minChars.insertElementAt(new String(curk), i);
                            }
                        }
                    }
                    size = minScores.size();
                    if (size > NUMKAN) {
                        minScores.removeElementAt(0);
                        minChars.removeElementAt(0);
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
