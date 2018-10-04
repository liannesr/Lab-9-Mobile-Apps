package com.techexchange.mobileapps.lab9;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ScoreActivity extends AppCompatActivity {

    static final String KEY_RESTART_QUIZ="Retake quiz";
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        Button quizAgain = findViewById(R.id.again_button);
        quizAgain.setOnClickListener(V -> quizAgainButtonPressed());

        Button sendEmail = findViewById(R.id.send_score_button);
        sendEmail.setOnClickListener(v -> onEmailButtonPressed());

        TextView scoreText = findViewById(R.id.score_text);
        score = getIntent().getIntExtra(QuizFragment.KEY_SCORE,0);
        //Second argument of get In Extra means.
        scoreText.setText("Quiz Score: "+ score);
    }

    private void quizAgainButtonPressed(){
        Intent data = new Intent();
        data.putExtra(KEY_RESTART_QUIZ,true);
        setResult(Activity.RESULT_OK,data);
        finish(); //Finishing the Score Activity
    }

    private void onEmailButtonPressed(){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        //intent.setData(Uri.parse("sms:7877093423"));
        //intent.putExtra(Intent.EXTRA_TEXT, "Your game score is: "+score);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"liannes@google.com", "sanchezrodriguezlianne@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Quiz Scores");
        intent.putExtra(Intent.EXTRA_TEXT,"Your email score was: "+score);
        if(intent.resolveActivity(getPackageManager())!=null){
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "The activity could not be resolved", Toast.LENGTH_SHORT).show();
        }

    }
}
