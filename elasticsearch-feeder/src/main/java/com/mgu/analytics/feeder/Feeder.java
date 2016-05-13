package com.mgu.analytics.feeder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Feeder extends AbstractBaseFeeder<TimeSeriesItem> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public void run() {
        withAdapter(search -> {
            search.index(generateDocuments());
        });
    }

    private List<TimeSeriesItem> generateDocuments() {
        final List<TimeSeriesItem> documents = new ArrayList<>();
        LocalDateTime start = LocalDateTime.now().minusDays(7);
        LocalDateTime end = LocalDateTime.now().plusDays(7);
        while (start.isBefore(end)) {
            final int count = (int) (Math.random() * 100);
            final TimeSeriesItem document = new TimeSeriesItem(start.format(formatter), count);
            documents.add(document);
            start = start.plusHours(1);
        }
        return documents;
    }

    public static void main(String[] args) {
        new Feeder().run();
    }
}
