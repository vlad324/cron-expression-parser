package io.github.vlad324.parser;

import static io.github.vlad324.model.CronExpression.Builder.cronExpression;
import static io.github.vlad324.model.Limits.DAYS_OF_MONTH_MAX;
import static io.github.vlad324.model.Limits.DAYS_OF_MONTH_MIN;
import static io.github.vlad324.model.Limits.DAYS_OF_WEEK_MAX;
import static io.github.vlad324.model.Limits.DAYS_OF_WEEK_MIN;
import static io.github.vlad324.model.Limits.HOURS_MAX;
import static io.github.vlad324.model.Limits.HOURS_MIN;
import static io.github.vlad324.model.Limits.MINUTES_MAX;
import static io.github.vlad324.model.Limits.MINUTES_MIN;
import static io.github.vlad324.model.Limits.MONTHS_MAX;
import static io.github.vlad324.model.Limits.MONTHS_MIN;
import static org.apache.commons.lang3.StringUtils.split;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;

import io.github.vlad324.exception.ExpressionValidationException;
import io.github.vlad324.model.CronExpression;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CronExpressionParser {

    private static final String PARTS_SEPARATOR = " ";

    private static final String VALUES_SEPARATOR = ",";
    private static final String RANGE_SEPARATOR = "-";
    private static final String STEP_SEPARATOR = "/";

    private static final String ANY_VALUE = "*";

    private static final int MINUTES = 0;
    private static final int HOURS = 1;
    private static final int DAYS_OF_MONTH = 2;
    private static final int MONTHS = 3;
    private static final int DAYS_OF_WEEK = 4;
    private static final int COMMAND = 5;

    public static CronExpression parse(String input) {
        final var cronExpressionParts = split(input, PARTS_SEPARATOR);
        checkLength(cronExpressionParts, 6);

        return cronExpression()
            .minutes(parseInternal(cronExpressionParts[MINUTES], MINUTES_MIN, MINUTES_MAX))
            .hours(parseInternal(cronExpressionParts[HOURS], HOURS_MIN, HOURS_MAX))
            .daysOfMonth(parseInternal(cronExpressionParts[DAYS_OF_MONTH], DAYS_OF_MONTH_MIN, DAYS_OF_MONTH_MAX))
            .months(parseInternal(cronExpressionParts[MONTHS], MONTHS_MIN, MONTHS_MAX))
            .daysOfWeek(parseInternal(cronExpressionParts[DAYS_OF_WEEK], DAYS_OF_WEEK_MIN, DAYS_OF_WEEK_MAX))
            .command(checkCommandIsNotEmpty(trimToEmpty(cronExpressionParts[COMMAND])))
            .build();
    }

    private static List<Integer> parseInternal(String input, int min, int max) {
        if (input.contains(VALUES_SEPARATOR)) {
            return Arrays.stream(input.split(VALUES_SEPARATOR))
                .flatMap(i -> parseInternal(i, min, max).stream())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        }

        if (ANY_VALUE.equals(input)) {
            return listWithValuesFromTo(min, max);
        }

        if (input.contains(STEP_SEPARATOR)) {
            return parseStep(input, min, max);
        }

        if (input.contains(RANGE_SEPARATOR)) {
            return parseRange(input, min, max);
        }

        final var value = parseInt(input);
        checkInsideRange(value, min, max);

        return List.of(value);
    }

    private static List<Integer> parseRange(String input, int min, int max) {
        final var rangeValues = input.split(RANGE_SEPARATOR);
        checkLength(rangeValues, 2);

        final var start = parseInt(rangeValues[0]);
        checkInsideRange(start, min, max);

        final var end = parseInt(rangeValues[1]);
        checkInsideRange(end, min, max);

        return listWithValuesFromTo(start, end);
    }

    private static List<Integer> listWithValuesFromTo(int start, int end) {
        final var result = new LinkedList<Integer>();
        for (int i = start; i <= end; i++) {
            result.add(i);
        }

        return result;
    }

    private static List<Integer> parseStep(String input, int min, int max) {
        final var stepValues = input.split(STEP_SEPARATOR);
        checkLength(stepValues, 2);

        final int step = parseInt(stepValues[1]);
        if (ANY_VALUE.equals(stepValues[0])) {
            return listWithStep(step, min, max);
        } else {
            final var range = parseRange(stepValues[0], min, max);
            return selectFromListWithStep(range, step);
        }
    }

    private static List<Integer> listWithStep(int stepValue, int start, int end) {
        final var result = new LinkedList<Integer>();
        for (int i = start; i <= end; i = i + stepValue) {
            result.add(i);
        }

        return result;
    }

    private static List<Integer> selectFromListWithStep(List<Integer> range, int step) {
        final var result = new LinkedList<Integer>();
        for (int i = 0; i < range.size(); i = i + step) {
            result.add(range.get(i));
        }

        return result;
    }

    private static int parseInt(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new ExpressionValidationException(String.format("String %s is not a number", input), e);
        }
    }

    private static void checkLength(String[] array, int expectedLength) {
        if (array == null || array.length != expectedLength) {
            throw new ExpressionValidationException("Invalid length of array");
        }
    }

    private static void checkInsideRange(int value, int min, int max) {
        if (value < min || value > max) {
            throw new ExpressionValidationException(String.format("Value %s is outside range [%s,%s]", value, min, max));
        }
    }

    private static String checkCommandIsNotEmpty(String command) {
        if (command.isEmpty()) {
            throw new ExpressionValidationException("Command is empty");
        }

        return command;
    }
}
