//package main.java;
package com.tabatatimer;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class AudioPlayer {

    /**
     * Reproduz um arquivo de som do classpath do projeto.
     * O arquivo deve estar na pasta 'src/main/resources/sounds/'.
     *
     * @param soundResourceName o nome do arquivo de som (ex: "prep.wav")
     */
    public static void playSound(String soundResourceName) {
    try {
        // Usa o ClassLoader para obter um InputStream do recurso
        InputStream audioStream = AudioPlayer.class.getResourceAsStream("/sounds/" + soundResourceName);
        if (audioStream == null) {
            System.err.println("Arquivo de som não encontrado: " + soundResourceName);
            return;
        }

        // Carrega o stream de áudio para a memória
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new java.io.BufferedInputStream(audioStream));

        // Obtém um clipe de áudio
        Clip clip = AudioSystem.getClip();

        // Abre o clipe com o stream de áudio
        clip.open(audioInputStream);

        // Inicia a reprodução
        clip.start();

        // Adiciona um listener para fechar o clipe após a reprodução
        clip.addLineListener(event -> {
            if (event.getType() == LineEvent.Type.STOP) {
                clip.close();
            }
        });

    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
        e.printStackTrace();
        System.err.println("Erro ao reproduzir o arquivo de som: " + soundResourceName);
    }
}
}