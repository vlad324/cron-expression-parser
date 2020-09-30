package io.github.vlad324.printer;

import static io.github.vlad324.model.CronExpression.Builder.cronExpression;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

class CronExpressionPrinterTest {

    @Test
    void should_print_as_expected() {
        // given
        final var expression = cronExpression()
            .minutes(List.of(45, 46, 47, 48, 49, 50))
            .hours(List.of(0, 1, 2, 21, 22, 23))
            .daysOfMonth(List.of(1, 3, 5, 7, 9))
            .months(List.of(1, 6, 11))
            .daysOfWeek(List.of(1, 2, 3, 4))
            .command("/usr/bin/find")
            .build();

        final var outputStream = new ByteArrayOutputStream();

        // when
        CronExpressionPrinter.print(expression, new PrintStream(outputStream, true));

        // then
        assertThat(outputStream.toString()).isEqualTo("" +
            "minute        45 46 47 48 49 50\n" +
            "hour          0 1 2 21 22 23\n" +
            "day of month  1 3 5 7 9\n" +
            "month         1 6 11\n" +
            "day of week   1 2 3 4\n" +
            "command       /usr/bin/find\n"
        );
    }
}