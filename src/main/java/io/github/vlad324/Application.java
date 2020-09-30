package io.github.vlad324;

import io.github.vlad324.exception.ExpressionValidationException;
import io.github.vlad324.parser.CronExpressionParser;
import io.github.vlad324.printer.CronExpressionPrinter;

public class Application {

    public static void main(String[] args) {
        if (args == null || args.length != 1) {
            printErrorMessage();
            return;
        }

        try {
            final var cronExpression = CronExpressionParser.parse(args[0]);
            CronExpressionPrinter.print(cronExpression, System.out);
        } catch (ExpressionValidationException e) {
            printErrorMessage();
        }
    }

    private static void printErrorMessage() {
        System.out.println("Invalid input, please correct and try again");
        System.out.println("Example of valid input: \"45-50 0-2,21-23 1-10/2 */5 1,2,3,4 /usr/bin/find\"");
    }

}
