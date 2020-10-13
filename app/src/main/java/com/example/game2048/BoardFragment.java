package com.example.game2048;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.game2048.Entity.URLInfo;
import com.example.game2048.databinding.FragmentBoardBinding;

import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.game2048.GameDataRecordContract.Game2048Entity;
import static com.example.game2048.GameDataRecordContract.GameBoardEntity;


/**
 * A simple {@link Fragment} subclass.
 */
public class BoardFragment extends Fragment {
    private float x1, x2, y1, y2;
    private static final int DISTANCE = 200;
    private int[][] blocks;
    private int[][] initialBlocks;
    private Board board;
    private Solver solver;
    private int index;
    private int lastIndex;
    private Board[] boards;
    private Boolean useRobot;
    private int screenWidth;
    private Boolean isStart;
    private long useTimes;
    private Handler handler;
    private Timer timer;
    private GameDataRecordDbHelper dbHelper;
    private SQLiteDatabase db;
    private ContentValues values;
    private int minTime;
    private Long userId = Long.MAX_VALUE;
    private FragmentBoardBinding binding;
    private PlayGameInterface playGameInterface;

    public BoardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBoardBinding.
                inflate(inflater, container, false);
        final TextView[] textViews = new TextView[9];
        textViews[0] = binding.boardOne;
        textViews[1] = binding.boardTwo;
        textViews[2] = binding.boardThree;
        textViews[3] = binding.boardFour;
        textViews[4] = binding.boardFive;
        textViews[5] = binding.boardSix;
        textViews[6] = binding.boardSeven;
        textViews[7] = binding.boardEight;
        textViews[8] = binding.boardNine;

        randomBoard(textViews);

        blocks = getBoard(textViews);
        initialBlocks = blocks;
        board = new Board(blocks);
        solver = new Solver(board);

        dbHelper    =   new GameDataRecordDbHelper(getContext());
        values      =   new ContentValues();
        URLInfo urlInfo = new URLInfo();
        minTime     =   0;
        String id   =   getArguments().getString("userId");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlInfo.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        playGameInterface = retrofit.create(PlayGameInterface.class);

        index = 0;
        useTimes    =   0L;
        lastIndex   =   0;
        useRobot    =   false;
        isStart     =   false;

        String[] projection = {
                GameBoardEntity.COLUMN_NAME_TITLE,
                GameBoardEntity.COLUMN_NAME_SECOND_TITLE
        };

        if (id != null) {
            userId = Long.parseLong(id);

            db = dbHelper.getReadableDatabase();
            String selection = GameBoardEntity.COLUMN_NAME_TITLE + " = ?";
            String[] selectionArgs = {String.valueOf(userId)};
            String sortOrder = GameBoardEntity.COLUMN_NAME_TITLE + " ASC";

            Cursor cursor = db.query(
                    GameBoardEntity.TABLE_NAME,
                    projection,                 // The array of columns to return (pass null to get all)
                    selection,                  // The columns for the WHERE clause
                    selectionArgs,              // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    sortOrder               // The sort order
            );
            values.put(GameBoardEntity.COLUMN_NAME_TITLE, userId);

            if (cursor.moveToNext()) minTime = cursor.getInt(1);
            else {
                db = dbHelper.getWritableDatabase();
                values.put(GameBoardEntity.COLUMN_NAME_SECOND_TITLE, 999999);
                db.insert(GameBoardEntity.TABLE_NAME, null, values);
            }

            cursor.close();
        } else values.put(GameBoardEntity.COLUMN_NAME_TITLE, 0);

        binding.boardMinUseTime.setText(String.valueOf(minTime));

