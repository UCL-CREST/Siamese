        public void actionPerformed(ActionEvent e) {
            System.out.println("Open...");
            fc = new JFileChooser();
            int retval = fc.showOpenDialog(null);
            try {
                if (retval == JFileChooser.APPROVE_OPTION) {
                    fileParser = new TitratorXMLParser(fc.getSelectedFile());
                    fileParser.parseFile();
                }
            } catch (SAXParseException err) {
                System.out.println("** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
                System.out.println(" " + err.getMessage());
            } catch (SAXException saxErr) {
                Exception x = saxErr.getException();
                ((x == null) ? saxErr : x).printStackTrace();
            } catch (IOException i) {
                i.printStackTrace();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
