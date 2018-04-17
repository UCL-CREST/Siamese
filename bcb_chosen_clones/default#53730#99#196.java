    public void loadGame(String name) {
        String playfile = "";
        URL theURL;
        BufferedReader in;
        boolean weregood = false;
        boolean malformed = false;
        try {
            theURL = new URL(name);
            URLConnection conn = null;
            BufferedReader data = null;
            String line;
            StringBuffer buf = new StringBuffer();
            try {
                conn = theURL.openConnection();
                conn.connect();
                data = new BufferedReader(new InputStreamReader(new BufferedInputStream(conn.getInputStream())));
                while ((line = data.readLine()) != null) {
                    buf.append(line);
                }
                playfile = buf.toString();
                data.close();
                weregood = true;
            } catch (IOException e) {
                System.out.println("IO Error:" + e.getMessage());
            }
        } catch (MalformedURLException e) {
            System.out.println("Bad URL: " + name);
            malformed = true;
        }
        if (malformed) {
            System.out.println("trying file: " + name);
            try {
                File file = new File(name);
                if (file != null && file.exists()) {
                    long inlen = (file.length());
                    in = new BufferedReader(new FileReader((File) file));
                    if (in != null) {
                        try {
                            int linecount = 0;
                            while ((playfile.length() + linecount) < inlen) {
                                playfile = playfile.concat(in.readLine());
                                linecount++;
                            }
                            weregood = true;
                        } catch (IOException io) {
                            System.out.println(io.toString());
                            weregood = false;
                        }
                    }
                }
            } catch (SecurityException ex) {
                System.out.println(ex.toString());
            } catch (Exception ex) {
                System.out.println(ex.toString());
            }
        }
        if (weregood) {
        } else {
            playfile = "[[BeginPlaybookFile 2.0 1~3000:260,100`3001:260,120`3002:260,140`3003:260,160`3004:260,180`3005:260,200`3006:260,220`3007:260,240`1000:260,260`1001:260,280`1002:260,300`1003:260,320`1004:260,340`1005:260,360`1006:260,380`2000:260,400`2001:260,420`2002:260,440`2003:260,460`2004:260,480`2005:260,500`2006:260,520`4000:160,540`~100~couldn't load url or file!| EndPlaybookFile]]";
            System.out.println("couldn't load url or file; using fallback example instead");
        }
        weregood = false;
        String header = playfile.substring(0, 23);
        if (header.equals("[[BeginPlaybookFile 2.0")) {
            weregood = true;
        } else if (header.equals("[[BeginPlaybookFile 1.0")) {
            System.out.println("UltiamtePlayBook 1.0 files not supported anymore!");
        } else {
            System.out.println("couldn't parse file; Not a UltiamtePlayBook 2.0 file!");
        }
        if (weregood) {
            String pframes = playfile.substring(23, playfile.lastIndexOf('|'));
            for (StringTokenizer t = new StringTokenizer(pframes, "|"); t.hasMoreTokens(); ) {
                String pframeraw = t.nextToken();
                PFrame apframe = new PFrame();
                int lag = Integer.valueOf(pframeraw.substring(pframeraw.indexOf('~', 10) + 1, pframeraw.lastIndexOf('~'))).intValue();
                apframe.setLag(lag);
                String fcomment = pframeraw.substring(pframeraw.lastIndexOf('~') + 1, pframeraw.length());
                apframe.setComment(fcomment);
                int i = pframeraw.indexOf('~') + 1;
                int endi = pframeraw.indexOf('~', 10);
                String pframe = pframeraw.substring(i, endi);
                for (StringTokenizer tk = new StringTokenizer(pframe, "`"); tk.hasMoreTokens(); ) {
                    String player = tk.nextToken();
                    Position pos = new Position();
                    int si = player.indexOf(':');
                    int ci = player.indexOf(',');
                    int ei = player.length();
                    pos.x = Integer.valueOf(player.substring(si + 1, ci)).intValue();
                    pos.y = Integer.valueOf(player.substring(ci + 1, ei)).intValue();
                    apframe.addPosition(pos);
                }
                game.addPFrame(apframe);
            }
        } else {
            System.out.println("Try another file!");
        }
    }
