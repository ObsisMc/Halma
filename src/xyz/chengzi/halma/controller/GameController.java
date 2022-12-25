package xyz.chengzi.halma.controller;

import xyz.chengzi.halma.listener.InputListener;
import xyz.chengzi.halma.model.*;
import xyz.chengzi.halma.model.Character;
import xyz.chengzi.halma.view.*;
import xyz.chengzi.halma.Frame.EmptyFrame;
import xyz.chengzi.halma.view.Player;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Random;

public class GameController implements InputListener {
    private ChessBoardComponent view;
    private ChessBoard model;
    private int playernumber;
    private ChessBoardLocation selectedLocation;
    private Color currentPlayer;
    private GameFrame gameFrame;
    private ChooseCharacter chooseCharacter;
    private int totalturn = 1;
    //    private int totalturnbefore=0;
    GameController self = this;
    int time = 0;
    boolean over = false;
    int winner = 0;
    boolean isvalid = false;
    Player player;

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    //用来判断存档是否有效的临时controller
    public GameController(ChooseCharacter chooseCharacter, ChessBoard chessBoard) {
        playernumber = chooseCharacter.getPlayernumber();
        model = chessBoard;
        this.chooseCharacter = chooseCharacter;
        if (playernumber == 2)
            isvalid = fileIsValid(String.format("save/twoplayer/%s.txt", chooseCharacter.getSavename()));
        else if (playernumber == 4)
            isvalid = fileIsValid(String.format("save/fourplayer/%s.txt", chooseCharacter.getSavename()));
    }

    //有效存档的加载
    public GameController(ChooseCharacter chooseCharacter, ChessBoardComponent chessBoardComponent, ChessBoard chessBoard) throws IOException {
        this.view = chessBoardComponent;
        this.model = chessBoard;
        this.chooseCharacter = chooseCharacter;
        this.playernumber = chooseCharacter.getPlayernumber();
        player = new Player();
        view.registerListener(this);
        model.registerListener(view);
        isvalid = true;

        if (chooseCharacter.getPlayernumber() == 2) {
            loadGameFromFile(String.format("save/twoplayer/%s.txt", chooseCharacter.getSavename()));
        } else if (chooseCharacter.getPlayernumber() == 4) {
            loadGameFromFile(String.format("save/fourplayer/%s.txt", chooseCharacter.getSavename()));
        }

    }

    //原版
    public GameController(ChessBoardComponent chessBoardComponent, ChessBoard chessBoard, ChooseCharacter chooseCharacter) {
        this.view = chessBoardComponent;
        this.model = chessBoard;
        this.chooseCharacter = chooseCharacter;
        this.playernumber = chooseCharacter.getPlayernumber();

        view.registerListener(this);
        model.registerListener(view);
        model.placeInitialPieces(playernumber);

        //开局随机先手
        if(playernumber==2){
            Random sente=new Random();
            int n=sente.nextInt(2);
            if(n==0) currentPlayer=((Player) chooseCharacter.getPlayer1()).getColor();
            else if(n==1) currentPlayer=((Player) chooseCharacter.getPlayer2()).getColor();
        }else if(playernumber==4){
            Random sente=new Random();
            int n=sente.nextInt(4);
            if(n==0) currentPlayer=((Player) chooseCharacter.getPlayer1()).getColor();
            else if(n==1) currentPlayer=((Player) chooseCharacter.getPlayer2()).getColor();
            else if(n==2) currentPlayer=((Player) chooseCharacter.getPlayer3()).getColor();
            else if(n==3) currentPlayer=((Player) chooseCharacter.getPlayer4()).getColor();
        }

    }

    public ChessBoardLocation getSelectedLocation() {
        return selectedLocation;
    }

    public void setSelectedLocation(ChessBoardLocation location) {
        this.selectedLocation = location;
    }

    public void resetSelectedLocation() {
        setSelectedLocation(null);
    }

    public boolean hasSelectedLocation() {
        return selectedLocation != null;
    }

    public Color nextPlayer() {
        Color color1 = ((Player) gameFrame.getChooseCharacter().getPlayer1()).getColor();
        Color color2 = ((Player) gameFrame.getChooseCharacter().getPlayer2()).getColor();

        if (playernumber == 4) {
            Color color3 = ((Player) gameFrame.getChooseCharacter().getPlayer3()).getColor();
            Color color4 = ((Player) gameFrame.getChooseCharacter().getPlayer4()).getColor();
            Player1 player1 = gameFrame.getChooseCharacter().getPlayer1();
            Player2 player2 = gameFrame.getChooseCharacter().getPlayer2();
            Player3 player3 = gameFrame.getChooseCharacter().getPlayer3();
            Player4 player4 = gameFrame.getChooseCharacter().getPlayer4();

            if (currentPlayer == color1) {
                if (!player2.isWin()) {
                    return currentPlayer = color2;
                } else if (!player3.isWin()) {
                    return currentPlayer = color3;
                } else if (!player4.isWin()) {
                    return currentPlayer = color4;
                }
            } else if (currentPlayer == color2) {
                if (!player3.isWin()) {
                    return currentPlayer = color3;
                } else if (!player4.isWin()) {
                    return currentPlayer = color4;
                } else if (!player1.isWin()) {
                    setTotalturn(getTotalturn() + 1);
                    return currentPlayer = color1;
                }
            } else if (currentPlayer == color3) {
                if (!player4.isWin()) return currentPlayer = color4;
                else if (!player1.isWin()) {
                    setTotalturn(getTotalturn() + 1);
                    return currentPlayer = color1;
                } else if (!player2.isWin()) {
                    if (player1.isWin()) {
                        setTotalturn(getTotalturn() + 1);
                    }
                    return currentPlayer = color2;
                }
            } else if (currentPlayer == color4) {
                if (!player1.isWin()) {
                    setTotalturn(getTotalturn() + 1);
                    return currentPlayer = color1;
                } else if (!player2.isWin()) {
                    if (player1.isWin()) {
                        setTotalturn(getTotalturn() + 1);
                    }
                    return currentPlayer = color2;
                } else if (!player3.isWin()) {
                    if (player2.isWin() && player1.isWin()) {
                        setTotalturn(getTotalturn() + 1);
                    }
                    return currentPlayer = color3;
                }
            }

        } else if (playernumber == 2) {
            if (currentPlayer == color1) return currentPlayer = color2;
            else if (currentPlayer == color2) {
//                totalturnbefore=totalturn;
                setTotalturn(getTotalturn() + 1);
                return currentPlayer = color1;
            }
        }
        return color1;
    }

