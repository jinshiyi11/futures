package com.shuai.futures.view.chart;

import java.util.List;

/**
 *
 */
public class XLabelInfo {
    private int xcount;
    private List<Label> labels;

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
}
