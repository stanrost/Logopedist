package com.example.strost.logopedist.controller.activities;

import android.Manifest;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
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
import com.example.strost.logopedist.model.request.GetCaregiverRequest;
import com.example.strost.logopedist.model.request.UpdateCaregiverRequest;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AddExerciseActvitiy extends AppCompatActivity {
    private List<Patient> patients = new ArrayList<Patient>();
    private Patient patient;
    private List<Caregiver> caregivers = new ArrayList<Caregiver>();
    private int caregiverId;
    private Caregiver caregiver, newCaregiver;
    EditText title;
    // take picture
    private Button takePictureButton;
    private ImageView imageView;
    private Uri file = null;
    private Boolean help = false;
    // voice recoder
    private MediaRecorder recorder = null;
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    private boolean permissionToRecordAccepted = false;
    private boolean permissionToWriteAccepted = false;
    String recordedFileName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addexercise_page);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        final int patientId = getIntent().getExtras().getInt("id");
        caregiverId = getIntent().getExtras().getInt("caregiverId");

        Button play  = (Button) findViewById(R.id.play);
        title = (EditText) findViewById(R.id.exerciseTitle);
        // help switch
        final Switch helpSwitch = (Switch) findViewById(R.id.help_switch);
        // description
        final EditText description = (EditText) findViewById(R.id.description_text);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        // take picture
        takePictureButton = (Button) findViewById(R.id.button_image);
        imageView = (ImageView) findViewById(R.id.imageview);

        getZorgverlender();

        for (int i = 0; i < caregivers.size(); i++) {
            if (caregiverId == caregivers.get(i).getId()) {
                caregiver = caregivers.get(i);
            }
        }

        newCaregiver = caregiver;
        patients = newCaregiver.getPatients();

        for (int i = 0; i < patients.size(); i++) {
            if (patientId == patients.get(i).getId()) {
                patient = patients.get(i);
            }
        }

        int maxId = 0;
        for (int i = 0; i < patient.getExercises().size(); i++)
        {
            if (patient.getExercises().get(i).getId() > maxId)
            {
                maxId = patient.getExercises().get(i).getId();
            }
        }
        final int newId = maxId + 1;


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                help = helpSwitch.isChecked();
                if (file != null) {
                    patient.addExercise(newId, title.getText().toString(), "https://develop.backendless.com/console/E5A95319-DFEE-9344-FF32-50448355EC00/appversion/F6785B8A-A5DD-8B45-FF46-80B5C2177500/jgdkmiwssukvghdidcejkhfufsdlgldaxbhi/files/view/media/" + title.getText().toString().replace(" ", "") + ".png", help, description.getText().toString());
                }
                else {
                    patient.addExercise(newId, title.getText().toString(), null, help, description.getText().toString());
                }
                Toast.makeText(AddExerciseActvitiy.this, getString(R.string.added_exercise), Toast.LENGTH_LONG).show();
                try {
                    addToFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(patient.getDevices().size() != 0){
                    sendPushNotification();
                }

                goBack();
            }
        });


        //take picture
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            takePictureButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 0);
        }



    }


    public void sendPushNotification(){

        for (Device d : patient.getDevices()){
            final DeliveryOptions deliveryOptions = new DeliveryOptions();
            deliveryOptions.addPushSinglecast(d.getDeviceId());

            final PublishOptions publishOptions = new PublishOptions();
            publishOptions.putHeader( "android-ticker-text", "You just got a private push notification!" );
            publishOptions.putHeader( "android-content-title", "Je hebt een nieuwe opdracht" );
            publishOptions.putHeader( "android-content-text", "De opdracht: " +title.getText().toString() + " is toegevoegd" );


            Runnable runnable = new Runnable() {
                public void run() {
                    try {
                        MessageStatus status = Backendless.Messaging.publish( "this is a private message!", publishOptions, deliveryOptions );  } catch (Exception e) {
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

    }


    public void addToFile() throws IOException {

        if (file != null) {
            Bitmap mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), file);

            //rotate
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_action_name)
                            .setContentTitle("Foto uploaden...")
                            .setContentText("De notificatie verdwijnt als de foto is geupload");


            final NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.notify(001, mBuilder.build());

            final AddPictureRequest apr = new AddPictureRequest();
            final Bitmap finalMBitmap = mBitmap;
            Runnable runnable = new Runnable() {
                public void run() {
                    try {
                        apr.AddPicture(finalMBitmap, "https://develop.backendless.com/console/E5A95319-DFEE-9344-FF32-50448355EC00/appversion/F6785B8A-A5DD-8B45-FF46-80B5C2177500/jgdkmiwssukvghdidcejkhfufsdlgldaxbhi/files/view/media" + title.getText().toString().replace(" ", ""), title.getText().toString().toString().replace(" ", ""), mNotifyMgr);
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
        final UpdateCaregiverRequest uzr = new UpdateCaregiverRequest();
        Runnable runnable2 = new Runnable() {
            public void run() {
                uzr.updateCaregiver(caregiver, newCaregiver);
            }
        };
        Thread mythread2 = new Thread(runnable2);
        mythread2.start();
        try {
            mythread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void getZorgverlender() {
        final GetCaregiverRequest gzr = new GetCaregiverRequest();
        Runnable runnable = new Runnable() {
            public void run() {
                caregivers = gzr.getCaregiver();
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


    public void goBack() {
        Intent detailIntent = new Intent(this, PatientActivity.class);
        detailIntent.putExtra("patientid", patient.getId());
        detailIntent.putExtra("caregiverId", caregiverId);
        startActivity(detailIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        goBack();
        return;
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
                imageView.setRotation(imageView.getRotation()+90);
            }
        }
    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }

}

