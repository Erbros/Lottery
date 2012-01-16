package net.erbros.Lottery;

// not in use at the moment

public class ChangeConfig {
    private boolean success;
    private String message;
    
    public ChangeConfig (boolean success, String message) {
        this.success = success;
        this.message = message;
        
    }
    
    public boolean getSuccess() {
        return success;
    }
    
    public String getMessage() {
        return message;
    }
}
