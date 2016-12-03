package com.example.minky.bigmeet;

		import java.util.List;
        import java.util.Map;

public final class Attendees {
	String uid;
	List<String> tokenList;

    public Attendees(String UID, List<String> tokens){
        uid = UID;
        tokenList = tokens;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<String> getTokenList() {
        return tokenList;
    }

    public void setTokenList(List<String> tokenList) {
        this.tokenList = tokenList;
    }
}
