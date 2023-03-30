# Lazyman
A java program that reads your Microsoft docx files, gets the answers through the chat GPT and stores them locally in another docx file locally.

Are you struggling to complete your homework on time? Do you find it challenging to research the answers to your assignments? As a student, I understand the frustration of having to complete multiple academic tasks and homework, leaving you with little time to focus on other activities.

That's why I came up with an idea for a program that will make your life easier! With this software, all you need to do is upload your homework's docx file, and the program will extract all the questions for you. Then, using artificial intelligence, the software will find answers to your questions, saving you time and energy.

Imagine having all the answers to your homework without spending hours researching! This software will make completing your homework much more comfortable, reducing your stress levels and freeing up your time to focus on other essential activities.

This program uses the OpenAI API to generate answers to a set of questions provided in a Microsoft Word document. The program then writes the answers to a new Microsoft Word document. Make sure you have your java project made in maven.

The program begins by importing the necessary libraries for working with Word documents and making HTTP requests.
```
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import java.util.Scanner;

```
The chatGPT function is used to send a question to the OpenAI API and receive an answer. It takes a string argument question and returns a string answer.
We create an object con to open a connection with OpenAi website. The website is mentioned in url variable as a string. It is available in the official documentation of OpenAI.
```
 String url = "https://api.openai.com/v1/completions";
 HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
```

NOTE : Replace YOUR-API-KEY in the request property with your personal api key that can be easily created once you make an account in OpenAI.
```
con.setRequestProperty("Authorization", "Bearer YOUR-API-KEY");
```


You can change the word limit of the responses recieved by changing the max_tokens(remember gpt 3.5 has a hard set limit of 4000 tokens). If you want to make your vary each time you ask the same question then change the temperature in data.put. Increasing the temperature adds the variation in the responses of your answers. Higher the temperature the lesser the accuracy of the response and more variation it gives.
```
    data.put("model", "text-davinci-003");
    data.put("prompt", question);
    data.put("max_tokens", 4000);
    data.put("temperature", 1.0);
```

In the main function, the program prompts the user to enter the location of the input Word document and reads the questions from the document using the XWPFWordExtractor class.

NOTE : After copying the path of the file always make sure to add an extra '/' to the path.
For example : 

```
  //Users//pankaj//test.docx
  Instead of /Users/pankaj/test.docx
```


The lines are each seperated via String.split()function in java. These serves as array to store the questions in the docx files.
```
FileInputStream fis = new FileInputStream(loc);
XWPFDocument document = new XWPFDocument(fis);
XWPFWordExtractor extractor = new XWPFWordExtractor(document);
String[] questions = extractor.getText().split("\n");

```

The program then loops over each question and generates an answer using the chatGPT function.
```
String[] answers = new String[questions.length];
for (int i = 0; i < questions.length; i++) 
{
    answers[i] = chatGPT(questions[i]);
    answers[i].trim();
}

```

The program prompts the user to enter the location where the output Word document should be saved and writes the answers to a new document using the XWPFDocument class.You can modify how the answers should be stored in the docx file. If you use the cold as it is then you will have answers stored in bold. You may modify its font, size, style, etc.

```

    XWPFParagraph p1 = doc.createParagraph();
    p1.setAlignment(ParagraphAlignment.LEFT);
    XWPFRun r1 = p1.createRun();
    r1.setBold(true);
    r1.setFontSize(12);
    r1.setFontFamily("New Roman");
    for (int i = 0; i < answers.length; i++) 
    {
        r1.setText((i+1)+")"+answers
    }
```
