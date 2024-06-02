package pojo;

public class CreateIssue {
    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getSelf() {
        return self;
    }

    private String id, key, self;
    public CreateIssue(String id, String key, String self){
        this.id=id;
        this.key=key;
        this.self=self;
    }


}
