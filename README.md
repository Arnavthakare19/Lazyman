# Lazyman
A java program that reads your Microsoft docx files, gets the answers through the chat GPT and stores them locally in another docx file locally.

Are you struggling to complete your homework on time? Do you find it challenging to research the answers to your assignments? As a student, I understand the frustration of having to complete multiple academic tasks and homework, leaving you with little time to focus on other activities.

That's why I came up with an idea for a program that will make your life easier! With this software, all you need to do is upload your homework's docx file, and the program will extract all the questions for you. Then, using artificial intelligence, the software will find answers to your questions, saving you time and energy.

Imagine having all the answers to your homework without spending hours researching! This software will make completing your homework much more comfortable, reducing your stress levels and freeing up your time to focus on other essential activities.

This program uses the OpenAI API to generate answers to a set of questions provided in a Microsoft Word document. The program then writes the answers to a new Microsoft Word document.

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
