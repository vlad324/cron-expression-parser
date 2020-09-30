package io.github.vlad324.parser;

import static io.github.vlad324.model.CronExpression.Builder.cronExpression;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import io.github.vlad324.exception.ExpressionValidationException;
import io.github.vlad324.model.CronExpression;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

class CronExpressionParserTest {

    private static Stream<Arguments> validTestDataProvider() {
        return Stream.<Arguments>builder()
            .add(arguments("*/15 0 1,15 * 1-5 /usr/bin/find",
                cronExpression()
                    .minutes(List.of(0, 15, 30, 45))
                    .hours(List.of(0))
                    .daysOfMonth(List.of(1, 15))
                    .months(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12))
                    .daysOfWeek(List.of(1, 2, 3, 4, 5))
                    .command("/usr/bin/find")
                    .build()))
            .add(arguments("45-50 0-2,21-23 1-10/2 */5 1,2,3,4 /usr/bin/find",
                cronExpression()
                    .minutes(List.of(45, 46, 47, 48, 49, 50))
                    .hours(List.of(0, 1, 2, 21, 22, 23))
                    .daysOfMonth(List.of(1, 3, 5, 7, 9))
                    .months(List.of(1, 6, 11))
                    .daysOfWeek(List.of(1, 2, 3, 4))
                    .command("/usr/bin/find")
                    .build()))
            .build();
    }

    private static Stream<String> invalidTestDataProvider() {
        return Stream.<String>builder()
            .add(null)
            .add("")
            .add("A/15 0 1,15 * 1-5 /usr/bin/find")
            .add("-1 * * * * /usr/bin/find")
            .add("60 * * * * /usr/bin/find")
            .add("* -2 * * * /usr/bin/find")
            .add("* 24 * * * /usr/bin/find")
            .add("* * 0 * * /usr/bin/find")
            .add("* * 32 * * /usr/bin/find")
            .add("* * * 0 * /usr/bin/find")
            .add("* * * 13 * /usr/bin/find")
            .add("* * * * -1 /usr/bin/find")
            .add("* * * * 7 /usr/bin/find")
            .add("* * * * * ")
            .build();
    }

    @ParameterizedTest
    @MethodSource("validTestDataProvider")
    void should_produce_expected_result(String inputString, CronExpression expectedExpression) {
        // when
        final var actualExpression = CronExpressionParser.parse(inputString);

        // then
        assertThat(actualExpression).isEqualTo(expectedExpression);
    }

    @ParameterizedTest
    @MethodSource("invalidTestDataProvider")
    void should_throw_exception_if_invalid_input_provided(String inputString) {
        assertThatThrownBy(() -> CronExpressionParser.parse(inputString))
            .isInstanceOf(ExpressionValidationException.class);
    }


}