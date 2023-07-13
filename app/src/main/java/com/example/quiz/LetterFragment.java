package com.example.quiz;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class LetterFragment extends Fragment {
    private TextView letterTextView, answerTextView;
    private char[] skyLetters = {'b', 'd', 'f', 'h', 'k', 'l', 't'};
    private char[] grassLetters = {'g', 'j', 'p', 'q', 'y'};
    private char[] rootLetters = {'a', 'c', 'e', 'i', 'm', 'n', 'o', 'r', 's', 'u', 'v', 'w', 'x', 'z'};
    private QuizDatabaseHelper databaseHelper;
    private List<String> currentAttemptAnswers;
    private int currentQuestionCount = 0;
    private int currentAttemptNumber = 1;
    private String answerString = "";
    String Letter;
    int result;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LetterFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static LetterFragment newInstance(String param1, String param2) {
        LetterFragment fragment = new LetterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        databaseHelper = new QuizDatabaseHelper(requireContext());
        currentAttemptAnswers = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test, container, false);

        Letter=getRandomLetter();
        letterTextView = view.findViewById(R.id.letter_text_view);
        letterTextView.setText(Letter);

        answerTextView = view.findViewById(R.id.answer_text_view);

        Button skyButton = view.findViewById(R.id.sky_button);
        skyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer("Sky Letter");
            }
        });

        Button grassButton = view.findViewById(R.id.grass_button);
        grassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer("Grass Letter");
            }
        });

        Button rootButton = view.findViewById(R.id.root_button);
        rootButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer("Root Letter");
            }
        });

        return view;
    }
    private void checkAnswer(String expectedAnswer) {

        if (expectedAnswer.equals(answerString)) {
            answerTextView.setText("Correct!");
            //currentAttemptAnswers.add("Correct! Selected Letter '"+Letter+"' is "+answerString);
            result=result+1;
        } else {
            answerTextView.setText("Incorrect! This is "+answerString+".");
            //currentAttemptAnswers.add("InCorrect! Selected Letter '"+Letter+"' is not "+expectedAnswer+" it is "+answerString);
        }
        currentQuestionCount++;

        if (currentQuestionCount == 5) {
            currentAttemptAnswers.add("Marks = "+result+" / 5");
            processShiftCompletion();
        }

        // Wait for 5 seconds and create a new question
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Letter=getRandomLetter();
                letterTextView.setText(Letter);
                answerTextView.setText("");
            }
        }, 1000);
    }

    private void processShiftCompletion() {
        if (!currentAttemptAnswers.isEmpty()) {
            databaseHelper.addAttempt(currentAttemptNumber, currentAttemptAnswers);
            currentAttemptNumber++;
            currentQuestionCount = 0;
            currentAttemptAnswers.clear();
            result=0;
            Toast.makeText(requireContext(), "One Attempt completed", Toast.LENGTH_SHORT).show();
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, new WellcomeFragment());
            fragmentTransaction.commit();
        }
    }
    private String getRandomLetter() {
        Random random = new Random();
        int category = random.nextInt(3);
        char letter;
        switch (category) {
            case 0:
                letter = skyLetters[random.nextInt(skyLetters.length)];
                answerString = "Sky Letter";
                break;
            case 1:
                letter = grassLetters[random.nextInt(grassLetters.length)];
                answerString = "Grass Letter";
                break;
            default:
                letter = rootLetters[random.nextInt(rootLetters.length)];
                answerString = "Root Letter";
                break;
        }
        return String.valueOf(letter);
    }
}