    //当无效的棋子移动的提示
    public void warning(JComponent jc, int index) {
        Thread warn = new Thread(() -> {
            //把图片放进JFrame里
            JFrame warning = new EmptyFrame(300, 160);
            if (index == 0) {
                ImageIcon icon = new ImageIcon("image/warning4.png");
                Image image = icon.getImage();
                JLabel jl = new JLabel(icon);
                jl.setBounds(0, 0, 300, 160);
                warning.add(jl);
            } else if (index == 1) {
                ImageIcon icon = new ImageIcon("image/warning1.png");
                Image image = icon.getImage();
                JLabel jl = new JLabel(icon);
                jl.setBounds(0, 0, 300, 160);
                warning.add(jl);
            }else if(index==2){
                ImageIcon icon = new ImageIcon("image/warning2.png");
                Image image = icon.getImage();
                JLabel jl = new JLabel(icon);
                jl.setBounds(0, 0, 300, 160);
                warning.add(jl);
            }
            warning.setBackground(new Color(0, 0, 0, 0));
            warning.setLocationRelativeTo(jc);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            warning.dispose();
        });
        warn.start();
    }

    @Override//“拿着”棋子后再点一个位置
    public boolean onPlayerClickSquare(ChessBoardLocation location, SquareComponent component) {
        int winnerAfter = 0;
        player = new Player();
        getPlayer().setWin(false);

        if (!over) {

            if (hasSelectedLocation() && model.isValidMove(getSelectedLocation(), location)) {
                boolean valid = true;
                if (currentPlayer == gameFrame.getChooseCharacter().getPlayer1().getColor())
                    valid = gameFrame.getChooseCharacter().getPlayer1().inDestination(model, playernumber, getSelectedLocation(), location);
                else if (currentPlayer == gameFrame.getChooseCharacter().getPlayer2().getColor())
                    valid = gameFrame.getChooseCharacter().getPlayer2().inDestination(model, playernumber, getSelectedLocation(), location);
                else if (currentPlayer == gameFrame.getChooseCharacter().getPlayer3().getColor())
                    valid = gameFrame.getChooseCharacter().getPlayer3().inDestination(model, playernumber, getSelectedLocation(), location);
                else if (currentPlayer == gameFrame.getChooseCharacter().getPlayer4().getColor())
                    valid = gameFrame.getChooseCharacter().getPlayer4().inDestination(model, playernumber, getSelectedLocation(), location);

                if (!valid) {
                    warning(view, 0);
                    return false;
                }

                if (!view.getGridAt(location).getSelected()) {
                    for (int i = 0; i < model.getDimension(); i++) {
                        for (int j = 0; j < model.getDimension(); j++) {
                            ChessBoardLocation c = new ChessBoardLocation(i, j);
                            view.getGridAt(c).setSelected(false);
                            view.getGridAt(c).repaint();
                        }
                    }
                    view.getGridAt(location).setSelected(true);
                    view.getGridAt(location).repaint();
                } else {

//棋子移动动画
                    Point set = new Point(location.getRow() * 30, location.getColumn() * 30);
                    Point get = new Point(selectedLocation.getRow() * 30, selectedLocation.getColumn() * 30);
                    double setx = set.getX();
                    double sety = set.getY();
                    double getx = get.getX();
                    double gety = get.getY();

                    int inter = 20;
                    double distancex = (setx - getx);
                    double distancey = (sety - gety);
                    double partionx = distancex / inter;
                    double partiony = distancey / inter;

                    JLabel c = new JLabel();
                    String image = null;
                    ImageIcon i = new ImageIcon(toPicture(currentPlayer));
                    c.setIcon(i);

                    Thread move = new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            for (int i = 0; i < inter; i++) {
                                view.add(c, new Integer(5));
                                c.setBounds((int) ((get.getX() + partionx * i)), (int) ((get.getY() + partiony * i)), 30, 30);
                                view.repaint();
                                try {
                                    Thread.sleep(70 / (inter / 10));
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                view.remove(c);
                                view.repaint();
                            }
                        }
                    };
                    move.start();

                    //原版代码
                    model.moveChessPiece(selectedLocation, location);
                    resetSelectedLocation();
                    nextPlayer();

                    for (int k = 0; k < model.getDimension(); k++) {
                        for (int l = 0; l < model.getDimension(); l++) {
                            ChessBoardLocation c2 = new ChessBoardLocation(k, l);
                            view.getGridAt(c2).setSelected(false);
                            view.getGridAt(c2).repaint();
                        }
                    }
//判断是否over

                    if (playernumber == 4) {
                        Player1 p1 = gameFrame.getChooseCharacter().getPlayer1();
                        Player2 p2 = gameFrame.getChooseCharacter().getPlayer2();
                        Player3 p3 = gameFrame.getChooseCharacter().getPlayer3();
                        Player4 p4 = gameFrame.getChooseCharacter().getPlayer4();


                        if (!p1.isWin() & p1.isWinner(model, playernumber)) {
                            winnerAfter++;
                            winner++;
                            gameFrame.getChooseCharacter().getPlayer1().setRank(winner);
                            saveGameToFile(getModel().saveGame());
                            score(chooseCharacter.getPlayer1(), playernumber);

                        } else if (!p2.isWin() & p2.isWinner(model, playernumber)) {
                            winnerAfter++;
                            winner++;
                            gameFrame.getChooseCharacter().getPlayer2().setRank(winner);
                            saveGameToFile(getModel().saveGame());
                            score(chooseCharacter.getPlayer2(), playernumber);
                        } else if (!p3.isWin() & p3.isWinner(model)) {
                            winnerAfter++;
                            winner++;
                            gameFrame.getChooseCharacter().getPlayer3().setRank(winner);
                            saveGameToFile(getModel().saveGame());
                            score(chooseCharacter.getPlayer3(), playernumber);
                        } else if (!p4.isWin() & p4.isWinner(model)) {

                            winnerAfter++;
                            winner++;
                            gameFrame.getChooseCharacter().getPlayer4().setRank(winner);
                            saveGameToFile(getModel().saveGame());
                            score(chooseCharacter.getPlayer4(), playernumber);

                        }
                        if (winner >= 3) over = true;
                        gameFrame.getPlayer1win().repaint();
                        gameFrame.getPlayer2win().repaint();
                        gameFrame.getPlayer3win().repaint();
                        gameFrame.getPlayer4win().repaint();
                        saveGameToFile(getModel().saveGame());
                    } else if (playernumber == 2) {
                        Player1 p1 = gameFrame.getChooseCharacter().getPlayer1();
                        Player2 p2 = gameFrame.getChooseCharacter().getPlayer2();
                        if (!p1.isWin() & p1.isWinner(model, playernumber)) {
                            winner++;
                            gameFrame.getChooseCharacter().getPlayer1().setRank(winner);
                            gameFrame.getChooseCharacter().getPlayer2().setRank(winner + 1);
                            saveGameToFile(getModel().saveGame());
                            score(chooseCharacter.getPlayer1(), playernumber);
                        } else if (!p2.isWin() & p2.isWinner(model, playernumber)) {
                            winner++;
                            p2.setRank(winner);
                            p1.setRank(winner + 1);
                            saveGameToFile(getModel().saveGame());
                            score(chooseCharacter.getPlayer2(), playernumber);
                        }
                        if (winner >= 1) over = true;
                        gameFrame.getPlayer1win().repaint();
                        gameFrame.getPlayer2win().repaint();
                        saveGameToFile(getModel().saveGame());

                    }

                    if (isOver()) {
                        if (playernumber == 2) {
                            String first = null;
                            String second = null;
                            if (chooseCharacter.getPlayer1().getRank() == 1) {
                                first = chooseCharacter.getPlayer1().getPlayername();
                                second = chooseCharacter.getPlayer2().getPlayername();
                            } else if (chooseCharacter.getPlayer1().getRank() == 2) {
                                first = chooseCharacter.getPlayer2().getPlayername();
                                second = chooseCharacter.getPlayer1().getPlayername();
                            }
                            JOptionPane.showMessageDialog(view, String.format("Game is over\n No.1: %s\n No.2: %s",
                                    first, second));
                        } else if (playernumber == 4) {
                            String first = rankPlayer(1, chooseCharacter.getPlayer1(), chooseCharacter.getPlayer2(),
                                    chooseCharacter.getPlayer3(), chooseCharacter.getPlayer4());
                            String second = rankPlayer(2, chooseCharacter.getPlayer1(), chooseCharacter.getPlayer2(),
                                    chooseCharacter.getPlayer3(), chooseCharacter.getPlayer4());
                            String third = rankPlayer(3, chooseCharacter.getPlayer1(), chooseCharacter.getPlayer2(),
                                    chooseCharacter.getPlayer3(), chooseCharacter.getPlayer4());
                            String fourth = rankPlayer(4, chooseCharacter.getPlayer1(), chooseCharacter.getPlayer2(),
                                    chooseCharacter.getPlayer3(), chooseCharacter.getPlayer4());
                            JOptionPane.showMessageDialog(view, String.format("Game is over\n " +
                                    "No.1: %s\n No.2: %s\n No.3: %s\n No.4: %s", first, second, third, fourth));
                        }

                    }
                    gameFrame.changeCurrentplayer(this);
                    gameFrame.changeTotalTurns(this);


                }
            } else if (hasSelectedLocation() && !(model.isValidMove(getSelectedLocation(), location))) {
                warning(view, 0);
                return false;
            }

        }else if(over) warning(view,2);

