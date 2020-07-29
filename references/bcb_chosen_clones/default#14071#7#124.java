    static void encode(String source, String destination, boolean noSeconds) {
        FileReader fr = null;
        BufferedReader br = null;
        RandomAccessFile raf = null;
        DatabaseHeader databaseHeader = new DatabaseHeader();
        AstroAppInfo astroAppInfo = new AstroAppInfo();
        RecordIndex recordIndex = new RecordIndex();
        Vector records = new Vector();
        try {
            fr = new FileReader(source);
            br = new BufferedReader(fr);
            raf = new RandomAccessFile(destination, "rw");
        } catch (FileNotFoundException fnfe) {
            System.err.println("Could not open " + source + ".");
            System.err.println(fnfe.getMessage());
            return;
        } catch (IOException ioe) {
            System.err.println("Could not open " + destination + ".");
            System.err.println(ioe.getMessage());
            return;
        }
        try {
            databaseHeader.setName(br.readLine());
            databaseHeader.setVersion((short) 1);
            databaseHeader.setTypeID("DATA");
            databaseHeader.setCreatorID("MWH2");
            while (br.ready()) {
                AstroRecord ar = new AstroRecord();
                String buffer = br.readLine();
                StringTokenizer st = null;
                String token;
                boolean negative;
                try {
                    st = new StringTokenizer(buffer, "\t", false);
                } catch (Exception e) {
                    throw new EOFException();
                }
                if (st.countTokens() == 0) throw new EOFException();
                ar.description = st.nextToken();
                token = st.nextToken();
                negative = false;
                if (token.charAt(0) == '-') {
                    negative = true;
                    token = token.substring(1);
                }
                ar.ra = Integer.parseInt(token) * Math.PI / 12;
                if (noSeconds) {
                    ar.ra += Double.valueOf(st.nextToken()).doubleValue() * Math.PI / (12 * 60);
                } else {
                    ar.ra += Integer.parseInt(st.nextToken()) * Math.PI / (12 * 60);
                    ar.ra += Double.valueOf(st.nextToken()).doubleValue() * Math.PI / (12 * 60 * 60);
                }
                if (negative) ar.ra = -ar.ra;
                token = st.nextToken();
                negative = false;
                if (token.charAt(0) == '-') {
                    negative = true;
                    token = token.substring(1);
                }
                ar.dec = Integer.parseInt(token) * Math.PI / 180;
                if (noSeconds) {
                    ar.dec += Double.valueOf(st.nextToken()).doubleValue() * Math.PI / (180 * 60);
                } else {
                    ar.dec += Integer.parseInt(st.nextToken()) * Math.PI / (180 * 60);
                    ar.dec += Double.valueOf(st.nextToken()).doubleValue() * Math.PI / (180 * 60 * 60);
                }
                if (negative) ar.dec = -ar.dec;
                token = st.nextToken();
                if (token.equals("--")) ar.magnitude = 127.99; else ar.magnitude = Double.valueOf(token).doubleValue();
                token = st.nextToken();
                if (token.equals("--")) ar.sizex = ar.sizey = 0; else {
                    int delim = token.indexOf('x');
                    if (delim < 0) delim = token.indexOf('/');
                    if (delim < 0) ar.sizex = ar.sizey = Double.valueOf(token).doubleValue(); else {
                        ar.sizex = Double.valueOf(token.substring(0, delim)).doubleValue();
                        ar.sizey = Double.valueOf(token.substring(delim + 1)).doubleValue();
                    }
                }
                ar.type = -1;
                if (st.hasMoreTokens()) {
                    token = st.nextToken();
                    if (!token.equals("--")) ar.type = Integer.parseInt(token);
                }
                records.addElement(ar);
                recordIndex.addOffset(new RecordOffset());
            }
        } catch (EOFException ee) {
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
            return;
        }
        try {
            int offset = databaseHeader.getSize() + recordIndex.getSize();
            databaseHeader.setAppInfoID(offset);
            offset += astroAppInfo.getSize();
            astroAppInfo.numRecords = recordIndex.getNumRecords();
            astroAppInfo.currRecord = 0;
            for (int i = 0; i < astroAppInfo.numRecords; i++) {
                RecordOffset ro = recordIndex.getOffset(i);
                AstroRecord ar = (AstroRecord) records.elementAt(i);
                ro.setOffset(offset);
                offset = offset + ar.getSize();
            }
            br.close();
            databaseHeader.write(raf);
            recordIndex.write(raf);
            astroAppInfo.write(raf);
            for (int i = 0; i < astroAppInfo.numRecords; i++) {
                AstroRecord ar = (AstroRecord) records.elementAt(i);
                ar.write(raf);
            }
            raf.close();
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }
