package main;

import javax.bluetooth.LocalDevice;
import javax.bluetooth.ServiceRecord;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.awt.*;
import java.io.IOException;
import java.util.Date;

public class Main {
    private static final String serverUUID = "29118080377548490730630927488271";

    private StreamConnectionNotifier server;

    public Main() throws IOException {
        // RFCOMMベースのサーバの開始。
        // - btspp:は PRCOMM 用なのでベースプロトコルによって変わる。
        server = (StreamConnectionNotifier) Connector.open(
                "btspp://localhost:" + serverUUID,
                Connector.READ_WRITE, true
        );
        // ローカルデバイスにサービスを登録。必須ではない。
        ServiceRecord record = LocalDevice.getLocalDevice().getRecord(server);
        LocalDevice.getLocalDevice().updateRecord(record);
    }

    /**
     * クライアントからの接続待ち。
     *
     * @return 接続されたたセッションを返す。
     */
    public Session accept() throws IOException, AWTException {
        log("Accept");
        StreamConnection channel = server.acceptAndOpen();
        log("Connect");
        return new Session(channel);
    }

    public void dispose() throws IOException {
        log("Dispose");
        if (server != null) {
            server.close();
        }
    }

    public static void main(String[] args) {
        try {
            Main server = new Main();
            while (true) {
                Session session = server.accept();
                new Thread(session).start();
            }
            //server.dispose();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    static void log(String msg) {
        System.out.println("[" + (new Date()) + "] " + msg);
    }
}
