    public static ISimpleChemObjectReader createReader(URL url, String urlString, String type) throws CDKException {
        if (type == null) {
            type = "mol";
        }
        ISimpleChemObjectReader cor = null;
        try {
            Reader input = new BufferedReader(getReader(url));
            FormatFactory formatFactory = new FormatFactory(8192);
            IChemFormat format = formatFactory.guessFormat(input);
            if (format != null) {
                if (format instanceof RGroupQueryFormat) {
                    cor = new RGroupQueryReader();
                    cor.setReader(input);
                } else if (format instanceof CMLFormat) {
                    cor = new CMLReader(urlString);
                    cor.setReader(url.openStream());
                } else if (format instanceof MDLV2000Format) {
                    cor = new MDLV2000Reader(getReader(url));
                    cor.setReader(input);
                }
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        if (cor == null) {
            if (type.equals(JCPFileFilter.cml) || type.equals(JCPFileFilter.xml)) {
                cor = new CMLReader(urlString);
            } else if (type.equals(JCPFileFilter.sdf)) {
                cor = new MDLV2000Reader(getReader(url));
            } else if (type.equals(JCPFileFilter.mol)) {
                cor = new MDLV2000Reader(getReader(url));
            } else if (type.equals(JCPFileFilter.inchi)) {
                try {
                    cor = new INChIReader(new URL(urlString).openStream());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (type.equals(JCPFileFilter.rxn)) {
                cor = new MDLRXNV2000Reader(getReader(url));
            } else if (type.equals(JCPFileFilter.smi)) {
                cor = new SMILESReader(getReader(url));
            }
        }
        if (cor == null) {
            throw new CDKException(GT._("Could not determine file format"));
        }
        if (cor instanceof MDLV2000Reader) {
            try {
                BufferedReader in = new BufferedReader(getReader(url));
                String line;
                while ((line = in.readLine()) != null) {
                    if (line.equals("$$$$")) {
                        String message = GT._("It seems you opened a mol or sdf" + " file containing several molecules. " + "Only the first one will be shown");
                        JOptionPane.showMessageDialog(null, message, GT._("sdf-like file"), JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return cor;
    }
