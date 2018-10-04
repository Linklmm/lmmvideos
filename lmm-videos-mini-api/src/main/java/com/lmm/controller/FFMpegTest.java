package com.lmm.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FFMpegTest {

    private String FFMpegEXE;

    public FFMpegTest(String FFMpegEXE) {
        this.FFMpegEXE = FFMpegEXE;
    }

    public void convertor(String videoInputPath,String videoOutputPath) throws Exception {
        //ffmpeg -i input.mp4 output.avi
        List<String> command=new ArrayList<>();
        command.add(FFMpegEXE);

        command.add("-i");
        command.add(videoInputPath);
        command.add(videoOutputPath);

        for (String c:command){
            System.out.println(c);
        }

        ProcessBuilder builder=new ProcessBuilder(command);
        Process process=builder.start();

        InputStream errorStream=process.getErrorStream();
        InputStreamReader inputStreamReader=new InputStreamReader(errorStream);
        BufferedReader br=new BufferedReader(inputStreamReader);


        String line="";
        while ((line=br.readLine())!=null){
        }

        if (br!=null){
            br.close();
        }

        if (inputStreamReader!=null){
            inputStreamReader.close();
        }

        if (errorStream!=null){
            errorStream.close();
        }

    }

    public static void main(String[] args) {
        FFMpegTest ffmpeg=new FFMpegTest(
                "D:\\ffmpeg-20181003-ebc3d04-win64-static\\bin\\ffmpeg.exe");
        try {
            ffmpeg.convertor("F:\\lmm_videos\\181001B4D76MGY14\\video\\超星.mp4",
                    "F:\\lmm_videos\\181001B4D76MGY14\\video\\超星.avi");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
