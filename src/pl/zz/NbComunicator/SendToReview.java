/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.zz.NbComunicator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.text.StyledDocument;
import org.netbeans.modules.editor.NbEditorUtilities;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.cookies.EditorCookie;
import org.openide.loaders.DataObject;
import org.openide.text.NbDocument;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;
import org.vertx.java.core.json.JsonObject;
import pl.zz.Server.ServerStarter;

@ActionID(
        category = "Bugtracking",
        id = "pl.zz.NbComunicator.SendToReview"
)
@ActionRegistration(
        displayName = "#CTL_SendToReview"
)
@ActionReference(path = "Menu/File", position = -100)
@Messages("CTL_SendToReview=Send To review")
public final class SendToReview implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO implement action body

        StyledDocument document = NbDocument.getDocument(TopComponent.getRegistry().getActivated());
        DataObject dobj = NbEditorUtilities.getDataObject(document);
        EditorCookie editor = dobj.getLookup().lookup(EditorCookie.class);
        String mimeType = NbEditorUtilities.getMimeType(document);
        String ext = dobj.getPrimaryFile().getExt();

        String selectedText = editor.getOpenedPanes()[0].getSelectedText();

        JsonObject jo = new JsonObject();
        jo.putString("ext", ext);
        jo.putString("text", selectedText);
        
        ServerStarter serverStarter = Lookup.getDefault().lookup(ServerStarter.class);
        
        serverStarter.send("pl.message", jo);
        
    }
}
