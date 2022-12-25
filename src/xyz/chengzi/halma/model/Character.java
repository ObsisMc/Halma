package xyz.chengzi.halma.model;

import xyz.chengzi.halma.listener.CharacterListener;

import javax.swing.*;

public class Character extends JLabel implements CharacterListener {
    private boolean isConfirmed;

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    @Override
    public void clickCharacterPanel() {
    }
}