package arnav;
/*Imports for manipulating Word docx*/
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/*Imports for I/O and URL handling */
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
public class App 
{
    public static String chatGPT(String prompt) {
       String url = "https://api.openai.com/v1/chat/completions";
       String apiKey = "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";
       String model = "gpt-3.5-turbo";
       int maxRetries = 3; // Maximum number of retries

       for (int retry = 0; retry < maxRetries; retry++) {
           try {
               URL obj = new URL(url);
               HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
               connection.setRequestMethod("POST");
               connection.setRequestProperty("Authorization", "Bearer " + apiKey);
               connection.setRequestProperty("Content-Type", "application/json");

               // The request body
               String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + prompt + "\"}]}";
               connection.setDoOutput(true);
               OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
               writer.write(body);
               writer.flush();
               writer.close();

               // Response from ChatGPT
               BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
               String line;

               StringBuffer response = new StringBuffer();

               while ((line = br.readLine()) != null) {
                   response.append(line);
               }
               br.close();

               // Calls the method to extract the message.
               return extractMessageFromJSONResponse(response.toString());

           } catch (IOException e) {
            // Retry on IOException
            System.out.println("Error: " + e.getMessage());
            System.out.println("Retry attempt: " + (retry + 1));
            continue; // Terminates the loop and goes to the next iteration
        }
       }

       // Return an error message if maxRetries are reached
       return "Error: Maximum number of retries reached. Unable to process the request.";
   }

   public static String extractMessageFromJSONResponse(String response) {
       int start = response.indexOf("content") + 11;
       int end = response.indexOf("\"", start);
       return response.substring(start, end);
   }
    private static JFrame frame;
    private static JFileChooser fileChooser;

    static public void FileChooserDemo() {
        frame = new JFrame("Choose the file");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Word Documents", "docx"));

        // Add a button to open the file chooser
        JButton openButton = new JButton("Open");
        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int returnValue = fileChooser.showOpenDialog(frame);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
    try {
        String loc = fileChooser.getSelectedFile().getAbsolutePath();
        String[] questions = getQuestions(loc);
        String[] answers = new String[questions.length];
        for (int i = 0; i < questions.length; i++) {
            answers[i] = chatGPT(questions[i]);
        }

        // Save the answers to the Word document
        XWPFDocument document = new XWPFDocument();

        for (int i = 0; i < questions.length; i++) {
            XWPFParagraph paragraph = document.createParagraph();
            paragraph.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun run = paragraph.createRun();
            run.setText(questions[i] + "\n");
            run.addCarriageReturn();
            run.setText(answers[i] + "\n");
        }

        try (FileOutputStream out = new FileOutputStream("output.docx")) {
            document.write(out);
        }
        document.close();
    } catch (Exception f) {
        System.out.println("Error: " + f.getMessage());
    }
}

            }
        });

        JPanel panel = new JPanel();
        panel.add(openButton);

        frame.add(panel, BorderLayout.CENTER);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static String[] getQuestions(String loc) throws Exception {
        FileInputStream fis = new FileInputStream(loc);
        XWPFDocument document = new XWPFDocument(fis);
        XWPFWordExtractor extractor = new XWPFWordExtractor(document);
        String[] questions = extractor.getText().split("\n");
        extractor.close();
        return questions;
    }

    public static void main(String[] args) {
        App.FileChooserDemo();
    }
}