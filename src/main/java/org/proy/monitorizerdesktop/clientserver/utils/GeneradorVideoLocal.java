package org.proy.monitorizerdesktop.clientserver.utils;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class GeneradorVideoLocal {
    FFmpegFrameRecorder recorder;
    Java2DFrameConverter converter;
    long startTime;
    Integer width=0;
    Integer height=0;
    Integer fps;
    String tmpVideo = null;

    public FFmpegFrameRecorder getRecorder() {
        return recorder;
    }

    public Integer getFps() {
        return fps;
    }
    public void setFps(Integer fps) {
        this.fps = fps;
    }
    public Integer getWidth() {
        return width;
    }
    public void setWidth(Integer width) {
        this.width = width;
    }
    public Integer getHeight() {
        return height;
    }
    public void setHeight(Integer height) {
        this.height = height;
    }

    public void setRecorderProperties(){
        try{

            tmpVideo = "temp_" + UUID.randomUUID() + ".mp4";
            recorder= new FFmpegFrameRecorder(tmpVideo, width, height);
            recorder.setFormat("mp4");
            recorder.setFrameRate(fps);
            recorder.setVideoBitrate(1920 * 1080* 3);
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            converter = new Java2DFrameConverter();
            startTime = System.currentTimeMillis();
            recorder.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void anadirFrame(BufferedImage screen){
        if(screen==null){
            return;
        }
        long timeStamp = (System.currentTimeMillis() - startTime) * 1000L;
        recorder.setTimestamp(timeStamp);
        BufferedImage corrected = ajustarRGB(screen);
        try{
            Frame frame = converter.convert(corrected);
            recorder.record(frame);
        }catch (Exception e){
            System.out.println("Error al recorrer el frame, probablemente se detuvo la captura");
        }
    }

    private BufferedImage ajustarRGB(BufferedImage screen) {
        BufferedImage corrected = new BufferedImage(screen.getWidth(), screen.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = corrected.createGraphics();
        g.drawImage(screen, 0, 0, null);
        g.dispose();
        return corrected;
    }

    public void detenerGeneracion(){
        try{
            recorder.stop();
            recorder.release();

        } catch (FFmpegFrameRecorder.Exception e) {
            throw new RuntimeException(e);
        }

    }

    public File guardarVideo(String path) {
        try {
            File mediaDir = new File(path);
            if (!mediaDir.exists()) {
                mediaDir.mkdirs();
            }

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = "grabacion_" + timestamp + ".mp4";
            File outputFile = new File(mediaDir, fileName);

            File temp = new File(tmpVideo);

            if (!temp.exists() || temp.length() == 0) {
                System.err.println("El archivo temporal no existe o está vacío.");
                return null;
            }

            try {
                Files.move(temp.toPath(), outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Video guardado en: " + outputFile.getAbsolutePath());
            } catch (Exception moveEx) {
                System.err.println("Falló move, intentando copy + delete...");
                Files.copy(temp.toPath(), outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                temp.delete();
                System.out.println("Video guardado con copia en: " + outputFile.getAbsolutePath());
            }

            return outputFile;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}


