package gui;

import java.nio.file.Paths;

import javafx.scene.media.AudioClip;
public class SoundFX {
	private static SoundFX inst;
	private AudioClip toPlay;
	private AudioClip errorClip;
	private AudioClip succClip;
	public static SoundFX getInstance() {
		if(inst==null)
			inst=new SoundFX();
		return inst;
	}
	private SoundFX() {
		errorClip=new AudioClip(Paths.get("src/gui/sounds/erro.mp3").toUri().toString());
		errorClip.setVolume(1);
		errorClip.setCycleCount(1);
		succClip=new AudioClip(Paths.get("src/gui/sounds/succ.wav").toUri().toString());
		succClip.setVolume(1);
		succClip.setCycleCount(1);
	}
	public void playSound(String soundFile) {
		toPlay=new AudioClip(Paths.get("src/gui/sounds/"+soundFile).toUri().toString());
		toPlay.setVolume(1);
		toPlay.setCycleCount(1);
		toPlay.play();
		
	}
	public void playError() {
		errorClip.play();
	}
	public void playSucc() {
		succClip.play();
	}
}
