package io.github.vlad324.printer;

import static org.apache.commons.lang3.StringUtils.rightPad;

import io.github.vlad324.model.CronExpression;

import java.io.PrintStream;
import java.util.List;
import java.util.stream.Collectors;

public class CronExpressionPrinter {
    private static final int NAME_LENGTH = 14;

    public static void print(CronExpression expression, PrintStream printStream) {
        printStream.println(prepareString(rightPad("minute", NAME_LENGTH), expression.minutes));
        printStream.println(prepareString(rightPad("hour", NAME_LENGTH), expression.hours));
        printStream.println(prepareString(rightPad("day of month", NAME_LENGTH), expression.daysOfMonth));
        printStream.println(prepareString(rightPad("month", NAME_LENGTH), expression.months));
        printStream.println(prepareString(rightPad("day of week", NAME_LENGTH), expression.daysOfWeek));
        printStream.println(rightPad("command", NAME_LENGTH) + expression.command);
    }

    private static String prepareString(String name, List<Integer> values) {
        return values.stream()
            .map(String::valueOf)
            .collect(Collectors.joining(" ", name, ""));
    }
}
