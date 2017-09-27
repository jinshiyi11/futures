package com.shuai.futures.protocol;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *
 */
public class XLabelInfo {
    private int xcount;
    private List<Label> labels;
    private transient List<Range> timeRanges;

    public static class HourMinute {
        public int hour;
        public int minute;

        public HourMinute() {
        }

        public HourMinute(int hour, int minute) {
            this.hour = hour;
            this.minute = minute;
        }

        public void setHour(int hour) {
            this.hour = hour;
        }

        public void setMinute(int minute) {
            this.minute = minute;
        }

        public boolean isBefore(HourMinute other) {
            if (other == null) {
                return false;
            }

            return (hour == other.hour && minute + 1 == other.minute)
                    || (hour + 1 == other.hour && minute == 59 && other.minute == 0);
        }

        public HourMinute addOneMinute() {
            HourMinute result = new HourMinute();
            if (minute < 59) {
                result.setHour(hour);
                result.setMinute(minute + 1);
            } else {
                if (hour + 1 < 24) {
                    result.setHour(hour + 1);
                } else {
                    result.setHour(0);
                }
                result.setMinute(0);
            }
            return result;
        }

        public int sub(HourMinute start) {
            int result = 0;
            if (start == null) {
                return result;
            }

            int hours = hour - start.hour;
            if (hour > start.hour) {
                result += hours * 60 + minute + (60 - start.minute);
            } else {
                result = minute - start.minute;
            }
            return result;
        }

        @Override
        public String toString() {
            return "HourMinute{" +
                    "hour=" + hour +
                    ", minute=" + minute +
                    '}';
        }
    }

    public static class Range {
        public HourMinute start;
        public HourMinute end;

        public boolean inRange(HourMinute item) {
            if (item == null) {
                return false;
            }
            return (item.hour > start.hour || (item.hour == start.hour && item.minute >= start.minute))
                    && (item.hour < end.hour || (item.hour == end.hour && item.minute <= end.minute));
        }

        @Override
        public String toString() {
            return "Range{" +
                    "start=" + start +
                    ", end=" + end +
                    '}';
        }
    }

    public static class Label {
        private int x;
        private String label;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return "Label{" +
                    "x=" + x +
                    ", label='" + label + '\'' +
                    '}';
        }
    }

    public int getXcount() {
        return xcount;
    }

    public void setXcount(int count) {
        this.xcount = count;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> lists) {
        this.labels = lists;
    }

    public List<Range> getTimeRanges() {
        return timeRanges;
    }

    public void setTimeRanges(List<Range> timeRanges) {
        this.timeRanges = timeRanges;
    }
}
