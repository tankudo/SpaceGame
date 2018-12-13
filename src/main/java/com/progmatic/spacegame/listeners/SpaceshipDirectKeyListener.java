/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.spacegame.listeners;

import com.progmatic.spacegame.MainGameFrame;
import com.progmatic.spacegame.components.Spaceship;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;
import javax.swing.Timer;

/**
 *
 * @author peti
 */
public class SpaceshipDirectKeyListener implements KeyListener {

    private final Spaceship spaceship;
    private int move = 20;
    private final Set<Integer> pressedKeys = new HashSet<>();
    private Dimension mainFrameDimensions;
    private final MainGameFrame gameFrame;

    private boolean goAround = false;

    Timer spMoveTimer;

    public SpaceshipDirectKeyListener(Spaceship spaceship, Dimension mDimension, MainGameFrame mainGameFrame) {
        this.spaceship = spaceship;
        this.mainFrameDimensions = mDimension;
        spMoveTimer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Integer pressedKey : pressedKeys) {
                    moveByKey(pressedKey);
                }
            }
        });
        this.gameFrame = mainGameFrame;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    private void moveByKey(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
                move(0, -1 * move);
                break;
            case KeyEvent.VK_DOWN:
                move(0, move);
                break;
            case KeyEvent.VK_LEFT:
                move(-1 * move, 0);
                break;
            case KeyEvent.VK_RIGHT:
                move(move, 0);
                break;
            default:
                break;
        }
    }

    private void move(int x, int y) {
        Rectangle speaceShipBounds = spaceship.getBounds();
        Point p;
        if (goAround) {
            p = getPositionWhenGoesAround(speaceShipBounds, x, y);
        } else {
            p = getPosition(speaceShipBounds, x, y);
        }
        spaceship.setBounds(p.x, p.y, speaceShipBounds.width, speaceShipBounds.height);
    }

    private Point getPositionWhenGoesAround(Rectangle speaceShipBounds, int x, int y) {
        int newX = speaceShipBounds.x + x;
        if (newX > mainFrameDimensions.width) {
            newX = 0 - speaceShipBounds.width;
        } else if (newX < 0 - speaceShipBounds.width) {
            newX = mainFrameDimensions.width;
        }
        int newY = speaceShipBounds.y + y;
        if (newY > mainFrameDimensions.height) {
            newY = 0 - speaceShipBounds.height;
        } else if (newY < 0 - speaceShipBounds.height) {
            newY = mainFrameDimensions.height;
        }
        return new Point(newX, newY);
    }

    private Point getPosition(Rectangle speaceShipBounds, int x, int y) {
        int newX = speaceShipBounds.x + x;
        newX = Math.min(newX, mainFrameDimensions.width - speaceShipBounds.width);
        newX = Math.max(0, newX);
        int newY = speaceShipBounds.y + y;
        newY = Math.min(newY, mainFrameDimensions.height - speaceShipBounds.height);
        newY = Math.max(newY, 0);
        return new Point(newX, newY);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.isActionKey()) {
            gameFrame.addPlanetsIfNeeded();
            int keyCode = e.getKeyCode();
            pressedKeys.add(keyCode);
            if (!spMoveTimer.isRunning()) {
                spMoveTimer.start();
            }

        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.isActionKey()) {
            int keyCode = e.getKeyCode();
            pressedKeys.remove(keyCode);
        }
        if (pressedKeys.isEmpty()) {
            spMoveTimer.stop();
        }
    }

    public Dimension getMainFrameDimensions() {
        return mainFrameDimensions;
    }

    public void setMainFrameDimensions(Dimension mainFrameDimensions) {
        this.mainFrameDimensions = mainFrameDimensions;
    }

}
