package com.moheeeetgupta.ladybuddy;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static java.lang.Math.sqrt;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Magnetometer extends AppCompatActivity implements SensorEventListener {


    private TextView magR, show_conditions, x_cor, y_cor, z_cor ;
    SpeedometerView Speed;

    MediaPlayer mediaPlayer;


    private double magD;

    private Sensor magnetometer ;

    private SensorManager sensorManager;
    Boolean flag = false;
    double prevx = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magnetometer);


        Speed = (SpeedometerView)findViewById(R.id.speedometer);
        Speed.setLabelConverter(new SpeedometerView.LabelConverter() {
            @Override
            public String getLabelFor(double progress, double maxProgress) {
                return String.valueOf((int) Math.round(progress));
            }
        });

        // configure value range and ticks
        Speed.setMaxSpeed(100);
        Speed.setMajorTickStep(10);
        Speed.setMinorTicks(0);

        // Configure value range colors
        Speed.addColoredRange(0, 10, Color.GREEN);
        Speed.addColoredRange(10, 20, Color.YELLOW);
        Speed.addColoredRange(20, 30, Color.RED);
        Speed.addColoredRange(30, 40, Color.GREEN);
        Speed.addColoredRange(40, 50, Color.YELLOW);
        Speed.addColoredRange(50, 60, Color.RED);
        Speed.addColoredRange(60, 70, Color.GREEN);
        Speed.addColoredRange(70, 80, Color.YELLOW);
        Speed.addColoredRange(80, 90, Color.RED);
        Speed.addColoredRange(90, 100, Color.GREEN);



        magR = (TextView) findViewById(R.id.value);
        show_conditions = findViewById( R.id.show_conditions );
        x_cor = findViewById( R.id.x_cor );
        y_cor = findViewById( R.id.y_cor );
        z_cor = findViewById( R.id.z_cor );
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);



        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (magnetometer != null){
            sensorManager.registerListener(Magnetometer.this,magnetometer,SensorManager.SENSOR_DELAY_NORMAL);
        }else {
            magR.setText("Magnetometer not Supported");
        }


    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        Sensor sensor = event.sensor;


        if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            double x;
            x = sqrt (event.values[0]*event.values[0]+event.values[1]*event.values[1]+event.values[2]*event.values[2]);


            BigDecimal bd = new BigDecimal(x).setScale(0, RoundingMode.HALF_UP);
            double newx = bd.doubleValue();



            if(newx>0 && newx<100){
                Speed.setSpeed(newx, 1, 1);

            }else if(newx>=100){
                Speed.setSpeed (100,1,1);
            }
            magR.setText(Double.toString(newx) + " μT");
            //x_cor.setText(String.format( "%0f", event.values[0]) );

            BigDecimal bdx = new BigDecimal(event.values[0]).setScale(0, RoundingMode.HALF_UP);
            double new_x = bdx.doubleValue();
            x_cor.setText(Double.toString(new_x));
            x_cor.setBackgroundColor( Color.GREEN );

            BigDecimal bdy = new BigDecimal(event.values[1]).setScale(0, RoundingMode.HALF_UP);
            double new_y = bdy.doubleValue();
            y_cor.setText(Double.toString(new_y));
            y_cor.setBackgroundColor( Color.RED );


            BigDecimal bdz = new BigDecimal(event.values[2]).setScale(0, RoundingMode.HALF_UP);
            double new_z = bdz.doubleValue();
            z_cor.setText(Double.toString(new_z));
            z_cor.setBackgroundColor( Color.YELLOW );


            double Pd=70d;
            double Fd=90d;
            if(Double.compare (x,Pd)>0 && Double.compare (x,Fd)<0) {
                mediaPlayer=null;
                mediaPlayer = MediaPlayer.create(this, R.raw.beep);
                mediaPlayer.start();
                show_conditions.setText( "Potential electronic device detected" );

            }
            else if(Double.compare (x,Fd)>0){
                mediaPlayer=null;
                mediaPlayer = MediaPlayer.create(this, R.raw.beepd);
                mediaPlayer.start();
                show_conditions.setText( "Finally electronic device detected." );

            }else {
                mediaPlayer = null;
                show_conditions.setText ("No Potential electronic device detected");
            }



        }



    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}