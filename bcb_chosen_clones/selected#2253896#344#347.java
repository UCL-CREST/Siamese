            @Override
            public void widgetSelected(final SelectionEvent event) {
                IOUtils.copyToClipboard(Version.getEnvironmentReport());
            }
