import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class Deadline extends Task {
    private LocalDate date;
    public Deadline(String content, String date) {
        super(content);
        this.date = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " ( by:" + date.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)) + ")";
    }

    @Override
    public String toData() {
        String isFinishedData;
        if (super.finished) {
            isFinishedData = "1";
        } else {
            isFinishedData = "0";
        }
        return "D:" + isFinishedData + ":" + super.content + ":" + date;
    }
}
