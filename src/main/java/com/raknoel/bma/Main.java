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
        int width = 1000, height = 1000;

        for (int h = 1; h <= 3; h++)
            for (int r = 2; r <= 3; r++)
                for (int k = 3; k <= 30; k++)
                    for (int i = 0; i < 20; i++) {
                        int d = new Random().nextInt(2 * k) + 1;
                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                        System.out.printf("Running: R %d, K %d, D %d, I %d %n", r, k, d, i);

                        var binaryMatrix = MatrixGenerator
                                .generateRKMatrix(width, height, d, h, new Random().nextInt());

                        Map<String, Object> content = new HashMap<>();
                        content.put("k", k);
                        content.put("r", r);
                        content.put("d", d);
                        content.put("h", h);
                        content.put("matrix_width", width);
                        content.put("matrix_height", height);
                        //content.put("matrix", binaryMatrix.toString());
                        content.put("computer_id", "DESKTOP_1");
                        content.put("rundate", SIMPLE_DATE_FORMAT.format(timestamp));

                        final long START = System.currentTimeMillis();
                        try {
                            BMA solver = new BMA(binaryMatrix, k, r);
                            var sol = solver.Approximate();
                            final long STOP = System.currentTimeMillis();
                            final long RUNTIME = STOP - START;

                            content.put("runtime_ms", RUNTIME);
                            content.put("solved", true);
                            //content.put("solution", new BinaryMatrix(sol.getCenters(), height).toString());
                            content.put("best_k", binaryMatrix.getChild().totalHammingDist(sol.getCenters()));
                            content.put("best_r", sol.getWidth());
                        } catch (Exception e) {
                            final long STOP = System.currentTimeMillis();
                            final long RUNTIME = STOP - START;
                            content.put("runtime_ms", RUNTIME);
                            content.put("solved", false);
                            //content.put("solution", "");
                            content.put("best_k", -1);
                            content.put("best_r", -1);
                        }

                        var res = insertAll(bigquery, content);
                        if (res.hasErrors()) {
                            res.getInsertErrors().forEach((x, y) -> System.out.println(y.toString()));
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
