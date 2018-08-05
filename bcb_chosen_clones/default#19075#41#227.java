    public DeskTopFrame() {
        setTitle("DeskTop XML/ZIP Renderer");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setLayout(new BorderLayout());
        setResizable(false);
        ActionListener validationListener = new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                if (validation.isSelected()) validationMethod = "ON"; else validationMethod = "OFF";
                if (strict.isSelected()) strictType = "ON"; else strictType = "OFF";
            }
        };
        headerString = "<html><center><h4>JAssess: A Java Implementation of IMS QTI Version 2</h4>" + "<h5>Developed by Graham Smith with support from UCLES and CARET </h5>" + "<h2>Desktop XML Test</h2><h5>Developed by Graham Smith with support from JISC </h5></center></html>";
        headerPanel = new JPanel();
        header = new JLabel(headerString);
        headerPanel.add(header);
        add(headerPanel, BorderLayout.NORTH);
        selectionPanel = new JPanel();
        selectionPanel.setLayout(new BorderLayout());
        selectionPanel.setSize(680, 20);
        border = BorderFactory.createLoweredBevelBorder();
        border2 = BorderFactory.createLineBorder(Color.BLACK, 2);
        selectionPanel.setBorder(border);
        Action = new JPanel();
        actionButton = new JButton("Render XML File");
        actionButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                String InputFilename = fileNameField.getText();
                extension = getFileExtension(InputFilename).toUpperCase();
                inFile = new File(InputFilename);
                String testData = "";
                String testFileRoot = "";
                String testItemIdentifier = "";
                String homeDirectory = "";
                if (extension.equals("XML")) {
                    try {
                        XMLCaller = new XMLHandlercallerDT2();
                        output = XMLCaller.run(InputFilename, testItemIdentifier, IncludeInFilenamelist, validationMethod, strictType, testData, testFileRoot, homeDirectory);
                    } catch (gqtiexcept.VersionException e) {
                        JOptionPane.showMessageDialog(null, "THIS EXAMPLE CAN ONLY BE RUN BY THE MATHASSESS VERSION", "Desktop XML Test", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (extension.equals("ZIP")) {
                    pkgeHandler = new PkgeHandlerDT();
                    PackageData packagedata = pkgeHandler.run(InputFilename, validationMethod, strictType, true);
                    String[] HTMFilePaths = packagedata.getFilePaths();
                    String[] HTMFileHrefs = packagedata.getFileHrefs();
                    for (int i2 = 0; i2 < HTMFilePaths.length; i2++) {
                        output = output + HTMFilePaths[i2] + "\n";
                        output = output + HTMFileHrefs[i2] + "\n";
                        output = output + "\n\n";
                    }
                } else if (inFile.isDirectory()) {
                    multipleHandler = new MultipleZipHandlerDT();
                    output = multipleHandler.run(InputFilename, validationMethod, strictType);
                } else {
                    JOptionPane.showMessageDialog(null, "INVALID FILENAME: " + fileNameField.getText(), "Desktop XML Test", JOptionPane.ERROR_MESSAGE);
                    output = "INVALID FILENAME" + InputFilename + " " + extension;
                }
                outputText.setText(output);
            }
        });
        Action.add(actionButton);
        XMLFilePanel = new JPanel();
        XMLFilePanel.setLayout(new BorderLayout());
        FileTypeSelectionPanel = new JPanel();
        typeGroup = new ButtonGroup();
        FileTypeSelectionPanel.add(new JLabel("Type of File to be Rendered:"));
        addRadioButton("XML", true);
        addRadioButton("ZIP (Content Packages and Packaged Tests)", false);
        addRadioButton("Directory", false);
        MainXMLFilePanel = new JPanel();
        fileNameField = new JTextField(40);
        MainXMLFilePanel.add(new JLabel("XML/ZIP File or Directory name:"));
        MainXMLFilePanel.add(fileNameField);
        fileOpenButton = new JButton("Browse");
        XMLFilechooser = new JFileChooser();
        XMLFilechooser.setCurrentDirectory(new File("."));
        XMLFilechooser.addChoosableFileFilter(new XMLFilter());
        XMLFilechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        ZIPFilechooser = new JFileChooser();
        ZIPFilechooser.setCurrentDirectory(new File("."));
        ZIPFilechooser.addChoosableFileFilter(new ZIPFilter());
        ZIPFilechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        Directorychooser = new JFileChooser();
        Directorychooser.setCurrentDirectory(new File("."));
        Directorychooser.addChoosableFileFilter(new XMLFilter());
        Directorychooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileOpenButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                if (fileType.equals("XML")) {
                    int result = XMLFilechooser.showOpenDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        InFilename = XMLFilechooser.getSelectedFile().getPath();
                        fileNameField.setText(InFilename);
                        actionButton.setEnabled(true);
                    }
                } else if (fileType.equals("ZIP (Content Packages and Packaged Tests)")) {
                    int result = ZIPFilechooser.showOpenDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        InFilename = ZIPFilechooser.getSelectedFile().getPath();
                        fileNameField.setText(InFilename);
                        actionButton.setEnabled(true);
                    }
                } else if (fileType.equals("Directory")) {
                    int result = Directorychooser.showOpenDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        InFilename = Directorychooser.getSelectedFile().getPath();
                        fileNameField.setText(InFilename);
                        actionButton.setEnabled(true);
                    }
                }
            }
        });
        MainXMLFilePanel.add(fileOpenButton);
        IncludeXMLFilePanel = new JPanel();
        IncludeFileNameField = new JTextField(40);
        IncludeXMLFilePanel.add(new JLabel("IncludeXML File name(s) (if any):"));
        IncludeXMLFilePanel.add(IncludeFileNameField);
        IncludeFileOpenButton = new JButton("Browse");
        IncludeInFilename = "";
        IncludeInFilenamelist = "";
        IncludeFileOpenButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                int result = XMLFilechooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    IncludeInFilename = XMLFilechooser.getSelectedFile().getPath();
                    IncludeInFilenamelist = IncludeInFilenamelist + IncludeInFilename + ";";
                    IncludeFileNameField.setText(IncludeInFilenamelist);
                }
            }
        });
        IncludeXMLFilePanel.add(IncludeFileOpenButton);
        XMLFilePanel.add(FileTypeSelectionPanel, BorderLayout.NORTH);
        XMLFilePanel.add(MainXMLFilePanel, BorderLayout.CENTER);
        XMLFilePanel.add(IncludeXMLFilePanel, BorderLayout.SOUTH);
        selectionPanel.add(XMLFilePanel, BorderLayout.NORTH);
        validationPanel = new JPanel();
        validation = new JCheckBox("Validation");
        validation.addActionListener(validationListener);
        validationPanel.add(validation);
        strict = new JCheckBox("Strict Type");
        strict.addActionListener(validationListener);
        validationPanel.add(strict);
        selectionPanel.add(validationPanel, BorderLayout.CENTER);
        String notesString = "<html><body><center><h4>Notes on Usage</h4></center>" + "1. The XML\\ZIP filename.<br>" + "<p>" + "The renderer will accept either a single XML file, a zip file with an IMS Content Package, or a directory containg ZIP files with ContentPackages. The operation of the renderer depends upon whether a file or directory is presented, and in the case of a file, the file extension '*.ZIP' or '*.XML'." + "<p><p>" + "2. The Include filename.<br>" + "Enter the name of the XML file(s) for any XInclude elements needed. More files can be added by using the 'Browse' button repeatedly. If adding files to the text box by hand, space the filenames with a semicolon';'." + "<p><p>" + "3. Validation.<br>" + "If this box is checked, the XML is validated against the Schema or DTD specified in the file. The Schema and DTD files are held locally to avoid the necessity of an internet connection. " + "Owing to the necessity to copy these large files, validation may take several seconds.<br>" + "In the case of package submission, note that it is only the XML of the submitted assessment items which is validated: the imsmanifest.xml is not. However malformed imsmanifest.xml will cause an error.<br>" + "<p><p>" + "5. StrictType Checking.<br>" + "The QTI specifications dictate stringent requirements for variable typing, particularly where expressions and operators are used." + "The rendering and responding engines in this version are very tolerant of type errors, but others are not. Checking this box submits the xml to type checking which other engines might demand. Type errors are notified by appending a 'WARNING' message to the returned HTML, either at the rendering or at the responding stage, depending upon where the error becomes significant." + "<p><p>" + "6. The Style and Appearance of the Rendered XML.<br>" + "The QTI version 2 specification provides means whereby authors can control the appearance of the rendered XML, through the inclusion of stylesheet references and certain XHTML tags. However, many XML examples, including those in the QTI documentation, contain very little, if any, style information." + "The HTML produced by the JAssess rendering engine used in this system contains no style information as such, other than that specified in the XML by these HTML tags. However, a number of the sections of the HTML are provided with '&lt;div&gt;' tags with 'class' attributes to provide means by which styles specified in a stylesheet may be added. 'View Source' of an example of the rendered HTML to examine these.<br />" + "The rendering engine of  has a number of built-in stylesheets available to it. By default, a stylesheet 'JAssessL.css' is used. However ,by using the Browser's View->Page Style menu item one of the other available stylesheets, or no stylesheet, may be selected. Using this requires that the stylesheet be re-selected for the reply after submitting the user's response." + "</p>" + "</body></html>";
        final JDialog notesDialog = new JDialog(this, "Notes on Usage");
        JLabel notesPane = new JLabel(notesString);
        notesPane.setSize(595, 495);
        notesPane.setBorder(border);
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                notesDialog.setVisible(false);
                notesDialog.dispose();
            }
        });
        JPanel notesContentPane = new JPanel();
        notesContentPane.setLayout(new BorderLayout(50, 50));
        notesContentPane.add(notesPane, BorderLayout.NORTH);
        notesContentPane.add(closeButton, BorderLayout.SOUTH);
        notesDialog.setContentPane(notesContentPane);
        notesDialog.setSize(650, 700);
        notesDialog.setVisible(false);
        notesButton = new JButton("Notes on Usage");
        notesButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                notesDialog.setVisible(true);
            }
        });
        Action.add(notesButton);
        selectionPanel.add(Action, BorderLayout.SOUTH);
        add(selectionPanel, BorderLayout.CENTER);
        outputPanel = new JPanel();
        outputPanel.setLayout(new BorderLayout());
        outputPanel.add(new JLabel("This text area contains diagnostic messages only.\n It will be removed in later versions"), BorderLayout.NORTH);
        Border titled = BorderFactory.createTitledBorder(border2, "Output");
        outputPanel.setBorder(titled);
        outputText = new JTextArea(15, 60);
        outputPane = new JScrollPane(outputText);
        outputPanel.add(outputPane, BorderLayout.SOUTH);
        add(outputPanel, BorderLayout.SOUTH);
    }
