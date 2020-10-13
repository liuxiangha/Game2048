package com.example.game2048;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.game2048.Entity.URLInfo;
import com.example.game2048.databinding.FragmentMainGameBinding;

import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.game2048.GameDataRecordContract.Game2048Entity;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainGame extends Fragment {
    private float x1, x2, y1, y2;
    private static final int DISTANCE = 150;
    private Map<Integer, TextView> integerTextViewMap;
    private Integer[][] indexs;
    private GameDataRecordDbHelper dbHelper;
    private SQLiteDatabase db;
    private ContentValues values;
    private int maxScore;
    private Long userId = 0L;
    private FragmentMainGameBinding binding;
    private PlayGameInterface playGameInterface;

    public MainGame() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainGameBinding.
                inflate(inflater, container, false);

        integerTextViewMap = new HashMap<>();

        integerTextViewMap.put(binding.oneAndOne.getId(), binding.oneAndOne);
        integerTextViewMap.put(binding.oneAndTwo.getId(), binding.oneAndTwo);
        integerTextViewMap.put(binding.oneAndThree.getId(), binding.oneAndThree);
        integerTextViewMap.put(binding.oneAndFour.getId(), binding.oneAndFour);

        integerTextViewMap.put(binding.twoAndOne.getId(), binding.twoAndOne);
        integerTextViewMap.put(binding.twoAndTwo.getId(), binding.twoAndTwo);
        integerTextViewMap.put(binding.twoAndThree.getId(), binding.twoAndThree);
        integerTextViewMap.put(binding.twoAndFour.getId(), binding.twoAndFour);

        integerTextViewMap.put(binding.threeAndOne.getId(), binding.threeAndOne);
        integerTextViewMap.put(binding.threeAndTwo.getId(), binding.threeAndTwo);
        integerTextViewMap.put(binding.threeAndThree.getId(), binding.threeAndThree);
        integerTextViewMap.put(binding.threeAndFour.getId(), binding.threeAndFour);

        integerTextViewMap.put(binding.fourAndOne.getId(), binding.fourAndOne);
        integerTextViewMap.put(binding.fourAndTwo.getId(), binding.fourAndTwo);
        integerTextViewMap.put(binding.fourAndThree.getId(), binding.fourAndThree);
        integerTextViewMap.put(binding.fourAndFour.getId(), binding.fourAndFour);

        indexs = new Integer[4][4];
        indexs[0][0] = binding.oneAndOne.getId();
        indexs[0][1] = binding.oneAndTwo.getId();
        indexs[0][2] = binding.oneAndThree.getId();
        indexs[0][3] = binding.oneAndFour.getId();

        indexs[1][0] = binding.twoAndOne.getId();
        indexs[1][1] = binding.twoAndTwo.getId();
        indexs[1][2] = binding.twoAndThree.getId();
        indexs[1][3] = binding.twoAndFour.getId();

        indexs[2][0] = binding.threeAndOne.getId();
        indexs[2][1] = binding.threeAndTwo.getId();
        indexs[2][2] = binding.threeAndThree.getId();
        indexs[2][3] = binding.threeAndFour.getId();

        indexs[3][0] = binding.fourAndOne.getId();
        indexs[3][1] = binding.fourAndTwo.getId();
        indexs[3][2] = binding.fourAndThree.getId();
        indexs[3][3] = binding.fourAndFour.getId();

        final Map<Integer, Integer> numToColor = new HashMap<>();
        numToColor.put(0, Color.parseColor("#FFFFFF"));
        numToColor.put(2, Color.parseColor("#FFFFCC"));
        numToColor.put(4, Color.parseColor("#FFFF99"));
        numToColor.put(8, Color.parseColor("#FFB266"));
        numToColor.put(16, Color.parseColor("#FF6666"));
        numToColor.put(32, Color.parseColor("#FF0000"));
        numToColor.put(64, Color.parseColor("#FF8000"));
        numToColor.put(128, Color.parseColor("#FFFF00"));
        numToColor.put(256, Color.parseColor("#CCCC00"));
        numToColor.put(512, Color.parseColor("#00FF00"));
        numToColor.put(1024, Color.parseColor("#FF007F"));
        numToColor.put(2048, Color.parseColor("#CC0066"));
        numToColor.put(4096, Color.parseColor("#CCE5FF"));

        randomIndexNum(integerTextViewMap, indexs);
        randomIndexNum(integerTextViewMap, indexs);

        dbHelper    =   new GameDataRecordDbHelper(getContext());
        values      =   new ContentValues();
        URLInfo urlInfo = new URLInfo();
        maxScore    =   0;
        String id = getArguments().getString("userId");

        String[] projection = {
                Game2048Entity.COLUMN_NAME_TITLE,
                Game2048Entity.COLUMN_NAME_SECOND_TITLE
        };

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlInfo.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        playGameInterface = retrofit.create(PlayGameInterface.class);

        if (id != null) {
            userId = Long.parseLong(id);

            db = dbHelper.getReadableDatabase();
            String selection = Game2048Entity.COLUMN_NAME_TITLE + " = ?";
            String[] selectionArgs = {String.valueOf(userId)};
            String sortOrder = Game2048Entity.COLUMN_NAME_TITLE + " DESC";

            Cursor cursor = db.query(
                    Game2048Entity.TABLE_NAME,
                    projection,                 // The array of columns to return (pass null to get all)
                    selection,                  // The columns for the WHERE clause
                    selectionArgs,              // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    sortOrder               // The sort order
            );
            values.put(Game2048Entity.COLUMN_NAME_TITLE, userId);

            if (cursor.moveToNext()) maxScore = cursor.getInt(1);
            else {
                db = dbHelper.getWritableDatabase();
                values.put(Game2048Entity.COLUMN_NAME_SECOND_TITLE, 0);
                db.insert(Game2048Entity.TABLE_NAME, null, values);
            }

            cursor.close();
        } else {
            values.put(Game2048Entity.COLUMN_NAME_TITLE, 0);
            db = dbHelper.getReadableDatabase();
            String selection = Game2048Entity.COLUMN_NAME_TITLE + " = ?";
            String[] selectionArgs = {String.valueOf(0)};
            String sortOrder = Game2048Entity.COLUMN_NAME_TITLE + " DESC";

            Cursor cursor = db.query(
                    Game2048Entity.TABLE_NAME,
                    projection,                 // The array of columns to return (pass null to get all)
                    selection,                  // The columns for the WHERE clause
                    selectionArgs,              // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    sortOrder               // The sort order
            );

            if (cursor.moveToNext()) maxScore = cursor.getInt(1);
            else {
                db = dbHelper.getWritableDatabase();
                values.put(Game2048Entity.COLUMN_NAME_SECOND_TITLE, 0);
                db.insert(Game2048Entity.TABLE_NAME, null, values);
            }

            cursor.close();
        }

        binding.highestScore.setText(String.valueOf(maxScore));

        View view = binding.getRoot();
        view.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint({"ClickableViewAccessibility", "ShowToast"})
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    x1 = event.getX();
                    y1 = event.getY();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    x2 = event.getX();
                    y2 = event.getY();
                    float xDistance = x1 - x2;
                    float yDistance = y1 - y2;
                    if (Math.abs(xDistance) >= DISTANCE) {
                        if (xDistance < 0) {
                            rightMove(integerTextViewMap, indexs, numToColor);
                            binding.score2048.setText(String.valueOf(get2048Score(integerTextViewMap)));
                            randomIndexNum(integerTextViewMap, indexs);
                        } else {
                            leftMove(integerTextViewMap, indexs, numToColor);
                            binding.score2048.setText(String.valueOf(get2048Score(integerTextViewMap)));
                            randomIndexNum(integerTextViewMap, indexs);
                        }
                    } else if (Math.abs(yDistance) >= DISTANCE) {
                        if (yDistance > 0) {
                            upMove(integerTextViewMap, indexs, numToColor);
                            binding.score2048.setText(String.valueOf(get2048Score(integerTextViewMap)));
                            randomIndexNum(integerTextViewMap, indexs);
                        } else {
                            downMove(integerTextViewMap, indexs, numToColor);
                            binding.score2048.setText(String.valueOf(get2048Score(integerTextViewMap)));
                            randomIndexNum(integerTextViewMap, indexs);
                        }
                    }
                }

                return true;
            }
        });

        binding.restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.score2048.setText("");
                for (Integer[] index : indexs) {
                    for (Integer integer : index) {
                        integerTextViewMap.get(integer).setText("0");
                        integerTextViewMap.get(integer).setBackgroundColor(numToColor.get(0));
                    }
                }
                randomIndexNum(integerTextViewMap, indexs);
                randomIndexNum(integerTextViewMap, indexs);

                int score = getScore(integerTextViewMap, indexs);

                if (score > maxScore) {
                    binding.highestScore.setText(String.valueOf(score));
                    values.put(Game2048Entity.COLUMN_NAME_SECOND_TITLE, score);

                    db = dbHelper.getWritableDatabase();
                    String selection = Game2048Entity.COLUMN_NAME_TITLE + " = ?";
                    String[] selectionArgs = {String.valueOf(userId)};

                    long count = db.update(Game2048Entity.TABLE_NAME, values, selection, selectionArgs);
                    if (count > 0) Log.i("Sqlite", "update max score success");
                    else Log.i("Sqlite", "update max score failed");

                    addRecordToServer(score);
                }
            }
        });

        return binding.getRoot();
    }

    private void randomIndexNum(Map<Integer, TextView> integerTextViewMap, Integer[][] integers) {
        if (gameOver(integerTextViewMap, integers)) {
            Toast.makeText(getActivity(), "Game over", Toast.LENGTH_SHORT).show();
            int score = getScore(integerTextViewMap, indexs);

            if (score > maxScore) {
                binding.highestScore.setText(String.valueOf(score));
                values.put(Game2048Entity.COLUMN_NAME_SECOND_TITLE, score);

                db = dbHelper.getWritableDatabase();
                String selection = Game2048Entity.COLUMN_NAME_TITLE + " = ?";
                String[] selectionArgs = {String.valueOf(userId)};

                long count = db.update(Game2048Entity.TABLE_NAME, values, selection, selectionArgs);
                if (count > 0) Log.i("Sqlite", "update max score success");
                else Log.i("Sqlite", "update max score failed");

                maxScore = score;

                addRecordToServer(score);
            }

            Bundle bundle = new Bundle();
            bundle.putString("userId", String.valueOf(userId));
            bundle.putString("score", String.valueOf(score));
            bundle.putString("maxScore", String.valueOf(maxScore));
            Navigation.findNavController(getView()).navigate(R.id.action_mainGame_to_gameOverFragment, bundle);

            return;
        }

        Integer[] indexs = new Integer[integerTextViewMap.size()];
        int i = 0;

        for (i = 0; i < indexs.length; ++i) {
            indexs[i] = 0;
        }
        i = 0;
        int zerosNum = 0;
        for (Integer integer : integerTextViewMap.keySet()) {
            TextView textView = integerTextViewMap.get(integer);
            assert textView != null;
            if (textView.getText().equals("")) {
                indexs[i++] = integer;
                zerosNum++;
            }
        }

        if (zerosNum > 0) {
            int index = new Random().nextInt(zerosNum);
            TextView textView = integerTextViewMap.get(indexs[index]);
            assert textView != null;
            textView.setText("2");
            textView.setBackgroundColor(Color.parseColor("#FFFFCC"));
        }
    }

    private void leftMove(Map<Integer, TextView> integerTextViewMap, Integer[][] indexs, Map<Integer, Integer> numToColor) {
//        printNum(integerTextViewMap, indexs);
        TextView textView1, textView2;
        int num1, num2;

        for (Integer[] integers : indexs) {
            for (int j = 1; j < integers.length; ++j) {
                textView1 = integerTextViewMap.get(integers[j - 1]);
                textView2 = integerTextViewMap.get(integers[j]);

                if (textView1.getText().equals("")) num1 = 0;
                else num1 = Integer.parseInt(textView1.getText().toString());
                if (textView2.getText().equals("")) num2 = 0;
                else num2 = Integer.parseInt(textView2.getText().toString());

                if (num1 == num2) {
                    if (num1 == 0) textView1.setText("");
                    else textView1.setText(String.valueOf(2 * num1));

                    textView2.setText("");
                    textView1.setBackgroundColor(numToColor.get(2 * num1));
                    textView2.setBackgroundColor(Color.parseColor("#FFFFFF"));
                } else if (num1 == 0) {
                    int index = j - 1;

                    while (index >= 0 && (num1 == 0 || num1 == num2)) {
                        textView1 = integerTextViewMap.get(integers[index]);
                        textView2 = integerTextViewMap.get(integers[index + 1]);

                        if (textView1.getText().equals("")) num1 = 0;
                        else num1 = Integer.parseInt(textView1.getText().toString());
                        if (textView2.getText().equals("")) num2 = 0;
                        else num2 = Integer.parseInt(textView2.getText().toString());

                        if (num1 == num2) {
                            if (num1 == 0) textView1.setText("");
                            else textView1.setText(String.valueOf(2 * num1));

                            textView2.setText("");
                            textView1.setBackgroundColor(numToColor.get(2 * num1));
                            textView2.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        } else {
                            if (num1 == 0) {
                                if (num2 == 0) textView1.setText("");
                                else textView1.setText(String.valueOf(num2));
                                textView2.setText("");
                                textView1.setBackgroundColor(numToColor.get(num2));
                                textView2.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            }
                        }

                        index--;
                    }
                }
            }
        }
    }

    private void downMove(Map<Integer, TextView> integerTextViewMap, Integer[][] indexs, Map<Integer, Integer> numToColor) {
        TextView textView1, textView2;
        int num1, num2;

        for (int j = 0; j < indexs[0].length; ++j) {
            for (int i = indexs.length - 2; i >= 0; --i) {
                textView1 = integerTextViewMap.get(indexs[i + 1][j]);
                textView2 = integerTextViewMap.get(indexs[i][j]);
                assert textView1 != null;
                assert textView2 != null;

                if (textView1.getText().equals("")) num1 = 0;
                else num1 = Integer.parseInt(textView1.getText().toString());
                if (textView2.getText().equals("")) num2 = 0;
                else num2 = Integer.parseInt(textView2.getText().toString());

                if (num1 == num2) {
                    if (num1 == 0) textView1.setText("");
                    else textView1.setText(String.valueOf(2 * num1));

                    textView2.setText("");
                    textView1.setBackgroundColor(numToColor.get(2 * num1));
                    textView2.setBackgroundColor(Color.parseColor("#FFFFFF"));
                } else if (num1 == 0) {
                    int index = i + 1;

                    while (index < indexs.length && (num1 == 0 || num1 == num2)) {
                        textView1 = integerTextViewMap.get(indexs[index][j]);
                        textView2 = integerTextViewMap.get(indexs[index - 1][j]);
                        assert textView1 != null;
                        assert textView2 != null;

                        if (textView1.getText().equals("")) num1 = 0;
                        else num1 = Integer.parseInt(textView1.getText().toString());
                        if (textView2.getText().equals("")) num2 = 0;
                        else num2 = Integer.parseInt(textView2.getText().toString());

                        if (num1 == num2) {
                            if (num1 == 0) textView1.setText("");
                            else textView1.setText(String.valueOf(2 * num1));

                            textView2.setText("");
                            textView1.setBackgroundColor(numToColor.get(2 * num1));
                            textView2.setBackgroundColor(numToColor.get(0));
                        } else {
                            if (num1 == 0) {
                                if (num2 == 0) textView1.setText("");
                                else textView1.setText(String.valueOf(num2));

                                textView2.setText("");
                                textView1.setBackgroundColor(numToColor.get(num2));
                                textView2.setBackgroundColor(numToColor.get(0));
                            }
                        }

                        index++;
                    }
                }
            }
        }
    }

    private void rightMove(Map<Integer, TextView> integerTextViewMap, Integer[][] indexs, Map<Integer, Integer> numToColor) {
        TextView textView1, textView2;
        int num1, num2;

        for (Integer[] integers : indexs) {
            for (int j = integers.length - 2; j >= 0; --j) {
                textView1 = integerTextViewMap.get(integers[j + 1]);
                textView2 = integerTextViewMap.get(integers[j]);
                assert textView1 != null;
                assert textView2 != null;

                if (textView1.getText().equals("")) num1 = 0;
                else num1 = Integer.parseInt(textView1.getText().toString());
                if (textView2.getText().equals("")) num2 = 0;
                else num2 = Integer.parseInt(textView2.getText().toString());

                if (num1 == num2) {
                    if (num1 == 0) textView1.setText("");
                    else textView1.setText(String.valueOf(2 * num1));

                    textView2.setText("");
                    textView1.setBackgroundColor(numToColor.get(2 * num1));
                    textView2.setBackgroundColor(Color.parseColor("#FFFFFF"));
                } else if (num1 == 0) {
                    int index = j + 1;

                    while (index < integers.length && (num1 == 0 || num1 == num2)) {
                        textView1 = integerTextViewMap.get(integers[index]);
                        textView2 = integerTextViewMap.get(integers[index - 1]);
                        assert textView1 != null;
                        assert textView2 != null;

                        if (textView1.getText().equals("")) num1 = 0;
                        else num1 = Integer.parseInt(textView1.getText().toString());
                        if (textView2.getText().equals("")) num2 = 0;
                        else num2 = Integer.parseInt(textView2.getText().toString());

                        if (num1 == num2) {
                            if (num1 == 0) textView1.setText("");
                            else textView1.setText(String.valueOf(2 * num1));

                            textView2.setText("");
                            textView1.setBackgroundColor(numToColor.get(2 * num1));
                            textView2.setBackgroundColor(numToColor.get(0));
                        } else {
                            if (num1 == 0) {
                                if (num2 == 0) textView1.setText("");
                                else textView1.setText(String.valueOf(num2));

                                textView2.setText("");
                                textView1.setBackgroundColor(numToColor.get(num2));
                                textView2.setBackgroundColor(numToColor.get(0));
                            }
                        }

                        index++;
                    }
                }
            }
        }
    }

    private void upMove(Map<Integer, TextView> integerTextViewMap, Integer[][] indexs, Map<Integer, Integer> numToColor) {
        TextView textView1, textView2;
        int num1, num2;

        for (int j = 0; j < indexs[0].length; ++j) {
            for (int i = 1; i < indexs.length; ++i) {
                textView1 = integerTextViewMap.get(indexs[i - 1][j]);
                textView2 = integerTextViewMap.get(indexs[i][j]);
                assert textView1 != null;
                assert textView2 != null;

                if (textView1.getText().equals("")) num1 = 0;
                else num1 = Integer.parseInt(textView1.getText().toString());
                if (textView2.getText().equals("")) num2 = 0;
                else num2 = Integer.parseInt(textView2.getText().toString());

                if (num1 == num2) {
                    if (num1 == 0) textView1.setText("");
                    else textView1.setText(String.valueOf(2 * num1));

                    textView2.setText("");
                    textView1.setBackgroundColor(numToColor.get(2 * num1));
                    textView2.setBackgroundColor(Color.parseColor("#FFFFFF"));
                } else if (num1 == 0) {
                    int index = i - 1;

                    while (index >= 0 && (num1 == 0 || num1 == num2)) {
                        textView1 = integerTextViewMap.get(indexs[index][j]);
                        textView2 = integerTextViewMap.get(indexs[index + 1][j]);
                        assert textView1 != null;
                        assert textView2 != null;

                        if (textView1.getText().equals("")) num1 = 0;
                        else num1 = Integer.parseInt(textView1.getText().toString());
                        if (textView2.getText().equals("")) num2 = 0;
                        else num2 = Integer.parseInt(textView2.getText().toString());

                        if (num1 == num2) {
                            if (num1 == 0) textView1.setText("");
                            else textView1.setText(String.valueOf(2 * num1));

                            textView2.setText("");
                            textView1.setBackgroundColor(numToColor.get(2 * num1));
                            textView2.setBackgroundColor(numToColor.get(0));
                        } else {
                            if (num1 == 0) {
                                if (num2 == 0) textView1.setText("");
                                else textView1.setText(String.valueOf(num2));

                                textView2.setText("");
                                textView1.setBackgroundColor(numToColor.get(num2));
                                textView2.setBackgroundColor(numToColor.get(0));
                            }
                        }

                        index--;
                    }
                }
            }
        }
    }

    private int get2048Score(Map<Integer, TextView> integerTextViewMap) {
        int score = 0;

        TextView textView = null;
        for (Integer integer : integerTextViewMap.keySet()) {
            textView = integerTextViewMap.get(integer);
            if (textView.getText().equals("")) score += 0;
            else score += Integer.parseInt(textView.getText().toString());
        }

        return score;
    }

    private int getScore(Map<Integer, TextView> integerTextViewMap, Integer[][] indexs) {
        int score = 0;
        TextView textView;
        for (Integer[] index : indexs) {
            for (Integer integer : index) {
                textView = integerTextViewMap.get(integer);
                if (!textView.getText().equals(""))
                    score += Integer.parseInt(textView.getText().toString());
            }
        }

        return score;
    }

    private Boolean gameOver(Map<Integer, TextView> integerTextViewMap, Integer[][] indexs) {
        TextView textView1, textView2, textView3;
        for (int i = 0; i < indexs.length; ++i) {
            for (int j = 0; j < indexs[0].length - 1; ++j) {
                if (i != indexs.length - 1) {
                    textView1 = integerTextViewMap.get(indexs[i][j]);
                    textView2 = integerTextViewMap.get(indexs[i][j + 1]);
                    textView3 = integerTextViewMap.get(indexs[i + 1][j]);

                    if (textView1.getText().equals("") || textView2.getText().equals("")
                            || textView3.getText().equals("")) return false;

                    if (textView1.getText().equals(textView2.getText()) ||
                            textView1.getText().equals(textView3.getText()))
                        return false;
                } else {
                    textView1 = integerTextViewMap.get(indexs[i][j]);
                    textView2 = integerTextViewMap.get(indexs[i][j + 1]);
                    if (textView1.getText().equals("") ||
                            textView2.getText().equals("")) return false;

                    if (textView1.getText().equals(textView2.getText()))
                        return false;
                }
            }
        }

        return true;
    }

    private String getFinialResult(Map<Integer, TextView> integerTextViewMap, Integer[][] indexs) {
        StringBuilder stringBuilder = new StringBuilder("[");
        for (Integer[] integers: indexs) {
            stringBuilder.append(" [");
            for (Integer integer: integers) {
                stringBuilder.append(integerTextViewMap.get(integer).getText()).append(" ");
            }
            stringBuilder.append("] ");
        }
        stringBuilder.append("]");

        return String.valueOf(stringBuilder);
    }

    private void addRecordToServer(int score){
        String pattern = "yyyy-MM-dd";
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String date = simpleDateFormat.format(new java.util.Date());

        Call<String> addNewRecord = playGameInterface.addNew2048Record(userId, score,
                Date.valueOf(date), getFinialResult(integerTextViewMap, indexs));
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
}
