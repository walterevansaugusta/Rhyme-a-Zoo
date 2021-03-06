package com.example.shouryakhare.rhyme_a_zoo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.view.Gravity.CENTER_HORIZONTAL;
import static android.view.Gravity.CENTER_VERTICAL;

/**
 * Activity to display rhyme menu to the user
 */
public class RhymeMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rhyme_menu);

        // IDProvider to get rhyme names and thumbnails
        IDProvider idProvider = new IDProvider();

        // Get views
        Button home = findViewById(R.id.rhymeActivity_home);
        LinearLayout layout = findViewById(R.id.rhymeMenuActivity_linearLayout);

        // Get window height for spacing calculations
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels - 40;

        // Display all rhymes in columns containing 3 rows
        for (int i = 0; i < idProvider.getThumbnailArrayLength(); i+=3) {
            LinearLayout column = new LinearLayout(this);
            column.setOrientation(LinearLayout.VERTICAL);

            // Display <=3 rhymes depending upon how many rhymes are left
            int indicesLeft = idProvider.getThumbnailArrayLength() - i;

            LinearLayout thumbnail1;
            LinearLayout thumbnail2 = new LinearLayout(this);
            LinearLayout thumbnail3 = new LinearLayout(this);

            if (indicesLeft == 1) {
                thumbnail1 = createThumbnail(i, idProvider.getThumbnailId(i), height);
                column.addView(thumbnail1);
            } else if (indicesLeft == 2) {
                thumbnail1 = createThumbnail(i+1, idProvider.getThumbnailId(i), height);
                thumbnail2 = createThumbnail(i+2, idProvider.getThumbnailId(i+1), height);

                column.addView(thumbnail1);
                column.addView(thumbnail2);
            } else {
                thumbnail1 = createThumbnail(i, idProvider.getThumbnailId(i), height);
                thumbnail2 = createThumbnail(i+1, idProvider.getThumbnailId(i+1), height);
                thumbnail3 = createThumbnail(i+2, idProvider.getThumbnailId(i+2), height);

                column.addView(thumbnail1);
                column.addView(thumbnail2);
                column.addView(thumbnail3);
            }

            final int currentIndex = i;

            // Set click listeners for each rhyme thumbnail. Go to RhymeActivity when clicked and display particular rhyme
            thumbnail1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(RhymeMenuActivity.this, RhymeActivity.class);
                    myIntent.putExtra("id", currentIndex);
                    RhymeMenuActivity.this.startActivity(myIntent);
                }
            });

            thumbnail2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(RhymeMenuActivity.this, RhymeActivity.class);
                    myIntent.putExtra("id", currentIndex+1);
                    RhymeMenuActivity.this.startActivity(myIntent);
                }
            });

            thumbnail3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(RhymeMenuActivity.this, RhymeActivity.class);
                    myIntent.putExtra("id", currentIndex+2);
                    RhymeMenuActivity.this.startActivity(myIntent);
                }
            });

            layout.addView(column);
        }

        // If home button pressed, go to home activity
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(RhymeMenuActivity.this, MainActivity.class);
                RhymeMenuActivity.this.startActivity(myIntent);
            }
        });
    }

    // Function to create a rhyme thumbnail
    protected LinearLayout createThumbnail(int drawableIndex, int drawableId, int screenHeight) {
        // The main layout containing the rhyme name, image, and coins earned for that rhyme
        LinearLayout outerLayout = new LinearLayout(this);
        outerLayout.setOrientation(LinearLayout.HORIZONTAL);

        // Set rhyme image
        ImageView img = new ImageView(this);
        img.setImageResource(drawableId);
        img.setLayoutParams(new LinearLayout.LayoutParams(screenHeight/3, screenHeight/3));
        outerLayout.addView(img);

        // Set rhyme title
        LinearLayout innerLayout = new LinearLayout(this);
        innerLayout.setBackgroundResource(R.drawable.gradient_12);
        innerLayout.setOrientation(LinearLayout.VERTICAL);
        innerLayout.setLayoutParams(new LinearLayout.LayoutParams(473/236 * screenHeight/3, screenHeight/3));
        innerLayout.setGravity(CENTER_VERTICAL);

        TextView rhymeName = new TextView(this);
        rhymeName.setTextColor(Color.WHITE);
        rhymeName.setTextSize(23.0f);

        JSONReader reader = new JSONReader(this);
        rhymeName.setText(reader.getTitle(drawableIndex));
        rhymeName.setTypeface(null, Typeface.BOLD);

        // Get and display number of coins earned by that rhyme using Shared Preferences
        String rhymePrefString = drawableIndex + "_coins";
        int coins = 0;
        SharedPreferences pref = getSharedPreferences("MyPref", 0);
        if ((pref.contains(rhymePrefString))) {
            coins = pref.getInt(rhymePrefString, 0);
        }

        int silverCoins = coins % 2;
        int goldCoins = coins / 2;

        LinearLayout coinShow = new LinearLayout(this);
        coinShow.setOrientation(LinearLayout.HORIZONTAL);
        coinShow.setMinimumWidth(473/236 * screenHeight/3);
        coinShow.setGravity(CENTER_HORIZONTAL);

        // display single coin
        for (int i = 0; i < goldCoins; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            iv.setImageResource(R.drawable.coingold);
            iv.getLayoutParams().height = 100;
            iv.getLayoutParams().width = 100;

            coinShow.addView(iv);
        }

        // display silver coin
        for (int i = 0; i < silverCoins; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            iv.setImageResource(R.drawable.coinsilver);
            iv.getLayoutParams().height = 100;
            iv.getLayoutParams().width = 100;

            coinShow.addView(iv);
        }

        innerLayout.addView(rhymeName);
        innerLayout.addView(coinShow);

        // Return rhyme "card"
        outerLayout.addView(innerLayout);
        return outerLayout;
    }
}
