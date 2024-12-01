package org.example;

import org.json.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

import static org.example.MeteoCommons.*;

public class Main {
    public static void main(String[] args) {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(METEO_URI+LAT_KEY+LAT_VALUE+"&"+LON_KEY+LON_VALUE))
                .header(HEADER, ACCESS_KEY)
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonResponse = new JSONObject(response.body());
            System.out.println(jsonResponse);
            JSONObject fact = jsonResponse.getJSONObject("fact");
            System.out.println("Температура: " + fact.getInt("temp"));
            Scanner sc = new Scanner(System.in);
            System.out.println("Введите число дней в периоде (1-7):");
            int limit = sc.nextInt();
            if (limit < 1 || limit > 7) {
                System.out.println("Неправильное число дней в периоде");
            }
            sc.close();
            JSONArray forecasts = jsonResponse.getJSONArray("forecasts");
            double temp = 0;
            for (int i=0; i < limit; i++) {
                JSONObject day = forecasts.getJSONObject(i);
                temp += day.getJSONObject("parts").getJSONObject("day").getDouble("temp_avg");
                }
            System.out.println("Средняя температура за период: " + temp);
        } catch (Exception e) {
            System.err.println("Error making HTTP request: " + e.getMessage());
        }
    }
}