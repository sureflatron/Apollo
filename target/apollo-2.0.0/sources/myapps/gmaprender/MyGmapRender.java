package myapps.gmaprender;

import org.primefaces.component.gmap.GMapRenderer;

import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class MyGmapRender extends GMapRenderer {

    @Override
    protected void startScript(ResponseWriter writer, String clientId) throws IOException {
        super.startScript(writer, clientId);
        writer.write("if(window.google)");
    }

}