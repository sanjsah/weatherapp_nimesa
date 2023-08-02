
package sanjana;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class WeatherApp {

    private static final String API_BASE_URL = "https://samples.openweathermap.org/data/2.5/forecast/hourly?q=London,us&appid=b6907d289e10d714a6e88b30761fae22";

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        int option;
        do {
            printMenu();
            option = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (option) {
                case 1:
                    getWeather(scanner);
                    break;
                case 2:
                    getWindSpeed(scanner);
                    break;
                case 3:
                    getPressure(scanner);
                    break;
                case 0:
                    System.out.println("Exiting the program. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (option != 0);

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("Weather API Options:");
        System.out.println("1. Get weather");
        System.out.println("2. Get Wind Speed");
        System.out.println("3. Get Pressure");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void getWeather(Scanner scanner) throws Exception {
        System.out.print("Enter a date (in format yyyy-MM-dd): ");
        String userInput = scanner.nextLine();

        try {
            JSONObject weatherData = getWeatherData(userInput);
            if (weatherData != null) {
                double temperature = weatherData.getJSONObject("main").getDouble("temp");
                System.out.println("Temperature on " + userInput + ": " + temperature + " °C");
            }
        } catch (IOException e) {
            System.out.println("Error occurred while fetching weather data: " + e.getMessage());
        }
    }

    private static void getWindSpeed(Scanner scanner) throws Exception {
        System.out.print("Enter a date (in format yyyy-MM-dd): ");
        String userInput = scanner.nextLine();

        try {
            JSONObject weatherData = getWeatherData(userInput);
            if (weatherData != null) {
                double windSpeed = weatherData.getJSONObject("wind").getDouble("speed");
                System.out.println("Wind Speed on " + userInput + ": " + windSpeed + " m/s");
            }
        } catch (IOException e) {
            System.out.println("Error occurred while fetching weather data: " + e.getMessage());
        }
    }

    private static void getPressure(Scanner scanner) throws Exception {
        System.out.print("Enter a date (in format yyyy-MM-dd): ");
        String userInput = scanner.nextLine();

        try {
            JSONObject weatherData = getWeatherData(userInput);
            if (weatherData != null) {
                double pressure = weatherData.getJSONObject("main").getDouble("pressure");
                System.out.println("Pressure on " + userInput + ": " + pressure + " hPa");
            }
        } catch (IOException e) {
            System.out.println("Error occurred while fetching weather data: " + e.getMessage());
        }
    }

    private static JSONObject getWeatherData(String date) throws IOException, Exception {
    	SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat apiDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date parsedDate = inputDateFormat.parse(date);
            date = apiDateFormat.format(parsedDate);
        } catch (Exception e) {
            System.out.println("Invalid date format! Please enter the date in format yyyy-MM-dd.");
            return null;
        }

        String apiUrl = API_BASE_URL + "&dt=" + date;

        try {
            String response = makeAPIRequest(apiUrl);
            JSONObject jsonObject = new JSONObject(response);
            JSONArray hourlyData = jsonObject.getJSONArray("hourly");
            return hourlyData.getJSONObject(0); // Assuming you want the first hourly data for the given date
        } catch (JSONException e) {
            System.out.println("Error occurred while parsing weather data: " + e.getMessage());
        }
		return null;
    }

    private static String makeAPIRequest(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();
        return response.toString();
    }
}
