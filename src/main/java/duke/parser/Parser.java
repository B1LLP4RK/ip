package duke.parser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import duke.command.Command;
import duke.command.CommandBye;
import duke.command.CommandDeadline;
import duke.command.CommandDelete;
import duke.command.CommandEvent;
import duke.command.CommandFind;
import duke.command.CommandList;
import duke.command.CommandMark;
import duke.command.CommandTag;
import duke.command.CommandTodo;
import duke.command.CommandUnclear;
import duke.command.CommandUnmark;
import duke.command.CommandUntag;
import duke.dukeexception.DukeException;
import duke.dukeexception.DukeNoSeparatorException;
import duke.dukeexception.DukeNoTimeGivenException;
import duke.dukeexception.DukeTooLittleArgException;
import duke.dukeexception.DukeTooManyArgException;

/**
 * Encapsulates the parsing logic of string commands
 */
public class Parser {
    /**
     * Returns the Command object that the user indicates through userInput
     *
     * @param userInput the String value that the user typed.
     * @param taskList  the current list of tasks that duke stores, encapsulated in TaskList class.
     * @return the Command object that can be executed.
     * @throws DukeNoTimeGivenException if the user types the wrong format of time.
     */
    public static Command parse(String userInput, duke.TaskList taskList) throws DukeException, NumberFormatException {
        String[] words = userInput.split(" ");
        String firstWord = words[0];
        int taskNo;
        duke.Task task;
        switch (firstWord) {
        case "list":
            return new CommandList(taskList);
        case "mark":
            checkArgNumber(words, 2);
            taskNo = Integer.parseInt(words[1]);
            return new CommandMark(taskList, taskNo);
        case "unmark":
            checkArgNumber(words, 2);
            taskNo = Integer.parseInt(words[1]);
            return new CommandUnmark(taskList, taskNo);
        case "todo":
            checkArgNumber(words, 2);
            String todoContent = words[1];
            for (int i = 2; i < words.length; i++) {
                todoContent = todoContent + " " + words[i];
            }
            return new CommandTodo(taskList, todoContent);
        case "deadline":
            String deadlineContent = extractContent(words, duke.Deadline.contentTimeDivder());
            String[] separateTime = userInput.split(duke.Deadline.contentTimeDivder());
            String dateString = separateTime[1].trim();
            LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE);
            return new CommandDeadline(taskList, deadlineContent, date);
        case "event":
            String eventContent = extractContent(words, duke.Event.contentTimeDivder());
            String timeString = userInput.split(duke.Event.contentTimeDivder())[1].trim();
            LocalDate time = LocalDate.parse(timeString, DateTimeFormatter.ISO_DATE);
            return new CommandEvent(taskList, eventContent, time);
        case "delete":
            checkArgNumber(words, 2);
            taskNo = Integer.parseInt(words[1]);
            return new CommandDelete(taskList, taskNo);
        case "find":
            checkArgNumber(words, 2);
            String searchKeyword = words[1];
            return new CommandFind(searchKeyword, taskList);
        case "tag":
            checkArgNumber(words, 3);
            taskNo = Integer.parseInt(words[1]);
            task = taskList.get(taskNo - 1);
            String tagMessage = words[2];
            return new CommandTag(task, tagMessage);
        case "untag":
            checkArgNumber(words, 3);
            taskNo = Integer.parseInt(words[1]);
            task = taskList.get(taskNo - 1);
            int tagNo = Integer.parseInt(words[2]);
            return new CommandUntag(task, tagNo);
        case "bye":
            checkArgNumber(words, 1);
            return new CommandBye();
        default:
            return new CommandUnclear();
        }
    }

    /**
     * Checks whether userInput provides correct number of arguments.
     *
     * @param words The userInput in an array of String.
     * @param num   The number of arguments that a command requires.
     * @throws DukeException If userInput does not have the correct number of arguments.
     */
    private static void checkArgNumber(String[] words, int num) throws DukeException {
        if (words.length > num) {
            throw new DukeTooManyArgException();
        } else if (words.length < num) {
            throw new DukeTooLittleArgException();
        }
    }

    private static void checkMinArgNumber(String[] words, int min) throws DukeTooLittleArgException {
        if (words.length < min) {
            throw new DukeTooLittleArgException();
        }
    }

    /**
     * Extracts the description of task for {@code Deadline} and {@code Event} classes from userInput.
     *
     * @param words   The userInput in an array of String.
     * @param divider The string that divides the content and time description.
     * @return Description of task.
     */
    private static String extractContent(String[] words, String divider) throws DukeNoSeparatorException {
        String content = words[1];
        boolean hasUsedSeparator = false;
        for (int i = 2; i < words.length; i++) {
            if (words[i].equals(divider)) {
                hasUsedSeparator = true;
                break;
            }
            content = content + " " + words[i];
        }
        if (!hasUsedSeparator) {
            throw new DukeNoSeparatorException(divider);
        }
        return content;
    }
}
