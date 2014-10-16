/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.zz.NbComunicator;

import pl.zz.Server.ServerStarter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import pl.zz.vx.VxServerStarter;

@ActionID(
        category = "Bugtracking",
        id = "pl.zz.NbComunicator.StartColaboration"
)
@ActionRegistration(
        displayName = "#CTL_StartColaboration"
)
@ActionReference(path = "Menu/File", position = 0)
@Messages("CTL_StartColaboration=Start Colaboration")
public final class StartColaboration implements ActionListener {


    @Override
    public void actionPerformed(ActionEvent e) {
        
        ServerStarter serverStarter = Lookup.getDefault().lookup(ServerStarter.class);
        
        if (serverStarter.isStarted()) {
            serverStarter.stop();
        } else {
            serverStarter.start();
        }
    }
}
