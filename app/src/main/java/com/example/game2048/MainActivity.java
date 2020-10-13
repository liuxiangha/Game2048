package com.example.game2048;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {
    private ActionBarDrawerToggle t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        DrawerLayout dl = findViewById(R.id.drawerLayout);
        t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);

        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView nv = findViewById(R.id.navView);
        System.out.println("t: " + t.toString());
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                Intent intent;
                switch (id) {
                    case R.id.rulesItem:
                        Toast.makeText(MainActivity.this, "Rule", Toast.LENGTH_SHORT).show();
//                        NavController navController = Navigation.findNavController(MainActivity.this, R.id.action_mainFragment_to_rulesFragment);
//                        navController.navigate(R.id.action_mainFragment_to_rulesFragment);
//                        MainFragment mainFragment = new MainFragment();
//                        getSupportFragmentManager().beginTransaction().add(R.id.rulesFragment, mainFragment).show(mainFragment).commit();
                        intent = new Intent(MainActivity.this, rules.class);
                        startActivity(intent);
                        break;
                    case R.id.aboutItem:
                        Toast.makeText(MainActivity.this, "About", Toast.LENGTH_SHORT).show();
//                        Navigation.findNavController(MainActivity.this, R.id.action_mainFragment_to_aboutFragment);
                        intent = new Intent(MainActivity.this, AboutActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "Hello Drawer", Toast.LENGTH_SHORT).show();
                        break;
                }

                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }
}
