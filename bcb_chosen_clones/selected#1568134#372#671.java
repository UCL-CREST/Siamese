    public boolean initFile(String filename) {
        showStatus("Loading the file, please wait...");
        x_units = "?";
        y_units = "ARBITRARY";
        Datatype = "UNKNOWN";
        if (filename.toLowerCase().endsWith(".spc")) {
            try {
                URL url = new URL(getDocumentBase(), filename);
                InputStream stream = url.openStream();
                DataInputStream fichier = new DataInputStream(stream);
                byte ftflgs = fichier.readByte();
                byte fversn = fichier.readByte();
                if (((ftflgs != 0) && (ftflgs != 0x20)) || (fversn != 0x4B)) {
                    Current_Error = ", support only Evenly Spaced new version 4B";
                    return false;
                }
                byte fexp = fichier.readByte();
                if (fexp != 0x80) YFactor = Math.pow(2, fexp) / Math.pow(2, 32);
                Nbpoints = NumericDataUtils.convToIntelInt(fichier.readInt());
                if (Firstx == shitty_starting_constant) {
                    Firstx = NumericDataUtils.convToIntelDouble(fichier.readLong());
                    Lastx = NumericDataUtils.convToIntelDouble(fichier.readLong());
                }
                byte fxtype = fichier.readByte();
                switch(fxtype) {
                    case 0:
                        x_units = "Arbitrary";
                        break;
                    case 1:
                        x_units = "Wavenumber (cm -1)";
                        break;
                    case 2:
                        x_units = "Micrometers";
                        break;
                    case 3:
                        x_units = "Nanometers";
                        break;
                    case 4:
                        x_units = "Seconds";
                        break;
                    case 5:
                        x_units = "Minuts";
                        break;
                    case 6:
                        x_units = "Hertz";
                        break;
                    case 7:
                        x_units = "Kilohertz";
                        break;
                    case 8:
                        x_units = "Megahertz";
                        break;
                    case 9:
                        x_units = "Mass (M/z)";
                        break;
                    case 10:
                        x_units = "Parts per million";
                        break;
                    case 11:
                        x_units = "Days";
                        break;
                    case 12:
                        x_units = "Years";
                        break;
                    case 13:
                        x_units = "Raman Shift (cm -1)";
                        break;
                    case 14:
                        x_units = "Electron Volt (eV)";
                        break;
                    case 16:
                        x_units = "Diode Number";
                        break;
                    case 17:
                        x_units = "Channel";
                        break;
                    case 18:
                        x_units = "Degrees";
                        break;
                    case 19:
                        x_units = "Temperature (F)";
                        break;
                    case 20:
                        x_units = "Temperature (C)";
                        break;
                    case 21:
                        x_units = "Temperature (K)";
                        break;
                    case 22:
                        x_units = "Data Points";
                        break;
                    case 23:
                        x_units = "Milliseconds (mSec)";
                        break;
                    case 24:
                        x_units = "Microseconds (uSec)";
                        break;
                    case 25:
                        x_units = "Nanoseconds (nSec)";
                        break;
                    case 26:
                        x_units = "Gigahertz (GHz)";
                        break;
                    case 27:
                        x_units = "Centimeters (cm)";
                        break;
                    case 28:
                        x_units = "Meters (m)";
                        break;
                    case 29:
                        x_units = "Millimeters (mm)";
                        break;
                    case 30:
                        x_units = "Hours";
                        break;
                    case -1:
                        x_units = "(double interferogram)";
                        break;
                }
                byte fytype = fichier.readByte();
                switch(fytype) {
                    case 0:
                        y_units = "Arbitrary Intensity";
                        break;
                    case 1:
                        y_units = "Interfeogram";
                        break;
                    case 2:
                        y_units = "Absorbance";
                        break;
                    case 3:
                        y_units = "Kubelka-Munk";
                        break;
                    case 4:
                        y_units = "Counts";
                        break;
                    case 5:
                        y_units = "Volts";
                        break;
                    case 6:
                        y_units = "Degrees";
                        break;
                    case 7:
                        y_units = "Milliamps";
                        break;
                    case 8:
                        y_units = "Millimeters";
                        break;
                    case 9:
                        y_units = "Millivolts";
                        break;
                    case 10:
                        y_units = "Log (1/R)";
                        break;
                    case 11:
                        y_units = "Percent";
                        break;
                    case 12:
                        y_units = "Intensity";
                        break;
                    case 13:
                        y_units = "Relative Intensity";
                        break;
                    case 14:
                        y_units = "Energy";
                        break;
                    case 16:
                        y_units = "Decibel";
                        break;
                    case 19:
                        y_units = "Temperature (F)";
                        break;
                    case 20:
                        y_units = "Temperature (C)";
                        break;
                    case 21:
                        y_units = "Temperature (K)";
                        break;
                    case 22:
                        y_units = "Index of Refraction [N]";
                        break;
                    case 23:
                        y_units = "Extinction Coeff. [K]";
                        break;
                    case 24:
                        y_units = "Real";
                        break;
                    case 25:
                        y_units = "Imaginary";
                        break;
                    case 26:
                        y_units = "Complex";
                        break;
                    case -128:
                        y_units = "Transmission";
                        break;
                    case -127:
                        y_units = "Reflectance";
                        break;
                    case -126:
                        y_units = "Arbitrary or Single Beam with Valley Peaks";
                        break;
                    case -125:
                        y_units = "Emission";
                        break;
                }
                if (ftflgs == 0) {
                    fichier.skipBytes(512 - 30);
                } else {
                    fichier.skipBytes(188);
                    byte b;
                    int i = 0;
                    x_units = "";
                    do {
                        b = fichier.readByte();
                        x_units += (char) b;
                        i++;
                    } while (b != 0);
                    int j = 0;
                    y_units = "";
                    do {
                        b = fichier.readByte();
                        y_units += (char) b;
                        j++;
                    } while (b != 0);
                    fichier.skipBytes(512 - 30 - 188 - i - j);
                }
                fichier.skipBytes(32);
                My_ZoneVisu.tableau_points = new double[Nbpoints];
                if (fexp == 0x80) {
                    for (int i = 0; i < Nbpoints; i++) {
                        My_ZoneVisu.tableau_points[i] = NumericDataUtils.convToIntelFloat(fichier.readInt());
                    }
                } else {
                    for (int i = 0; i < Nbpoints; i++) {
                        My_ZoneVisu.tableau_points[i] = NumericDataUtils.convToIntelInt(fichier.readInt());
                    }
                }
            } catch (Exception e) {
                Current_Error = "SPC file corrupted";
                return false;
            }
            Datatype = "XYDATA";
            return true;
        }
        try {
            URL url = new URL(getDocumentBase(), filename);
            InputStream stream = url.openStream();
            BufferedReader fichier = new BufferedReader(new InputStreamReader(stream));
            texte = new Vector();
            String s;
            while ((s = fichier.readLine()) != null) {
                texte.addElement(s);
            }
            nbLignes = texte.size();
        } catch (Exception e) {
            return false;
        }
        int My_Counter = 0;
        String uneligne = "";
        while (My_Counter < nbLignes) {
            try {
                StringTokenizer mon_token;
                do {
                    uneligne = (String) texte.elementAt(My_Counter);
                    My_Counter++;
                    mon_token = new StringTokenizer(uneligne, " ");
                } while (My_Counter < nbLignes && mon_token.hasMoreTokens() == false);
                if (mon_token.hasMoreTokens() == true) {
                    String keyword = mon_token.nextToken();
                    if (StringDataUtils.compareStrings(keyword, "##TITLE=") == 0) TexteTitre = uneligne.substring(9);
                    if (StringDataUtils.compareStrings(keyword, "##FIRSTX=") == 0) Firstx = Double.valueOf(mon_token.nextToken()).doubleValue();
                    if (StringDataUtils.compareStrings(keyword, "##LASTX=") == 0) Lastx = Double.valueOf(mon_token.nextToken()).doubleValue();
                    if (StringDataUtils.compareStrings(keyword, "##YFACTOR=") == 0) YFactor = Double.valueOf(mon_token.nextToken()).doubleValue();
                    if (StringDataUtils.compareStrings(keyword, "##NPOINTS=") == 0) Nbpoints = Integer.valueOf(mon_token.nextToken()).intValue();
                    if (StringDataUtils.compareStrings(keyword, "##XUNITS=") == 0) x_units = uneligne.substring(10);
                    if (StringDataUtils.compareStrings(keyword, "##YUNITS=") == 0) y_units = uneligne.substring(10);
                    if (StringDataUtils.compareStrings(keyword, "##.OBSERVE") == 0 && StringDataUtils.compareStrings(mon_token.nextToken(), "FREQUENCY=") == 0) nmr_observe_frequency = Double.valueOf(mon_token.nextToken()).doubleValue();
                    if (StringDataUtils.compareStrings(keyword, "##XYDATA=") == 0 && StringDataUtils.compareStrings(mon_token.nextToken(), "(X++(Y..Y))") == 0) Datatype = "XYDATA";
                    if (StringDataUtils.compareStrings(keyword, "##XYDATA=(X++(Y..Y))") == 0) Datatype = "XYDATA";
                    if (StringDataUtils.compareStrings(keyword, "##PEAK") == 0 && StringDataUtils.compareStrings(mon_token.nextToken(), "TABLE=") == 0 && StringDataUtils.compareStrings(mon_token.nextToken(), "(XY..XY)") == 0) Datatype = "PEAK TABLE";
                    if (StringDataUtils.compareStrings(keyword, "##PEAK") == 0 && StringDataUtils.compareStrings(mon_token.nextToken(), "TABLE=(XY..XY)") == 0) Datatype = "PEAK TABLE";
                }
            } catch (Exception e) {
            }
        }
        if (Datatype.compareTo("UNKNOWN") == 0) return false;
        if (Datatype.compareTo("PEAK TABLE") == 0 && x_units.compareTo("?") == 0) x_units = "M/Z";
        if (StringDataUtils.truncateEndBlanks(x_units).compareTo("HZ") == 0 && nmr_observe_frequency != shitty_starting_constant) {
            Firstx /= nmr_observe_frequency;
            Lastx /= nmr_observe_frequency;
            x_units = "PPM.";
        }
        String resultat_move_points = Move_Points_To_Tableau();
        if (resultat_move_points.compareTo("OK") != 0) {
            Current_Error = resultat_move_points;
            return false;
        }
        return true;
    }
