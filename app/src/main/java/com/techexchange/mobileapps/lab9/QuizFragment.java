package com.techexchange.mobileapps.lab9;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Preconditions;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuizFragment extends Fragment {
    private TextView questionText;
    private TextView correctText;
    private Button leftButton;
    private Button rightButton;
    private Button nextButton;

    private List<Question> questionList;
    private int currentQuestionIndex;

    // Lab 3
    private static final String TAG = MainActivity.class.getSimpleName();
    private int currentScore;
    private boolean isAnswered;
    static final String KEY_SCORE = "Score";


    public QuizFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_quiz, container, false);
        rootView.findViewById(R.id.fragment_container);

        //questionList = initialQuestionList();
        questionText = rootView.findViewById(R.id.question_text);

        leftButton = rootView.findViewById(R.id.left_button);
        leftButton.setOnClickListener(this::onAnswerButtonPressed);

        rightButton = rootView.findViewById(R.id.right_button);
        rightButton.setOnClickListener(this::onAnswerButtonPressed);

        nextButton = rootView.findViewById(R.id.next_button);
        nextButton.setOnClickListener(this::onNextButtonPressed);
        nextButton.setEnabled(false);

        correctText = rootView.findViewById(R.id.correct_incorrect_text);

        currentQuestionIndex = 0;
        currentScore=0;

        Log.d(TAG, "Sup " );

        FirebaseApp app = FirebaseApp.initializeApp(getActivity());
        Log.d(TAG, "app: " + app);
        FirebaseDatabase database = FirebaseDatabase.getInstance(app);
        Log.d(TAG, "database " + database);
        database.goOnline();

        DatabaseReference ref = database.getReference();
        ref = ref.child("lab9").child("questions");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                onQuestionListChanged(dataSnapshot);
            }

            @Override
            public void onCancelled( DatabaseError databaseError) {
                Log.d(TAG, "The database transaction was cancelled", databaseError.toException());
            }
        });

        if(savedInstanceState!=null){
            currentQuestionIndex=savedInstanceState.getInt("CurrentIndex");
            currentScore = savedInstanceState.getInt("CurrentScore");
            isAnswered=savedInstanceState.getBoolean("QuestionAnswered");
        }

        if(!isAnswered){
            leftButton.setEnabled(true);
            rightButton.setEnabled(true);
            nextButton.setEnabled(false);
        }
        else{
            leftButton.setEnabled(false);
            rightButton.setEnabled(false);
            nextButton.setEnabled(true);
        }

        return rootView;

    }

    private void onQuestionListChanged(DataSnapshot dataSnapshot){
        //To be implemented....\
        String question;
        String correctAnswer;
        String wrongAnswer;
        questionList = new ArrayList<>();

        for(int i=0; i< dataSnapshot.getChildrenCount();i++) {
            question = dataSnapshot.child("" + i).child("question").getValue().toString();
            correctAnswer = dataSnapshot.child("" + i).child("correctAnswer").getValue().toString();
            wrongAnswer = dataSnapshot.child("" + i).child("wrongAnswer").getValue().toString();
            Question newQuestion = new Question(question, correctAnswer, wrongAnswer);
            questionList.add(newQuestion);
        }
        updateView();

    }
    public void updateView(){
        Question currentQuestion = questionList.get(currentQuestionIndex);
        questionText.setText(currentQuestion.getQuestion());
        if(Math.random() < 0.5){
            leftButton.setText(currentQuestion.getCorrectAnswer());
            rightButton.setText(currentQuestion.getWrongAnswer());
        }
        else{
            rightButton.setText(currentQuestion.getCorrectAnswer());
            leftButton.setText(currentQuestion.getWrongAnswer());
        }
        if(!isAnswered){
            leftButton.setEnabled(true);
            rightButton.setEnabled(true);
            nextButton.setEnabled(false);
        }
        else{
            leftButton.setEnabled(false);
            rightButton.setEnabled(false);
            nextButton.setEnabled(true);
        }
        correctText.setText(R.string.initial_correct_incorrect);
    }

    private void onAnswerButtonPressed(View v){ //What does view mean
        Button buttonSelected = (Button)v;
        Question questionOn = questionList.get(currentQuestionIndex);
        leftButton.setEnabled(false);
        rightButton.setEnabled(false);
        if(questionOn.getCorrectAnswer().contentEquals(buttonSelected.getText())){
            correctText.setText("You got it right! Congrats!");
            currentScore++;

        }
        else{
            correctText.setText("Too Bad! You got it wrong!");
        }

        nextButton.setEnabled(true);
        isAnswered=true;

    }

    private void onNextButtonPressed(View v) {
        //Creating a new random number
        //Random randomNumber = new Random();
        // this.currentQuestionIndex = randomNumber.nextInt(5);
        isAnswered=false;
        currentQuestionIndex++;
        if (currentQuestionIndex < questionList.size()) {
            updateView();
        } else {
            Intent scoreIntent = new Intent(getActivity(), ScoreActivity.class);
            //Attaching extra to the intent, thus making accessible to class score activity
            scoreIntent.putExtra(KEY_SCORE, currentScore);
            startActivityForResult(scoreIntent, 0);

            //Request Code
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("CurrentIndex", currentQuestionIndex);
        outState.putInt("CurrentScore", currentScore);
        outState.putBoolean("QuestionAnswered",isAnswered);
    }


    @Override
    public void onActivityResult(
            int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK || requestCode != 0 || data == null) {
            getActivity().finish();
        }
        if(data.getBooleanExtra(ScoreActivity.KEY_RESTART_QUIZ,false)){
            Log.d(TAG, "I am in!!");
            // The OS will do this automatically
            currentQuestionIndex=0;
            currentScore=0;
            updateView();
        }
        else{
            getActivity().finish();

        }

    }


    @Override
    public void onResume(){
        super.onResume();
        //this.updateView(); FOR NOW
    }

    //First, in your MainActivity class, override the onStart, onDestroy, onStop and onPause methods.

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() was called!");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() was called!");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() was called!");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() was called!");
    }



}
