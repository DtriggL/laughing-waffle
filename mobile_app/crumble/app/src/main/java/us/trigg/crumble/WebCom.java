package us.trigg.crumble;

import java.util.ArrayList;

import us.trigg.crumble.interfaces.WebComHandler;

/**
 * Created by trigglatour on 4/27/16.
 */
public class WebCom {
    private WebComHandler webComHandler;
    public WebCom(WebComHandler handler) {
        webComHandler = handler;
    }

    public void getOwnedCrumbs(int user_id) {
        // Make an Async task to handle the request
        // Get result and post to parent with
        // webComHandler.onOwnCrumbReturned(ArrayList)
    }
}
