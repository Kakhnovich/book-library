package by.itechart.studets_lab.book_library.command;


import by.itechart.studets_lab.book_library.command.page.RedirectIndexPage;
import by.itechart.studets_lab.book_library.command.page.ShowReadersPage;
import by.itechart.studets_lab.book_library.command.page.ShowSearchPage;
import by.itechart.studets_lab.book_library.command.page.ShowMainPage;
import by.itechart.studets_lab.book_library.command.search.FindBook;

/**
 * enum of commands for the project
 *
 * @author Elmax19
 * @version 1.0
 */
public enum CommandManager {
    /**
     * possible commands
     */
    MAIN(RedirectIndexPage.INSTANCE),
    SEARCH(ShowSearchPage.INSTANCE),
    FIND_BOOK(FindBook.INSTANCE),
    READERS(ShowReadersPage.INSTANCE),
    DEFAULT(ShowMainPage.INSTANCE);

    /**
     * variables value of {@link Command}
     */
    private final Command command;

    /**
     * class Constructor
     *
     * @param command {@link CommandManager#command} value
     */
    CommandManager(Command command) {
        this.command = command;
    }

    /**
     * method for getting command by its name
     *
     * @param name name of command
     * @return command class
     * @see CommandManager#of(String)
     */
    static Command of(String name) {
        for (CommandManager action : values()) {
            if (action.name().equalsIgnoreCase(name)) {
                return action.command;
            }
        }
        return DEFAULT.command;
    }
}
