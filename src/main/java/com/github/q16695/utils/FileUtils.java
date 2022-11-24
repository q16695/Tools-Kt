package com.github.q16695.utils;

import java.io.*;
import java.util.ArrayList;

public class FileUtils {
    public static ArrayList<String> getFileAllContent1(InputStream streamReader) throws IOException {
        String content = "";
        ArrayList<String> arrayList = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(streamReader));
        while ((content = bufferedReader.readLine()) != null) {
            arrayList.add(content);
        }
        return arrayList;
    }
    public static ArrayList<String> getFileAllContent1(InputStreamReader streamReader) throws IOException {
        String content = "";
        ArrayList<String> arrayList = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(streamReader);
        while ((content = bufferedReader.readLine()) != null) {
            arrayList.add(content);
        }
        return arrayList;
    }
    public static ArrayList<String> getFileAllContent1(File file) throws IOException {
        ArrayList<String> list = new ArrayList<>();
        String content = "";
        InputStreamReader streamReader = new InputStreamReader(new FileInputStream(file), "gbk");
        BufferedReader bufferedReader = new BufferedReader(streamReader);
        while ((content = bufferedReader.readLine()) != null) {
            list.add(content);
        }
        return list;
    }
    public static String getFileAllContent(File file) throws IOException {
        String content = "";
        StringBuilder stringBuilder = new StringBuilder();
        InputStreamReader streamReader = new InputStreamReader(new FileInputStream(file), "gbk");
        BufferedReader bufferedReader = new BufferedReader(streamReader);
        while ((content = bufferedReader.readLine()) != null) {
            stringBuilder.append(content);
        }
        return stringBuilder.toString();
    }
    public static String getFileAllContent(InputStreamReader streamReader) throws IOException {
        String content = "";
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(streamReader);
        while ((content = bufferedReader.readLine()) != null) {
            stringBuilder.append(content);
        }
        return stringBuilder.toString();
    }
}