        timer = new Timer();
        final TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = (int) useTimes++;
                handler.sendMessage(message);
            }
        };

        handler = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@NonNull Message msg) {
                binding.boardUseTime.setText(String.valueOf(useTimes));
            }
        };

        timer.schedule(timerTask, 0, 1000);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;

        View view = binding.getRoot();
        view.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    x1 = event.getX();
                    y1 = event.getY();
                    if (!isStart) {
                        isStart = true;
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    x2 = event.getX();
                    y2 = event.getY();
                    float xDistance = x1 - x2;
                    float yDistance = y1 - y2;
                    if (Math.abs(xDistance) >= DISTANCE) {
                        if (xDistance < 0) {
                            rightMove(textViews);
                            if (gameOver(textViews)) isStart = false;
                            solver = new Solver(new Board(getBoard(textViews)));
                            index = 0;
                            lastIndex = 0;
                        } else {
                            leftMove(textViews);
                            if (gameOver(textViews)) isStart = false;
                            solver = new Solver(new Board(getBoard(textViews)));
                            index = 0;
                            lastIndex = 0;
                        }
                    } else if (Math.abs(yDistance) >= DISTANCE) {
                        if (yDistance > 0) {
                            upMove(textViews);
                            if (gameOver(textViews)) isStart = false;
                            solver = new Solver(new Board(getBoard(textViews)));
                            index = 0;
                            lastIndex = 0;
                        } else {
                            downMove(textViews);
                            if (gameOver(textViews)) isStart = false;
                            solver = new Solver(new Board(getBoard(textViews)));
                            index = 0;
                            lastIndex = 0;
                        }
                    } else if (useRobot) {
                        binding.boardUseTime.setText("0");
                        if (x1 >= (int) (screenWidth / 2)) {
                            if (index < boards.length) {
                                assert boards != null;
                                lastIndex = index;
                                setBoardByBlocks(boards[index++].getBlocks(), textViews);
                            } else {
                                Toast.makeText(getActivity(), "Has been solution", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            assert boards != null;
                            if (index > 0) {
                                if (lastIndex != 0) {
                                    setBoardByBlocks(boards[lastIndex].getBlocks(), textViews);
                                    index = lastIndex--;
                                } else {
                                    setBoardByBlocks(boards[index--].getBlocks(), textViews);
                                }
                            } else {
                                Toast.makeText(getActivity(), "Has been back initial site", Toast.LENGTH_LONG).show();
                                isStart = false;
                            }
                        }
                    }
                }

                if (!isStart) {
                    if (useTimes < minTime) {
                        binding.boardMinUseTime.setText(String.valueOf(useTimes));
                        values.put(GameBoardEntity.COLUMN_NAME_SECOND_TITLE, useTimes);

                        db = dbHelper.getWritableDatabase();
                        String selection = GameBoardEntity.COLUMN_NAME_TITLE + " = ?";
                        String[] selectionArgs = {String.valueOf(userId)};

                        long count = db.update(GameBoardEntity.TABLE_NAME, values, selection, selectionArgs);
                        if (count > 0) Log.i("Sqlite", "update max score success");
                        else Log.i("Sqlite", "update max score failed");

                        addRecordToServer(useTimes);
                    }
                    timer.cancel();
                }

                return true;
            }
        });

        binding.checkSolverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!solver.isSolvable()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("The Board Can't Solver, Please Reset.")
                            .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    randomBoard(textViews);
                                    blocks = getBoard(textViews);
                                    board = new Board(blocks);
                                    solver = new Solver(board);
                                    index = 0;
                                    lastIndex = 0;
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).create().show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("The Board Can Solver, Please try.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).create().show();
                }
            }
        });

        binding.resetNumsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                randomBoard(textViews);
                useTimes = 0;
                solver = new Solver(new Board(getBoard(textViews)));
                index = 0;
                lastIndex = 0;
            }
        });

        binding.boardRobot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                if (!solver.isSolvable()) {
                    builder.setMessage("The Board Can't Solver, Please Reset")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    randomBoard(textViews);
                                    timer.cancel();
                                    blocks = getBoard(textViews);
                                    board = new Board(blocks);
                                    solver = new Solver(board);
                                    index = 0;
                                    lastIndex = 0;
                                }
                            })
                            .create().show();
                } else {
                    builder.setMessage("Are you sure use robot?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getActivity(), "At least move " + solver.moves()
                                            + " Step", Toast.LENGTH_LONG).show();
                                    solver = new Solver(new Board(getBoard(textViews)));
                                    index = 0;
                                    lastIndex = 0;
                                    useRobot = true;
                                    boards = getBoards(solver);
                                    timer.cancel();
                                    useTimes = 0;
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    useRobot = false;
                                }
                            })
                            .create().show();
                }
            }
        });

        return binding.getRoot();
    }

    private void randomBoard(TextView[] textViews) {
        Integer[] array = new Integer[9];
        Arrays.fill(array, 0);

        int num;
        for (TextView textView : textViews) {
            num = randomNum(array);
            if (num == 0) textView.setText("");
            else textView.setText(String.valueOf(num));
        }
    }

    private int randomNum(Integer[] array) {
        int index;
        Random random = new Random();
        index = random.nextInt(9);
        while (array[index] == 1) {
            index = random.nextInt(9);
        }
        array[index] = 1;

        return index;
    }

    private int[][] getBoard(TextView[] textViews) {
        int[][] board = new int[3][3];
        int textViewIndex = 0;
        for (int i = 0; i < board.length; ++i) {
            for (int j = 0; j < board[i].length; ++j) {
                textViewIndex = i * board[i].length + j;
                if (textViews[textViewIndex].getText().equals("")) board[i][j] = 0;
                else board[i][j] = Integer.parseInt(textViews[textViewIndex].getText().toString());
            }
        }

        return board;
    }

    private void leftMove(TextView[] textViews) {
        final int dimension = (int) Math.sqrt(textViews.length);
//        System.out.println("Dimension: " + dimension);
        TextView[][] boardTextViews = new TextView[dimension][dimension];

        for (int i = 0; i < dimension; ++i) {
            System.arraycopy(textViews, i * dimension, boardTextViews[i], 0, dimension);
        }

        int row = 0, col = 0;
        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                if (boardTextViews[i][j].getText().equals("")) {
                    row = i;
                    col = j;
                }
            }
        }

        if (col != dimension - 1) {
            textViews[row * dimension + col].setText(textViews[row * dimension + col + 1].getText());
            textViews[row * dimension + col + 1].setText("");
        }
    }

    private void rightMove(TextView[] textViews) {
        final int dimension = (int) Math.sqrt(textViews.length);
        TextView[][] boardTextViews = new TextView[dimension][dimension];

        for (int i = 0; i < dimension; ++i) {
            System.arraycopy(textViews, i * dimension, boardTextViews[i], 0, dimension);
        }

        int row = 0, col = 0;
        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                if (boardTextViews[i][j].getText().equals("")) {
                    row = i;
                    col = j;
                }
            }
        }

        if (col != 0) {
            textViews[row * dimension + col].setText(textViews[row * dimension + col - 1].getText());
            textViews[row * dimension + col - 1].setText("");
        }
    }

    private void downMove(TextView[] textViews) {
        final int dimension = (int) Math.sqrt(textViews.length);
        TextView[][] boardTextViews = new TextView[dimension][dimension];

        for (int i = 0; i < dimension; ++i) {
            System.arraycopy(textViews, i * dimension, boardTextViews[i], 0, dimension);
        }

        int row = 0, col = 0;
        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                if (boardTextViews[i][j].getText().equals("")) {
                    row = i;
                    col = j;
                }
            }
        }

        if (row != 0) {
            textViews[row * dimension + col].setText(textViews[(row - 1) * dimension + col].getText());
            textViews[(row - 1) * dimension + col].setText("");
        }
    }

    private void upMove(TextView[] textViews) {
        final int dimension = (int) Math.sqrt(textViews.length);
        TextView[][] boardTextViews = new TextView[dimension][dimension];

        for (int i = 0; i < dimension; ++i) {
            System.arraycopy(textViews, i * dimension, boardTextViews[i], 0, dimension);
        }

        int row = 0, col = 0;
        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                if (boardTextViews[i][j].getText().equals("")) {
                    row = i;
                    col = j;
                }
            }
        }

        if (row != dimension - 1) {
            textViews[row * dimension + col].setText(textViews[(row + 1) * dimension + col].getText());
            textViews[(row + 1) * dimension + col].setText("");
        }
    }

    private void setBoardByBlocks(int[][] blocks, TextView[] textViews) {
        final int dimension = (int) Math.sqrt(textViews.length);
        for (int i = 0; i < blocks.length; ++i) {
            for (int j = 0; j < blocks[i].length; ++j) {
                if (blocks[i][j] == 0) textViews[i * dimension + j].setText("");
                else textViews[i * dimension + j].setText(String.valueOf(blocks[i][j]));
            }
        }
    }

    private Boolean gameOver(TextView[] textViews) {
        for (int i = 0; i < textViews.length - 1; ++i) {
            if (!textViews[i].getText().equals(String.valueOf(i + 1))) return false;
        }

        return true;
    }

    private Board[] getBoards(Solver solver) {
        if (solver.isSolvable()) {
            index = 0;
            int moveSteps = solver.moves();
            boards = new Board[moveSteps + 1];
            int i = 0;
            for (Board board1 : solver.solution()) {
                boards[i++] = board1;
            }

            return boards;
        }

        return null;
    }

    private void addRecordToServer(long useTimes) {
        String pattern = "yyyy-MM-dd";
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String date = simpleDateFormat.format(new java.util.Date());

        Call<String> addNewRecord = playGameInterface.addNewBoardRecord(userId, (int) useTimes,
                Date.valueOf(date), getInitialBoard(initialBlocks));
        addNewRecord.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equalsIgnoreCase("true")) {
                    Log.i("Add Server", "onResponse: add to server success");
                } else {
                    Log.i("Add Server", "onResponse: add to server failed");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("Add Server", "onFailure: " + t);
            }
        });
    }

    private String getInitialBoard(int[][] initialBlocks) {
        StringBuilder stringBuilder = new StringBuilder("[");
        for (int[] initialBlock : initialBlocks) {
            stringBuilder.append(" [");
            for (int i : initialBlock) {
                stringBuilder.append(" ").append(i).append(" ");
            }
            stringBuilder.append("] ");
        }
        stringBuilder.append("]");

        return String.valueOf(stringBuilder);
    }
}
