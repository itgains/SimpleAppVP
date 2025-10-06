package com.example.simpleapp

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView

class PlayerActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var seekBarVolume: SeekBar
    private lateinit var tvTrackInfo: TextView
    private var volumeLevel = 0.5f

    private val songs = intArrayOf(R.raw.sample, R.raw.sample2, R.raw.sample3)
    private val songNames = arrayOf("Песня 1", "Песня 2", "Песня 3")
    private var currentSongIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        tvTrackInfo = findViewById(R.id.tvTrackInfo)
        seekBarVolume = findViewById(R.id.seekBarVolume)

        // Инициализация MediaPlayer с первой песней
        initializeMediaPlayer()

        val btnPlay = findViewById<Button>(R.id.btnPlay)
        val btnPause = findViewById<Button>(R.id.btnPause)
        val btnStop = findViewById<Button>(R.id.btnStop)
        val btnBack = findViewById<Button>(R.id.btnBackFromPlayer)
        val btnNext = findViewById<Button>(R.id.btnNext)
        val btnPrev = findViewById<Button>(R.id.btnPrev)

        btnPlay.setOnClickListener { playAudio() }
        btnPause.setOnClickListener { pauseAudio() }
        btnStop.setOnClickListener { stopAudio() }
        btnBack.setOnClickListener { finish() }
        btnNext.setOnClickListener { nextSong() }
        btnPrev.setOnClickListener { previousSong() }

        setupVolumeControl()
    }

    private fun initializeMediaPlayer() {
        mediaPlayer = MediaPlayer.create(this, songs[currentSongIndex])
        updateTrackInfo()
    }

    private fun playAudio() {
        try {
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
                tvTrackInfo.text = "Воспроизведение: ${songNames[currentSongIndex]}"
            }
        } catch (e: Exception) {
            showError("Ошибка воспроизведения")
        }
    }

    private fun pauseAudio() {
        try {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                tvTrackInfo.text = "Пауза: ${songNames[currentSongIndex]}"
            }
        } catch (e: Exception) {
            showError("Ошибка паузы")
        }
    }

    private fun stopAudio() {
        try {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
                // После остановки нужно переинициализировать плеер
                initializeMediaPlayer()
            }
        } catch (e: Exception) {
            showError("Ошибка остановки")
        }
    }

    private fun nextSong() {
        currentSongIndex = (currentSongIndex + 1) % songs.size
        switchSong(songs[currentSongIndex])
    }

    private fun previousSong() {
        currentSongIndex = (currentSongIndex - 1 + songs.size) % songs.size
        switchSong(songs[currentSongIndex])
    }

    private fun switchSong(songResource: Int) {
        try {
            mediaPlayer.release() // Освобождаем предыдущий плеер
            mediaPlayer = MediaPlayer.create(this, songResource)
            mediaPlayer.setVolume(volumeLevel, volumeLevel)
            updateTrackInfo()

            // Автоматически начинаем воспроизведение новой песни
            playAudio()
        } catch (e: Exception) {
            showError("Ошибка загрузки песни")
        }
    }

    private fun updateTrackInfo() {
        tvTrackInfo.text = "${songNames[currentSongIndex]} (${currentSongIndex + 1}/${songs.size})"
    }

    private fun setupVolumeControl() {
        seekBarVolume.progress = 50
        seekBarVolume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                volumeLevel = progress / 100f
                mediaPlayer.setVolume(volumeLevel, volumeLevel)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun showError(message: String) {
        tvTrackInfo.text = message
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}