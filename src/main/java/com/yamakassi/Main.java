package com.yamakassi;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {
    private static final int NUMBER_OF_THREADS = 10;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9999);

        ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

        while (true) {
            System.out.println("$Socket server running..");
            Socket socket = serverSocket.accept();
            executorService.submit(new Handler(socket));
        }
    }
}