        if (winnerAfter > 0) getPlayer().setWin(true);
        return true;

    }

    @Override//点棋子获取其信息
    public boolean onPlayerClickChessPiece(ChessBoardLocation location, ChessComponent component) {

        if (!over) {
            ChessPiece piece = model.getChessPieceAt(location);
            if (piece.getColor() == currentPlayer && (!hasSelectedLocation() || location.equals(getSelectedLocation()))) {
                if (!hasSelectedLocation()) {
                    setSelectedLocation(location);
                } else {
                    resetSelectedLocation();
                    for (int k = 0; k < model.getDimension(); k++) {
                        for (int l = 0; l < model.getDimension(); l++) {
                            ChessBoardLocation c2 = new ChessBoardLocation(k, l);
                            view.getGridAt(c2).setSelected(false);
                            view.getGridAt(c2).repaint();
                        }
                    }
                }
                component.setSelected(!component.isSelected());
                component.repaint();
                return true;
            } else if (piece.getColor() != currentPlayer && (!hasSelectedLocation() || location.equals(getSelectedLocation()))) {
                warning(view, 1);
                return false;
            }
        }else if(over) warning(view,2);
        return false;
    }

    //undo
    public boolean Undo(boolean all) {
        if (over) {
            JOptionPane.showMessageDialog(gameFrame, "Game is over!");
            return false;
        }
//        int turn=getTotalturnbefore();
//        int currentPlayerNumber = playernumber;
//        if (playernumber == 4) {
//            if (gameFrame.getChooseCharacter().getPlayer1().isWin()) currentPlayerNumber--;
//            if (gameFrame.getChooseCharacter().getPlayer2().isWin()) currentPlayerNumber--;
//            if (gameFrame.getChooseCharacter().getPlayer3().isWin()) currentPlayerNumber--;
//            if (gameFrame.getChooseCharacter().getPlayer4().isWin()) currentPlayerNumber--;
//        }
        if(self.getPlayernumber()==4&&all){
            JOptionPane.showMessageDialog(gameFrame, "Sorry,you can undo a turn in four-player game!");
            return false;
        }
        if (model.undo(all, playernumber)) {
            Color color1 = ((Player) gameFrame.getChooseCharacter().getPlayer1()).getColor();
            Color color2 = ((Player) gameFrame.getChooseCharacter().getPlayer2()).getColor();
            if (playernumber == 2) {
                if(all) setTotalturn(getTotalturn()-1);
                else if (!all) {
                    if (currentPlayer == color1) {
                        setCurrentPlayer(color2);
                        setTotalturn(getTotalturn() - 1);
                    } else if (currentPlayer == color2) setCurrentPlayer(color1);
                    gameFrame.changeCurrentplayer(self);
                }
            }
            if (playernumber == 4) {
                Color color3 = ((Player) gameFrame.getChooseCharacter().getPlayer3()).getColor();
                Color color4 = ((Player) gameFrame.getChooseCharacter().getPlayer4()).getColor();
                if(all) setTotalturn(getTotalturn()-1);
                else if (!all) {
                    if (currentPlayer == color1) {
                        if (!gameFrame.getChooseCharacter().getPlayer4().isWin()) setCurrentPlayer(color4);
                        else if (!gameFrame.getChooseCharacter().getPlayer3().isWin()) setCurrentPlayer(color3);
                        else if (!gameFrame.getChooseCharacter().getPlayer2().isWin()) setCurrentPlayer(color2);
                        setTotalturn(getTotalturn() - 1);
                    } else if (currentPlayer == color2) {
                        if (!gameFrame.getChooseCharacter().getPlayer1().isWin()) setCurrentPlayer(color1);
                        else if (!gameFrame.getChooseCharacter().getPlayer4().isWin()) setCurrentPlayer(color4);
                        else if (!gameFrame.getChooseCharacter().getPlayer3().isWin()) setCurrentPlayer(color3);
                        if (gameFrame.getChooseCharacter().getPlayer1().isWin()) setTotalturn(getTotalturn() - 1);
                    } else if (currentPlayer == color3) {
                        if (!gameFrame.getChooseCharacter().getPlayer2().isWin()) setCurrentPlayer(color2);
                        else if (!gameFrame.getChooseCharacter().getPlayer1().isWin()) setCurrentPlayer(color1);
                        else if (!gameFrame.getChooseCharacter().getPlayer4().isWin()) setCurrentPlayer(color4);
                        if (gameFrame.getChooseCharacter().getPlayer1().isWin() &&
                                gameFrame.getChooseCharacter().getPlayer2().isWin()) setTotalturn(getTotalturn() - 1);
                    } else if (currentPlayer == color4) {
                        if (!gameFrame.getChooseCharacter().getPlayer3().isWin()) setCurrentPlayer(color3);
                        else if (!gameFrame.getChooseCharacter().getPlayer2().isWin()) setCurrentPlayer(color2);
                        else if (!gameFrame.getChooseCharacter().getPlayer1().isWin()) setCurrentPlayer(color1);
                        if (gameFrame.getChooseCharacter().getPlayer1().isWin() &&
                                gameFrame.getChooseCharacter().getPlayer2().isWin() &&
                                gameFrame.getChooseCharacter().getPlayer3().isWin()) setTotalturn(getTotalturn() - 1);
                    }
                    gameFrame.changeCurrentplayer(self);
                }
            }
            gameFrame.changeTotalTurns(self);
            return true;
        }
        if (model.getMoveTo().size() <= 1) {
            JOptionPane.showMessageDialog(gameFrame, "Sorry,you haven't finish a valid step! Please finish a valid step first!");
            return false;
        }
        if (!(model.getMoveTo().size() == model.getMoveFrom().size()*2)) {
            JOptionPane.showMessageDialog(gameFrame, "Sorry, you can't undo during a movement! Please finish or cancel the step first!");
            return false;
        }
        if (self.getPlayernumber() == 2) {
            if (model.getMoveFrom().size()==1) {
                JOptionPane.showMessageDialog(gameFrame, "Sorry,you haven't finish a turn! Please finish a turn first!");
                return false;
            }
        }
        return false;
    }
