package api.options;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class JUnitOptions {
    public static final Option SELECT_PACKAGE = Option.builder("p").hasArg(true).build();

    public static Options getOptions() {
        return new Options()
                .addOption(SELECT_PACKAGE);
    }
}
