    public void setInput(String input, Component caller, FFMpegProgressReceiver recv) throws IOException {
        inputMedium = null;
        if (input.contains("youtube")) {
            URL url = new URL(input);
            InputStreamReader read = new InputStreamReader(url.openStream());
            BufferedReader in = new BufferedReader(read);
            String inputLine;
            String line = null;
            String vid = input.substring(input.indexOf("?v=") + 3);
            if (vid.indexOf("&") != -1) vid = vid.substring(0, vid.indexOf("&"));
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.contains("\"t\": \"")) {
                    line = inputLine.substring(inputLine.indexOf("\"t\": \"") + 6);
                    line = line.substring(0, line.indexOf("\""));
                    break;
                }
            }
            in.close();
            if (line == null) throw new IOException("Could not find flv-Video");
            Downloader dl = new Downloader("http://www.youtube.com/get_video?video_id=" + vid + "&t=" + line, recv, lang);
            dl.start();
            return;
        }
        Runtime rt = Runtime.getRuntime();
        Process p = rt.exec(new String[] { path, "-i", input });
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        String line;
        Codec videoCodec = null;
        Codec audioCodec = null;
        double duration = -1;
        String aspectRatio = null;
        String scala = null;
        String colorSpace = null;
        String rate = null;
        String mrate = null;
        String aRate = null;
        String aFreq = null;
        String aChannel = null;
        try {
            while ((line = br.readLine()) != null) {
                if (Constants.debug) System.out.println(line);
                if (line.contains("Duration:")) {
                    int hours = Integer.parseInt(line.substring(12, 14));
                    int mins = Integer.parseInt(line.substring(15, 17));
                    double secs = Double.parseDouble(line.substring(18, line.indexOf(',')));
                    duration = secs + 60 * mins + hours * 60 * 60;
                    Pattern pat = Pattern.compile("[0-9]+ kb/s");
                    Matcher m = pat.matcher(line);
                    if (m.find()) mrate = line.substring(m.start(), m.end());
                }
                if (line.contains("Video:")) {
                    String info = line.substring(24);
                    String parts[] = info.split(", ");
                    Pattern pat = Pattern.compile("Video: [a-zA-Z0-9]+,");
                    Matcher m = pat.matcher(line);
                    String codec = "";
                    if (m.find()) codec = line.substring(m.start(), m.end());
                    videoCodec = supportedCodecs.getCodecByName(codec.replace("Video: ", "").replace(",", ""));
                    colorSpace = parts[1];
                    pat = Pattern.compile("[0-9]+x[0-9]+");
                    m = pat.matcher(info);
                    if (m.find()) scala = info.substring(m.start(), m.end());
                    pat = Pattern.compile("DAR [0-9]+:[0-9]+");
                    m = pat.matcher(info);
                    if (m.find()) aspectRatio = info.substring(m.start(), m.end()).replace("DAR ", ""); else if (scala != null) aspectRatio = String.valueOf((double) (Math.round(((double) ConvertUtils.getWidthFromScala(scala) / (double) ConvertUtils.getHeightFromScala(scala)) * 100)) / 100);
                    pat = Pattern.compile("[0-9]+ kb/s");
                    m = pat.matcher(info);
                    if (m.find()) rate = info.substring(m.start(), m.end());
                } else if (line.contains("Audio:")) {
                    String info = line.substring(24);
                    Pattern pat = Pattern.compile("Audio: [a-zA-Z0-9]+,");
                    Matcher m = pat.matcher(line);
                    String codec = "";
                    if (m.find()) codec = line.substring(m.start(), m.end()).replace("Audio: ", "").replace(",", "");
                    if (codec.equals("mp3")) codec = "libmp3lame";
                    audioCodec = supportedCodecs.getCodecByName(codec);
                    pat = Pattern.compile("[0-9]+ kb/s");
                    m = pat.matcher(info);
                    if (m.find()) aRate = info.substring(m.start(), m.end());
                    pat = Pattern.compile("[0-9]+ Hz");
                    m = pat.matcher(info);
                    if (m.find()) aFreq = info.substring(m.start(), m.end());
                    if (line.contains("5.1")) aChannel = "5.1"; else if (line.contains("2.1")) aChannel = "2.1"; else if (line.contains("stereo")) aChannel = "Stereo"; else if (line.contains("mono")) aChannel = "Mono";
                }
                if (videoCodec != null && audioCodec != null && duration != -1) {
                    if (rate == null && mrate != null && aRate != null) rate = String.valueOf(ConvertUtils.getRateFromRateString(mrate) - ConvertUtils.getRateFromRateString(aRate)) + " kb/s";
                    inputMedium = new InputMedium(audioCodec, videoCodec, input, duration, colorSpace, aspectRatio, scala, rate, mrate, aRate, aFreq, aChannel);
                    break;
                }
            }
            if ((videoCodec != null || audioCodec != null) && duration != -1) inputMedium = new InputMedium(audioCodec, videoCodec, input, duration, colorSpace, aspectRatio, scala, rate, mrate, aRate, aFreq, aChannel);
        } catch (Exception exc) {
            if (caller != null) JOptionPane.showMessageDialog(caller, lang.inputerror + " Audiocodec? " + (audioCodec != null) + " Videocodec? " + (videoCodec != null), lang.error, JOptionPane.ERROR_MESSAGE);
            if (Constants.debug) System.out.println("Audiocodec: " + audioCodec + "\nVideocodec: " + videoCodec);
            if (Constants.debug) exc.printStackTrace();
            throw new IOException("Input file error");
        }
        if (inputMedium == null) {
            if (caller != null) JOptionPane.showMessageDialog(caller, lang.inputerror + " Audiocodec? " + (audioCodec != null) + " Videocodec? " + (videoCodec != null), lang.error, JOptionPane.ERROR_MESSAGE);
            if (Constants.debug) System.out.println("Audiocodec: " + audioCodec + "\nVideocodec: " + videoCodec);
            throw new IOException("Input file error");
        }
    }
