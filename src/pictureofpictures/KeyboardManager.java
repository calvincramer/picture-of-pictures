package pictureofpictures;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;

public class KeyboardManager 
    implements KeyEventDispatcher {
    
    protected boolean shiftKeyDown = false;
    
    @Override public boolean dispatchKeyEvent(KeyEvent e) {
        
        shiftKeyDown = e.isShiftDown();
        
        return true;    //consume action 
    }
    
}
