package com.elbejaj.domoapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.UUID;

import static java.lang.System.out;

public class MainActivity extends AppCompatActivity {


    Button boutonOuvrir ;
    Button boutonFermer ;
    Button boutonStopper ;
    Button boutonReprendre;
    Button boutonConnection ;
    EditText editMacAdress;
    String adresseMac;
    String ourMac;
    BluetoothAdapter adapterBluetooth;
    BluetoothSocket socket;
    String SPP_UUID;
    DataInputStream in ;
    DataOutputStream out ;
    Boolean connecter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boutonOuvrir = (Button) findViewById(R.id.bouton_ouvrir);
        boutonFermer = (Button) findViewById(R.id.bouton_fermer);
        boutonStopper = (Button) findViewById(R.id.bouton_stopper);
        boutonReprendre = (Button) findViewById(R.id.bouton_reprendre);
        boutonConnection = (Button) findViewById(R.id.bouton_connection);
        editMacAdress = (EditText) findViewById(R.id.MacAdresse);
        adapterBluetooth = BluetoothAdapter.getDefaultAdapter();
        SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";

        boutonConnection.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setMacAdress(editMacAdress.getText().toString());
                initialisationBluetooth(adapterBluetooth);
                try {
                    connectionEV3(getAdresseMac());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

        boutonOuvrir.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        try {
                            envoyerAction((byte) 1);
                            return true;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return false;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
                return false;
            }
        });

    }

    public void setMacAdress(String adresse){
        adresseMac = adresse;
    }

    public String getAdresseMac(){
     return adresseMac;
    }

    public void initialisationBluetooth(BluetoothAdapter adaptateur) {
        adaptateur.isEnabled();
    }

    public  boolean connectionEV3(String macAdd) throws InterruptedException, IOException, NoSuchFieldException, IllegalAccessException  {


            BluetoothDevice ev3 = adapterBluetooth.getRemoteDevice(getAdresseMac());
            socket = ev3.createRfcommSocketToServiceRecord(UUID.fromString(SPP_UUID));
            socket.connect();
            Field mServiceField = adapterBluetooth.getClass().getDeclaredField("mService");
            mServiceField.setAccessible(true);

            Object btManagerService = mServiceField.get(adapterBluetooth);



            out=new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            out.flush();
            Thread.sleep(1000);
            connecter = true;


            return connecter;
    }

    public void envoyerAction(byte msg) throws InterruptedException, IOException {
        //TTE
                out.write(msg);
                out.flush();
                Thread.sleep(1000);
    }



}
