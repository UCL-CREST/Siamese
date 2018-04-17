        public void actionPerformed(ActionEvent arg0) {
            Object[] constructorArgs = { collapsedGraph };
            Class<? extends Layout> layoutC = (Class<? extends Layout>) jcb.getSelectedItem();
            try {
                Constructor<? extends Layout> constructor = layoutC.getConstructor(new Class[] { Graph.class });
                Object o = constructor.newInstance(constructorArgs);
                Layout l = (Layout) o;
                l.setInitializer(vv.getGraphLayout());
                l.setSize(vv.getSize());
                layout = l;
                LayoutTransition lt = new LayoutTransition(vv, vv.getGraphLayout(), l);
                Animator animator = new Animator(lt);
                animator.start();
                vv.getRenderContext().getMultiLayerTransformer().setToIdentity();
                vv.repaint();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
