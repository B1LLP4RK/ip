package duke.command;

import duke.TaskList;

public class CommandList extends Command {
    private TaskList taskList;

    public CommandList(TaskList taskList) {
        assert taskList != null;
        this.taskList = taskList;
    }

    @Override
    public String execute() {
        return taskList.toString();
    }
}

