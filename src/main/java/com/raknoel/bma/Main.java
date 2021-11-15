package com.raknoel.bma;

import com.google.cloud.bigquery.*;
import com.raknoel.bma.generators.MatrixGenerator;
import com.raknoel.bma.tools.BMA;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Main {
    static final String DATASET_NAME = "matrixResults";
    static final String TABLE_NAME = "second-run";
    static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    public static void main(String[] args) {
        BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();
        int width = 300, height = 20;
        int h = 100;
        int r = 120;

        for (int k = 100; k < 200; k++) {
            int d = new Random().nextInt(2 * k) + 1;
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            System.out.printf(
                    "Running: WH:%dx%d, R:%d, H:%d, K:%d, D:%d, I:%d %n",
                    width, height, r, h, k, d, k
            );

            var binaryMatrix = MatrixGenerator
                    .generateRKMatrix(width, height, d, h, new Random().nextInt());

            Map<String, Object> content = new HashMap<>();
            content.put("k", k);
            content.put("r", r);
            content.put("d", d);
            content.put("h", h);
            content.put("matrix_width", width);
            content.put("matrix_height", height);
            content.put("computer_id", "DESKTOP_1");
            content.put("rundate", SIMPLE_DATE_FORMAT.format(timestamp));

            final long START = System.currentTimeMillis();
            try {
                BMA solver = new BMA(binaryMatrix, k, r);
                var sol = solver.Approximate();

                content.put("solved", true);
                content.put("best_k", binaryMatrix.getChild().totalHammingDist(sol.getCenters()));
                content.put("best_r", sol.getWidth());
            } catch (Exception e) {
                content.put("solved", false);
                content.put("best_k", -1);
                content.put("best_r", -1);
            } finally {
                final long STOP = System.currentTimeMillis();
                final long RUNTIME = STOP - START;
                content.put("runtime_ms", RUNTIME);

                var res = insertAll(bigquery, content);
                if (res.hasErrors()) {
                    res.getInsertErrors().forEach((x, y) -> System.out.println(y.toString()));
                }
            }
        }
    }

    public static InsertAllResponse insertAll(BigQuery bq, Map<String, Object> rowContent) {
        TableId tableId = TableId.of(DATASET_NAME, TABLE_NAME);

        return bq.insertAll(
                InsertAllRequest.newBuilder(tableId)
                        .addRow(rowContent)
                        .build());
    }
}
