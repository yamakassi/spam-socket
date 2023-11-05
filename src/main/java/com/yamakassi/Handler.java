package com.yamakassi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class Handler implements Runnable {
    ObjectMapper objectMapper = new ObjectMapper();
    private final Socket socket;


    public Handler(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        while (true) {
            System.out.println("-------------------------####-----------------");
            try {
                String json = generateRandomUserJson();
                OutputStream outputStream = socket.getOutputStream();
                System.out.println(json);
                // json  вида "{"name":"USER", "spend": 1999, "city: "Saratov"}\n"
                outputStream.write(json.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
                Thread.sleep(1000);

            } catch (IOException e) {
                System.out.println("ERROR");
                e.printStackTrace();
            } catch (InterruptedException e) {
                System.out.println("ERROR");
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    private String generateRandomUserJson() {
        String name = generateUserName();
        String city = generateCity();
        Integer spent = generateSpend();
        Timestamp timestamp = new Timestamp(new Date().getTime());
        Person person = new Person();
        person.setName(name);
        person.setSpent(spent);
        person.setCity(city);
        person.setTimestamp(timestamp);
        try {
            String json = objectMapper.writeValueAsString(person);
            return json + "\n";
            // json  вида "{"name":"USER", "spend": 1999, "city: "Saratov", "timestamp": "ddnnyyy"}\n"
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }

    private static Integer generateSpend() {
        // Среднее значение и стандартное отклонение выбраны так, чтобы покрыть диапазон от 1000 до 5000
        double mean = 4.2;
        double stdDev = 7;
        double logNormalRandom = Math.exp(mean + stdDev * ThreadLocalRandom.current().nextGaussian());

        double minSpend = 100.0;
        double maxSpend = 100000.0;

        //(int) (Math.max(minSpend, Math.min(maxSpend, logNormalRandom)));
        return (int) logNormalRandom;
    }

    private String generateCity() {
        int countCities = Constants.DICTIONARY_CITY.size();
        int indexCity = ThreadLocalRandom.current().nextInt(0, countCities);
        return Constants.DICTIONARY_CITY.get(indexCity);
    }

    private String generateUserName() {
        int countNames = Constants.DICTIONARY_NAMES.size();
        int indexName = ThreadLocalRandom.current().nextInt(0, countNames);
        return Constants.DICTIONARY_NAMES.get(indexName);
    }

}


