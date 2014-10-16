package pl.zz.vx;

import java.io.File;
import java.io.FileWriter;
import pl.zz.Server.ServerStarter;
import java.io.IOException;
import java.net.Socket;
import javax.swing.text.StyledDocument;
import org.netbeans.modules.editor.NbEditorUtilities;
import org.openide.cookies.EditorCookie;
import org.openide.cookies.OpenCookie;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.text.NbDocument;
import org.openide.util.Exceptions;
import org.openide.util.lookup.ServiceProvider;
import org.openide.windows.TopComponent;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.PlatformLocator;
import org.vertx.java.platform.PlatformManager;
import org.vertx.java.spi.cluster.impl.hazelcast.ProgrammableClusterManagerFactory;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author zulk
 */
@ServiceProvider(service = ServerStarter.class, position = 0)
public class VxServerStarter implements ServerStarter {

    private EventBus eb;
    private Vertx v;
    private int port;
    private PlatformManager pm;
    private boolean starterd = false;

    public VxServerStarter() {
    }

    private static boolean availablePort(int port, String host) {
        try (Socket ignored = new Socket(host, port)) {
            return false;
        } catch (IOException ignored) {
            return true;
        }
    }

    @Override
    public void start() {
        System.setProperty("vertx.clusterManagerFactory", ProgrammableClusterManagerFactory.class.getCanonicalName());

        port = 5555;
        port = findPort(port);
        pm = PlatformLocator.factory.createPlatformManager(port, "0.0.0.0");
        v = pm.vertx();
        Handler<Message<String>> m = new HandlerImpl();

        eb = v.eventBus();
        eb.registerHandler("pl.message", m);
        starterd = true;
        System.out.println("Started " + v.toString());
    }

    @Override
    public void stop() {
        pm.stop();
        starterd = false;
    }

    @Override
    public boolean isStarted() {
        return starterd;
    }

    public void listMembers() {
    }

    @Override
    public void send(String messageType, JsonObject message) {
        eb.publish(messageType, message);
    }

    private int findPort(int port) {
        while (!availablePort(port,
                "0.0.0.0")) {
            port++;
        }
        return port;
    }

    private static class HandlerImpl implements Handler<Message<String>> {

        public HandlerImpl() {
        }

        @Override
        public void handle(Message e) {
            System.out.println("MESSAGE RECEIVED " + e.toString());

            JsonObject json = (JsonObject) e.body();
            
            try {
                File f = File.createTempFile("Review", "." + json.getString("ext"));
                try (FileWriter fw = new FileWriter(f)) {
                    fw.write(json.getString("text"));
                }

                DataObject dataO = DataObject.find(FileUtil.createData(f));

                OpenCookie openCookie = dataO.getLookup().lookup(OpenCookie.class);
                openCookie.open();

            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }

        }
    }

}
