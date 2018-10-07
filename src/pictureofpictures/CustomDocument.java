package pictureofpictures;

import java.awt.Toolkit;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class CustomDocument
    extends PlainDocument {
    
    private boolean hasCharLimit;
    private int charLimit;
    
    private int documentType;
    
    public static final int TYPE_ONLY_DIGITS = 1;
    public static final int TYPE_ALL_CHARS = 2;
    
    
    public CustomDocument() {
        this.hasCharLimit = false;
        this.documentType = CustomDocument.TYPE_ALL_CHARS;
    }
    
    public CustomDocument(int charLimit, int documentType) {
        this.hasCharLimit = true;
        this.charLimit = charLimit;
        if (this.charLimit < 1)
            this.charLimit = 1;
        
        this.documentType = documentType;
        if (this.documentType < 1 || this.documentType > 2)
            this.documentType = 2;
    }
    
    @Override public void insertString( int offset, String str, AttributeSet attr ) throws BadLocationException {
        if (str == null) return;

        //check for non numbers
        if (this.documentType == CustomDocument.TYPE_ONLY_DIGITS) {
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if ( ! Character.isDigit(c)) {
                    Toolkit.getDefaultToolkit().beep();
                    return;
                }
            }
        }

        if (this.hasCharLimit) {
            if ((getLength() + str.length()) <= this.charLimit) {
                super.insertString(offset, str, attr);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }
                
    }
}
