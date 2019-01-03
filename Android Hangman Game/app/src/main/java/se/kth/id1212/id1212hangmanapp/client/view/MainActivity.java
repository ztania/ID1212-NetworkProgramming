package se.kth.id1212.id1212hangmanapp.client.view;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import se.kth.id1212.id1212hangmanapp.R;
import se.kth.id1212.id1212hangmanapp.client.net.Connect;
import se.kth.id1212.id1212hangmanapp.client.net.HandleServerResponse;
//import se.kth.id1212.id1212hangmanapp.client.view.ResponseParser;

public class MainActivity extends AppCompatActivity {

    private Connect server = new Connect();
    private boolean connected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //By setting the layout activity_main.xml that we will use as our view
        setContentView(R.layout.activity_main);

        //Adding listeners for all the buttons
        submitButtonListener();
        newGameButtonListener();
        quitButtonListener();

        new NetworkHandler("Connect").execute();
    }

    private void quitButtonListener() {
        //Getting the object that was created in the interface so that we can manipulate it in code
        Button quitButton = (Button)findViewById(R.id.quit_button);

        //Event listener for the button
        assert quitButton != null;
        quitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //When the "quit" button is pressed, call finish to get to the onDestroy method
                finish();
            }
        });
    }

    private void newGameButtonListener() {
        //Getting the object that was created in the interface so that we can manipulate it in code
        Button newGameButton = (Button)findViewById(R.id.new_game_button);

        //Event listener for the button
        assert newGameButton != null;
        newGameButton.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        new NetworkHandler("New Game").execute();
                    }
                }
        );
    }

    private void submitButtonListener() {
        //Getting the object that was created in the interface so that we can manipulate it in code
        Button submitButton = (Button)findViewById(R.id.submit_button);

        //Event listener for the button
        assert submitButton != null;
        submitButton.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){

                        EditText userGuess = (EditText)findViewById(R.id.user_guess);
                        userGuess.setGravity(Gravity.CENTER_HORIZONTAL);
                        String userGuessString = userGuess.getText().toString();
                        userGuess.getText().clear();
                        new NetworkHandler(userGuessString).execute();
                    }
                }
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            server.disconnect("Quit");
        } catch (IOException e) {
            e.printStackTrace();
        }
        connected = false;
    }

    private class NetworkHandler extends AsyncTask<Void, Void, Void>{

        private String message;

        public NetworkHandler(String message)
        {
            this.message = message;
        }

        @Override
        protected Void doInBackground(Void... params) {

            //Connect to the server
            if(!connected){
                try {
                    server.connect("10.0.2.2", 8085, new Printer());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                connected = true;
            }

            server.sendMessage(message);


            return null;
        }
    }

    private class Printer implements HandleServerResponse {

        @Override
        public void handleResponse(final String response) {

            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    TextView wordText = (TextView)findViewById(R.id.word_text);

                    wordText.setGravity(Gravity.CENTER_HORIZONTAL);

                    if (response.contains("_")){
                        TextView numGuesses = (TextView)findViewById(R.id.num_guesses);
                        TextView score = (TextView)findViewById(R.id.score);

                        ResponseParser parser = new ResponseParser(response);
                        wordText.setText(parser.getMessage());
                        numGuesses.setText(parser.getNumOfGuesses());

                        if (parser.getScore() != null) {
                            score.setText(parser.getScore());
                        }
                    }
                    else wordText.setText(response);
                }
            });
        }
    }


}
