package io.github.vlad324.model;

import java.util.List;
import java.util.Objects;

public class CronExpression {

    public final List<Integer> minutes;
    public final List<Integer> hours;
    public final List<Integer> daysOfMonth;
    public final List<Integer> months;
    public final List<Integer> daysOfWeek;
    public final String command;

    public CronExpression(Builder builder) {
        this.months = builder.months;
        this.hours = builder.hours;
        this.minutes = builder.minutes;
        this.daysOfMonth = builder.daysOfMonth;
        this.daysOfWeek = builder.daysOfWeek;
        this.command = builder.command;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CronExpression that = (CronExpression) o;
        return Objects.equals(minutes, that.minutes) &&
            Objects.equals(hours, that.hours) &&
            Objects.equals(daysOfMonth, that.daysOfMonth) &&
            Objects.equals(months, that.months) &&
            Objects.equals(daysOfWeek, that.daysOfWeek) &&
            Objects.equals(command, that.command);
    }

    @Override
    public int hashCode() {
        return Objects.hash(minutes, hours, daysOfMonth, months, daysOfWeek, command);
    }

    @Override
    public String toString() {
        return "CronExpression{" +
            "minutes=" + minutes +
            ", hours=" + hours +
            ", daysOfMonth=" + daysOfMonth +
            ", months=" + months +
            ", daysOfWeek=" + daysOfWeek +
            ", command='" + command + '\'' +
            '}';
    }

    public static final class Builder {
        private List<Integer> minutes;
        private List<Integer> hours;
        private List<Integer> daysOfMonth;
        private List<Integer> months;
        private List<Integer> daysOfWeek;
        private String command;

        private Builder() {
        }

        public static Builder cronExpression() {
            return new Builder();
        }

        public Builder minutes(List<Integer> minutes) {
            this.minutes = minutes;
            return this;
        }

        public Builder hours(List<Integer> hours) {
            this.hours = hours;
            return this;
        }

        public Builder daysOfMonth(List<Integer> daysOfMonth) {
            this.daysOfMonth = daysOfMonth;
            return this;
        }

        public Builder months(List<Integer> months) {
            this.months = months;
            return this;
        }

        public Builder daysOfWeek(List<Integer> daysOfWeek) {
            this.daysOfWeek = daysOfWeek;
            return this;
        }

        public Builder command(String command) {
            this.command = command;
            return this;
        }

        public CronExpression build() {
            return new CronExpression(this);
        }
    }
}
