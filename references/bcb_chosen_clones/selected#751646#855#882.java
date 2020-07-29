    @SuppressWarnings("unchecked")
    protected AbstractLayout<Node, Edge> makeLayoutInstance(final Class<? extends Layout<Node, Edge>> thisLayoutClass, final Object thisGraph) {
        if (thisLayoutClass == null) return null;
        try {
            final Constructor<? extends Layout<Node, Edge>> thisConstructor = thisLayoutClass.getConstructor(new Class[] { Graph.class });
            final Object[] theseArgs = { thisGraph };
            final Object thisLayoutObject = thisConstructor.newInstance(theseArgs);
            final AbstractLayout<Node, Edge> thisLayout = (AbstractLayout<Node, Edge>) thisLayoutObject;
            if (thisLayoutObject instanceof HasVertexWeightFunction) {
                final HasVertexWeightFunction<Node> thisVertexWeightFunctionClient = (HasVertexWeightFunction<Node>) thisLayoutObject;
                thisVertexWeightFunctionClient.setWeightFunction(makeVertexWeightFunction());
            }
            return thisLayout;
        } catch (final SecurityException e) {
            e.printStackTrace();
        } catch (final NoSuchMethodException e) {
            e.printStackTrace();
        } catch (final IllegalArgumentException e) {
            e.printStackTrace();
        } catch (final InstantiationException e) {
            e.printStackTrace();
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
        } catch (final InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
