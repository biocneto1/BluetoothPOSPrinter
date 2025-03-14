package com.example.bluetoothposprinter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import fi.iki.elonen.NanoHTTPD;
import java.io.IOException;

public class PrinterService extends Service {
    private BluetoothPrinterManager printerManager;
    private HttpServer server;
    private static final int PORT = 40250;

    @Override
    public void onCreate() {
        super.onCreate();
        printerManager = new BluetoothPrinterManager();
        printerManager.connect("00:11:22:33:44:55"); // Cambiar por la dirección real
        server = new HttpServer();
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        server.stop();
        printerManager.disconnect();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class HttpServer extends NanoHTTPD {
        public HttpServer() {
            super(PORT);
        }

        @Override
        public Response serve(IHTTPSession session) {
            if (session.getMethod() == Method.POST) {
                try {
                    session.parseBody(null);
                    String cadenaImprimir = session.getParms().get("cadenaImprimir");
                    
                    if (cadenaImprimir == null) {
                        return newFixedLengthResponse(Response.Status.BAD_REQUEST, MIME_PLAINTEXT, 
                            "Parámetro 'cadenaImprimir' requerido");
                    }

                    if (!printerManager.isConnected()) {
                        return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, 
                            "No hay conexión con la impresora");
                    }

                    boolean success = printerManager.print(cadenaImprimir);
                    String status = success ? "Impresión exitosa" : "Error al imprimir";
                    return newFixedLengthResponse(status);
                } catch (Exception e) {
                    return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, 
                        "Error: " + e.getMessage());
                }
            }
            return newFixedLengthResponse(Response.Status.METHOD_NOT_ALLOWED, MIME_PLAINTEXT, 
                "Solo se aceptan peticiones POST");
        }
    }
}
