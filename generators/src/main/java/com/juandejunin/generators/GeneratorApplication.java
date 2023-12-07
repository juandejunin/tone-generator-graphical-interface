// GeneratorApplication.java
package com.juandejunin.generators;

import javax.sound.sampled.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GeneratorApplication extends Application {

    private Stage primaryStage;
    private GeneratorController generatorController;

    private volatile boolean stopToneFlag;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;

        // Cargar la interfaz de usuario desde FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/juandejunin/generators/Generator-view.fxml"));
        Parent root = loader.load();

        // Asignar el controlador desde el nuevo archivo
        generatorController = loader.getController();

        // Pasar la referencia de la aplicación al controlador
        generatorController.setGeneratorApplication(this);

        primaryStage.setTitle("Generador de Tonos");
        primaryStage.setScene(new Scene(root, 300, 150));
        primaryStage.show();
    }

    @Override
    public void stop() {
        stopToneFlag = true;
        generatorController.stopTone();
    }

    // Método para reproducir el tono
    public void playTone(int frequency, int duration, String waveform) throws LineUnavailableException, IOException {
        stopToneFlag = false;

        // Crear un objeto AudioFormat personalizado
        AudioFormat audioFormat = new AudioFormat(44100, 16, 1, true, false);

        // Abrir una SourceDataLine
        SourceDataLine sourceDataLine = AudioSystem.getSourceDataLine(audioFormat);
        sourceDataLine.open(audioFormat);
        sourceDataLine.start();

        // Generar las muestras de audio
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        for (int i = 0; i < duration * 44100 / 1000; i += bufferSize / 2) {
            if (stopToneFlag) {
                break;
            }

            for (int j = 0; j < bufferSize / 2; j++) {
                double angle = (i + j) / (44100.0 / frequency) * 2.0 * Math.PI;
                short sample = 0;

                switch (waveform) {
                    case "Sine":
                        sample = (short) (Short.MAX_VALUE * Math.sin(angle));
                        break;
                    case "Square":
                        sample = (short) ((angle % (2 * Math.PI) < Math.PI) ? Short.MAX_VALUE : Short.MIN_VALUE);
                        break;
                    case "Sawtooth":
                        sample = (short) (Short.MAX_VALUE * ((angle % (2 * Math.PI)) / (2 * Math.PI) - 0.5));
                        break;
                    case "Triangle":
                        sample = (short) (Short.MAX_VALUE * Math.asin(Math.sin(angle)) / (Math.PI / 2));
                        break;
                    default:
                        System.out.println("Tipo de onda no reconocido: " + waveform);
                }

                buffer[j * 2] = (byte) (sample & 0xFF);
                buffer[j * 2 + 1] = (byte) ((sample >> 8) & 0xFF);
            }

            sourceDataLine.write(buffer, 0, bufferSize);
        }

        sourceDataLine.drain();
        sourceDataLine.close();

    }
}