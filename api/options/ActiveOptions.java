package api.options;

import org.apache.commons.cli.CommandLine;

public class ActiveOptions {
    public boolean selectPackage;

    public ActiveOptions(CommandLine commandLine) {
        selectPackage = commandLine.hasOption(JUnitOptions.SELECT_PACKAGE);
    }
}