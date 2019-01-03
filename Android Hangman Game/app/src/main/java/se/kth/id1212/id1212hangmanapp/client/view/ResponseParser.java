package se.kth.id1212.id1212hangmanapp.client.view;

public class ResponseParser {

    private String serverResponse;
    private String[] responseArgs;

    public ResponseParser(String serverResponse) {
        this.serverResponse = serverResponse;
        splitResponse();
    }

    private void splitResponse() {
        responseArgs = serverResponse.split("_");
    }

    public String getMessage(){
        return responseArgs[0];
    }

    public String getNumOfGuesses(){
        return responseArgs[1];
    }

    public String getScore(){
        String score = null;
        if (responseArgs.length > 2){
            score = responseArgs[2];
        }
        return score;
    }
}
