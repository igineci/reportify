package melany.cli;

import melany.utils.Constants;
import org.apache.commons.cli.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Utility Class ruled for setting options and parsing Command Line Arguments.
 *
 * @author andjela.djekic
 */
public class CliArgsParser {
    /**
     * The host of the database server.
     */
    private String host;
    /**
     * The port number of the database server.
     */
    private int port;
    /**
     * The name of the database to connect to.
     */
    private String database;
    /**
     * The username used for authentication with the database.
     */
    private String username;
    /**
     * The password associated with the username for database access.
     */
    private String password;
    /**
     * The Set of reports that need to be generated and sent to the API.
     */
    private Set<String> reports;
    private CommandLine cmd;

    public CliArgsParser() {
        this.reports = new HashSet<>();
    }

    /**
     * Defining command line arguments that need to be entered through the command line.
     *
     * @return Options object containing defined options for command line arguments.
     */
    private Options defineOptions() {
        Options options = new Options();

        options.addOption(Option.builder(Constants.HOST_OPTION).longOpt(Constants.HOST_OPTION_LONG).hasArg().required(true).desc(Constants.HOST_OPTION_DESC).build());
        options.addOption(Option.builder(Constants.PORT_OPTION).longOpt(Constants.PORT_OPTION_LONG).hasArg().required(false).desc(Constants.PORT_OPTION_DESC).type(Integer.class).build());
        options.addOption(Option.builder(Constants.DATABASE_OPTION).longOpt(Constants.DATABASE_OPTION_LONG).hasArg().required(true).desc(Constants.DATABASE_OPTION_DESC).build());
        options.addOption(Option.builder(Constants.USERNAME_OPTION).longOpt(Constants.USERNAME_OPTION_LONG).hasArg().required(true).desc(Constants.USERNAME_OPTION_DESC).build());
        options.addOption(Option.builder(Constants.PASSWORD_OPTION).longOpt(Constants.PASSWORD_OPTION_LONG).hasArg().required(true).desc(Constants.PASSWORD_OPTION_DESC).build());
        options.addOption(Option.builder(Constants.REPORTS_OPTION).longOpt(Constants.REPORTS_OPTION_LONG).hasArg().required(true).desc(Constants.REPORTS_OPTION_DESC).build());

        return options;
    }

    /**
     * Parsing command line arguments for connecting to the database and reports that need to be generated and sent to the API.
     *
     * @param args arguments inserted through command line.
     */
    public void parse(String[] args) {

        Options options = defineOptions();
        CommandLineParser parser = new DefaultParser();
        HelpFormatter helper = new HelpFormatter();

        try {

            cmd = parser.parse(options, args);

            // Parameters for connection with Database
            this.host = cmd.getOptionValue(Constants.HOST_OPTION);
            this.port = Integer.parseInt(cmd.getOptionValue(Constants.PORT_OPTION, Constants.DEFAULT_CLI_PORT));
            this.database = cmd.getOptionValue(Constants.DATABASE_OPTION);
            this.username = cmd.getOptionValue(Constants.USERNAME_OPTION);
            this.password = cmd.getOptionValue(Constants.PASSWORD_OPTION);

            String[] reportOptions = cmd.getOptionValue(Constants.REPORTS_OPTION).split(Constants.COMMA);

            // Reports that need to be generated and sent to the API
            for (String report : reportOptions) {
                this.reports.add(report.trim());
            }

            // Validating required options
            if (!cmd.hasOption(Constants.HOST_OPTION) || !cmd.hasOption(Constants.PORT_OPTION) || !cmd.hasOption(Constants.DATABASE_OPTION) || !cmd.hasOption(Constants.USERNAME_OPTION) || !cmd.hasOption(Constants.PASSWORD_OPTION)) {
                System.err.println(Constants.CLI_MISSING_ARGS_HELP);
                helper.printHelp(Constants.CLI_EXAMPLE_ARGS_HELP, options);
            }

        } catch (ParseException e) {
            System.err.println(Constants.CLI_ERROR + e.getMessage());
            helper.printHelp(Constants.CLI_EXAMPLE_ARGS_HELP, options);
        }
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Set<String> getReports() {
        return reports;
    }

}
