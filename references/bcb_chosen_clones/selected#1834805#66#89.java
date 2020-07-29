    private void downloadResults() {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(System.currentTimeMillis());
        String filename = String.format("%s%sresult_%tF.xml", vysledky, File.separator, cal);
        String EOL = "" + (char) 0x0D + (char) 0x0A;
        try {
            LogManager.getInstance().log("Stahuji soubor result.xml a ukl�d�m do vysledky ...");
            File f = new File(filename);
            FileWriter fw = new FileWriter(f);
            URL url = new URL(Konfigurace.getInstance().getURLvysledkuValidatoru());
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = "";
            while ((line = br.readLine()) != null) {
                fw.write(line + EOL);
            }
            fw.write("</vysledky>" + EOL);
            br.close();
            fw.close();
            LogManager.getInstance().changeLog("Stahuji soubor result.xml a ukl�d�m do slo�ky vysledky ... OK");
        } catch (IOException e) {
            e.printStackTrace();
            LogManager.getInstance().changeLog("Stahuji soubor result.xml a ukl�d�m do slo�ky vysledky ... X");
        }
    }
