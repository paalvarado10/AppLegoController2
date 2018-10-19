package com.proyectogrado.pabloalvarado.legocontroller;

import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    WebView cameraView;
    SensorManager sensorManager;
    Sensor sensor;
    SensorEventListener sensorEventListener;
    ImageView img, orien;
    String orientacion;
    TextView orientacionV;
    private final static long ACC_CHECK_INTERVAL = 4000;
    private long lastAccCheck;
    public static int DEV_DELAY=10000000;
    float lastx=0;
    float lasty=0;
    float lastz=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lastAccCheck = System.currentTimeMillis();
        setContentView(R.layout.activity_main);
        orientacion="A";
        orientacionV = findViewById(R.id.textView3);
        cameraView= findViewById(R.id.cameraView);
        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        cameraView.loadUrl("https://www.youtube.com");
        cameraView.getSettings().setJavaScriptEnabled(true);
        cameraView.setWebViewClient(new WebViewClient());
        cameraView.setWebChromeClient(new WebChromeClient());
        img= (ImageView) findViewById(R.id.image_dir);
        orien= (ImageView) findViewById(R.id.image_orientation);
        orien.setImageResource(R.drawable.arriba);
        if(sensor==null)
        {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Lo siento, el dispositivo debe contar con acelerometro",
                    Toast.LENGTH_SHORT);
            toast.show();
            finish();
        }
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event)
            {
                long currTime = System.currentTimeMillis();
                float x= event.values[0];
                float y= event.values[1];
                float z= event.values[2];
                if(currTime - lastAccCheck >= ACC_CHECK_INTERVAL) {
                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    if(Math.abs(z)>Math.abs(y) && lastz!=z)
                    {
                        if(z>0)
                        {
                            img.setImageResource(R.drawable.arriba);
                        }
                        else {
                            img.setImageResource(R.drawable.abajo);
                        }
                    }
                    else if (lasty!=y)
                    {
                        if(y>0)
                        {
                            img.setImageResource(R.drawable.izquierda);
                            changeOrientation("I");
                        }
                        else{
                            img.setImageResource(R.drawable.derecha);
                            changeOrientation("D");
                        }
                    }
                } else {
                    if(Math.abs(z)>Math.abs(x) && lastz!=z)
                    {
                        if(z>0)
                        {
                            img.setImageResource(R.drawable.arriba);
                        }
                        else {
                            img.setImageResource(R.drawable.abajo);
                        }
                    }
                    else if (lastx!=x)
                    {
                        if(x>0)
                        {
                            img.setImageResource(R.drawable.izquierda);
                            changeOrientation("I");
                        }
                        else{
                            img.setImageResource(R.drawable.derecha);
                            changeOrientation("D");
                        }
                    }
                }
                lastx=x;
                lasty=y;
                lastz=z;
                System.out.println(" Valores en X: "+x+", Valores en Y: "+y+", Valores en Z: "+ z);
                lastAccCheck = currTime;
            }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        start();
    }
    private void changeOrientation(String orientation)
    {
        String newOrien = "";
        if(orientacion.equals("A"))
        {
            if(orientation.equals("D"))
            {
                newOrien=orientation;
                orien.setImageResource(R.drawable.derecha);
            }
            else if(orientation.equals("I"))
            {
                newOrien=orientation;
                orien.setImageResource(R.drawable.izquierda);
            }
        }
        else if(orientacion.equals("D"))
        {
            if(orientation.equals("D"))
            {
                newOrien="B";
                orien.setImageResource(R.drawable.abajo);
            }
            else if(orientation.equals("I"))
            {
                newOrien="A";
                orien.setImageResource(R.drawable.arriba);
            }
        }
        else if(orientacion.equals("I"))
        {
            if(orientation.equals("D"))
            {
                newOrien="A";
                orien.setImageResource(R.drawable.arriba);
            }
            else if(orientation.equals("I"))
            {
                newOrien="B";
                orien.setImageResource(R.drawable.abajo);
            }
        }
        else if(orientacion.equals("B"))
        {
            if(orientation.equals("D"))
            {
                newOrien="I";
                orien.setImageResource(R.drawable.izquierda);
            }
            else if(orientation.equals("I"))
            {
                newOrien="D";
                orien.setImageResource(R.drawable.derecha);
            }
        }
        orientacion=newOrien;
        orientacionV.setText(orientacion);
        Toast.makeText(this, "Cambio de orientacion", Toast.LENGTH_SHORT).show();
    }
    private void start(){
        sensorManager.registerListener(sensorEventListener, sensor, DEV_DELAY);
    }
    private void stop(){
        sensorManager.unregisterListener(sensorEventListener);
    }
    @Override
    protected void onPause(){
        stop();
        super.onPause();
    }
    @Override
    protected void onResume(){
        start();
        super.onResume();
    }
}
