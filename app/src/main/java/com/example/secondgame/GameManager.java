package com.example.secondgame;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.secondgame.activities.MainActivity;
import com.example.secondgame.model.Item;
import com.example.secondgame.model.ListOfResults;
import com.example.secondgame.model.Result;
import com.example.secondgame.model.Type;
import com.example.secondgame.utils.GPS;
import com.example.secondgame.utils.MySPV3;
import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiresApi(api = Build.VERSION_CODES.O)
public class GameManager {

    private int life;
    private int placeOfPlane = 2;
    private final int rows;
    private final int columns;
    private Item[][] matrix;
    private int lastIteration = -1;
    private int wrong;
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
    private int score = 0;


    public GameManager(int life, int rows, int columns) {
        this.life = life;
        this.rows = rows;
        this.columns = columns;
        this.matrix = new Item[rows][columns];

    }


    public int getRandomIndex() {
        int min = 0;
        int max = columns - 1;
        return (int) Math.floor(Math.random() * (max - min) + min);
    }

    public Item[][] getMatrix() {
        return matrix;
    }

    private void shiftMatrix() {

        for (int i = rows - 1; i >= 0; i--) {
            for (int j = 0; j < columns; j++) {
                if (i == rows - 1) {
                    if (matrix[i][j].getType() == Type.VISIBLE) {
                        matrix[i][j].setType(Type.INVISIBLE);
                        lastIteration = j;
                    }
                } else {
                    matrix[i + 1][j].setType(matrix[i][j].getType());
                }
            }
        }


    }

    public void updateTable() {
        shiftMatrix();
        putRandomBomb();
        raiseScore();
    }

    private void raiseScore() {
        this.score += 1;
    }

    private void putRandomBomb() {
        int randomNumber = getRandomIndex();
        for (int i = 0; i < columns; i++) {
            if (i == randomNumber) {
                matrix[0][i].setType(Type.VISIBLE);

            } else {
                matrix[0][i].setType(Type.INVISIBLE);

            }
        }
    }

    public boolean move(String move) {
        if (move.equals(MainActivity.KEY_LEFT)) {
            if (placeOfPlane > 0) {
                placeOfPlane--;
                return true;
            }
        } else if (move.equals(MainActivity.KEY_RIGHT)) {
            if (placeOfPlane < columns - 1) {
                placeOfPlane++;
                return true;
            }
        }
        return false;

    }

    public int getCurrentPlace() {
        return placeOfPlane;
    }

    public void initMatrix(AppCompatImageView[][] matrixView) {

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = new Item()
                        .setImage(matrixView[i][j])
                        .setType(Type.INVISIBLE);
                ;
            }
        }
    }

    public boolean checkIsWrong() {
        if (placeOfPlane == lastIteration && wrong < life) {
            wrong++;
            return true;
        }

        return false;
    }

    public int getWrong() {
        return wrong;
    }

    public boolean isEndGame() {
        return wrong == life;
    }


    public void saveResults() {
        ListOfResults listOfResults;
        String jsonData = MySPV3.getInstance().getString("records", "");
        listOfResults = new Gson().fromJson(jsonData, ListOfResults.class);
        if (listOfResults == null) {
            listOfResults = new ListOfResults();
        }
        listOfResults.getResults().add(createResult());
        MySPV3.getInstance().putString("records", new Gson().toJson(listOfResults));
    }

    private Result createResult() {
        return new Result().setTime(dtf.format(LocalDateTime.now())).setScore(score).setX(GPS.getInstance().getLatitude()).setY(GPS.getInstance().getLongitude());
    }
}
