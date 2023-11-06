package com.yamakassi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class Handler implements Runnable {
    private static final double MEAN = 7.2;
    private static final double STD_DEV = 0.6;
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
        System.out.println("SPENT:" + spent);
        Date date = new Date();
        Person person = new Person();
        person.setName(name);
        person.setSpent(spent);
        person.setCity(city);
        person.setTimestamp(date.getTime() / 1000);
        try {
            String json = objectMapper.writeValueAsString(person);
            return json + "\n";
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }

    private static Integer generateSpend() {
        double logNormalRandom = Math.exp(MEAN + STD_DEV * ThreadLocalRandom.current().nextGaussian());
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


