        public void actionPerformed(ActionEvent arg0) {
            Object[] constructorArgs = { g };
            Class layoutC = (Class) jcb.getSelectedItem();
            System.out.println("Setting to " + layoutC);
            Class lay = layoutC;
            try {
                Constructor constructor = lay.getConstructor(constructorArgsWanted);
                Object o = constructor.newInstance(constructorArgs);
                Layout l = (Layout) o;
                gd.setGraphLayout(l);
                gd.restartLayout();
            } catch (Exception e) {
                System.out.println("Can't handle " + lay);
            }
        }
