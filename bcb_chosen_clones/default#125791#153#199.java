    public void makeMageObjects() {
        String type = (String) typeCombo.getSelectedItem();
        String name = nameText.getText();
        boolean isBackground = backgroundCBox.isSelected();
        String chan = (String) channelCombo.getSelectedItem();
        String scale = (String) scaleCombo.getSelectedItem();
        String dType = (String) dTypeCombo.getSelectedItem();
        int confidence = (int) confIndText.getValue();
        try {
            String prefix = "org.biomage.QuantitationType.";
            Class quanClass = Class.forName(prefix + type);
            Class[] paramArr = {};
            Object[] parValArr = {};
            Constructor ctor = quanClass.getConstructor(paramArr);
            quanType = (QuantitationType) ctor.newInstance(parValArr);
            quanType.setName(name);
            quanType.setIdentifier(type + ":" + fieldId);
            if (!chan.equals("N/A")) {
                Channel channel = new Channel();
                channel.setIdentifier("Channel:" + chan);
                channel.setName(chan);
                quanType.setChannel(channel);
                addChannel(channel);
            }
            quanType.setIsBackground(isBackground);
            OntologyEntry ontScale = new OntologyEntry();
            ontScale.setCategory("Scale");
            ontScale.setValue(scale);
            quanType.setScale(ontScale);
            OntologyEntry ontDataType = new OntologyEntry();
            ontDataType.setCategory("DataType");
            ontDataType.setValue(dType);
            quanType.setDataType(ontDataType);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        } catch (InvocationTargetException ite) {
            ite.printStackTrace();
        }
    }
