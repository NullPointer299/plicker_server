package main;

import javax.microedition.io.StreamConnection;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Session implements Runnable {
    private StreamConnection channel;
    private InputStream btIn;
    private OutputStream btOut;
    private final ObjectOutputStream objOut;
    private Robot robot;
    private static ExecutorService exe = Executors.newSingleThreadExecutor();

    public Session(StreamConnection channel) throws IOException, AWTException {
        this.channel = channel;
        btIn = channel.openInputStream();
        btOut = channel.openOutputStream();
        objOut = new ObjectOutputStream(btOut);
        robot = new Robot();
    }

    @Override
    public void run() {
        try {
            byte[] buff = new byte[1];
            int n;
            while ((n = btIn.read(buff)) > 0) {
                String data = new String(buff, 0, n);
                Main.log("Receive:" + data);
                if (data.equals("g")) {
                    exe.submit(() -> {
                        try {
                            Thread.sleep(100);
                            byte[] cap = Capture.getScreenCapture(0.1f);
                            objOut.writeObject(cap);
                            objOut.flush();
                        } catch (IOException | AWTException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                } else {
                    activateKey(data);
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            close();
        }
    }

    private void activateKey(final String data) {
        switch (data) {
            case "r":
                pressKey(KeyEvent.VK_RIGHT);
                break;
            case "l":
                pressKey(KeyEvent.VK_LEFT);
                break;
        }
    }

    private void pressKey(final int key) {
        robot.keyPress(key);
        robot.keyRelease(key);
    }

    public void close() {
        Main.log("Session Close");
        if (btIn != null) try {
            btIn.close();
        } catch (Exception e) {/*ignore*/}
        if (btOut != null) try {
            btOut.close();
        } catch (Exception e) {/*ignore*/}
        if (channel != null) try {
            channel.close();
        } catch (Exception e) {/*ignore*/}
    }
}

