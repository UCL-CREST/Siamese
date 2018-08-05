    public void addTabItem(Class<?> cl, Set<?> list, String tabText) {
        boolean exists = false;
        for (CTabItem it : tab.getItems()) {
            if (it.getText().equalsIgnoreCase(tabText)) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            CTabItem item = new CTabItem(tab, SWT.CLOSE);
            item.setText(tabText);
            try {
                Constructor<?>[] c = cl.getConstructors();
                Object o = c[0].newInstance(tab, list);
                TableList<?> tablist = (TableList<?>) o;
                item.setControl(tablist);
                tab.setSelection(item);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
