        tabbedTableItem(String aaClass, String choice, boolean stdModel) {
            className = aaClass;
            itemText = choice;
            standardModel = stdModel;
            bottomBox = Box.createHorizontalBox();
            bottomBox.add(Box.createHorizontalGlue());
            bottomBoxIndex = 0;
            try {
                Class<?> cl = Class.forName(aaClass);
                java.lang.reflect.Constructor<?> co = cl.getConstructor(new Class[] { String.class });
                tableAction = (AbstractTableAction) co.newInstance(choice);
            } catch (ClassNotFoundException e1) {
                log.error("Not a valid class : " + aaClass);
                return;
            } catch (NoSuchMethodException e2) {
                log.error("Not such method : " + aaClass);
                return;
            } catch (InstantiationException e3) {
                log.error("Not a valid class : " + aaClass);
                return;
            } catch (ClassCastException e4) {
                log.error("Not part of the abstractTableActions : " + aaClass);
                return;
            } catch (Exception e) {
                log.error("Exception " + e.toString());
                return;
            }
            dataPanel.setLayout(new BorderLayout());
            if (stdModel) createDataModel(); else addPanelModel();
        }
