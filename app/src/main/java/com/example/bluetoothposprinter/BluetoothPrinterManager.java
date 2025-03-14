package com.example.bluetoothposprinter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothPrinterManager {
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private OutputStream outputStream;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public BluetoothPrinterManager() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public boolean connect(String deviceAddress) {
        try {
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
            bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            bluetoothSocket.connect();
            outputStream = bluetoothSocket.getOutputStream();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean print(String text) {
        try {
            if (outputStream != null) {
                outputStream.write(text.getBytes());
                byte[] cutPaper = new byte[]{0x1D, 0x56, 0x00};
                outputStream.write(cutPaper);
                outputStream.flush();
                return true;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isConnected() {
        return bluetoothSocket != null && bluetoothSocket.isConnected();
    }

    public void disconnect() {
        try {
            if (outputStream != null) outputStream.close();
            if (bluetoothSocket != null) bluetoothSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
