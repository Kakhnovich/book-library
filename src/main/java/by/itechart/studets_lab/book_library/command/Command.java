package by.itechart.studets_lab.book_library.command;

/**
 * interface of options for command
 *
 * @author Elmax19
 * @version 1.0
 */
public interface Command {

    /**
     * method for creation response to redirect or forward
     * @param request received request
     * @return custom response
     */
    ResponseContext execute(RequestContext request);


    /**
     * method call from {@link CommandManager}
     * @param name command name
     * @return command class
     */
    static Command of(String name) {
        return CommandManager.of(name);
    }
}