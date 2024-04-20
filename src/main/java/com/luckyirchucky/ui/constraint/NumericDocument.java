package com.luckyirchucky.ui.constraint;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class NumericDocument extends PlainDocument {
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if (str == null) {
            return;
        }
        if (str.matches("\\d")) {
            super.insertString(offs, str, a);
        }
    }
}
