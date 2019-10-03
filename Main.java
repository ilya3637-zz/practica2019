import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Main {

    static public String iNN = "0000";
    static public Integer k = 1;

    public static void main(String[] args) throws IOException, InterruptedException {

        try {
            //первый запрос
            String result1 = "";
            iNN = args[0];
            String urlParameters = "vyp3CaptchaToken=&page=&query=" + iNN + "&region=&PreventChromeAutocomplete=";
            URL url1 = new URL("https://egrul.nalog.ru/");
            URLConnection conn = url1.openConnection();
            //Использовать данные куки в случае проблем с доступом из-за защиты от ботов
            //String cookie = "top100_id=t1.-1.330694146.1568320375538; _ym_uid=156832037639061401; _ym_d=1568320376; _ga=GA1.2.1923758932.1568320377; last_visit=1568729872432::1568740672432; _ym_isad=1; JSESSIONID=FDD7C5A98CEB847B3EACF2468037A096; _ym_visorc_23729197=w";
            //String cookie = "JSESSIONID=6276B2D824DB5B3B38BE8AA1F22DB5AC; _ym_isad=2; _ym_d=1569934786; _ym_visorc_23729197=b; _ym_uid=156993478640311120";
            //conn.setRequestProperty("Cookie" , cookie);
            conn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(urlParameters);
            writer.flush();
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = reader.readLine()) != null) {
                result1 = line;
            }
            writer.close();
            reader.close();
            result1 = result1.substring(6);
            result1 = result1.substring(0, result1.indexOf("\""));

            //второй запрос
            String time = Long.toString(System.currentTimeMillis());
            String url2 = "https://egrul.nalog.ru/search-result/" + result1 + "?r=" + time + "&_=" + time;
            URL obj = new URL(url2);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            String result2 = response.toString();
            result2 = result2.substring(result2.indexOf("\"t"));
            result2 = result2.substring(5);
            result2 = result2.substring(0, result2.indexOf("\""));

            //третий запрос
            time = Long.toString(System.currentTimeMillis());
            String url3 = "https://egrul.nalog.ru/vyp-request/" + result2 + "?r=&_=" + time;
            URL obj3 = new URL(url3);
            HttpURLConnection con3 = (HttpURLConnection) obj3.openConnection();
            con3.setRequestMethod("GET");
            BufferedReader in3 = new BufferedReader(new InputStreamReader(con3.getInputStream()));
            in3.close();

            //четвертый запрос
            time = Long.toString(System.currentTimeMillis());
            String url4 = "https://egrul.nalog.ru/vyp-status/" + result2 + "?r=" + time + "&_=" + time;
            URL obj4 = new URL(url4);
            HttpURLConnection con4 = (HttpURLConnection) obj4.openConnection();
            con4.setRequestMethod("GET");
            BufferedReader in4 = new BufferedReader(new InputStreamReader(con4.getInputStream()));
            in4.close();

            //пятый запрос
            time = Long.toString(System.currentTimeMillis());
            String url5 = "https://egrul.nalog.ru/vyp-status/" + result2 + "?r=" + time + "&_=" + time;
            URL obj5 = new URL(url4);
            HttpURLConnection con5 = (HttpURLConnection) obj5.openConnection();
            con5.setRequestMethod("GET");
            BufferedReader in5 = new BufferedReader(new InputStreamReader(con5.getInputStream()));
            in5.close();

            //pdf
            URL url = new URL("https://egrul.nalog.ru/vyp-download/" + result2);
            InputStream in7 = url.openStream();
            Files.copy(in7, Paths.get(iNN + ".pdf"), StandardCopyOption.REPLACE_EXISTING);
            in.close();
            File pdfFile = new File(iNN + ".pdf");
            Desktop.getDesktop().open(pdfFile);
            System.out.println("PDF " + iNN + ".pdf загружен и откроется автоматически по возможности.");
        } catch (Exception e)
        {
            k++;
            if (k < 10) {
                System.out.println("Повторный проход №" + k);
                Main.main(args);
            } else {
                System.out.println("Сервер определил автозагрузчик, попробуйте в рчуном режиме загрузить. ИНН: " + iNN);
            }
        }
    }
}
