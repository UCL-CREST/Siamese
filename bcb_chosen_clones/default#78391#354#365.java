                    public void actionPerformed(ActionEvent evt) {
                        System.out.println("File Browse button pressed.");
                        NewFileChooser = new JFileChooser(mediator.getCurDir());
                        int returnVal = NewFileChooser.showOpenDialog(AssignmentEdit.this);
                        if (returnVal == JFileChooser.APPROVE_OPTION) {
                            String file = mediator.molestPath(NewFileChooser.getSelectedFile().toString());
                            txtFilePath.setText(mediator.molestPath(file));
                            System.out.println(mediator.molestPath(file.replaceFirst(mediator.getCurDir(), "").trim()));
                        } else {
                            System.out.print("Open command cancelled by user.\n");
                        }
                    }
