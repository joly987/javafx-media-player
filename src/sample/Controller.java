package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.FileChooser;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;


public class Controller implements Initializable {

    private String path;
    private File file;
    private Media media;
    private MediaPlayer mediaPlayer;
    private Timer time;
    private TimerTask timerTask;

    private double volume;
    private int countMute, begin, countRepeat, repeatMusic=0;
    private boolean runMusic;
    private boolean search = false;

    @FXML
    private ProgressBar songProgressBar;
    @FXML
    private Label songLabel, repeatStateLabel;
    @FXML
    private Button playButton, pauseButton, stopButton, volumeUpButton, volumeDownButton, muteButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

//    Méthode qui permet de choisir un média
    public void chooseFileMedia() {
        FileChooser fileChooser = new FileChooser();
        file = fileChooser.showOpenDialog(null);
        path = file.toURI().toString();

        if (path != null) {
            media = new Media(path);
            mediaPlayer = new MediaPlayer(media);
            playMedia();
            songLabel.setText(file.getName());
        }

    }

//    Méthode qui permet de jouer un média
    public void playMedia() {
        if(begin == 0){
            volume = 0.5;
            mediaPlayer.setVolume(volume);
        }
        mediaPlayer.play();
        progressBar();
        begin = 1;
    }

//     Méthode qui permet de pause un média
    public void pauseMedia() {
        mediaPlayer.pause();
    }

//     Méthode qui permet de stopper un média
    public void stopMedia() {
        mediaPlayer.stop();
        resetTimer();
        songProgressBar.setProgress(0);
    }

//    Méthode qui permet de mettre en sourdine un média
    public void muteMedia() {
        countMute++;
        if (countMute%2 != 0){
            mediaPlayer.setVolume(0);
        }else mediaPlayer.setVolume(volume);

    }

//    Agmente le volume d'un média
    public void volumeUpMedia() {
        volume = volume + 0.15;
        mediaPlayer.setVolume(volume);
    }

//    Diminue le volume d'un média
    public  void volumeDownMedia() {

        volume = volume - 0.1;
        mediaPlayer.setVolume(volume);
    }

//    Répete un média
    public void repeatMedia() {
        countRepeat++;

        if(countRepeat%2 != 0){
            System.out.println("ON");
            repeatStateLabel.setText("ON");
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.play();

        }else{
            System.out.println("OFF");
            repeatStateLabel.setText("OFF");
        }

    }

//    Affiche si le button répéter est activé
    public void displayRepeat() {
        if(repeatMusic%2==0){
            System.out.println("Repeat OFF");
            repeatStateLabel.setText("");
        }else{
            System.out.println("Repeat ON");
            repeatStateLabel.setText("");
        }
    }

//    Bar de progression de la music jouer
    public void progressBar() {
        time = new Timer();

        timerTask = new TimerTask() {
            @Override
            public void run() {
                runMusic = true;

                double current= mediaPlayer.getCurrentTime().toSeconds();
                double end= mediaPlayer.getCycleDuration().toSeconds();

                songProgressBar.setProgress(current/end);

                if(current/end == 1){
                    time.cancel();
                    songProgressBar.setProgress(0);

                    if(repeatMusic%2 != 0){
                        if (!search){
                            media = new Media(path);
                            mediaPlayer = new MediaPlayer(media);
                            songLabel.setText(file.getName());
                        }
                    playMedia();
                    }else {
                        if (!search){
                            media = new Media(path);
                            mediaPlayer = new MediaPlayer(media);
                            songLabel.setText(file.getName());
                        }
                        repeatMusic = 0;
                    }
                }
            }
        };
        time.scheduleAtFixedRate(timerTask, 1000, 1000);
        displayRepeat();

    }

//    Remet la progresse barre au debut
    public void resetTimer() {
        runMusic = false;
        time.cancel();

    }

//  Ferme la fenêtre
    public void closeMedia() {
        System.exit(0);
    }

}
