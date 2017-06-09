package com.example.strost.logopedist.controller.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.messaging.DeliveryOptions;
import com.backendless.messaging.PublishOptions;
import com.backendless.services.messaging.MessageStatus;
import com.example.strost.logopedist.R;
import com.example.strost.logopedist.model.entities.Device;
import com.example.strost.logopedist.model.entities.Patient;
import com.example.strost.logopedist.model.entities.Caregiver;
import com.example.strost.logopedist.model.request.AddPictureRequest;
import com.example.strost.logopedist.model.request.AddRecordRequest;
import com.example.strost.logopedist.model.request.PutPatientHttpRequest;
import com.example.strost.logopedist.model.request.SendPushNotification;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class AddExerciseActvitiy extends AppCompatActivity {
    private Patient mPatient;
    private Caregiver mCaregiver;
    private EditText title, pickDate, description;
    // take picture
    private Button takePictureButton;
    private ImageView imageView;
    private Uri file = null;
    private Boolean help = false;
    // voice recoder
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    private MediaRecorder recorder = null;

    private int currentFormat = 0;
    private int output_formats[] = {MediaRecorder.OutputFormat.MPEG_4, MediaRecorder.OutputFormat.THREE_GPP};

    private String mAudioFilename;
    private String mOriginalAdioFileName = null;


    private final String AUDIOFILE_URL = "https://api.backendless.com/e5a95319-dfee-9344-ff32-50448355ec00/v1/files/media/recorded/";
    private final String PICTURE_URL = "https://api.backendless.com/e5a95319-dfee-9344-ff32-50448355ec00/v1/files/media/";

    private final String AUDIOFILE_TYPE = ".wma";
    private final String PICTURE_TYPE = ".png";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addexercise);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mPatient = (Patient) getIntent().getSerializableExtra("Patient");
        mCaregiver = (Caregiver) getIntent().getSerializableExtra("Caregiver");
        title = (EditText) findViewById(R.id.exerciseTitle);
        pickDate = (EditText) findViewById(R.id.etPickDate);
        // help switch
        Switch helpSwitch = (Switch) findViewById(R.id.help_switch);
        // description
        description = (EditText) findViewById(R.id.description_text);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAddPatient);
        // take picture
        takePictureButton = (Button) findViewById(R.id.button_image);
        imageView = (ImageView) findViewById(R.id.imageview);

        setMediaPlayer();
        setOnclickFAB(fab, helpSwitch);
        checkPermission();
        selectDate();
    }

    public void setMediaPlayer(){
        Button stop = (Button) findViewById(R.id.btnStop);
        Button play = (Button) findViewById(R.id.btnPlay);
        Button start = (Button) findViewById(R.id.btnStart);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioPlayer();
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecord();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecord();
            }
        });
    }

    public void setOnclickFAB(FloatingActionButton fab, final Switch helpSwitch){
        int maxId = 0;
        for (int i = 0; i < mPatient.getExercises().size(); i++) {
            if (mPatient.getExercises().get(i).getId() > maxId) {
                maxId = mPatient.getExercises().get(i).getId();
            }
        }
        final int newId = maxId + 1;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                help = helpSwitch.isChecked();

                String mPictureUrl = null;
                String mAudioUrl = null;

                String currentTime = System.currentTimeMillis() + "";

                if (file != null) {
                    mPictureUrl = PICTURE_URL + title.getText().toString().replace(" ", "") + "_" + currentTime + PICTURE_TYPE;
                }
                if (mOriginalAdioFileName != null) {
                    mAudioUrl = AUDIOFILE_URL + mOriginalAdioFileName + AUDIOFILE_TYPE;
                    addRecord();
                }

                mPatient.addExercise(newId, title.getText().toString(), mPictureUrl, help, description.getText().toString(), mAudioUrl, pickDate.getText().toString());
                Toast.makeText(AddExerciseActvitiy.this, getString(R.string.added_exercise), Toast.LENGTH_LONG).show();

                try {
                    addToFile(currentTime);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (mPatient.getDevices().size() != 0) {
                    sendPushNotification();
                }

                PutPatientHttpRequest pPR = new PutPatientHttpRequest();
                pPR.putPatient(mPatient);

                finish();
            }
        });
    }

    public void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            takePictureButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 0);
        }
    }

    public void selectDate(){
        pickDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();

                new DatePickerDialog(AddExerciseActvitiy.this, date, myCalendar
                        .get(cal.YEAR), myCalendar.get(cal.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    Date endDate = null;

    private void updateLabel() {

        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        endDate = myCalendar.getTime();
        pickDate.setText(sdf.format(myCalendar.getTime()));
    }


    public void sendPushNotification() {
        final DeliveryOptions deliveryOptions = new DeliveryOptions();
        for (Device d : mPatient.getDevices()) {
            deliveryOptions.addPushSinglecast(d.getDeviceId());
        }
        mPatient.getDevices().clear();

        final PublishOptions publishOptions = new PublishOptions();
        publishOptions.putHeader("android-ticker-text", getString(R.string.you_got_a_notification));
        publishOptions.putHeader("android-content-title", getString(R.string.you_got_a_new_exercise));
        publishOptions.putHeader("android-content-text", getString(R.string.the_exercise) + " " + title.getText().toString() + " " + getString(R.string.is_added));

        SendPushNotification spn = new SendPushNotification();
        spn.sendPushNotificaation(deliveryOptions, publishOptions);
    }


    public void addToFile(final String currentTime) throws IOException {

        if (file != null) {
            Bitmap mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), file);

            //rotate
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_action_name)
                            .setContentTitle(getString(R.string.upload_picture))
                            .setContentText(getString(R.string.notification_will_disappear));


            final NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.notify(001, mBuilder.build());

            final AddPictureRequest apr = new AddPictureRequest();
            final Bitmap finalMBitmap = mBitmap;
            Runnable runnable = new Runnable() {
                public void run() {
                    try {
                        apr.AddPicture(finalMBitmap, title.getText().toString().replace(" ", "") + "_" + currentTime, mNotifyMgr);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            Thread myThread = new Thread(runnable);
            myThread.start();
            try {
                myThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


  
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                takePictureButton.setEnabled(true);
            }
        }
    }

    public void takePicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        file = FileProvider.getUriForFile(
                this,
                this.getApplicationContext()
                        .getPackageName() + ".provider", getOutputMediaFile());

        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                imageView.setImageURI(file);
                imageView.setRotation(imageView.getRotation() + 90);
            }
        }
    }

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }

    public void addRecord() {
        final AddRecordRequest aRR = new AddRecordRequest();
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    aRR.AddRecord(mAudioFilename, "media/recorded");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
        try {
            mythread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void enableButton(int id, boolean isEnable) {
        ((Button) findViewById(id)).setEnabled(isEnable);
    }

    private void enableButtons(boolean isRecording) {
        enableButton(R.id.btnStart, !isRecording);
        enableButton(R.id.btnStop, isRecording);
    }

    private String getmAudioFilename() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, AUDIO_RECORDER_FOLDER);


        if (!file.exists()) {
            file.mkdirs();
        }

        mOriginalAdioFileName = System.currentTimeMillis() + "";
        return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + AUDIOFILE_TYPE);
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(output_formats[currentFormat]);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mAudioFilename = getmAudioFilename();
        recorder.setOutputFile(mAudioFilename);
        recorder.setOnErrorListener(errorListener);
        recorder.setOnInfoListener(infoListener);
        try {
            recorder.prepare();
            recorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        if (null != recorder) {
            recorder.stop();
            recorder.reset();
            recorder.release();
            recorder = null;
        }
    }


    private MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
        @Override
        public void onError(MediaRecorder mr, int what, int extra) {
        }
    };

    private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
        @Override
        public void onInfo(MediaRecorder mr, int what, int extra) {
        }
    };

    public void startRecord() {

        Toast.makeText(AddExerciseActvitiy.this, getString(R.string.start_recording), Toast.LENGTH_SHORT).show();
        enableButtons(true);
        startRecording();

    }

    public void stopRecord() {

        Toast.makeText(AddExerciseActvitiy.this, getString(R.string.stop_recording), Toast.LENGTH_SHORT).show();
        enableButtons(false);
        stopRecording();
    }

    public void audioPlayer() {
        MediaPlayer mp = new MediaPlayer();
        try {
            mp.setDataSource(mAudioFilename);
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mp.start();

    }


}

