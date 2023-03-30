/*Imports for manipulating Word docx*/
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
/*Imports for I/O and URL handling */
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import java.util.Scanner;
public class App 
{
    public static String chatGPT(String question) throws Exception 
    {
        String url = "https://api.openai.com/v1/completions";
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer YOUR-API-KEY");

        JSONObject data = new JSONObject();
        data.put("model", "text-davinci-003");
        data.put("prompt", question);
        data.put("max_tokens", 4000);
        data.put("temperature", 1.0);

        con.setDoOutput(true);
        con.getOutputStream().write(data.toString().getBytes());

        String output = new BufferedReader(new InputStreamReader(con.getInputStream())).lines()
                .reduce((a, b) -> a + b).get();

         String answer = new JSONObject(output).getJSONArray("choices").getJSONObject(0).getString("text");
         return answer;
    }
    
    public static void main(String[] args) throws Exception 
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the location of the file : ");
        String loc = sc.nextLine();
        FileInputStream fis = new FileInputStream(loc);
        XWPFDocument document = new XWPFDocument(fis);
        XWPFWordExtractor extractor = new XWPFWordExtractor(document);
        String[] questions = extractor.getText().split("\n");
        String answers[] = new String[questions.length];
        for (int i = 0; i < questions.length; i++) 
        {
            answers[i] = chatGPT(questions[i]);
            answers[i].trim();
        }
        System.out.println("Please enter the location where the answers need to be stored : ");
        String filename = sc.nextLine();
        try (XWPFDocument doc = new XWPFDocument()) 
        {
            XWPFParagraph p1 = doc.createParagraph();
            p1.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun r1 = p1.createRun();
            r1.setBold(true);
            r1.setFontSize(12);
            r1.setFontFamily("New Roman");
            for (int i = 0; i < answers.length; i++) 
            {
             r1.setText((i+1)+")"+answers[i]+"\n");
             r1.addCarriageReturn();
            }
            try (FileOutputStream out = new FileOutputStream(filename)) 
            {
                doc.write(out);
            }
        }
        System.out.println("SUCCESS!! The answers have been stored on the specified file at the specified location.");
        extractor.close();
        sc.close();
    }
}