/*
    public boolean Undo(boolean all) {
        if (over) {
            JOptionPane.showMessageDialog(gameFrame, "Game is over!");
            return false;
        }

        int currentPlayerNumber = playernumber;
        if (playernumber == 4) {
            if (gameFrame.getChooseCharacter().getPlayer1().isWin()) currentPlayerNumber--;
            if (gameFrame.getChooseCharacter().getPlayer2().isWin()) currentPlayerNumber--;
            if (gameFrame.getChooseCharacter().getPlayer3().isWin()) currentPlayerNumber--;
            if (gameFrame.getChooseCharacter().getPlayer4().isWin()) currentPlayerNumber--;
        }
        if (self.getPlayernumber() == 4 && all) {
            JOptionPane.showMessageDialog(gameFrame, "Sorry,you can undo a turn in four-player game!");
            return false;
        }
        if (model.undo(all, currentPlayerNumber)) {
            Color color1 = ((Player) gameFrame.getChooseCharacter().getPlayer1()).getColor();
            Color color2 = ((Player) gameFrame.getChooseCharacter().getPlayer2()).getColor();
            if (playernumber == 2) {
                if (all) setTotalturn(getTotalturn() - 1);
                else if (!all) {
                    if (currentPlayer == color1) {
                        setCurrentPlayer(color2);
                        setTotalturn(getTotalturn() - 1);
                    } else if (currentPlayer == color2) setCurrentPlayer(color1);
                    gameFrame.changeCurrentplayer(self);
                }
            }
            if (playernumber == 4) {
                Color color3 = ((Player) gameFrame.getChooseCharacter().getPlayer3()).getColor();
                Color color4 = ((Player) gameFrame.getChooseCharacter().getPlayer4()).getColor();
                if (all) setTotalturn(getTotalturn() - 1);
                else if (!all) {
                    if (currentPlayer == color1) {
                        if (!gameFrame.getChooseCharacter().getPlayer4().isWin()) setCurrentPlayer(color4);
                        else if (!gameFrame.getChooseCharacter().getPlayer3().isWin()) setCurrentPlayer(color3);
                        else if (!gameFrame.getChooseCharacter().getPlayer2().isWin()) setCurrentPlayer(color2);
                        setTotalturn(getTotalturn() - 1);
                    } else if (currentPlayer == color2) {
                        if (!gameFrame.getChooseCharacter().getPlayer1().isWin()) setCurrentPlayer(color1);
                        else if (!gameFrame.getChooseCharacter().getPlayer4().isWin()) setCurrentPlayer(color4);
                        else if (!gameFrame.getChooseCharacter().getPlayer3().isWin()) setCurrentPlayer(color3);
                        if (gameFrame.getChooseCharacter().getPlayer1().isWin()) setTotalturn(getTotalturn() - 1);
                    } else if (currentPlayer == color3) {
                        if (!gameFrame.getChooseCharacter().getPlayer2().isWin()) setCurrentPlayer(color2);
                        else if (!gameFrame.getChooseCharacter().getPlayer1().isWin()) setCurrentPlayer(color1);
                        else if (!gameFrame.getChooseCharacter().getPlayer4().isWin()) setCurrentPlayer(color4);
                        if (gameFrame.getChooseCharacter().getPlayer1().isWin() &&
                                gameFrame.getChooseCharacter().getPlayer2().isWin()) setTotalturn(getTotalturn() - 1);
                    } else if (currentPlayer == color4) {
                        if (!gameFrame.getChooseCharacter().getPlayer3().isWin()) setCurrentPlayer(color3);
                        else if (!gameFrame.getChooseCharacter().getPlayer2().isWin()) setCurrentPlayer(color2);
                        else if (!gameFrame.getChooseCharacter().getPlayer1().isWin()) setCurrentPlayer(color1);
                        if (gameFrame.getChooseCharacter().getPlayer1().isWin() &&
                                gameFrame.getChooseCharacter().getPlayer2().isWin() &&
                                gameFrame.getChooseCharacter().getPlayer3().isWin()) setTotalturn(getTotalturn() - 1);
                    }
                    gameFrame.changeCurrentplayer(self);
                }
            }
            gameFrame.changeTotalTurns(self);
            return true;
        }
        if (model.getMoveTo().size() < 1) {
            JOptionPane.showMessageDialog(gameFrame, "Sorry,you haven't finish a valid step! Please finish a valid step first!");
            return false;
        }
        if (!(model.getMoveTo().size() == model.getMoveFrom().size())) {
            JOptionPane.showMessageDialog(gameFrame, "Sorry, you can't undo during a movement! Please finish or cancel the step first!");
            return false;
        }
        if (self.getPlayernumber() == 2) {
            if (!(model.getMoveFrom().size() % 2 == 0) || !(model.getMoveTo().size() % 2 == 0)) {
                JOptionPane.showMessageDialog(gameFrame, "Sorry,you haven't finish a turn! Please finish a turn first!");
                return false;
            }
        }

        JOptionPane.showMessageDialog(gameFrame, "Sorry,you haven't finish a turn! Please finish a turn first!");
        return false;
    }
*/
    //存文件
    public void saveGameToFile(char[][] saveGame) {
        if (chooseCharacter.getPlayernumber() == 2) {
            String player1 = colorToString(((Player) chooseCharacter.getPlayer1()).getColor());
            String player2 = colorToString(((Player) chooseCharacter.getPlayer2()).getColor());
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(String.format("save/twoplayer/%s.txt", chooseCharacter.getSavename())));
                for (char[] chars : saveGame) {
                    writer.write(chars);
                    writer.newLine();
                }

//分割线
                writer.write("$");
                writer.newLine();
//角色
                writer.write(String.format("%s ", player1));
                writer.write(String.format("%s ", player2));
                writer.write(colorToString(currentPlayer));
//用户名
                writer.newLine();
                writer.write(String.format("%s ", chooseCharacter.getPlayer1().getPlayername()));
                writer.write(String.format("%s", chooseCharacter.getPlayer2().getPlayername()));
//排名
                writer.newLine();
                writer.write(String.format("%s ", chooseCharacter.getPlayer1().getRank()));
                writer.write(String.format("%s ", chooseCharacter.getPlayer2().getRank()));

//回合数
                writer.newLine();
                writer.write(String.valueOf(getTotalturn()));
//时间
                writer.newLine();
                writer.write(String.valueOf(getTime()));

                writer.newLine();
                writer.write(String.valueOf(isOver()));

                writer.close();


            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else if (chooseCharacter.getPlayernumber() == 4) {
            String player1 = colorToString(((Player) chooseCharacter.getPlayer1()).getColor());
            String player2 = colorToString(((Player) chooseCharacter.getPlayer2()).getColor());
            String player3 = colorToString(((Player) chooseCharacter.getPlayer3()).getColor());
            String player4 = colorToString(((Player) chooseCharacter.getPlayer4()).getColor());

            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(String.format("save/fourplayer/%s.txt", gameFrame.getSavename()), false));
                for (char[] chars : saveGame) {
                    writer.write(chars);
                    writer.newLine();
                }
//分割线
                writer.write("$");
                writer.newLine();
//角色
                writer.write(String.format("%s ", player1));
                writer.write(String.format("%s ", player2));
                writer.write(String.format("%s ", player3));
                writer.write(String.format("%s ", player4));
                writer.write(colorToString(currentPlayer));
//用户名
                writer.newLine();
                writer.write(String.format("%s ", chooseCharacter.getPlayer1().getPlayername()));
                writer.write(String.format("%s ", chooseCharacter.getPlayer2().getPlayername()));
                writer.write(String.format("%s ", chooseCharacter.getPlayer3().getPlayername()));
                writer.write(String.format("%s", chooseCharacter.getPlayer4().getPlayername()));
//排名
                writer.newLine();
                writer.write(String.format("%s ", chooseCharacter.getPlayer1().getRank()));
                writer.write(String.format("%s ", chooseCharacter.getPlayer2().getRank()));
                writer.write(String.format("%s ", chooseCharacter.getPlayer3().getRank()));
                writer.write(String.format("%s ", chooseCharacter.getPlayer4().getRank()));
//回合数
                writer.newLine();
                writer.write(String.valueOf(getTotalturn()));
//时间
                writer.newLine();
                writer.write(String.valueOf(getTime()));

                writer.newLine();
                writer.write(String.valueOf(isOver()));

                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    //加载文件
    public void loadGameFromFile(String path) {
        char[][] game = new char[model.getDimension()][model.getDimension()];
        String line;
        int stop = 1;
        try {
            BufferedReader bf = new BufferedReader(new FileReader(path));
            int row = 0;
            Player1 player1 = new Player1();
            Player2 player2 = new Player2();
            Player3 player3 = new Player3();
            Player4 player4 = new Player4();
            //载入棋盘
            while ((line = bf.readLine()) != null) {
                if (stop <= model.getDimension()) {
                    for (int i = 0; i < model.getDimension(); i++) {
                        game[row][i] = line.charAt(i);
                    }
                    if (row < 15) {
                        row++;
                    }

                } else if (stop == 17) {//不加这一行读档不能继续下去
                    ;
                } else if (stop == 18) {
                    //载入角色
                    String[] color = line.split(" ");
                    chooseCharacter.setPlayer1(player1);
                    chooseCharacter.setPlayer2(player2);
                    player1.setColor(stringToColor(color[0]));
                    player2.setColor(stringToColor(color[1]));
                    player1.setCharacter(stringToCharacter(color[0]));
                    player2.setCharacter(stringToCharacter(color[1]));


                    if (chooseCharacter.getPlayernumber() == 4) {
                        chooseCharacter.setPlayer3(player3);
                        chooseCharacter.setPlayer4(player4);
                        player3.setColor(stringToColor(color[2]));
                        player4.setColor(stringToColor(color[3]));
                        player3.setCharacter(stringToCharacter(color[2]));
                        player4.setCharacter(stringToCharacter(color[3]));


                    }
                    currentPlayer = stringToColor(color[color.length - 1]);
                } else if (stop == 19) {//载入用户名
                    String[] playername = line.split(" ");
                    player1.setPlayername(playername[0]);
                    player2.setPlayername(playername[1]);
                    if (playernumber == 4) {
                        chooseCharacter.setPlayer3(player3);
                        chooseCharacter.setPlayer4(player4);
                        player3.setPlayername(playername[2]);
                        player4.setPlayername(playername[3]);
                    }
                } else if (stop == 20) {//载入名次
                    String[] playerrank = line.split(" ");
                    player1.setRank(Integer.parseInt(playerrank[0]));
                    player2.setRank(Integer.parseInt(playerrank[1]));
                    winner = Math.max(Integer.parseInt(playerrank[0]), Integer.parseInt(playerrank[1]));
                    if (playernumber == 4) {
                        player3.setRank(Integer.parseInt(playerrank[2]));
                        player4.setRank(Integer.parseInt(playerrank[3]));
                        int winner2 = Math.max(Integer.parseInt(playerrank[2]), Integer.parseInt(playerrank[3]));
                        winner = Math.max(winner, winner2);
                    }


                } else if (stop == 21) {//载入回合数
                    String[] turnnumber = line.split(" ");
                    setTotalturn(Integer.parseInt(turnnumber[0]));
                } else if (stop == 22) {//时间
                    String[] timelast = line.split(" ");
                    setTime(Integer.parseInt(timelast[0]));
                } else if (stop == 23) {//游戏是否结束
                    String[] over = line.split(" ");
                    setOver(Boolean.parseBoolean(over[0]));
                } else break;
                stop++;
            }

            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.loadGame(game);
        chooseCharacter.getPlayer1().isWinner(model, playernumber);
        chooseCharacter.getPlayer2().isWinner(model, playernumber);
        if (playernumber == 4) {
            chooseCharacter.getPlayer3().isWinner(model);
            chooseCharacter.getPlayer4().isWinner(model);
        }


    }

    //判断文件是否符合要求
    public boolean fileIsValid(String path) {
        int dimension = 16;
        char[][] gamelist = new char[dimension][dimension];
        String[] characterlist = new String[playernumber + 1];
        String[] playernamelist = new String[playernumber];
        String[] ranklist = new String[playernumber];
        String[] turnlist = new String[1];
        String[] timelist = new String[1];
        String[] isoverlist = new String[1];

//先判断存档行数是不是对的
        int prerow = 0;
        boolean isrightchessboard = false;
        try {
            String preline;
            BufferedReader prebf = new BufferedReader(new FileReader(path));
            while ((preline = prebf.readLine()) != null) {
                prerow++;
                if (prerow == dimension + 1) {
                    char[] separate = preline.toCharArray();
                    if (separate[0] == '$') isrightchessboard = true;
                }
            }
            prebf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (prerow == 23) {//要是存档行数是对的
            if (isrightchessboard) {
                String line;
                int stop = 1;
                try {
                    BufferedReader bf = new BufferedReader(new FileReader(path));
                    int row = 0;
                    //载入棋盘
                    while ((line = bf.readLine()) != null) {
                        if (stop <= dimension) {
                            char[] game = line.toCharArray();
                            if (game.length != dimension) {
                                JOptionPane.showMessageDialog(gameFrame, "Wrong scale of chessboard or some chess pieces are out of chessboard");
                                return false;
                            } else {
                                gamelist[row] = line.toCharArray();
                                row++;
                            }
                        } else if (stop == 17) {
                            ;
                        } else if (stop == 18) {
                            //角色
                            String[] color = line.split(" ");

                            if (color.length != characterlist.length) {
                                JOptionPane.showMessageDialog(gameFrame, "Wrong number of character");
                                return false;
                            } else {
                                for (int i = 0; i < characterlist.length; i++) {
                                    characterlist[i] = color[i];
                                }
                            }

                        } else if (stop == 19) {
                            //用户
                            String[] playername = line.split(" ");
                            if (playername.length != playernamelist.length) {
                                JOptionPane.showMessageDialog(gameFrame, "Wrong number of player");
                                return false;
                            } else {
                                for (int i = 0; i < playernamelist.length; i++) {
                                    playernamelist[i] = playername[i];
                                }
                            }
                        } else if (stop == 20) {
                            String[] playerrank = line.split(" ");
                            if (playerrank.length != ranklist.length) {
                                JOptionPane.showMessageDialog(gameFrame, "Wrong rank numbers");
                                return false;
                            } else {
                                for (int i = 0; i < playernumber; i++) {
                                    ranklist[i] = playerrank[i];
                                }
                            }
                        } else if (stop == 21) {
                            String[] turnnumber = line.split(" ");
                            if (turnnumber.length != 1) {
                                JOptionPane.showMessageDialog(gameFrame, "Wrong format of turn");
                                return false;
                            } else {
                                turnlist[0] = turnnumber[0];
                            }

                        } else if (stop == 22) {

                            String[] timelast = line.split(" ");
                            if (timelast.length != 1) {
                                JOptionPane.showMessageDialog(gameFrame, "Wrong format of time");
                                return false;
                            } else {
                                timelist[0] = timelast[0];
                            }
                        } else if (stop == 23) {

                            String[] over = line.split(" ");
                            System.out.println(over[0]);
                            if (over.length != 1) {
                                JOptionPane.showMessageDialog(gameFrame, "Wrong format of game status");
                                return false;
                            } else {
                                isoverlist[0] = over[0];
                            }
                        } else break;
                        stop++;
                    }
                    bf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //判断棋子数量
                int piecenumber = 0;
                for (int i = 0; i < dimension; i++) {
                    for (int j = 0; j < dimension; j++) {
                        if (Integer.parseInt(String.valueOf(gamelist[i][j])) != 0) piecenumber++;
                    }
                }
                if (playernumber == 2) {
                    if (piecenumber != 38) {
                        JOptionPane.showMessageDialog(gameFrame, "Wrong number of pieces");
                        System.out.println(piecenumber);
                        return false;
                    }
                } else if (playernumber == 4) {
                    if (piecenumber != 52) {
                        JOptionPane.showMessageDialog(gameFrame, "Wrong number of pieces");
                        return false;
                    }
                }
                //判断排名是否与游戏状态对应
                int zeronumber = 0;
                for (int i = 0; i < ranklist.length; i++) {
                    if (ranklist[i].equals("0")) zeronumber++;
                }
                if (isoverlist[0].equals("true")) {
                    if (playernumber == 2) {
                        if (zeronumber != 0) {
                            JOptionPane.showMessageDialog(gameFrame, "Conflict of game status(rank)");
                            return false;
                        }
                    } else if (playernumber == 4) {
                        if (zeronumber != 1) {
                            JOptionPane.showMessageDialog(gameFrame, "Conflict of game status(rank)");
                            return false;
                        }
                    }
                } else if (isoverlist[0].equals("false")) {
                    if (playernumber == 2) {
                        if (zeronumber == 0) {
                            JOptionPane.showMessageDialog(gameFrame, "Conflict of game status(rank)");
                            return false;
                        }
                    } else if (playernumber == 4) {
                        if (zeronumber <= 1) {
                            JOptionPane.showMessageDialog(gameFrame, "Conflict of game status(rank)");
                            return false;
                        }
                    }
                }
                //玩家数量,排名数量,角色数量是否一致
                if (playernamelist.length != (characterlist.length - 1) || playernamelist.length != ranklist.length || ranklist.length != (characterlist.length - 1)) {
                    JOptionPane.showMessageDialog(gameFrame, "numbers of Player, character and rank aren't consistent");
                    return false;
                }
                //判断玩家的输赢是否符合游戏的状态
//                               Player1 player1 = new Player1();
//                Player2 player2 = new Player2();
//                Player3 player3 = new Player3();
//                Player4 player4 = new Player4();
//                chooseCharacter.setPlayer1(player1);
//                chooseCharacter.setPlayer2(player2);
//                player1.setColor(stringToColor(characterlist[0]));
//                player2.setColor(stringToColor(characterlist[1]));
//                player1.setCharacter(stringToCharacter(characterlist[0]));
//                player2.setCharacter(stringToCharacter(characterlist[1]));
//                if (chooseCharacter.getPlayernumber() == 4) {
//                    chooseCharacter.setPlayer3(player3);
//                    chooseCharacter.setPlayer4(player4);
//                    player3.setColor(stringToColor(characterlist[2]));
//                    player4.setColor(stringToColor(characterlist[3]));
//                    player3.setCharacter(stringToCharacter(characterlist[2]));
//                    player4.setCharacter(stringToCharacter(characterlist[3]));
//                }
//                System.out.printf("%s, %s",player1.isWinner(model,2),player2.isWinner(model,2));
//                if(playernumber==2){
//                    if(player1.isWinner(model,2)||player2.isWinner(model,2)){
//                        if(isoverlist[0].equals("false")){
//                            JOptionPane.showMessageDialog(gameFrame,"Conflict of game status(player)");
//                            System.out.println("false1");
//                            return false;
//                        }
//                    }else {
//                        if(isoverlist[0].equals("true")){
//                            JOptionPane.showMessageDialog(gameFrame,"Conflict of game status(player)");
//                            System.out.println("false2");
//                            return false;
//                        }
//                    }
//                }else if(playernumber==4){
//                    int winnernumber=0;
//                    if(player1.isWinner(model,4)) winnernumber++;
//                    if(player3.isWinner(model)) winnernumber++;
//                    if(player2.isWinner(model,4)) winnernumber++;
//                    if(player4.isWinner(model)) winnernumber++;
//                    if(winnernumber>=3){
//                        if(isoverlist[0].equals("false")){
//                            JOptionPane.showMessageDialog(gameFrame,"Conflict of game status(player)");
//                            return false;
//                        }
//                    }else{
//                        if(isoverlist[0].equals("true")){
//                            JOptionPane.showMessageDialog(gameFrame,"Conflict of game status(player)");
//                            return false;
//                        }
//                    }
//
//                }
//
//

            } else {
                JOptionPane.showMessageDialog(gameFrame, "Wrong scale of chessboard or some chess pieces are out of chessboard");
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(gameFrame, "Wrong format of save");
            return false;
        }
        System.out.println("true");
        return true;

    }


    //积分每走一步就判断，若得到名次就记录积分，文件名为玩家名，文件第一行为二人游戏赢的次数,第
    //二行之第四行记录四人游戏获得第一名到第三名的次数
    public void score(Player p, int playernumber) {
        try {
            //读排名
            BufferedReader reader = new BufferedReader(new FileReader(String.format("save/scoreboard/%s.txt", p.getPlayername())));
            //若未取得名次则退出
            if (p.getRank() == 0 || p.getRank() == 4) return;
            int firsttwop = 0;
            int firstfourp = 0;
            int seconfourp = 0;
            int thirdfourp = 0;

            String line;
            int row = 1;
            while ((line = reader.readLine()) != null) {
                String[] tem = line.split(" ");
                switch (row) {
                    case 1:
                        firsttwop = Integer.parseInt(tem[0]);
                        break;
                    case 2:
                        firstfourp = Integer.parseInt(tem[0]);
                        break;
                    case 3:
                        seconfourp = Integer.parseInt(tem[0]);
                        break;
                    case 4:
                        thirdfourp = Integer.parseInt(tem[0]);
                        break;
                }
                row++;
            }
            reader.close();
            //重新写排名
            BufferedWriter writer = new BufferedWriter(new FileWriter(String.format("save/scoreboard/%s.txt", p.getPlayername())));
            if (playernumber == 2) {
                if (p.getRank() == 1) {
                    writer.write(String.format("%s", firsttwop + 1));
                    writer.newLine();
                    writer.write(String.format("%s", firstfourp));
                    writer.newLine();
                    writer.write(String.format("%s", seconfourp));
                    writer.newLine();
                    writer.write(String.format("%s", thirdfourp));

                }
            } else if (playernumber == 4) {
                if (p.getRank() == 1) {
                    writer.write(String.format("%s", firsttwop));
                    writer.newLine();
                    writer.write(String.format("%s", firstfourp + 1));
                    writer.newLine();
                    writer.write(String.format("%s", seconfourp));
                    writer.newLine();
                    writer.write(String.format("%s", thirdfourp));
                }
            } else if (p.getRank() == 2) {
                writer.write(String.format("%s", firsttwop));
                writer.newLine();
                writer.write(String.format("%s", firstfourp));
                writer.newLine();
                writer.write(String.format("%s", seconfourp + 1));
                writer.newLine();
                writer.write(String.format("%s", thirdfourp));
            } else if (p.getRank() == 3) {
                writer.write(String.format("%s", firsttwop));
                writer.newLine();
                writer.write(String.format("%s", firstfourp));
                writer.newLine();
                writer.write(String.format("%s", seconfourp));
                writer.newLine();
                writer.write(String.format("%s", thirdfourp + 1));
            }
            writer.close();
        } catch (IOException e) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(String.format("save/scoreboard/%s.txt", p.getPlayername())));
                if (p.getRank() == 0 || p.getRank() == 4) {
                    writer.write(String.format("%s", 0));
                    writer.newLine();
                    writer.write(String.format("%s", 0));
                    writer.newLine();
                    writer.write(String.format("%s", 0));
                    writer.newLine();
                    writer.write(String.format("%s", 0));
                }

                if (playernumber == 2) {
                    if (p.getRank() == 1) {
                        writer.write(String.format("%s", 1));
                        writer.newLine();
                        writer.write(String.format("%s", 0));
                        writer.newLine();
                        writer.write(String.format("%s", 0));
                        writer.newLine();
                        writer.write(String.format("%s", 0));
                    }
                } else if (playernumber == 4) {
                    if (p.getRank() == 1) {
                        writer.write(String.format("%s", 0));
                        writer.newLine();
                        writer.write(String.format("%s", 1));
                        writer.newLine();
                        writer.write(String.format("%s", 0));
                        writer.newLine();
                        writer.write(String.format("%s", 0));

                    } else if (p.getRank() == 2) {
                        writer.write(String.format("%s", 0));
                        writer.newLine();
                        writer.write(String.format("%s", 0));
                        writer.newLine();
                        writer.write(String.format("%s", 1));
                        writer.newLine();
                        writer.write(String.format("%s", 0));
                    } else if (p.getRank() == 3) {
                        writer.write(String.format("%s", 0));
                        writer.newLine();
                        writer.write(String.format("%s", 0));
                        writer.newLine();
                        writer.write(String.format("%s", 0));
                        writer.newLine();
                        writer.write(String.format("%s", 1));
                    }
                }
                System.out.println("close");

                writer.close();

            } catch (IOException ew) {
                ew.printStackTrace();
            }
        }
    }

    // 颜色转换字符
    public String colorToString(Color c) {
        if (c == Color.GREEN) return "Green";
        else if (c == Color.RED) return "Red";
        else if (c == Color.WHITE) return "White";
        else if (c == Color.BLACK) return "Black";
        else if (c == Color.YELLOW) return "Yellow";
        else if (c == Color.BLUE) return "Blue";
        return null;
    }

    public String toPicture(Color c) {
        if (c == Color.GREEN) return "image/snakeps.png";
        else if (c == Color.BLUE) return "image/owlfly.png";
        else if (c == Color.BLACK) return "image/cloud.png";
        else if (c == Color.RED) return "image/lightning.png";
        else if (c == Color.WHITE) return "image/vortex.png";
        else if (c == Color.YELLOW) return "image/deermove.png";
        return null;
    }

    public Color stringToColor(String s) {
        if (s.equals("Red")) return Color.RED;
        else if (s.equals("White")) return Color.WHITE;
        else if (s.equals("Green")) return Color.GREEN;
        else if (s.equals("Yellow")) return Color.YELLOW;
        else if (s.equals("Black")) return Color.BLACK;
        else if (s.equals("Blue")) return Color.BLUE;
        return null;
    }

    public Character stringToCharacter(String s) {
        if (s.equals("Red")) return new Zeus();
        else if (s.equals("White")) return new Poseidon();
        else if (s.equals("Green")) return new Medusa();
        else if (s.equals("Yellow")) return new Artemis();
        else if (s.equals("Black")) return new Hades();
        else if (s.equals("Blue")) return new Athena();
        return null;
    }

    public String rankPlayer(int rank, Player p1, Player p2, Player p3, Player p4) {
        if (rank == 1) {
            if (p1.getRank() == 1) return p1.getPlayername();
            else if (p2.getRank() == 1) return p2.getPlayername();
            else if (p3.getRank() == 1) return p3.getPlayername();
            else if (p4.getRank() == 1) return p4.getPlayername();
        } else if (rank == 2) {
            if (p1.getRank() == 2) return p1.getPlayername();
            else if (p2.getRank() == 2) return p2.getPlayername();
            else if (p3.getRank() == 2) return p3.getPlayername();
            else if (p4.getRank() == 2) return p4.getPlayername();
        } else if (rank == 3) {
            if (p1.getRank() == 3) return p1.getPlayername();
            else if (p2.getRank() == 3) return p2.getPlayername();
            else if (p3.getRank() == 3) return p3.getPlayername();
            else if (p4.getRank() == 3) return p4.getPlayername();
        } else if (rank == 4) {
            if (p1.getRank() == 0) return p1.getPlayername();
            else if (p2.getRank() == 0) return p2.getPlayername();
            else if (p3.getRank() == 0) return p3.getPlayername();
            else if (p4.getRank() == 0) return p4.getPlayername();
        }
        return null;
    }

    public void setView(ChessBoardComponent view) {
        this.view = view;
    }

    public void setModel(ChessBoard model) {
        this.model = model;
    }

    public void setPlayernumber(int playernumber) {
        this.playernumber = playernumber;
    }

    public void setCurrentPlayer(Color currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public ChessBoardComponent getView() {
        return view;
    }

    public ChessBoard getModel() {
        return model;
    }

    public int getPlayernumber() {
        return playernumber;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public GameFrame getGameFrame() {
        return gameFrame;
    }

    public void setGameFrame(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
    }

    public int getTotalturn() {
        return totalturn;
    }

    public void setTotalturn(int totalturn) {
        this.totalturn = totalturn;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over = over;
    }

    public boolean isIsvalid() {
        return isvalid;
    }

    public void setIsvalid(boolean isvalid) {
        this.isvalid = isvalid;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

//    public int getTotalturnbefore() {
//        return totalturnbefore;
//    }
//
//    public void setTotalturnbefore(int totalturnbefore) {
//        this.totalturnbefore = totalturnbefore;
//    }
}
