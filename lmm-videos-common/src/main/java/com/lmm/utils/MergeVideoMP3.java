package com.lmm.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MergeVideoMP3 {

    private String FFMpegEXE;

    public MergeVideoMP3(String FFMpegEXE) {
        this.FFMpegEXE = FFMpegEXE;
    }

    public void convertor(String videoInputPath,String mp3InputPath,
            double seconds,String videoOutputPath) throws Exception {
        //ffmpeg -i input.mp4 output.avi
        //ffmpeg.exe -i ***.mp4 -i bgm.mp3 -t 7 -y 新的视频.mp4
        List<String> command=new ArrayList<>();
        command.add(FFMpegEXE);

        command.add("-i");
        command.add(videoInputPath);

        command.add("-i");
        command.add(mp3InputPath);

        command.add("-t");
        command.add(String.valueOf(seconds));

        command.add("-y");
        command.add(videoOutputPath);

//        for (String c:command){
//            System.out.print(c);
//        }

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
        MergeVideoMP3 ffmpeg=new MergeVideoMP3(
                "D:\\ffmpeg-20181003-ebc3d04-win64-static\\bin\\ffmpeg.exe");
        try {
            ffmpeg.convertor("F:\\lmm_videos\\181001B4D76MGY14\\video\\超星.mp4","F:\\lmm_videos\\181001B4D76MGY14\\video\\测试.mp3",
                    30.0, "F:\\lmm_videos\\181001B4D76MGY14\\video\\新的视频.avi");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
