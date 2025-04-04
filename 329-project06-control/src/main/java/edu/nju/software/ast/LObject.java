package edu.nju.software.ast;

public abstract class LObject {
    public Boolean toBoolean() {
        if (this instanceof LInteger) {
            return ((LInteger) this).value != 0;
        }
        if (this instanceof LList) {
            return !((LList) this).content.isEmpty();
        }
        return true;
    }
}