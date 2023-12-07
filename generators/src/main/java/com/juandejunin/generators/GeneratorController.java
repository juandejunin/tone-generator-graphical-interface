// GeneratorController.java
package com.juandejunin.generators;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javax.sound.sampled.*;
import javafx.scene.control.ChoiceBox;

import java.io.IOException;

public class GeneratorController {

    @FXML
    private TextField frequencyInput;

    @FXML
    private TextField durationInput;

    @FXML
    ChoiceBox<String> waveformChoice;

    private GeneratorApplication generatorApplication;

    private volatile boolean stopToneFlag = false;

    // Método para detener el tono
    public void stopTone() {
        stopToneFlag = true;
    }


    public void playTone(int frequency, int duration, String waveform) {
        stopToneFlag = false;

        try {
            AudioFormat audioFormat = new AudioFormat(44100, 16, 1, true, false);
            SourceDataLine sourceDataLine = AudioSystem.getSourceDataLine(audioFormat);
            sourceDataLine.open(audioFormat);
            sourceDataLine.start();

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
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    // Setter para asignar la referencia de GeneratorApplication
    public void setGeneratorApplication(GeneratorApplication generatorApplication) {
        this.generatorApplication = generatorApplication;
    }
    @FXML
    protected void playButtonClicked() {
        try {
            int frequency = Integer.parseInt(frequencyInput.getText());
            int duration = Integer.parseInt(durationInput.getText());

            // Obtener el tipo de onda seleccionado desde el ChoiceBox
            String waveform = waveformChoice.getValue();

            if (waveform == null) {
                // No se ha seleccionado ninguna onda
                showAlert("Por favor, selecciona un tipo de onda.");
                return;
            }

            // Validar que se ingresaron valores para frecuencia y duración
            if (frequency <= 0 || duration <= 0) {
                showAlert("Por favor, ingresa valores válidos para frecuencia y duración.");
                return;
            }

            // Llamar al método de GeneratorApplication para reproducir el tono con el tipo de onda seleccionado
            generatorApplication.playTone(frequency, duration, waveform);
        } catch (NumberFormatException | LineUnavailableException | IOException e) {
            showAlert("Por favor, ingresa valores numéricos para frecuencia y duración.");
        }
    }

    private void showAlert(String message) {
        // Mostrar un cuadro de diálogo de alerta con el mensaje de error
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


//    @FXML
//    protected void playButtonClicked() {
//        try {
//            int frequency = Integer.parseInt(frequencyInput.getText());
//            int duration = Integer.parseInt(durationInput.getText());
//
//            // Obtener el tipo de onda seleccionado desde el ChoiceBox
//            String waveform = waveformChoice.getValue();
//            System.out.println(waveform);
//
//            // Llamar al método de GeneratorApplication para reproducir el tono con el tipo de onda seleccionado
//            generatorApplication.playTone(frequency, duration, waveform);
//        } catch (NumberFormatException e) {
//            System.out.println("Ingrese valores numéricos para frecuencia y duración.");
//        } catch (LineUnavailableException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
