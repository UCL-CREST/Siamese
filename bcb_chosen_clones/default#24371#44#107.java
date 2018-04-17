    public DispAllFonts() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        for (int familyIndex = 0; familyIndex < TEXT_FAMILY.length; ++familyIndex) {
            for (int styleIndex = 0; styleIndex < BOLD_ITALIC_SHADOW.length; ++styleIndex) {
                vtkTextMapper mapper = new vtkTextMapper();
                int bold = BOLD_ITALIC_SHADOW[styleIndex][0];
                int italic = BOLD_ITALIC_SHADOW[styleIndex][1];
                int shadow = BOLD_ITALIC_SHADOW[styleIndex][2];
                String attrib = "";
                if (bold != 0) {
                    attrib += "b";
                }
                if (italic != 0) {
                    if (attrib.length() > 0) attrib += ",";
                    attrib += "i";
                }
                if (shadow != 0) {
                    if (attrib.length() > 0) attrib += ",";
                    attrib += "s";
                }
                String faceName = TEXT_FAMILY[familyIndex] + " " + attrib;
                mapper.SetInput(faceName + ": " + DEFAULT_TEXT);
                vtkTextProperty tprop = mapper.GetTextProperty();
                String methodName = "SetFontFamilyTo" + TEXT_FAMILY[familyIndex];
                Method method = tprop.getClass().getMethod(methodName, null);
                method.invoke(tprop, null);
                tprop.SetColor(TEXT_COLOR);
                tprop.SetBold(bold);
                tprop.SetItalic(italic);
                tprop.SetShadow(shadow);
                vtkActor2D actor = new vtkActor2D();
                actor.SetMapper(mapper);
                textActors.add(actor);
                renWin.GetRenderer().AddActor(actor);
                textMappers.add(mapper);
            }
        }
        slider.setMinimum(MIN_FONT_SIZE);
        slider.setMaximum(MAX_FONT_SIZE);
        slider.setMajorTickSpacing(5);
        slider.setMinorTickSpacing(1);
        slider.setSnapToTicks(true);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                if (e.getSource() == slider) {
                    current_font_size = slider.getValue();
                    renWin.setSize(800, 20 + textActors.size() * (current_font_size + 5));
                    for (int i = 0; i < textActors.size(); i++) {
                        vtkActor2D actor = (vtkActor2D) textActors.get(i);
                        vtkTextMapper mapper = (vtkTextMapper) textMappers.get(i);
                        mapper.GetTextProperty().SetFontSize(current_font_size);
                        actor.SetDisplayPosition(10, (i + 1) * (current_font_size + 5));
                    }
                }
            }
        });
        renWin.GetRenderer().SetBackground(BG_COLOR);
        setLayout(new BorderLayout());
        add(slider, BorderLayout.NORTH);
        add(renWin, BorderLayout.CENTER);
        slider.setValue(current_font_size);
    }
